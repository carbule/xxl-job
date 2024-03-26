package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.*;
import com.korant.youya.workplace.enums.huntjob.HuntJobStatusEnum;
import com.korant.youya.workplace.enums.user.UserAccountStatusEnum;
import com.korant.youya.workplace.enums.user.UserAuthenticationStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.huntjob.*;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.expectedposition.PersonalExpectedPositionVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.PersonalExpectedWorkAreaVo;
import com.korant.youya.workplace.pojo.vo.huntjob.*;
import com.korant.youya.workplace.pojo.vo.user.UserPublicInfoVo;
import com.korant.youya.workplace.service.HuntJobService;
import com.korant.youya.workplace.utils.CalculationUtil;
import com.korant.youya.workplace.utils.JwtUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 求职表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
@Slf4j
public class HuntJobServiceImpl extends ServiceImpl<HuntJobMapper, HuntJob> implements HuntJobService {

    @Resource
    private HuntJobMapper huntJobMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private InternalRecommendMapper internalRecommendMapper;

    @Resource
    private EducationExperienceMapper educationExperienceMapper;

    @Resource
    private AttentionHuntJobMapper attentionHuntJobMapper;

    @Resource
    private ExpectedPositionMapper expectedPositionMapper;

    @Resource
    private ExpectedWorkAreaMapper expectedWorkAreaMapper;

    @Resource
    private SysVirtualProductMapper sysVirtualProductMapper;

    @Resource
    private BonusDistributionRuleMapper bonusDistributionRuleMapper;

    @Resource
    private UserWalletAccountMapper userWalletAccountMapper;

    @Resource
    private UserWalletFreezeRecordMapper userWalletFreezeRecordMapper;

    @Resource
    private WalletTransactionFlowMapper walletTransactionFlowMapper;

    @Resource
    private RedissonClient redissonClient;

    private static final String NANJING_CITY_CODE = "320100";

    private static final String HUNT_JOB_ONBOARD_PRODUCT = "hunt_job_onboard";

    private static final String HUNT_JOB_ONBOARD_BONUS_DISTRIBUTION_RULE = "hunt_job_onboard_rule";

    private static final String HUNT_JOB_ONBOARD_FREEZE_DES = "求职发布 - 入职奖励";

    private static final String HUNT_JOB_ONBOARD_UNFREEZE_DES = "求职下架 - 入职奖励";

    /**
     * 查询首页求职信息列表
     *
     * @param listDto
     * @param request
     * @return
     */
    @Override
    public Page<HuntJobHomePageVo> queryHomePageList(HuntJobQueryHomePageListDto listDto, HttpServletRequest request) {
        String token = request.getHeader("token");
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        int count;
        List<HuntJobHomePageVo> list;
        if (StringUtils.isBlank(token)) {
            listDto.setCityCode(NANJING_CITY_CODE);
            count = huntJobMapper.queryHomePageListCount(listDto);
            list = huntJobMapper.queryHomePageList(listDto);
        } else {
            Long userId = null;
            try {
                userId = JwtUtil.getId(token);
            } catch (Exception e) {
                throw new YouyaException("token校验失败");
            }
            LoginUser loginUser = userMapper.selectUserToLoginById(userId);
            if (null == loginUser) return null;
            Long enterpriseId = loginUser.getEnterpriseId();
            count = huntJobMapper.queryHomePageListCountByUserId(userId, enterpriseId, listDto);
            list = huntJobMapper.queryHomePageListByUserId(userId, enterpriseId, listDto);
        }
        list.forEach(s -> s.setOnboardingAward(s.getOnboardingAward().multiply(new BigDecimal("0.01"))));
        Page<HuntJobHomePageVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 根据求职id查询首页求职信息详情
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobHomePageDetailVo queryHomePageDetailById(Long id) {
        Long userId = SpringSecurityUtil.getUserId();
        HuntJobHomePageDetailVo homePageDetailVo = huntJobMapper.queryHomePageDetailById(userId, id);
        homePageDetailVo.setOnboardingAward(homePageDetailVo.getOnboardingAward().multiply(new BigDecimal("0.01")));
        return homePageDetailVo;
    }

    /**
     * 查询hr列表
     *
     * @return
     */
    @Override
    public List<EnterpriseHRVo> queryHRList() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("您好！您尚未加入任何公司，请联系管理员，加入后即可帮助推荐！");
        Long userId = loginUser.getId();
        return huntJobMapper.queryHRList(userId, enterpriseId);
    }

