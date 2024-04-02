package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.*;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.JobBonusAllocation;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.user.UserGraphVo;
import com.korant.youya.workplace.properties.BindingProperties;
import com.korant.youya.workplace.properties.RabbitMqConfigurationProperties;
import com.korant.youya.workplace.service.GraphSharedService;
import com.korant.youya.workplace.service.JobMainTaskService;
import com.korant.youya.workplace.utils.CalculationUtil;
import com.korant.youya.workplace.utils.IdGenerationUtil;
import com.korant.youya.workplace.utils.RabbitMqUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-03-29
 */
@Service
@Slf4j
public class JobMainTaskServiceImpl extends ServiceImpl<JobMainTaskMapper, JobMainTask> implements JobMainTaskService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SysWalletAccountMapper sysWalletAccountMapper;

    @Resource
    private UserWalletAccountMapper userWalletAccountMapper;

    @Resource
    private EnterpriseWalletAccountMapper enterpriseWalletAccountMapper;

    @Resource
    private EnterpriseWalletFreezeRecordMapper enterpriseWalletFreezeRecordMapper;

    @Resource
    private SysVirtualProductMapper sysVirtualProductMapper;

    @Resource
    private SysOrderMapper sysOrderMapper;

    @Resource
    private WalletTransactionFlowMapper walletTransactionFlowMapper;

    @Resource
    private JobMainTaskMapper jobMainTaskMapper;

    @Resource
    private JobSubTaskMapper jobSubTaskMapper;

    @Resource
    private RabbitMqUtil rabbitMqUtil;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private GraphSharedService graphSharedService;

    @Resource
    private BonusDistributionRuleMapper bonusDistributionRuleMapper;

    @Resource
    private SysCommissionRecordMapper sysCommissionRecordMapper;

    @Resource
    RabbitMqConfigurationProperties mqConfigurationProperties;

    /**
     * 职位奖金分配
     *
     * @param bonusAllocation
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bonusAllocation(JobBonusAllocation bonusAllocation) {
        Job job = bonusAllocation.getJob();
        Long jobId = job.getId();
        BigDecimal award = job.getAward();
        Integer processStep = bonusAllocation.getProcessStep();
        log.info("职位id：【{}】，开始奖金分配，招聘环节：【{}】", jobId, processStep);
        //判断是否有奖励金额
        if (null != award) {
            Long enterpriseId = job.getEnterpriseId();
            if (null == enterpriseId) {
                log.info("职位id：【{}】未关联企业，结束奖金分配", jobId);
                return;
            }
            BigDecimal rewardRate = new BigDecimal("0");
            if (RecruitmentProcessEnum.INTERVIEW.getType() == processStep) {
                rewardRate = job.getInterviewRewardRate();
            } else if (RecruitmentProcessEnum.ONBOARD.getType() == processStep) {
                rewardRate = job.getOnboardRewardRate();
            } else if (RecruitmentProcessEnum.FULL_MEMBER.getType() == processStep) {
                rewardRate = job.getFullMemberRewardRate();
            }
            if (rewardRate.compareTo(new BigDecimal("0")) > 0) {
                BigDecimal rate = rewardRate.multiply(new BigDecimal("0.01"));
                BigDecimal amount = CalculationUtil.multiply(award, rate, 0);
                if (amount.compareTo(new BigDecimal("0")) > 0) {
                    //找出分享链
                    Integer taskType = bonusAllocation.getTaskType();
                    Long lastSharerId;
                    ApplyJob applyJob = null;
                    InternalRecommend internalRecommend = null;
                    if (taskType == MainTaskTypeEnum.CANDIDATE.getType()) {
                        applyJob = bonusAllocation.getApplyJob();
                        lastSharerId = applyJob.getApplicant();
                    } else {
                        internalRecommend = bonusAllocation.getInternalRecommend();
                        lastSharerId = internalRecommend.getRecommender();
                    }
                    List<UserGraphVo> jobSharedList = graphSharedService.findJobShared(lastSharerId, jobId);
                    if (jobSharedList.size() > 0) {
                        List<UserGraphVo> otherSharers;
                        //找出最后一个分享人
                        UserGraphVo lastSharer = graphSharedService.findJobSharer(lastSharerId, jobId);
                        if (null != lastSharer) {
                            otherSharers = jobSharedList.stream().filter(s -> !s.getId().equals(lastSharer.getId())).collect(Collectors.toList());
                        } else {
                            otherSharers = jobSharedList;
                        }
                        if (null != lastSharer || otherSharers.size() > 0) {
                            String bonusDistributionRuleCode = "";
                            if (RecruitmentProcessEnum.INTERVIEW.getType() == processStep) {
                                bonusDistributionRuleCode = job.getInterviewBonusDistributionRule();
                            } else if (RecruitmentProcessEnum.ONBOARD.getType() == processStep) {
                                bonusDistributionRuleCode = job.getOnboardBonusDistributionRule();
                            } else if (RecruitmentProcessEnum.FULL_MEMBER.getType() == processStep) {
                                bonusDistributionRuleCode = job.getFullMemberBonusDistributionRule();
                            }
                            if (StringUtils.isBlank(bonusDistributionRuleCode)) {
                                log.error("职位id：【{}】，招聘环节：【{}】，预设奖金分配规则缺失，结束奖金分配", jobId, processStep);
                                return;
                            }
                            BonusDistributionRule bonusDistributionRule = bonusDistributionRuleMapper.selectOne(new LambdaQueryWrapper<BonusDistributionRule>().eq(BonusDistributionRule::getCode, bonusDistributionRuleCode).eq(BonusDistributionRule::getIsDelete, 0));
                            if (null == bonusDistributionRule) {
                                log.error("职位id：【{}】，招聘环节：【{}】，预设奖金分配规则缺失，结束奖金分配", jobId, processStep);
                                return;
                            }
                            //创建主任务
                            JobMainTask jobMainTask = new JobMainTask();
                            jobMainTask.setJobId(jobId).setTaskType(taskType).setProcessStep(processStep).setBonusDistributionRule(bonusDistributionRuleCode).setStatus(TaskStatusEnum.RUNNING.getStatus());
                            if (taskType == MainTaskTypeEnum.CANDIDATE.getType()) {
                                if (null != applyJob) {
                                    jobMainTask.setApplyId(applyJob.getId());
                                }
                            } else {
                                if (null != internalRecommend) {
                                    jobMainTask.setInterId(internalRecommend.getId());
                                }
                            }
                            jobMainTaskMapper.insert(jobMainTask);
                            log.info("职位id：【{}】，招聘环节：【{}】，创建主任务成功", jobId, processStep);
                            Long mainTaskId = jobMainTask.getId();
                            //创建子任务和子订单
                            List<SysOrder> orders = new ArrayList<>();
                            List<WalletTransactionFlow> transactionFlows = new ArrayList<>();
                            List<JobSubTask> subTasks = new ArrayList<>();
                            //计算平台抽成金额
                            BigDecimal platformRatio = bonusDistributionRule.getPlatformRatio();
                            BigDecimal platformCommissionAmount = new BigDecimal("0");
                            if (platformRatio.compareTo(new BigDecimal("0")) > 0) {
                                platformCommissionAmount = CalculationUtil.multiply(amount, platformRatio, 0);
                                log.info("职位id：【{}】，招聘环节：【{}】，平台抽成金额为：【{}】", jobId, processStep, platformCommissionAmount);
                            }
                            //生成平台抽成订单
                            SysCommissionRecord sysCommissionRecord = null;
                            if (platformCommissionAmount.compareTo(new BigDecimal("0")) > 0) {
                                SysWalletAccount sysWalletAccount = sysWalletAccountMapper.selectOne(new LambdaQueryWrapper<SysWalletAccount>().eq(SysWalletAccount::getName, "友涯").eq(SysWalletAccount::getIsDelete, 0));
                                if (null == sysWalletAccount) {
                                    log.error("职位id：【{}】，招聘环节：【{}】，系统钱包账户未找到，结束奖金分配", jobId, processStep);
                                    return;
                                }
                                Long sysWalletAccountId = sysWalletAccount.getId();
                                sysCommissionRecord = new SysCommissionRecord();
                                String orderId = IdGenerationUtil.generateOrderId(YYConsumerCodeEnum.SYSTEM.getCode(), YYBusinessCode.SYSTEM_COMMISSION.getCode());
                                sysCommissionRecord.setCommissionOrderId(orderId).setAccountId(sysWalletAccountId).setJobId(jobId).setType(1).setProcessType(RecruitmentProcessEnum.ONBOARD.getType())
                                        .setTotalAmount(amount).setCommissionRate(platformRatio).setCommissionAmount(platformCommissionAmount).setStatus(1);
                                sysCommissionRecordMapper.insert(sysCommissionRecord);
                                WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                                String transactionFlowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.SYSTEM_COMMISSION.getCode());
                                walletTransactionFlow.setId(IdWorker.getId()).setTransactionId(transactionFlowId).setAccountId(sysWalletAccountId).setOrderId(orderId).setTransactionType(TransactionTypeEnum.REWARD.getType()).setTransactionDirection(TransactionDirectionTypeEnum.CREDIT.getType()).setAmount(platformCommissionAmount).setCurrency(CurrencyTypeEnum.CNY.getType())
                                        .setInitiationDate(LocalDateTime.now()).setStatus(TransactionFlowStatusEnum.PROCESSING.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.PROCESSING.getStatusDesc());
                                transactionFlows.add(walletTransactionFlow);
                                log.info("职位id：【{}】，招聘环节：【{}】，平台抽成订单创建成功", jobId, processStep);
                            }
                            SysVirtualProduct virtualProduct = sysVirtualProductMapper.selectOne(new LambdaQueryWrapper<SysVirtualProduct>().eq(SysVirtualProduct::getCode, "job_recommended_service").eq(SysVirtualProduct::getIsDelete, 0));
                            if (null == virtualProduct) {
                                log.error("职位id：【{}】，招聘环节：【{}】，职位推荐预设商品服务未找到", jobId, processStep);
                                return;
                            }
                            Long productId = virtualProduct.getId();
                            BigDecimal price = virtualProduct.getPrice();
                            //计算最后一个分享者奖励分配金额
                            BigDecimal recommenderAward = new BigDecimal("0");
                            BigDecimal recommenderRatio = bonusDistributionRule.getRecommenderRatio();
                            Long walletId = enterpriseWalletAccountMapper.queryWalletIdByEnterpriseId(enterpriseId);
                            if (recommenderRatio.compareTo(new BigDecimal("0")) > 0) {
                                User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, lastSharerId).eq(User::getIsDelete, 0));
                                if (null != user) {
                                    recommenderAward = CalculationUtil.multiply(award, recommenderRatio, 0);
                                    log.info("职位id：【{}】，招聘环节：【{}】，最后一个分享者奖励分配金额为：【{}】", jobId, processStep, recommenderAward);
                                    Long sharerId = user.getId();
                                    UserWalletAccount userWalletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, sharerId).eq(UserWalletAccount::getIsDelete, 0));
                                    if (null != userWalletAccount) {
                                        Long userWalletAccountId = userWalletAccount.getId();
                                        SysOrder sysOrder = new SysOrder();
                                        String orderId = IdGenerationUtil.generateOrderId(YYConsumerCodeEnum.ENTERPRISE.getCode(), YYBusinessCode.ENTERPRISE_RECHARGE.getCode());
                                        int quantity = CalculationUtil.divide(recommenderAward, price, 0).intValue();
                                        sysOrder.setId(IdWorker.getId()).setOrderId(orderId).setBuyerId(walletId).setSellerId(userWalletAccountId)
                                                .setType(OrderTypeEnum.NORMAL.getType()).setPaymentMethod(PaymentMethodTypeEnum.BALANCE_PAYMENT.getType()).setOrderDate(LocalDateTime.now())
                                                .setQuantity(quantity).setTotalAmount(recommenderAward).setActualAmount(recommenderAward).setCurrency(CurrencyTypeEnum.CNY.getType())
                                                .setStatus(OrderStatusEnum.PROCESSING_PAYMENT.getStatus());
                                        orders.add(sysOrder);
                                        WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                                        String transactionFlowId;
                                        if (RecruitmentProcessEnum.INTERVIEW.getType() == processStep) {
                                            transactionFlowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.ENTERPRISE_INTERVIEW_REWARD.getCode());
                                            walletTransactionFlow.setDetailDesc("人才推荐 - 面试");
                                        } else if (RecruitmentProcessEnum.ONBOARD.getType() == processStep) {
                                            transactionFlowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.ENTERPRISE_ONBOARD_REWARD.getCode());
                                            walletTransactionFlow.setDetailDesc("人才推荐 - 入职");
                                        } else {
                                            transactionFlowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.ENTERPRISE_FULL_MEMBER_REWARD.getCode());
                                            walletTransactionFlow.setDetailDesc("人才推荐 - 转正");
                                        }
                                        walletTransactionFlow.setId(IdWorker.getId()).setTransactionId(transactionFlowId).setAccountId(userWalletAccountId).setProductId(productId).setOrderId(orderId).setTransactionType(TransactionTypeEnum.REWARD.getType()).setTransactionDirection(TransactionDirectionTypeEnum.CREDIT.getType()).setAmount(recommenderAward).setCurrency(CurrencyTypeEnum.CNY.getType())
                                                .setInitiationDate(LocalDateTime.now()).setStatus(TransactionFlowStatusEnum.PROCESSING.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.PROCESSING.getStatusDesc());
                                        transactionFlows.add(walletTransactionFlow);
                                        JobSubTask subTask = new JobSubTask();
                                        subTask.setId(IdWorker.getId()).setMainTaskId(mainTaskId).setOrderId(sysOrder.getOrderId()).setStatus(TaskStatusEnum.RUNNING.getStatus());
                                        subTasks.add(subTask);
                                        log.info("职位id：【{}】，招聘环节：【{}】，最后一个分享者子任务和子订单生成成功", jobId, processStep);
                                    }
                                }
                            }
                            //计算其他单个分享者奖励分配金额
                            new BigDecimal("0");
                            BigDecimal singleOtherSharerAward;
                            BigDecimal otherSharersTotalAward = new BigDecimal("0");
                            BigDecimal sharerRatio = bonusDistributionRule.getSharerRatio();
                            if (sharerRatio.compareTo(new BigDecimal("0")) > 0) {
                                AtomicInteger sharerNumber = new AtomicInteger(0);
                                singleOtherSharerAward = CalculationUtil.multiply(award, sharerRatio, 0);
                                log.info("职位id：【{}】，招聘环节：【{}】，其他单个分享者奖励分配金额为：【{}】", jobId, processStep, recommenderAward);
                                otherSharers.forEach(s -> {
                                    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, s.getId()).eq(User::getIsDelete, 0));
                                    if (null != user) {
                                        Long sharerId = user.getId();
                                        UserWalletAccount userWalletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, sharerId).eq(UserWalletAccount::getIsDelete, 0));
                                        if (null != userWalletAccount) {
                                            Long userWalletAccountId = userWalletAccount.getId();
                                            SysOrder sysOrder = new SysOrder();
                                            String orderId = IdGenerationUtil.generateOrderId(YYConsumerCodeEnum.ENTERPRISE.getCode(), YYBusinessCode.ENTERPRISE_RECHARGE.getCode());
                                            int quantity = CalculationUtil.divide(singleOtherSharerAward, price, 0).intValue();
                                            sysOrder.setId(IdWorker.getId()).setOrderId(orderId).setBuyerId(walletId).setSellerId(userWalletAccountId)
                                                    .setType(OrderTypeEnum.NORMAL.getType()).setPaymentMethod(PaymentMethodTypeEnum.BALANCE_PAYMENT.getType())
                                                    .setOrderDate(LocalDateTime.now()).setQuantity(quantity).setTotalAmount(singleOtherSharerAward).setActualAmount(singleOtherSharerAward)
                                                    .setCurrency(CurrencyTypeEnum.CNY.getType()).setStatus(OrderStatusEnum.PROCESSING_PAYMENT.getStatus());
                                            orders.add(sysOrder);
                                            WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                                            String transactionFlowId;
                                            if (RecruitmentProcessEnum.INTERVIEW.getType() == processStep) {
                                                transactionFlowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.ENTERPRISE_INTERVIEW_REWARD.getCode());
                                                walletTransactionFlow.setDetailDesc("人才推荐 - 面试");
                                            } else if (RecruitmentProcessEnum.ONBOARD.getType() == processStep) {
                                                transactionFlowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.ENTERPRISE_ONBOARD_REWARD.getCode());
                                                walletTransactionFlow.setDetailDesc("人才推荐 - 入职");
                                            } else {
                                                transactionFlowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.ENTERPRISE_FULL_MEMBER_REWARD.getCode());
                                                walletTransactionFlow.setDetailDesc("人才推荐 - 转正");
                                            }
                                            walletTransactionFlow.setId(IdWorker.getId()).setTransactionId(transactionFlowId).setAccountId(userWalletAccountId).setProductId(productId).setOrderId(orderId).setTransactionType(TransactionTypeEnum.REWARD.getType()).setTransactionDirection(TransactionDirectionTypeEnum.CREDIT.getType()).setAmount(singleOtherSharerAward).setCurrency(CurrencyTypeEnum.CNY.getType())
                                                    .setInitiationDate(LocalDateTime.now()).setStatus(TransactionFlowStatusEnum.PROCESSING.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.PROCESSING.getStatusDesc());
                                            transactionFlows.add(walletTransactionFlow);
                                            JobSubTask subTask = new JobSubTask();
                                            subTask.setId(IdWorker.getId()).setMainTaskId(mainTaskId).setOrderId(sysOrder.getOrderId()).setStatus(1);
                                            subTasks.add(subTask);
                                            sharerNumber.getAndIncrement();
                                        }
                                    }
                                });
                                log.info("职位id：【{}】，招聘环节：【{}】，其他分享者子任务和子订单生成成功", jobId, processStep);
                                int number = sharerNumber.get();
                                if (number > 0) {
                                    otherSharersTotalAward = singleOtherSharerAward.multiply(new BigDecimal(number));
                                }
                                log.info("职位id：【{}】，招聘环节：【{}】，其他分享者有效人数为：【{}】", jobId, processStep, number);
                            }
                            //插入所有子任务
                            if (subTasks.size() > 0) {
                                jobSubTaskMapper.batchInsert(subTasks);
                                log.info("职位id：【{}】，招聘环节：【{}】，插入所有子任务成功", jobId, processStep);
                            }
                            //插入所有订单
                            if (orders.size() > 0) {
                                sysOrderMapper.batchInsert(orders);
                                log.info("职位id：【{}】，招聘环节：【{}】，插入所有子订单成功", jobId, processStep);
                            }
                            //插入所有交易流水
                            if (transactionFlows.size() > 0) {
                                walletTransactionFlowMapper.batchInsert(transactionFlows);
                                log.info("职位id：【{}】，招聘环节：【{}】，插入所有交易流水成功", jobId, processStep);
                            }
                            //计算企业方需要支付的总奖励金额
                            BigDecimal totalAward = platformCommissionAmount.add(recommenderAward).add(otherSharersTotalAward);
                            log.info("职位id：【{}】，招聘环节：【{}】，企业方需要支付的总奖励金额为：【{}】", jobId, processStep, totalAward);
                            //如果总奖励金额大于0
                            if (totalAward.compareTo(new BigDecimal("0")) > 0) {
                                Long walletAccountId = enterpriseWalletAccountMapper.queryWalletIdByEnterpriseId(enterpriseId);
                                String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, walletAccountId);
                                RLock walletLock = redissonClient.getLock(walletLockKey);
                                try {
                                    boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
                                    if (tryWalletLock) {
                                        EnterpriseWalletAccount walletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getEnterpriseId, enterpriseId).eq(EnterpriseWalletAccount::getIsDelete, 0));
                                        if (null == walletAccount) {
                                            log.error("职位id：【{}】，招聘环节：【{}】，企企业钱包账户未找到，结束奖金分配", jobId, processStep);
                                            return;
                                        }
                                        //获取可用余额
                                        BigDecimal availableBalance = walletAccount.getAvailableBalance();
                                        //获取冻结金额
                                        BigDecimal freezeAmount = walletAccount.getFreezeAmount();
                                        if (amount.compareTo(freezeAmount) > 0) {
                                            log.error("职位id：【{}】，招聘环节：【{}】，奖励支付金额大于账户内冻结金额，结束奖金分配", jobId, processStep);
                                            return;
                                        }
                                        BigDecimal newFreezeAmount = freezeAmount.subtract(amount);
                                        BigDecimal newAvailableAmount = availableBalance.add(amount);
                                        if (totalAward.compareTo(newAvailableAmount) > 0) {
                                            log.error("职位id：【{}】，招聘环节：【{}】，当前钱包账户可用余额不足支付奖励，结束奖金分配", jobId, processStep);
                                            return;
                                        }
                                        //解冻奖励金额
                                        LocalDateTime now = LocalDateTime.now();
                                        EnterpriseWalletFreezeRecord enterpriseWalletFreezeRecord = new EnterpriseWalletFreezeRecord();
                                        String freezeOrderId = IdGenerationUtil.generateOrderId(YYConsumerCodeEnum.ENTERPRISE.getCode(), YYBusinessCode.ENTERPRISE_FREEZE_OR_UNFREEZE.getCode());
                                        enterpriseWalletFreezeRecord.setFreezeOrderId(freezeOrderId);
                                        enterpriseWalletFreezeRecord.setEnterpriseWalletId(walletAccountId);
                                        enterpriseWalletFreezeRecord.setJobId(jobId);
                                        enterpriseWalletFreezeRecord.setProcessType(RecruitmentProcessEnum.ONBOARD.getType());
                                        enterpriseWalletFreezeRecord.setAmount(amount);
                                        enterpriseWalletFreezeRecord.setType(WalletFreezeTypeEnum.UNFREEZE.getType());
                                        enterpriseWalletFreezeRecord.setOperateTime(now);
                                        enterpriseWalletFreezeRecordMapper.insert(enterpriseWalletFreezeRecord);
                                        WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                                        String transactionFlowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.ENTERPRISE_FREEZE_OR_UNFREEZE.getCode());
                                        walletTransactionFlow.setTransactionId(transactionFlowId).setAccountId(walletAccountId).setOrderId(freezeOrderId).setTransactionType(TransactionTypeEnum.FREEZE_OR_UNFREEZE.getType()).setTransactionDirection(TransactionDirectionTypeEnum.CREDIT.getType()).setAmount(amount).setCurrency(CurrencyTypeEnum.CNY.getType())
                                                .setDescription("入职奖励分配").setInitiationDate(now).setCompletionDate(now).setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc()).setBalanceBefore(availableBalance).setBalanceAfter(newAvailableAmount);
                                        walletTransactionFlowMapper.insert(walletTransactionFlow);
                                        //扣除奖励金额
                                        BigDecimal subtract = newAvailableAmount.subtract(totalAward);
                                        walletAccount.setFreezeAmount(newFreezeAmount);
                                        walletAccount.setAvailableBalance(subtract);
                                        enterpriseWalletAccountMapper.updateById(walletAccount);
                                        WalletTransactionFlow transactionFlow = new WalletTransactionFlow();
                                        String flowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.ENTERPRISE_FREEZE_OR_UNFREEZE.getCode());
                                        transactionFlow.setTransactionId(flowId).setAccountId(walletAccountId).setOrderId(freezeOrderId).setTransactionType(TransactionTypeEnum.REWARD.getType()).setTransactionDirection(TransactionDirectionTypeEnum.DEBIT.getType()).setAmount(amount).setCurrency(CurrencyTypeEnum.CNY.getType())
                                                .setDescription("入职").setInitiationDate(now).setCompletionDate(now).setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc()).setBalanceBefore(availableBalance).setBalanceAfter(newAvailableAmount);
                                        walletTransactionFlowMapper.insert(walletTransactionFlow);
                                    } else {
                                        log.error("职位id：【{}】，招聘环节：【{}】，获取钱包账户锁超时", jobId, processStep);
                                        throw new YouyaException("获取钱包账户锁超时");
                                    }
                                } catch (InterruptedException e) {
                                    log.error("职位id：【{}】，招聘环节：【{}】，获取钱包账户锁失败，原因：", jobId, processStep, e);
                                    throw new YouyaException("获取钱包账户锁失败");
                                } catch (YouyaException e) {
                                    log.error("职位id：【{}】，招聘环节：【{}】，奖金分配失败，原因：", jobId, processStep, e);
                                    throw e;
                                } catch (Exception e) {
                                    log.error("职位id：【{}】，招聘环节：【{}】，奖金分配失败，原因：", jobId, processStep, e);
                                    throw new YouyaException("奖金分配失败");
                                } finally {
                                    if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                                        walletLock.unlock();
                                    }
                                }
                            } else {
                                log.info("职位id：【{}】，招聘环节：【{}】，企业方需要支付的总奖励金额小于等于0，结束奖金分配", jobId, processStep);
                            }
                            //主任务推入队列5分钟查询一次
                            try {
                                HashMap<String, BindingProperties> delayProperties = mqConfigurationProperties.getDelayProperties();
                                BindingProperties properties = delayProperties.get("job_main_task");
                                rabbitMqUtil.sendDelayedMsg(properties.getExchangeName(), properties.getRoutingKey(), mainTaskId, 300);
                            } catch (Exception e) {
                                log.error("职位id：【{}】，主任务id：【{}】，推送至查询主任务状态队列失败，原因：", jobId, mainTaskId, e);
                                throw new YouyaException("网络异常，请稍后重试");
                            }
                            //子任务推入队列进行奖金支付
                            if (subTasks.size() > 0) {
                                subTasks.forEach(s -> {
                                    try {
                                        HashMap<String, BindingProperties> normalProperties = mqConfigurationProperties.getNormalProperties();
                                        BindingProperties properties = normalProperties.get("job_sub_task");
                                        Long subTaskId = s.getId();
                                        rabbitMqUtil.sendMsg(properties.getExchangeName(), properties.getRoutingKey(), subTaskId);
                                    } catch (Exception e) {
                                        log.error("职位id：【{}】，子任务id：【{}】，推送至奖金队列失败，原因：", jobId, s.getId(), e);
                                        throw new YouyaException("网络异常，请稍后重试");
                                    }
                                });
                            }
                            //平台抽成推入队列
                            if (null != sysCommissionRecord) {
                                try {
                                    HashMap<String, BindingProperties> normalProperties = mqConfigurationProperties.getNormalProperties();
                                    BindingProperties properties = normalProperties.get("sys_commission_payment");
                                    Long id = sysCommissionRecord.getId();
                                    rabbitMqUtil.sendMsg(properties.getExchangeName(), properties.getRoutingKey(), id);
                                } catch (Exception e) {
                                    log.error("职位id：【{}】，平台抽成id：【{}】，推送至平台奖金抽成队列失败，原因：", jobId, sysCommissionRecord.getId(), e);
                                    throw new YouyaException("网络异常，请稍后重试");
                                }
                            }
                        } else {
                            log.info("职位id：【{}】，招聘环节：【{}】，分享链中没有最后分享人或其他分享者，结束奖金分配", jobId, processStep);
                        }
                    } else {
                        log.info("职位id：【{}】，招聘环节：【{}】，分享链中没有分享人，结束奖金分配", jobId, processStep);
                    }
                } else {
                    log.info("职位id：【{}】，招聘环节：【{}】，实算奖金金额小于等于0，结束奖金分配", jobId, processStep);
                }
            } else {
                log.info("职位id：【{}】，招聘环节：【{}】，分配比例小于等于0，结束奖金分配", jobId, processStep);
            }
        } else {
            log.info("职位id：【{}】未设置奖金，结束奖金分配", jobId);
        }
    }

    /**
     * 查询职位子任务执行状态
     *
     * @param mainTaskId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void querySubTaskStatus(Long mainTaskId) {
        log.info("开始查询职位子任务执行状态");
        JobMainTask jobMainTask = jobMainTaskMapper.selectOne(new LambdaQueryWrapper<JobMainTask>().eq(JobMainTask::getId, mainTaskId).eq(JobMainTask::getIsDelete, 0));
        if (null == jobMainTask) {
            log.info("职位主任务id：【{}】，未找到对应记录，停止查询", mainTaskId);
            return;
        }
        Integer status = jobMainTask.getStatus();
        if (TaskStatusEnum.RUNNING.getStatus() != status) {
            log.info("职位主任务id：【{}】，当前状态不是运行中，停止查询", mainTaskId);
        }
        List<JobSubTask> subTasks = jobSubTaskMapper.selectList(new LambdaQueryWrapper<JobSubTask>().eq(JobSubTask::getMainTaskId, mainTaskId).eq(JobSubTask::getIsDelete, 0));
        if (subTasks.size() > 0) {
            long count = subTasks.stream().filter(s -> s.getStatus().equals(TaskStatusEnum.RUNNING.getStatus())).count();
            if (count > 0) {
                //主任务再次推入队列5分钟查询一次
                try {
                    HashMap<String, BindingProperties> delayProperties = mqConfigurationProperties.getDelayProperties();
                    BindingProperties properties = delayProperties.get("job_main_task");
                    rabbitMqUtil.sendDelayedMsg(properties.getExchangeName(), properties.getRoutingKey(), mainTaskId, 300);
                } catch (Exception e) {
                    log.error("职位主任务id：【{}】，推送至查询主任务状态队列失败，原因：", mainTaskId, e);
                    throw new YouyaException("网络异常，请稍后重试");
                }
            } else {
                long failCount = subTasks.stream().filter(s -> s.getStatus().equals(TaskStatusEnum.FAIL.getStatus())).count();
                if (failCount > 0) {
                    jobMainTask.setStatus(TaskStatusEnum.FAIL.getStatus());
                } else {
                    jobMainTask.setStatus(TaskStatusEnum.SUCCESS.getStatus());
                }
                jobMainTaskMapper.updateById(jobMainTask);
                log.info("职位主任务id：【{}】，查询子任务执行状态成功", mainTaskId);
            }
        }
    }
}
