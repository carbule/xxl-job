package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.OrderStatusEnum;
import com.korant.youya.workplace.enums.TaskStatusEnum;
import com.korant.youya.workplace.enums.TransactionFlowStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.JobSubTaskMapper;
import com.korant.youya.workplace.mapper.SysOrderMapper;
import com.korant.youya.workplace.mapper.UserWalletAccountMapper;
import com.korant.youya.workplace.mapper.WalletTransactionFlowMapper;
import com.korant.youya.workplace.pojo.po.JobSubTask;
import com.korant.youya.workplace.pojo.po.SysOrder;
import com.korant.youya.workplace.pojo.po.UserWalletAccount;
import com.korant.youya.workplace.pojo.po.WalletTransactionFlow;
import com.korant.youya.workplace.service.JobSubTaskService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

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
public class JobSubTaskServiceImpl extends ServiceImpl<JobSubTaskMapper, JobSubTask> implements JobSubTaskService {

    @Resource
    private UserWalletAccountMapper userWalletAccountMapper;

    @Resource
    private SysOrderMapper sysOrderMapper;

    @Resource
    private WalletTransactionFlowMapper walletTransactionFlowMapper;

    @Resource
    private JobSubTaskMapper jobSubTaskMapper;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 职位奖金支付
     *
     * @param subTaskId
     */
    @Override
    public void bonusPayment(Long subTaskId) {
        log.info("开始职位奖金支付流程");
        JobSubTask subTask = jobSubTaskMapper.selectOne(new LambdaQueryWrapper<JobSubTask>().eq(JobSubTask::getId, subTaskId).eq(JobSubTask::getIsDelete, 0));
        if (null == subTask) {
            log.info("职位子任务：【{}】，未找到，奖金支付流程结束", subTaskId);
            return;
        }
        String orderId = subTask.getOrderId();
        String orderLockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, orderId);
        RLock orderLock = redissonClient.getLock(orderLockKey);
        try {
            boolean tryOrderLock = orderLock.tryLock(3, TimeUnit.SECONDS);
            if (tryOrderLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getOrderId, orderId).eq(SysOrder::getIsDelete, 0));
                if (null == sysOrder) {
                    log.info("职位子任务：【{}】，奖金分配订单未找到，奖金支付流程结束", subTaskId);
                    subTask.setStatus(TaskStatusEnum.FAIL.getStatus());
                    jobSubTaskMapper.updateById(subTask);
                    return;
                }
                Integer status = sysOrder.getStatus();
                if (OrderStatusEnum.PENDING_PAYMENT.getStatus() != status) {
                    log.info("职位子任务订单id:【{}】，不是待支付状态，停止处理", orderId);
                }
                WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, orderId).eq(WalletTransactionFlow::getIsDelete, 0));
                if (null == walletTransactionFlow) {
                    log.info("职位子任务：【{}】，订单交易流水未找到，奖金支付流程结束", subTaskId);
                    sysOrder.setStatus(OrderStatusEnum.PAYMENT_FAILED.getStatus());
                    sysOrderMapper.updateById(sysOrder);
                    subTask.setStatus(TaskStatusEnum.FAIL.getStatus());
                    jobSubTaskMapper.updateById(subTask);
                    return;
                }
                Long sellerId = sysOrder.getSellerId();
                String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, sellerId);
                RLock walletLock = redissonClient.getLock(walletLockKey);
                try {
                    boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
                    if (tryWalletLock) {
                        UserWalletAccount userWalletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getId, sellerId).eq(UserWalletAccount::getIsDelete, 0));
                        if (null == userWalletAccount) {
                            log.info("职位子任务：【{}】，用户钱包账户未找到，奖金支付流程结束", subTaskId);
                            sysOrder.setStatus(OrderStatusEnum.PAYMENT_FAILED.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.FAILED.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.FAILED.getStatusDesc());
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            subTask.setStatus(TaskStatusEnum.FAIL.getStatus());
                            jobSubTaskMapper.updateById(subTask);
                            return;
                        }
                        sysOrder.setStatus(OrderStatusEnum.PAID.getStatus());
                        BigDecimal availableBalance = userWalletAccount.getAvailableBalance();
                        sysOrderMapper.updateById(sysOrder);
                        BigDecimal actualAmount = sysOrder.getActualAmount();
                        BigDecimal amount = availableBalance.add(actualAmount);
                        userWalletAccount.setAvailableBalance(amount);
                        userWalletAccountMapper.updateById(userWalletAccount);
                        walletTransactionFlow.setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus());
                        walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc());
                        walletTransactionFlow.setBalanceBefore(availableBalance);
                        walletTransactionFlow.setBalanceAfter(amount);
                        walletTransactionFlow.setCompletionDate(LocalDateTime.now());
                        walletTransactionFlowMapper.updateById(walletTransactionFlow);
                        subTask.setStatus(TaskStatusEnum.SUCCESS.getStatus());
                        jobSubTaskMapper.updateById(subTask);
                        log.info("职位子任务：【{}】，奖金支付完成，流程结束", subTaskId);
                    } else {
                        //获取钱包账户锁超时
                        log.error("职位子任务：【{}】，获取钱包账户锁超时", subTaskId);
                        throw new YouyaException("获取钱包账户锁超时");
                    }
                } catch (InterruptedException e) {
                    //获取钱包账户锁失败
                    log.error("职位子任务：【{}】，获取钱包账户锁失败，原因：", orderId, e);
                    throw new YouyaException("获取钱包账户锁失败");
                } finally {
                    if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                        walletLock.unlock();
                    }
                }
            } else {
                log.error("职位子任务：【{}】，获取订单锁超时", subTaskId);
                throw new YouyaException("获取订单锁超时，职位奖金支付失败");
            }
        } catch (InterruptedException e) {
            log.error("职位子任务：【{}】，获取订单锁失败，原因：", subTaskId, e);
            throw new YouyaException("获取订单锁失败，职位奖金支付失败");
        } finally {
            if (orderLock != null && orderLock.isHeldByCurrentThread()) {
                orderLock.unlock();
            }
        }
    }
}