    /**
     * 内推
     *
     * @param recommendDto
     */
    @Override
    public void recommend(HuntJobRecommendDto recommendDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long huntJobId = recommendDto.getHuntJobId();
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, huntJobId).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        Integer status = huntJob.getStatus();
        if (HuntJobStatusEnum.PUBLISHED.getStatus() != status) throw new YouyaException("求职信息暂未发布");
        Long hrId = recommendDto.getHr();
        LoginUser hr = userMapper.selectUserToLoginById(hrId);
        Long hrEnterpriseId = hr.getEnterpriseId();
        if (null == hrEnterpriseId) throw new YouyaException("该用户暂未加入企业，无法向其推荐求职信息");
        Long enterpriseId = loginUser.getEnterpriseId();
        if (!enterpriseId.equals(hrEnterpriseId)) throw new YouyaException("只能向本企业的HR推荐");
        Long userId = loginUser.getId();
        boolean exists = internalRecommendMapper.exists(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getReferee, userId).eq(InternalRecommend::getHuntId, huntJobId).eq(InternalRecommend::getHr, hrId).eq(InternalRecommend::getIsDelete, 0));
        if (exists) throw new YouyaException("您已向该HR推荐过此求职信息，请勿重复推荐");
        InternalRecommend internalRecommend = new InternalRecommend();
        internalRecommend.setHuntId(huntJobId).setReferee(userId).setHr(hrId);
        internalRecommendMapper.insert(internalRecommend);
    }

    /**
     * 根据id查询分享信息
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobShareInfo queryShareInfo(Long id) {
        HuntJobShareInfo shareInfo = huntJobMapper.queryShareInfo(id);
        Long userId = SpringSecurityUtil.getUserId();
        UserPublicInfoVo userPublicInfoVo = userMapper.queryUserPublicInfo(userId);
        shareInfo.setRefereeAvatar(userPublicInfoVo.getAvatar());
        shareInfo.setRefereeLastName(userPublicInfoVo.getLastName());
        shareInfo.setRefereeFirstName(userPublicInfoVo.getFirstName());
        shareInfo.setRefereeGender(userPublicInfoVo.getGender());
        shareInfo.setOnboardingAward(shareInfo.getOnboardingAward().multiply(new BigDecimal("0.01")));
        return shareInfo;
    }

    /**
     * 收藏或取消收藏求职信息
     *
     * @param id
     */
    @Override
    public void collect(Long id) {
        Long userId = SpringSecurityUtil.getUserId();
        boolean exists = attentionHuntJobMapper.exists(new LambdaQueryWrapper<AttentionHuntJob>().eq(AttentionHuntJob::getUid, userId).eq(AttentionHuntJob::getHuntId, id));
        //取消收藏
        if (exists) {
            attentionHuntJobMapper.delete(new LambdaQueryWrapper<AttentionHuntJob>().eq(AttentionHuntJob::getUid, userId).eq(AttentionHuntJob::getHuntId, id));
        } else {//收藏
            AttentionHuntJob a = new AttentionHuntJob();
            a.setUid(userId);
            a.setHuntId(id);
            attentionHuntJobMapper.insert(a);
        }
    }

    /**
     * 查询用户个人求职列表
     *
     * @param personalListDto
     * @return
     */
    @Override
    public Page<HuntJobPersonalVo> queryPersonalList(HuntJobQueryPersonalListDto personalListDto) {
        Long userId = SpringSecurityUtil.getUserId();
        Integer status = personalListDto.getStatus();
        int pageNumber = personalListDto.getPageNumber();
        int pageSize = personalListDto.getPageSize();
        Long count = huntJobMapper.selectCount(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getUid, userId).eq(HuntJob::getStatus, status).eq(HuntJob::getIsDelete, 0));
        List<HuntJobPersonalVo> list = huntJobMapper.queryPersonalList(userId, status, personalListDto);
        list.forEach(s -> s.setOnboardingAward(s.getOnboardingAward().multiply(new BigDecimal("0.01"))));
        Page<HuntJobPersonalVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 校验用户信息
     */
    @Override
    public void checkUserInfo() {
        Long userId = SpringSecurityUtil.getUserId();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId).eq(User::getIsDelete, 0));
        if (null == user) throw new YouyaException("用户未注册或已注销");
        Integer authenticationStatus = user.getAuthenticationStatus();
        if (UserAuthenticationStatusEnum.NOT_CERTIFIED.getStatus() == authenticationStatus)
            throw new YouyaException("请先完成实名认证");
        Integer accountStatus = user.getAccountStatus();
        if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus) throw new YouyaException("账号已被冻结,详情请咨询客服");
        Integer gender = user.getGender();
        if (null == gender) throw new YouyaException("请完善个人信息中性别");
        LocalDate birthday = user.getBirthday();
        if (null == birthday) throw new YouyaException("请完善个人信息中出生日期");
        boolean exists = educationExperienceMapper.exists(new LambdaQueryWrapper<EducationExperience>().eq(EducationExperience::getUid, userId).eq(EducationExperience::getIsDelete, 0));
        if (!exists) throw new YouyaException("请至少补充一条教育经历");
        String startWorkingTime = user.getStartWorkingTime();
        if (null == startWorkingTime) throw new YouyaException("请完善个人信息中开始工作时间");
        int positionCount = expectedPositionMapper.selectCountByUserId(userId);
        if (positionCount <= 0) throw new YouyaException("请至少添加一条意向职位");
        int workAreaCount = expectedWorkAreaMapper.selectCountByUserId(userId);
        if (workAreaCount <= 0) throw new YouyaException("请至少添加一条意向区域");
    }

    /**
     * 查询个人意向职位列表
     *
     * @return
     */
    @Override
    public List<PersonalExpectedPositionVo> queryPersonalExpectedPositionList() {
        Long userId = SpringSecurityUtil.getUserId();
        return huntJobMapper.queryPersonalExpectedPositionList(userId);
    }

    /**
     * 查询个人意向区域列表
     *
     * @return
     */
    @Override
    public List<PersonalExpectedWorkAreaVo> queryPersonalExpectedWorkAreaList() {
        Long userId = SpringSecurityUtil.getUserId();
        return huntJobMapper.queryPersonalExpectedWorkAreaList(userId);
    }

    /**
     * 求职发布预览
     *
     * @return
     */
    @Override
    public HuntJobPublishPreviewVo publishPreview() {
        Long userId = SpringSecurityUtil.getUserId();
        return huntJobMapper.publishPreview(userId);
    }

    /**
     * 创建求职信息
     *
     * @param createDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(HuntJobCreateDto createDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        String phone = loginUser.getPhone();
        Integer authenticationStatus = loginUser.getAuthenticationStatus();
        if (UserAuthenticationStatusEnum.CERTIFIED.getStatus() != authenticationStatus)
            throw new YouyaException("请先完成实名认证再发布求职信息");
        HuntJob huntJob = new HuntJob();
        Long id = IdWorker.getId();
        huntJob.setId(id);
        String amount = createDto.getOnboardingAward();
        if (StringUtils.isNotBlank(amount)) {
            if (CalculationUtil.containsNonNumericCharacter(amount)) throw new YouyaException("请输入有效金额");
            BigDecimal onboardingAward = new BigDecimal(amount);
            if (CalculationUtil.isNegativeNumber(onboardingAward)) throw new YouyaException("奖励金额必须是正数");
            BigDecimal divisor = new BigDecimal("1");
            boolean isPositiveInteger = onboardingAward.remainder(divisor).compareTo(BigDecimal.ZERO) == 0;
            if (!isPositiveInteger) throw new YouyaException("奖励金额必须是1的整数倍");
            BonusDistributionRule bonusDistributionRule = bonusDistributionRuleMapper.selectOne(new LambdaQueryWrapper<BonusDistributionRule>().eq(BonusDistributionRule::getCode, HUNT_JOB_ONBOARD_BONUS_DISTRIBUTION_RULE).eq(BonusDistributionRule::getIsDelete, 0));
            if (null == bonusDistributionRule) throw new YouyaException("求职入职阶段预设奖金分配规则缺失，请联系客服");
            huntJob.setOnboardBonusDistributionRule(HUNT_JOB_ONBOARD_BONUS_DISTRIBUTION_RULE);
            Long walletAccountId = loginUser.getWalletAccountId();
            if (null == walletAccountId) throw new YouyaException("钱包账户不存在");
            BigDecimal totalAward = onboardingAward.multiply(new BigDecimal("100"));
            huntJob.setOnboardingAward(totalAward);
            String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, walletAccountId);
            RLock walletLock = redissonClient.getLock(walletLockKey);
            try {
                boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
                if (tryWalletLock) {
                    UserWalletAccount walletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
                    if (null == walletAccount) throw new YouyaException("钱包账户不存在");
                    Integer accountStatus = walletAccount.getStatus();
                    if (WalletAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                        throw new YouyaException("钱包账户已被冻结，请联系客服");
                    BigDecimal availableBalance = walletAccount.getAvailableBalance();
                    if (availableBalance.compareTo(new BigDecimal("0")) <= 0)
                        throw new YouyaException("当前账户可用余额为0，无法支付推荐奖励");
                    BigDecimal shortfall = availableBalance.subtract(totalAward);
                    if (shortfall.compareTo(new BigDecimal("0")) < 0) {
                        String msg = String.format("钱包账户余额：【%s】元，还差：【%s】元", availableBalance.multiply(new BigDecimal("0.01")), shortfall.abs().multiply(new BigDecimal("0.01")));
                        throw new YouyaException(msg);
                    }
                    LocalDateTime now = LocalDateTime.now();
                    UserWalletFreezeRecord userWalletFreezeRecord = new UserWalletFreezeRecord();
                    userWalletFreezeRecord.setUserWalletId(walletAccountId);
                    userWalletFreezeRecord.setHuntId(id);
                    userWalletFreezeRecord.setAmount(totalAward);
                    userWalletFreezeRecord.setType(WalletFreezeTypeEnum.FREEZE.getType());
                    userWalletFreezeRecord.setOperateTime(now);
                    userWalletFreezeRecordMapper.insert(userWalletFreezeRecord);
                    BigDecimal freezeAmount = walletAccount.getFreezeAmount();
                    walletAccount.setFreezeAmount(freezeAmount.add(totalAward));
                    walletAccount.setAvailableBalance(shortfall);
                    userWalletAccountMapper.updateById(walletAccount);
                    Long walletFreezeRecordId = userWalletFreezeRecord.getId();
                    WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                    walletTransactionFlow.setAccountId(walletAccountId).setOrderId(walletFreezeRecordId).setTransactionType(TransactionTypeEnum.FREEZE_OR_UNFREEZE.getType()).setTransactionDirection(TransactionDirectionTypeEnum.DEBIT.getType()).setAmount(totalAward).setCurrency(CurrencyTypeEnum.CNY.getType())
                            .setDescription(HUNT_JOB_ONBOARD_FREEZE_DES).setInitiationDate(now).setCompletionDate(now).setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc()).setBalanceBefore(availableBalance).setBalanceAfter(shortfall);
                    walletTransactionFlowMapper.insert(walletTransactionFlow);
                } else {
                    log.error("用户：【{}】，创建求职信息，获取钱包账户锁超时", phone);
                    throw new YouyaException("网络异常，请稍后重试");
                }
            } catch (InterruptedException e) {
                log.error("用户：【{}】，创建求职信息，获取钱包账户锁失败，原因：", phone, e);
                throw new YouyaException("网络异常，请稍后重试");
            } catch (YouyaException e) {
                log.error("用户：【{}】，创建求职信息失败", phone);
                throw e;
            } catch (Exception e) {
                log.error("用户：【{}】，创建求职信息失败，原因", phone, e);
                throw new YouyaException("网络异常，请稍后重试");
            } finally {
                if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                    walletLock.unlock();
                }
            }
        }
        Long positionId = createDto.getPositionId();
        Long areaId = createDto.getAreaId();
        String description = createDto.getDescription();
        huntJob.setPositionId(positionId);
        huntJob.setAreaId(areaId);
        huntJob.setDescription(description);
        huntJob.setUid(loginUser.getId());
        huntJob.setStatus(HuntJobStatusEnum.PUBLISHED.getStatus());
        huntJob.setRefreshTime(LocalDateTime.now());
        huntJobMapper.insert(huntJob);
    }

    /**
     * 修改求职信息
     *
     * @param modifyDto
     */
    @Override
    public void modify(HuntJobModifyDto modifyDto) {
        Long id = modifyDto.getId();
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, id).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        Integer status = huntJob.getStatus();
        if (HuntJobStatusEnum.PUBLISHED.getStatus() == status) throw new YouyaException("已发布的职位不可修改");
        String amount = modifyDto.getOnboardingAward();
        if (StringUtils.isNotBlank(amount)) {
            if (CalculationUtil.containsNonNumericCharacter(amount)) throw new YouyaException("请输入有效金额");
            BigDecimal onboardingAward = new BigDecimal(amount);
            if (CalculationUtil.isNegativeNumber(onboardingAward)) throw new YouyaException("奖励金额必须是正数");
            BigDecimal divisor = new BigDecimal("1");
            boolean isPositiveInteger = onboardingAward.remainder(divisor).compareTo(BigDecimal.ZERO) == 0;
            if (!isPositiveInteger) throw new YouyaException("奖励金额必须是1的整数倍");
            huntJob.setOnboardingAward(onboardingAward.multiply(new BigDecimal("100")));
        }
        huntJob.setPositionId(modifyDto.getPositionId());
        huntJob.setAreaId(modifyDto.getAreaId());
        huntJob.setDescription(modifyDto.getDescription());
        huntJobMapper.updateById(huntJob);
    }

    /**
     * 根据id预览求职详细信息
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobDetailsPreviewVo detailsPreview(Long id) {
        Long userId = SpringSecurityUtil.getUserId();
        HuntJobDetailsPreviewVo detailsPreview = huntJobMapper.detailsPreview(userId, id);
        detailsPreview.setOnboardingAward(detailsPreview.getOnboardingAward().multiply(new BigDecimal("0.01")));
        return detailsPreview;
    }

    /**
     * 根据id查询求职信息详情
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobDetailVo detail(Long id) {
        HuntJobDetailVo detail = huntJobMapper.detail(id);
        detail.setOnboardingAward(detail.getOnboardingAward().multiply(new BigDecimal("0.01")));
        return detail;
    }

    /**
     * 根据id关闭职位
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void close(Long id) {
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, id).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        String phone = loginUser.getPhone();
        if (!userId.equals(huntJob.getUid())) throw new YouyaException("非法操作");
        Integer status = huntJob.getStatus();
        if (HuntJobStatusEnum.UNPUBLISHED.getStatus() == status) throw new YouyaException("当前求职信息已关闭");
        BigDecimal onboardingAward = huntJob.getOnboardingAward();
        if (null != onboardingAward) {
            Long walletAccountId = loginUser.getWalletAccountId();
            if (null == walletAccountId) throw new YouyaException("钱包账户不存在");
            String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, walletAccountId);
            RLock walletLock = redissonClient.getLock(walletLockKey);
            try {
                boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
                if (tryWalletLock) {
                    UserWalletAccount walletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
                    if (null == walletAccount) throw new YouyaException("钱包账户不存在");
                    Integer accountStatus = walletAccount.getStatus();
                    if (WalletAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                        throw new YouyaException("钱包账户已被冻结，请联系客服");
                    BigDecimal availableBalance = walletAccount.getAvailableBalance();
                    BigDecimal freezeAmount = walletAccount.getFreezeAmount();
                    LocalDateTime now = LocalDateTime.now();
                    UserWalletFreezeRecord userWalletFreezeRecord = new UserWalletFreezeRecord();
                    userWalletFreezeRecord.setUserWalletId(walletAccountId);
                    userWalletFreezeRecord.setHuntId(id);
                    userWalletFreezeRecord.setAmount(onboardingAward);
                    userWalletFreezeRecord.setType(WalletFreezeTypeEnum.UNFREEZE.getType());
                    userWalletFreezeRecord.setOperateTime(now);
                    userWalletFreezeRecordMapper.insert(userWalletFreezeRecord);
                    walletAccount.setFreezeAmount(freezeAmount.subtract(onboardingAward));
                    walletAccount.setAvailableBalance(availableBalance.add(onboardingAward));
                    userWalletAccountMapper.updateById(walletAccount);
                    Long walletFreezeRecordId = userWalletFreezeRecord.getId();
                    WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                    walletTransactionFlow.setAccountId(walletAccountId).setOrderId(walletFreezeRecordId).setTransactionType(TransactionTypeEnum.FREEZE_OR_UNFREEZE.getType()).setTransactionDirection(TransactionDirectionTypeEnum.CREDIT.getType()).setAmount(onboardingAward).setCurrency(CurrencyTypeEnum.CNY.getType())
                            .setDescription(HUNT_JOB_ONBOARD_UNFREEZE_DES).setInitiationDate(now).setCompletionDate(now).setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc()).setBalanceBefore(availableBalance).setBalanceAfter(availableBalance.add(onboardingAward));
                    walletTransactionFlowMapper.insert(walletTransactionFlow);
                } else {
                    log.error("用户：【{}】，关闭求职信息，获取钱包账户锁超时", phone);
                    throw new YouyaException("网络异常，请稍后重试");
                }
            } catch (InterruptedException e) {
                log.error("用户：【{}】，关闭求职信息，获取钱包账户锁失败，原因：", phone, e);
                throw new YouyaException("网络异常，请稍后重试");
            } catch (YouyaException e) {
                log.error("用户：【{}】，关闭求职信息失败", phone);
                throw e;
            } catch (Exception e) {
                log.error("用户：【{}】，关闭求职信息失败，原因", phone, e);
                throw new YouyaException("网络异常，请稍后重试");
            } finally {
                if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                    walletLock.unlock();
                }
            }
        }
        huntJob.setStatus(HuntJobStatusEnum.UNPUBLISHED.getStatus());
        huntJobMapper.updateById(huntJob);
    }

    /**
     * 根据id发布职位
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long id) {
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, id).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        String phone = loginUser.getPhone();
        if (!userId.equals(huntJob.getUid())) throw new YouyaException("非法操作");
        Integer status = huntJob.getStatus();
        if (HuntJobStatusEnum.PUBLISHED.getStatus() == status) throw new YouyaException("当前求职信息已发布");
        BigDecimal onboardingAward = huntJob.getOnboardingAward();
        if (null != onboardingAward) {
            Long walletAccountId = loginUser.getWalletAccountId();
            if (null == walletAccountId) throw new YouyaException("钱包账户不存在");
            String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, walletAccountId);
            RLock walletLock = redissonClient.getLock(walletLockKey);
            try {
                boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
                if (tryWalletLock) {
                    UserWalletAccount walletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
                    if (null == walletAccount) throw new YouyaException("钱包账户不存在");
                    Integer accountStatus = walletAccount.getStatus();
                    if (WalletAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                        throw new YouyaException("钱包账户已被冻结，请联系客服");
                    BigDecimal availableBalance = walletAccount.getAvailableBalance();
                    if (availableBalance.compareTo(new BigDecimal("0")) <= 0)
                        throw new YouyaException("当前账户可用余额为0，无法支付推荐奖励");
                    BigDecimal shortfall = availableBalance.subtract(onboardingAward);
                    if (shortfall.compareTo(new BigDecimal("0")) < 0) {
                        String msg = String.format("钱包账户余额：【%s】元，还差：【%s】元", availableBalance.multiply(new BigDecimal("0.01")), shortfall.abs().multiply(new BigDecimal("0.01")));
                        throw new YouyaException(msg);
                    }
                    LocalDateTime now = LocalDateTime.now();
                    UserWalletFreezeRecord userWalletFreezeRecord = new UserWalletFreezeRecord();
                    userWalletFreezeRecord.setUserWalletId(walletAccountId);
                    userWalletFreezeRecord.setHuntId(id);
                    userWalletFreezeRecord.setAmount(onboardingAward);
                    userWalletFreezeRecord.setType(WalletFreezeTypeEnum.FREEZE.getType());
                    userWalletFreezeRecord.setOperateTime(now);
                    userWalletFreezeRecordMapper.insert(userWalletFreezeRecord);
                    BigDecimal freezeAmount = walletAccount.getFreezeAmount();
                    walletAccount.setFreezeAmount(freezeAmount.add(onboardingAward));
                    walletAccount.setAvailableBalance(shortfall);
                    userWalletAccountMapper.updateById(walletAccount);
                    Long walletFreezeRecordId = userWalletFreezeRecord.getId();
                    WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                    walletTransactionFlow.setAccountId(walletAccountId).setOrderId(walletFreezeRecordId).setTransactionType(TransactionTypeEnum.FREEZE_OR_UNFREEZE.getType()).setTransactionDirection(TransactionDirectionTypeEnum.DEBIT.getType()).setAmount(onboardingAward).setCurrency(CurrencyTypeEnum.CNY.getType())
                            .setDescription(HUNT_JOB_ONBOARD_FREEZE_DES).setInitiationDate(now).setCompletionDate(now).setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc()).setBalanceBefore(availableBalance).setBalanceAfter(shortfall);
                    walletTransactionFlowMapper.insert(walletTransactionFlow);
                } else {
                    log.error("用户：【{}】，发布求职信息，获取钱包账户锁超时", phone);
                    throw new YouyaException("网络异常，请稍后重试");
                }
            } catch (InterruptedException e) {
                log.error("用户：【{}】，发布求职信息，获取钱包账户锁失败，原因：", phone, e);
                throw new YouyaException("网络异常，请稍后重试");
            } catch (YouyaException e) {
                log.error("用户：【{}】，发布求职信息失败", phone);
                throw e;
            } catch (Exception e) {
                log.error("用户：【{}】，发布求职信息失败，原因", phone, e);
                throw new YouyaException("网络异常，请稍后重试");
            } finally {
                if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                    walletLock.unlock();
                }
            }
        }
        huntJob.setStatus(HuntJobStatusEnum.PUBLISHED.getStatus());
        huntJobMapper.updateById(huntJob);
    }

    /**
     * 删除求职信息
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, id).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        Integer status = huntJob.getStatus();
        if (HuntJobStatusEnum.PUBLISHED.getStatus() == status) throw new YouyaException("已发布的职位不可删除");
        huntJob.setIsDelete(1);
        huntJobMapper.updateById(huntJob);
    }
}
