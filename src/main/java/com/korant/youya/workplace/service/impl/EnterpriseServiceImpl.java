package com.korant.youya.workplace.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.config.ObsBucketConfig;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.constants.WechatConstant;
import com.korant.youya.workplace.constants.WechatPayConstant;
import com.korant.youya.workplace.enums.*;
import com.korant.youya.workplace.enums.enterprise.EnterpriseAuthStatusEnum;
import com.korant.youya.workplace.enums.enterprisechangetodo.EnterpriseChangeTodoOperateEnum;
import com.korant.youya.workplace.enums.enterprisechangetodo.EnterpriseChangeTodoTypeEnum;
import com.korant.youya.workplace.enums.job.JobAuditStatusEnum;
import com.korant.youya.workplace.enums.job.JobStatusEnum;
import com.korant.youya.workplace.enums.role.RoleEnum;
import com.korant.youya.workplace.enums.user.UserAuthenticationStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.dto.sysorder.CancelOrderDto;
import com.korant.youya.workplace.pojo.dto.sysorder.GeneratePaymentParametersDto;
import com.korant.youya.workplace.pojo.dto.sysorder.QueryClosedOrderListDto;
import com.korant.youya.workplace.pojo.dto.sysorder.QueryOrderListDto;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.enterprise.*;
import com.korant.youya.workplace.pojo.vo.sysorder.SysOrderVo;
import com.korant.youya.workplace.properties.DelayProperties;
import com.korant.youya.workplace.properties.RabbitMqConfigurationProperties;
import com.korant.youya.workplace.service.EnterpriseService;
import com.korant.youya.workplace.utils.*;
import com.obs.services.model.PutObjectResult;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.model.TransactionAmount;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 企业信息表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
@Slf4j
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements EnterpriseService {

    @Resource
    private EnterpriseMapper enterpriseMapper;

    @Resource
    private UserEnterpriseMapper userEnterpriseMapper;

    @Resource
    private EnterpriseChangeTodoMapper enterpriseChangeTodoMapper;

    @Resource
    private EnterpriseInvitationQrCodeMapper enterpriseInvitationQrCodeMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private JobMapper jobMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EnterpriseWalletAccountMapper enterpriseWalletAccountMapper;

    @Resource
    private SysVirtualProductMapper sysVirtualProductMapper;

    @Resource
    private SysOrderMapper sysOrderMapper;

    @Resource
    private WalletTransactionFlowMapper walletTransactionFlowMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RabbitMqUtil rabbitMqUtil;

    @Resource
    private RedissonClient redissonClient;

    @Value("${enterprise_qrcode_url}")
    private String enterpriseQrcodeUrl;

    @Value("${enterprise_recharge_notify_url}")
    private String enterpriseRechargeNotifyUrl;

    @Resource
    RabbitMqConfigurationProperties mqConfigurationProperties;

    private static final String ENTERPRISE_BUCKET = "enterprise";

    private static final String ENTERPRISE_QRCODE_BUCKET = "activity";

    private static final String DEFAULT_LOGO = "https://resources.youyai.cn/svg/door-open.svg";

    private static final String ENTERPRISE_RECHARGE_PRODUCT_CODE = "enterprise_recharge";

    private static final String RECHARGE_DESCRIPTION = "友涯企业充值";

    /**
     * 查询企业hr列表
     *
     * @param queryHRListDto
     * @return
     */
    @Override
    public Page<HRVo> queryHRList(QueryHRListDto queryHRListDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) return null;
        int pageNumber = queryHRListDto.getPageNumber();
        int pageSize = queryHRListDto.getPageSize();
        int count = enterpriseMapper.queryHRListCount(userId, enterpriseId);
        List<HRVo> list = enterpriseMapper.queryHRList(userId, enterpriseId, queryHRListDto);
        Page<HRVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询企业员工列表
     *
     * @param queryEmployeeListDto
     * @return
     */
    @Override
    public Page<EmployeeVo> queryEmployeeList(QueryEmployeeListDto queryEmployeeListDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) return null;
        int pageNumber = queryEmployeeListDto.getPageNumber();
        int pageSize = queryEmployeeListDto.getPageSize();
        int count = enterpriseMapper.queryEmployeeListCount(userId, enterpriseId);
        List<EmployeeVo> list = enterpriseMapper.queryEmployeeList(userId, enterpriseId, queryEmployeeListDto);
        Page<EmployeeVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询转让人员列表
     *
     * @param queryHRListDto
     * @return
     */
    @Override
    public Page<TransferPersonnelVo> queryTransferPersonnelList(QueryTransferPersonnelListDto queryHRListDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) return null;
        int pageNumber = queryHRListDto.getPageNumber();
        int pageSize = queryHRListDto.getPageSize();
        int count = enterpriseMapper.queryTransferPersonnelCount(userId, enterpriseId, queryHRListDto);
        List<TransferPersonnelVo> list = enterpriseMapper.queryTransferPersonnelList(userId, enterpriseId, queryHRListDto);
        Page<TransferPersonnelVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 转让职位
     *
     * @param transferJobDto
     */
    @Override
    public void transferJob(TransferJobDto transferJobDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        Long jobId = transferJobDto.getJobId();
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        if (!userId.equals(job.getUid())) throw new YouyaException("非法操作");
        Long transferUserId = transferJobDto.getUserId();
        LoginUser user = userMapper.selectUserToLoginById(transferUserId);
        if (null == user) throw new YouyaException("用户未注册或已注销");
        Long enterpriseId = user.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("用户未关联企业，无法转让");
        if (!enterpriseId.equals(loginUser.getEnterpriseId())) throw new YouyaException("该用户不是您所在企业员工");
        Integer status = job.getStatus();
        Integer auditStatus = job.getAuditStatus();
        if (JobStatusEnum.PUBLISHED.getStatus() != status || JobAuditStatusEnum.AUDIT_SUCCESS.getStatus() != auditStatus)
            throw new YouyaException("只有已发布且审核通过的职位才可以进行转让");
        jobMapper.transferJob(jobId, transferUserId);
    }

    /**
     * 撤销申请
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revoke(Long id) {
        Long userId = SpringSecurityUtil.getUserId();
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, id).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        Integer authStatus = enterprise.getAuthStatus();
        UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getEnterpriseId, id).eq(UserEnterprise::getIsDelete, 0));
        if (null == userEnterprise) throw new YouyaException("企业管理员缺失");
        if (!userEnterprise.getUid().equals(userId)) throw new YouyaException("非法操作");
        if (EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus() == authStatus) throw new YouyaException("企业已认证无法撤销");
        enterprise.setIsDelete(1);
        enterpriseMapper.updateById(enterprise);
        userEnterprise.setIsDelete(1);
        userEnterpriseMapper.updateById(userEnterprise);
    }

    /**
     * 变更为hr
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeHR(Long id) {
        if (null == id) throw new YouyaException("人员id不能为空");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("未找到企业信息");
        if (!enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getAuthStatus, EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus()).eq(Enterprise::getIsDelete, 0)))
            throw new YouyaException("企业未创建");
        if (!userEnterpriseMapper.exists(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getEnterpriseId, enterpriseId).eq(UserEnterprise::getUid, id).eq(UserEnterprise::getIsDelete, 0)))
            throw new YouyaException("该用户暂未加入企业");
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, RoleEnum.HR.getRole()).eq(Role::getIsDelete, 0));
        if (null == role) throw new YouyaException("预设角色缺失");
        UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, id).eq(UserRole::getIsDelete, 0));
        if (null == userRole) {
            userRole = new UserRole();
            userRole.setUid(id).setRid(role.getId());
            userRoleMapper.insert(userRole);
        } else {
            userRole.setRid(role.getId());
            userRoleMapper.updateById(userRole);
        }
        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
        redisUtil.del(cacheKey);
    }

    /**
     * 变更为员工
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeEmployee(Long id) {
        if (null == id) throw new YouyaException("人员id不能为空");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("未找到企业信息");
        if (!enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getAuthStatus, EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus()).eq(Enterprise::getIsDelete, 0)))
            throw new YouyaException("企业未创建");
        if (!userEnterpriseMapper.exists(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getEnterpriseId, enterpriseId).eq(UserEnterprise::getUid, id).eq(UserEnterprise::getIsDelete, 0)))
            throw new YouyaException("该用户暂未加入企业");
        if (jobMapper.exists(new LambdaQueryWrapper<Job>().eq(Job::getUid, id).eq(Job::getIsDelete, 0))) {
            Long userId = SpringSecurityUtil.getUserId();
            jobMapper.compulsoryTransferJob(id, userId);
        }
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, RoleEnum.EMPLOYEE.getRole()).eq(Role::getIsDelete, 0));
        if (null == role) throw new YouyaException("预设角色缺失");
        UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, id).eq(UserRole::getIsDelete, 0));
        if (null == userRole) {
            userRole = new UserRole();
            userRole.setUid(id).setRid(role.getId());
            userRoleMapper.insert(userRole);
        } else {
            userRole.setRid(role.getId());
            userRoleMapper.updateById(userRole);
        }
        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
        redisUtil.del(cacheKey);
    }

    /**
     * 校验员工是否有职位
     *
     * @param id
     * @return
     */
    @Override
    public boolean checkEmployeeHavePositions(Long id) {
        return jobMapper.exists(new LambdaQueryWrapper<Job>().eq(Job::getUid, id).eq(Job::getIsDelete, 0));
    }

    /**
     * 强制移除员工
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forceRemoveEmployee(Long id) {
        UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, id).eq(UserEnterprise::getIsDelete, 0));
        if (null != userEnterprise) {
            Long enterpriseId = userEnterprise.getEnterpriseId();
            LoginUser loginUser = SpringSecurityUtil.getUserInfo();
            if (!enterpriseId.equals(loginUser.getEnterpriseId())) throw new YouyaException("非法操作");
            if (jobMapper.exists(new LambdaQueryWrapper<Job>().eq(Job::getUid, id).eq(Job::getIsDelete, 0))) {
                Long userId = SpringSecurityUtil.getUserId();
                jobMapper.compulsoryTransferJob(id, userId);
            }
            userEnterprise.setIsDelete(1);
            userEnterpriseMapper.updateById(userEnterprise);
        }
        UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, id).eq(UserRole::getIsDelete, 0));
        if (null != userRole) {
            userRole.setIsDelete(1);
            userRoleMapper.updateById(userRole);
        }
        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
        redisUtil.del(cacheKey);
    }

    /**
     * 转让管理员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferAdministrator(TransferAdministratorDto transferAdministratorDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        Long transferUserId = transferAdministratorDto.getUserId();
        if (userId.equals(transferUserId)) throw new YouyaException("禁止自我转让");
        Long enterpriseId = loginUser.getEnterpriseId();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, transferUserId).eq(User::getIsDelete, 0));
        if (null == user) throw new YouyaException("转让用户未注册");
        UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getEnterpriseId, enterpriseId).eq(UserEnterprise::getUid, transferUserId).eq(UserEnterprise::getIsDelete, 0));
        if (null == userEnterprise) throw new YouyaException("用户未加入企业");
        String role = userMapper.queryUserRoleById(transferUserId);
        if (StringUtils.isBlank(role)) throw new YouyaException("该用户不是HR角色，不可转让");
        if (!RoleEnum.HR.getRole().equals(role)) throw new YouyaException("该用户不是HR角色，不可转让");
        UserRole admin = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, userId).eq(UserRole::getIsDelete, 0));
        UserRole hr = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, transferUserId).eq(UserRole::getIsDelete, 0));
        if (null == admin || null == hr) throw new YouyaException("未找到关联角色，转让失败");
        Role adminRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, RoleEnum.ADMIN.getRole()).eq(Role::getIsDelete, 0));
        Role hrRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, RoleEnum.HR.getRole()).eq(Role::getIsDelete, 0));
        if (null == adminRole || null == hrRole) throw new YouyaException("预设角色缺失");
        admin.setRid(hrRole.getId());
        hr.setRid(adminRole.getId());
        userRoleMapper.updateById(admin);
        userRoleMapper.updateById(hr);
        String adminCacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
        String hrCacheKey = String.format(RedisConstant.YY_USER_CACHE, transferUserId);
        redisUtil.del(adminCacheKey);
        redisUtil.del(hrCacheKey);
    }

    /**
     * 退出企业
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exit() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        String role = loginUser.getRole();
        if (RoleEnum.ADMIN.getRole().equals(role)) throw new YouyaException("请先转让管理员再退出");
        Long enterpriseId = loginUser.getEnterpriseId();
        Long userId = loginUser.getId();
        boolean exists = jobMapper.exists(new LambdaQueryWrapper<Job>().eq(Job::getEnterpriseId, enterpriseId).eq(Job::getUid, userId).eq(Job::getIsDelete, 0));
        if (exists) {
            Long adminId = enterpriseMapper.queryAdminIdByEnterpriseId(enterpriseId);
            if (null == adminId) throw new YouyaException("管理员缺失，无法转让职位");
            jobMapper.compulsoryTransferJob(userId, adminId);
        }
        UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getEnterpriseId, enterpriseId).eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getIsDelete, 0));
        if (null != userEnterprise) {
            userEnterprise.setIsDelete(1);
            userEnterpriseMapper.updateById(userEnterprise);
        }
        UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, userId).eq(UserRole::getIsDelete, 0));
        if (null != userRole) {
            userRole.setIsDelete(1);
            userRoleMapper.updateById(userRole);
            String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
            redisUtil.del(cacheKey);
        }
    }

    /**
     * 获取营业执照信息
     *
     * @param file
     * @return
     */
    @Override
    public JSONObject getBusinessLicenseInfo(MultipartFile file) {
        log.info("进入获取营业执照信息接口。。。");
        if (null == file) throw new YouyaException("文件不能为空");
        String bucketName = ObsBucketConfig.getBucketName(ENTERPRISE_BUCKET);
        String fileName = ObsUtil.getFileBase64Name(file);
        log.info("营业执照图片名称：{}", fileName);
        if (StringUtils.isBlank(fileName)) throw new YouyaException("上传营业执照失败");
        if (!ObsUtil.doesObjectExist(bucketName, fileName)) {
            log.info("营业执照图片不存在 准备上传。。。。");
            PutObjectResult result = ObsUtil.putObject(bucketName, fileName, file);
            if (null == result) throw new YouyaException("上传营业执照失败");
            String etag = result.getEtag();
            String objectKey = result.getObjectKey();
            if (StringUtils.isBlank(etag) && StringUtils.isBlank(objectKey)) throw new YouyaException("上传营业执照失败");
            log.info("营业执照图片上传成功。。。");
        }
        log.info("准备获取营业执照信息。。。");
        String signedUrl = ObsUtil.getSignedUrl(bucketName, fileName);
        String encode = URLEncoder.encode(signedUrl, StandardCharsets.UTF_8);
        JSONObject resultObj = HuaWeiUtil.getLicenseContentByUrl(encode);
        if (null == resultObj) throw new YouyaException("获取营业执照信息失败");
        resultObj.put("objectKey", fileName);
        resultObj.put("signedUrl", signedUrl);
        log.info("营业执照信息获取成功 准备返回响应内容。。。");
        return resultObj;
    }

    /**
     * 创建企业
     *
     * @param createDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(EnterpriseCreateDto createDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Integer authenticationStatus = loginUser.getAuthenticationStatus();
        if (!authenticationStatus.equals(UserAuthenticationStatusEnum.CERTIFIED.getStatus()))
            throw new YouyaException("请先完成实名认证");
        Long userId = loginUser.getId();
        if (userEnterpriseMapper.exists(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getIsDelete, 0)))
            throw new YouyaException("您已绑定过企业或企业创建正在审批中");
        String socialCreditCode = createDto.getSocialCreditCode();
        boolean exists = enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getSocialCreditCode, socialCreditCode).eq(Enterprise::getIsDelete, 0));
        if (exists) throw new YouyaException("该企业已被其他用户注册");
        LocalDate establishDate = createDto.getEstablishDate();
        Date date = Date.from(establishDate.atStartOfDay(ZoneId.of("Asia/Shanghai")).toInstant());
        long between = DateUtil.between(date, new Date(), DateUnit.DAY);
        if (between < 365) throw new YouyaException("成立时间未届满一年的企业不可注册");
        Enterprise enterprise = new Enterprise();
        BeanUtils.copyProperties(createDto, enterprise);
        enterprise.setLogo(DEFAULT_LOGO);
        enterprise.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_IN_PROGRESS.getStatus());
        enterpriseMapper.insert(enterprise);
        UserEnterprise userEnterprise = new UserEnterprise();
        userEnterprise.setUid(userId);
        userEnterprise.setEnterpriseId(enterprise.getId());
        userEnterpriseMapper.insert(userEnterprise);
        EnterpriseWalletAccount enterpriseWalletAccount = new EnterpriseWalletAccount();
        enterpriseWalletAccount.setEnterpriseId(enterprise.getId());
        enterpriseWalletAccount.setAccountBalance(new BigDecimal(0));
        enterpriseWalletAccount.setFreezeAmount(new BigDecimal(0));
        enterpriseWalletAccount.setStatus(0);
        enterpriseWalletAccountMapper.insert(enterpriseWalletAccount);
    }

    /**
     * 查询企业创建失败详情
     *
     * @param id
     * @return
     */
    @Override
    public EnterpriseCreateFailureDetailVo queryCreateFailureDetail(Long id) {
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, id).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        Integer authStatus = enterprise.getAuthStatus();
        if (EnterpriseAuthStatusEnum.AUTH_FAIL.getStatus() != authStatus) return null;
        UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getEnterpriseId, id).eq(UserEnterprise::getIsDelete, 0));
        if (null == userEnterprise) throw new YouyaException("企业管理员缺失");
        Long userId = SpringSecurityUtil.getUserId();
        if (!userEnterprise.getUid().equals(userId)) throw new YouyaException("非法操作");
        EnterpriseCreateFailureDetailVo createFailureDetailVo = enterpriseMapper.queryCreateFailureDetail(id);
        String businessLicense = createFailureDetailVo.getBusinessLicense();
        String businessLicenseEncode = URLEncoder.encode(businessLicense, StandardCharsets.UTF_8);
        String businessLicenseCdnPath = "https://ent.youyai.cn/" + businessLicenseEncode;
        createFailureDetailVo.setBusinessLicense(businessLicenseCdnPath);
        String powerOfAttorney = createFailureDetailVo.getPowerOfAttorney();
        String powerOfAttorneyEncode = URLEncoder.encode(powerOfAttorney, StandardCharsets.UTF_8);
        String powerOfAttorneyCdnPath = "https://ent.youyai.cn/" + powerOfAttorneyEncode;
        createFailureDetailVo.setPowerOfAttorney(powerOfAttorneyCdnPath);
        return createFailureDetailVo;
    }

    /**
     * 重新提交企业信息
     *
     * @param resubmitDto
     */
    @Override
    public void resubmit(EnterpriseResubmitDto resubmitDto) {
        Long id = resubmitDto.getId();
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, id).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        Integer authStatus = enterprise.getAuthStatus();
        if (EnterpriseAuthStatusEnum.AUTH_FAIL.getStatus() != authStatus) throw new YouyaException("企业只有审核失败状态下才能重新提交");
        UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getEnterpriseId, id).eq(UserEnterprise::getIsDelete, 0));
        if (null == userEnterprise) throw new YouyaException("企业管理员缺失");
        Long userId = SpringSecurityUtil.getUserId();
        if (!userEnterprise.getUid().equals(userId)) throw new YouyaException("非法操作");
        enterpriseMapper.resubmit(resubmitDto);
    }

    /**
     * 根据企业名称查询企业
     *
     * @param nameDto
     * @return
     */
    @Override
    public List<EnterpriseByNameVo> queryEnterpriseByName(EnterpriseByNameDto nameDto) {
        return enterpriseMapper.queryEnterpriseByName(nameDto);
    }

    /**
     * 加入企业
     *
     * @param joinDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void join(EnterpriseJoinDto joinDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        if (userEnterpriseMapper.exists(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getIsDelete, 0)))
            throw new YouyaException("您已绑定过企业");
        Long enterpriseId = joinDto.getEnterpriseId();
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        Integer authStatus = enterprise.getAuthStatus();
        if (EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus() != authStatus) throw new YouyaException("企业暂未通过审核，无法加入");
        UserEnterprise userEnterprise = new UserEnterprise();
        userEnterprise.setEnterpriseId(enterpriseId).setUid(userId);
        userEnterpriseMapper.insert(userEnterprise);
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, RoleEnum.EMPLOYEE.getRole()).eq(Role::getIsDelete, 0));
        if (null == role) throw new YouyaException("预设角色缺失");
        UserRole userRole = new UserRole();
        userRole.setUid(userId).setRid(role.getId());
        userRoleMapper.insert(userRole);
        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
        redisUtil.del(cacheKey);
    }

    /**
     * 修改企业logo
     *
     * @param modifyLogoDto
     */
    @Override
    public void modifyLogo(EnterpriseModifyLogoDto modifyLogoDto) {
        Long id = modifyLogoDto.getId();
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (!id.equals(enterpriseId)) throw new YouyaException("非法操作");
        enterpriseMapper.modifyLogo(modifyLogoDto);
    }

    /**
     * 修改企业
     *
     * @param modifyDto
     */
    @Override
    public void modify(EnterpriseModifyDto modifyDto) {
        Long id = modifyDto.getId();
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (!id.equals(enterpriseId)) throw new YouyaException("非法操作");
        enterpriseMapper.modify(modifyDto);
    }

    /**
     * 根据当前登陆用户查询企业信息
     *
     * @return
     */
    @Override
    public EnterpriseInfoByLoginUserVo queryEnterpriseInfoByLoginUser() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getIsDelete, 0));
        //未认证
        if (null == userEnterprise) {
            //没有认证也没有加入
            return null;
        } else {
            Long enterpriseId = userEnterprise.getEnterpriseId();
            Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
            if (null == enterprise) return null;
            Integer authStatus = enterprise.getAuthStatus();
            //管理员
            if (EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus() != authStatus) {
                EnterpriseInfoByLoginUserVo loginUserVo = enterpriseMapper.queryEnterpriseInfoByAdmin(enterpriseId);
                loginUserVo.setRole(RoleEnum.ADMIN.getRole());
                return loginUserVo;
            } else {
                String role = userMapper.queryUserRoleById(userId);
                if (RoleEnum.ADMIN.getRole().equals(role)) {
                    EnterpriseInfoByLoginUserVo loginUserVo = enterpriseMapper.queryEnterpriseInfoByAdmin(enterpriseId);
                    loginUserVo.setRole(RoleEnum.ADMIN.getRole());
                    return loginUserVo;
                } else if (RoleEnum.HR.getRole().equals(role)) {
                    EnterpriseInfoByLoginUserVo loginUserVo = enterpriseMapper.queryEnterpriseInfoByHR(userId, enterpriseId);
                    loginUserVo.setRole(RoleEnum.HR.getRole());
                    return loginUserVo;
                } else {
                    EnterpriseInfoByLoginUserVo loginUserVo = enterpriseMapper.queryEnterpriseInfoByEmployee(userId, enterpriseId);
                    loginUserVo.setRole(RoleEnum.EMPLOYEE.getRole());
                    return loginUserVo;
                }
            }
        }
    }

    /**
     * 查询企业结构信息
     *
     * @return
     */
    @Override
    public EnterpriseStructureInfoVo queryEnterpriseStructureInfo() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        String role = loginUser.getRole();
        //管理员
        if (RoleEnum.ADMIN.getRole().equals(role)) {
            return enterpriseMapper.queryEnterpriseStructureInfoByAdmin(enterpriseId);
        } else {
            //hr或者员工
            return enterpriseMapper.queryEnterpriseStructureInfoByHROrEmployee(enterpriseId);
        }
    }

    /**
     * 查询企业基础信息
     *
     * @return
     */
    @Override
    public EnterpriseBasicInfoVo queryEnterpriseBasicInfo() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        return enterpriseMapper.queryEnterpriseBasicInfo(enterpriseId);
    }

    /**
     * 查询企业营业执照信息
     *
     * @return
     */
    @Override
    public EnterpriseBusinessLicenseVo queryEnterpriseBusinessLicense() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        EnterpriseBusinessLicenseVo enterpriseBusinessLicense = enterpriseMapper.queryEnterpriseBusinessLicense(enterpriseId);
        String businessLicense = enterpriseBusinessLicense.getBusinessLicense();
        String encode = URLEncoder.encode(businessLicense, StandardCharsets.UTF_8);
        String cdnPath = "https://ent.youyai.cn/" + encode;
        enterpriseBusinessLicense.setBusinessLicense(cdnPath);
        return enterpriseBusinessLicense;
    }

    /**
     * 变更企业信息
     *
     * @param changeDto
     */
    @Override
    public void changeEnterprise(EnterpriseChangeDto changeDto) {
        Integer changeType = changeDto.getChangeType();
        if (EnterpriseChangeTodoTypeEnum.NAME_CHANGE.getType() != changeType && EnterpriseChangeTodoTypeEnum.ADDRESS_CHANGE.getType() != changeType)
            throw new YouyaException("非法的变更类型");
        Long id = changeDto.getEnterpriseId();
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (!id.equals(enterpriseId)) throw new YouyaException("非法操作");
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, id).eq(Enterprise::getAuthStatus, EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus()).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        String socialCreditCode = enterprise.getSocialCreditCode();
        if (!socialCreditCode.equals(changeDto.getSocialCreditCode()))
            throw new YouyaException("统一社会信用码不一致，请重新上传");
        //名称变更
        if (EnterpriseChangeTodoTypeEnum.NAME_CHANGE.getType() == changeType) {
            boolean exists = enterpriseChangeTodoMapper.exists(new LambdaQueryWrapper<EnterpriseChangeTodo>().eq(EnterpriseChangeTodo::getEnterpriseId, id).eq(EnterpriseChangeTodo::getType, EnterpriseChangeTodoTypeEnum.NAME_CHANGE.getType()).eq(EnterpriseChangeTodo::getOperate, EnterpriseChangeTodoOperateEnum.PENDING_REVIEW.getOperate()).eq(EnterpriseChangeTodo::getIsDelete, 0));
            if (exists) throw new YouyaException("已有正在审核中的名称变更申请");
            if (enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().ne(Enterprise::getId, id).eq(Enterprise::getName, changeDto.getName()).eq(Enterprise::getIsDelete, 0)))
                throw new YouyaException("更名企业名称已存在");
            EnterpriseChangeTodo changeTodo = new EnterpriseChangeTodo();
            BeanUtils.copyProperties(changeDto, changeTodo);
            changeTodo.setType(EnterpriseChangeTodoTypeEnum.NAME_CHANGE.getType());
            changeTodo.setOperate(EnterpriseChangeTodoOperateEnum.PENDING_REVIEW.getOperate());
            enterpriseChangeTodoMapper.insert(changeTodo);
        } else {
            //地址变更
            boolean exists = enterpriseChangeTodoMapper.exists(new LambdaQueryWrapper<EnterpriseChangeTodo>().eq(EnterpriseChangeTodo::getEnterpriseId, id).eq(EnterpriseChangeTodo::getType, EnterpriseChangeTodoTypeEnum.ADDRESS_CHANGE.getType()).eq(EnterpriseChangeTodo::getOperate, EnterpriseChangeTodoOperateEnum.PENDING_REVIEW.getOperate()).eq(EnterpriseChangeTodo::getIsDelete, 0));
            if (exists) throw new YouyaException("已有正在审核中的地址变更申请");
            EnterpriseChangeTodo changeTodo = new EnterpriseChangeTodo();
            BeanUtils.copyProperties(changeDto, changeTodo);
            changeTodo.setType(EnterpriseChangeTodoTypeEnum.ADDRESS_CHANGE.getType());
            changeTodo.setOperate(EnterpriseChangeTodoOperateEnum.PENDING_REVIEW.getOperate());
            enterpriseChangeTodoMapper.insert(changeTodo);
        }
    }

    /**
     * 获取企业分享信息
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EnterpriseSharedInfoVo getSharedInfo() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) return null;
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        Integer authStatus = enterprise.getAuthStatus();
        if (EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus() != authStatus) throw new YouyaException("请等待企业审核通过后再使用");
        long id = IdWorker.getId();
        try {
            InputStream inputStream = QrCodeUtil.getQrCode(enterpriseQrcodeUrl + id, 180, 180);
            String bucketName = ObsBucketConfig.getBucketName(ENTERPRISE_QRCODE_BUCKET);
            String path = "enterprise_qrcode/";
            PutObjectResult result = ObsUtil.putObject(bucketName, path, "jpg", inputStream);
            if (null == result) throw new YouyaException("上传文件失败");
            String etag = result.getEtag();
            String objectUrl = result.getObjectUrl();
            String objectKey = result.getObjectKey();
            if (StringUtils.isBlank(etag) && StringUtils.isBlank(objectUrl)) throw new YouyaException("上传文件失败");
            String encode = URLEncoder.encode(objectKey, StandardCharsets.UTF_8);
            EnterpriseInvitationQrCode invitationQrCode = new EnterpriseInvitationQrCode();
            invitationQrCode.setId(id);
            invitationQrCode.setEnterpriseId(enterpriseId);
            invitationQrCode.setMd5FileName(objectKey);
            enterpriseInvitationQrCodeMapper.insert(invitationQrCode);
            HashMap<String, DelayProperties> delayProperties = mqConfigurationProperties.getDelayProperties();
            DelayProperties properties = delayProperties.get("enterprise_qrcode");
            rabbitMqUtil.sendDelayedMsg(properties.getExchangeName(), properties.getRoutingKey(), id, 604800);
            String qrcodeUrl = "https://" + ObsBucketConfig.getCdn(ENTERPRISE_QRCODE_BUCKET) + "/" + encode;
            EnterpriseSharedInfoVo sharedInfoVo = new EnterpriseSharedInfoVo();
            sharedInfoVo.setAvatar(loginUser.getAvatar());
            sharedInfoVo.setLastName(loginUser.getLastName());
            sharedInfoVo.setFirstName(loginUser.getFirstName());
            sharedInfoVo.setEnterpriseName(enterprise.getName());
            sharedInfoVo.setQrcodeUrl(qrcodeUrl);
            return sharedInfoVo;
        } catch (Exception e) {
            log.error("企业：{},获取邀请二维码失败,异常信息:{}", enterpriseId, e);
            throw new YouyaException("获取邀请二维码失败，请稍后再试");
        }
    }

    /**
     * 上传分享图片
     *
     * @param file
     */
    @Override
    public String uploadShareImage(MultipartFile file) {
        if (null == file) throw new YouyaException("分享图片不能为空");
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new YouyaException("文件流获取失败");
        }
        String bucketName = ObsBucketConfig.getBucketName(ENTERPRISE_QRCODE_BUCKET);
        String path = "enterprise_qrcode/";
        PutObjectResult result = ObsUtil.putObject(bucketName, path, "jpg", inputStream);
        if (null == result) throw new YouyaException("上传文件失败");
        String etag = result.getEtag();
        String objectUrl = result.getObjectUrl();
        String objectKey = result.getObjectKey();
        if (StringUtils.isBlank(etag) && StringUtils.isBlank(objectUrl)) throw new YouyaException("上传文件失败");
        HashMap<String, DelayProperties> delayProperties = mqConfigurationProperties.getDelayProperties();
        DelayProperties properties = delayProperties.get("enterprise_shareImage");
        rabbitMqUtil.sendDelayedMsg(properties.getExchangeName(), properties.getRoutingKey(), objectKey, 7200);
        String encode = URLEncoder.encode(objectKey, StandardCharsets.UTF_8);
        return "https://" + ObsBucketConfig.getCdn(ENTERPRISE_QRCODE_BUCKET) + "/" + encode;
    }

    /**
     * 根据二维码id查询企业信息
     *
     * @param qrcodeIdDto
     * @return
     */
    @Override
    public EnterpriseInfoByQrcodeIdVo queryEnterpriseInfoByQrcodeId(QueryEnterpriseInfoByQrcodeIdDto qrcodeIdDto) {
        Long qrcodeId = qrcodeIdDto.getQrcodeId();
        EnterpriseInvitationQrCode qrCode = enterpriseInvitationQrCodeMapper.selectOne(new LambdaQueryWrapper<EnterpriseInvitationQrCode>().eq(EnterpriseInvitationQrCode::getId, qrcodeId).eq(EnterpriseInvitationQrCode::getIsDelete, 0));
        if (null == qrCode) throw new YouyaException("二维码已失效");
        Long enterpriseId = qrCode.getEnterpriseId();
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getAuthStatus, EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus()).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        EnterpriseInfoByQrcodeIdVo qrcodeIdVo = new EnterpriseInfoByQrcodeIdVo();
        qrcodeIdVo.setId(enterpriseId);
        qrcodeIdVo.setEnterpriseName(enterprise.getName());
        return qrcodeIdVo;
    }

    /**
     * 删除邀请二维码
     *
     * @param id
     */
    @Override
    public void deleteInvitationQrcode(Long id) {
        EnterpriseInvitationQrCode qrCode = enterpriseInvitationQrCodeMapper.selectOne(new LambdaQueryWrapper<EnterpriseInvitationQrCode>().eq(EnterpriseInvitationQrCode::getId, id).eq(EnterpriseInvitationQrCode::getIsDelete, 0));
        if (null != qrCode) {
            String md5FileName = qrCode.getMd5FileName();
            String bucketName = ObsBucketConfig.getBucketName(ENTERPRISE_QRCODE_BUCKET);
            ObsUtil.deleteObject(bucketName, md5FileName);
            qrCode.setIsDelete(1);
            enterpriseInvitationQrCodeMapper.updateById(qrCode);
        }
    }

    /**
     * 删除分享图片
     *
     * @param objectKey
     */
    @Override
    public void deleteShareImage(String objectKey) {
        String bucketName = ObsBucketConfig.getBucketName(ENTERPRISE_QRCODE_BUCKET);
        ObsUtil.deleteObject(bucketName, objectKey);
    }

    /**
     * 查询企业钱包信息
     *
     * @return
     */
    @Override
    public EnterpriseWalletVo queryEnterpriseWalletInfo() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) return null;
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        Integer authStatus = enterprise.getAuthStatus();
        if (EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus() != authStatus) throw new YouyaException("请等待企业审核通过后再使用");
        EnterpriseWalletAccount enterpriseWalletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getEnterpriseId, enterpriseId).eq(EnterpriseWalletAccount::getIsDelete, 0));
        if (null == enterpriseWalletAccount) throw new YouyaException("钱包账户不存在");
        Integer status = enterpriseWalletAccount.getStatus();
        if (WalletAccountStatusEnum.FROZEN.getStatus() == status) throw new YouyaException("钱包账户已被冻结，请联系客服");
        //账户总额
        BigDecimal accountBalance = enterpriseWalletAccount.getAccountBalance();
        //冻结金额
        BigDecimal freezeAmount = enterpriseWalletAccount.getFreezeAmount();
        //可用余额
        BigDecimal availableBalance = accountBalance.subtract(freezeAmount);
        EnterpriseWalletVo enterpriseWalletVo = new EnterpriseWalletVo();
        enterpriseWalletVo.setAccountBalance(accountBalance.multiply(new BigDecimal("0.01")));
        enterpriseWalletVo.setFreezeAmount(freezeAmount.multiply(new BigDecimal("0.01")));
        enterpriseWalletVo.setAvailableBalance(availableBalance.multiply(new BigDecimal("0.01")));
        return enterpriseWalletVo;
    }

    /**
     * 企业微信充值
     *
     * @param enterpriseRechargeDto
     * @return
     */
    @Override
    public JSONObject recharge(EnterpriseRechargeDto enterpriseRechargeDto) {
        log.info("收到企业充值请求");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("当前账号未关联企业");
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        Integer authStatus = enterprise.getAuthStatus();
        if (EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus() != authStatus) throw new YouyaException("请等待企业审核通过后再使用");
        EnterpriseWalletAccount enterpriseWalletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getEnterpriseId, enterpriseId).eq(EnterpriseWalletAccount::getIsDelete, 0));
        if (null == enterpriseWalletAccount) throw new YouyaException("钱包账户不存在");
        Integer status = enterpriseWalletAccount.getStatus();
        if (WalletAccountStatusEnum.FROZEN.getStatus() == status) throw new YouyaException("钱包账户已被冻结，请联系客服");
        String enterpriseName = enterprise.getName();
        String code = enterpriseRechargeDto.getCode();
        Integer quantity = enterpriseRechargeDto.getQuantity();
        SysVirtualProduct virtualProduct = sysVirtualProductMapper.selectOne(new LambdaQueryWrapper<SysVirtualProduct>().eq(SysVirtualProduct::getCode, ENTERPRISE_RECHARGE_PRODUCT_CODE).eq(SysVirtualProduct::getIsDelete, 0));
        if (null == virtualProduct) throw new YouyaException("充值商品不存在");
        Long productId = virtualProduct.getId();
        BigDecimal price = virtualProduct.getPrice();
        BigDecimal multiply = price.multiply(new BigDecimal(quantity));
        int totalAmount = multiply.intValue();
        log.info("企业名称：【{}】，企业id：【{}】，购买商品id：【{}】，单价：【{}】，数量：【{}】，总金额：【{}】", enterpriseName, enterpriseId, productId, price, quantity, totalAmount);
        //todo 放开最低充值限制
        //if (totalAmount < 100) throw new YouyaException("最低充值金额为1元");
        Long walletAccountId = enterpriseWalletAccount.getId();
        SysOrder sysOrder = new SysOrder();
        sysOrder.setSysProductId(productId).setBuyerId(walletAccountId).setType(OrderTypeEnum.VIRTUAL_PRODUCT.getType()).setPaymentMethod(PaymentMethodTypeEnum.WECHAT_PAYMENT.getType()).setOrderDate(LocalDateTime.now())
                .setQuantity(quantity).setTotalAmount(multiply).setActualAmount(multiply).setCurrency(CurrencyTypeEnum.CNY.getType()).setStatus(OrderStatusEnum.PENDING_PAYMENT.getStatus());
        sysOrderMapper.insert(sysOrder);
        String openid = WeChatUtil.code2Session(code);
        if (StringUtils.isBlank(openid)) throw new YouyaException("用户微信openid获取失败");
        if (StringUtils.isBlank(enterpriseRechargeNotifyUrl)) throw new YouyaException("支付通知地址获取失败");
        Long orderId = sysOrder.getId();
        PrepayWithRequestPaymentResponse response = WechatPayUtil.prepayWithRequestPayment(RECHARGE_DESCRIPTION, orderId.toString(), enterpriseRechargeNotifyUrl, totalAmount, openid);
        if (null == response) throw new YouyaException("充值下单失败，请稍后重试");
        log.info("企业名称：【{}】，企业id：【{}】，购买商品id：【{}】，小程序下单并生成调起支付参数成功，微信响应报文：【{}】", enterpriseName, enterpriseId, productId, response);
        WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
        walletTransactionFlow.setAccountId(walletAccountId).setProductId(productId).setOrderId(orderId).setTransactionType(TransactionTypeEnum.RECHARGE.getType()).setTransactionDirection(TransactionDirectionTypeEnum.CREDIT.getType()).setAmount(new BigDecimal(totalAmount)).setCurrency(CurrencyTypeEnum.CNY.getType())
                .setDescription(RECHARGE_DESCRIPTION).setInitiationDate(LocalDateTime.now()).setStatus(TransactionFlowStatusEnum.PENDING.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.PENDING.getStatusDesc());
        walletTransactionFlowMapper.insert(walletTransactionFlow);
        try {
            HashMap<String, DelayProperties> delayProperties = mqConfigurationProperties.getDelayProperties();
            DelayProperties properties = delayProperties.get("enterprise_order_timeout");
            rabbitMqUtil.sendDelayedMsg(properties.getExchangeName(), properties.getRoutingKey(), orderId, 900);
        } catch (Exception e) {
            log.error("企业名称：【{}】，企业id：【{}】，购买商品id：【{}】，推送至订单超时队列失败，原因：", enterpriseName, enterpriseId, orderId, e);
            log.error("企业名称：【{}】，企业id：【{}】，购买商品id：【{}】，下单失败", enterpriseName, enterpriseId, orderId);
            throw new YouyaException("网络异常，请稍后重试");
        }
        JSONObject result = new JSONObject();
        result.put("timeStamp", response.getTimeStamp());
        result.put("nonceStr", response.getNonceStr());
        result.put("package", response.getPackageVal());
        result.put("signType", response.getSignType());
        result.put("paySign", response.getPaySign());
        result.put("orderId", orderId);
        log.info("企业名称：【{}】，企业id：【{}】，购买商品id：【{}】，下单成功", enterpriseName, enterpriseId, productId);
        return result;
    }

    /**
     * 订单超时处理
     *
     * @param orderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderTimeoutProcessing(Long orderId) {
        log.info("企业订单id:【{}】开始进行超时处理", orderId);
        String lockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, orderId);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean tryLock = lock.tryLock(3, TimeUnit.SECONDS);
            if (tryLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
                if (null != sysOrder) {
                    Integer status = sysOrder.getStatus();
                    if (OrderStatusEnum.PENDING_PAYMENT.getStatus() == status) {
                        Long buyerId = sysOrder.getBuyerId();
                        EnterpriseWalletAccount enterpriseWalletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getId, buyerId).eq(EnterpriseWalletAccount::getIsDelete, 0));
                        if (null != enterpriseWalletAccount) {
                            WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, orderId).eq(WalletTransactionFlow::getIsDelete, 0));
                            if (null != walletTransactionFlow) {
                                sysOrder.setStatus(OrderStatusEnum.PAYMENT_TIMEOUT.getStatus());
                                sysOrderMapper.updateById(sysOrder);
                                BigDecimal accountBalance = enterpriseWalletAccount.getAccountBalance();
                                walletTransactionFlow.setBalanceBefore(accountBalance);
                                walletTransactionFlow.setBalanceAfter(accountBalance);
                                walletTransactionFlow.setStatus(TransactionFlowStatusEnum.EXPIRED.getStatus());
                                walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.EXPIRED.getStatusDesc());
                                walletTransactionFlowMapper.updateById(walletTransactionFlow);
                                try {
                                    HashMap<String, DelayProperties> delayProperties = mqConfigurationProperties.getDelayProperties();
                                    DelayProperties properties = delayProperties.get("close_enterprise_order");
                                    rabbitMqUtil.sendDelayedMsg(properties.getExchangeName(), properties.getRoutingKey(), orderId, 600);
                                } catch (Exception e) {
                                    log.error("企业订单id：【{}】，推送至关闭订单队列失败，原因：", orderId, e);
                                    log.error("企业订单id:【{}】超时处理失败", orderId);
                                    throw new YouyaException("网络异常，请稍后重试");
                                }
                                log.info("企业订单id:【{}】超时处理成功", orderId);
                            } else {
                                log.info("企业订单id:【{}】未找到对应交易流水，停止处理", orderId);
                            }
                        } else {
                            log.info("企业订单id:【{}】未找到对应钱包账户，停止处理", orderId);
                        }
                    } else {
                        log.info("企业订单id:【{}】不是待支付状态，停止处理", orderId);
                    }
                }
            } else {
                log.error("企业订单id：【{}】,获取订单锁超时", orderId);
                throw new YouyaException("获取订单锁超时，订单超时处理失败");
            }
        } catch (InterruptedException e) {
            log.error("企业订单id：【{}】,获取订单锁失败，原因：", orderId, e);
            throw new YouyaException("获取订单锁失败，订单超时处理失败");
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 企业完成支付
     *
     * @param completePaymentDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    //todo 如果当前状态为待支付完成支付操作更改状态成功后将其推入到队列 每隔5分钟查询一次订单状态
    public void completePayment(EnterpriseCompletePaymentDto completePaymentDto) {
        log.info("收到企业完成支付请求");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("当前账号未关联企业");
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        EnterpriseWalletAccount enterpriseWalletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getEnterpriseId, enterpriseId).eq(EnterpriseWalletAccount::getIsDelete, 0));
        if (null == enterpriseWalletAccount) throw new YouyaException("钱包账户不存在");
        String enterpriseName = enterprise.getName();
        Long orderId = completePaymentDto.getOrderId();
        String lockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, orderId);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean tryLock = lock.tryLock(3, TimeUnit.SECONDS);
            if (tryLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
                if (null != sysOrder) {
                    Long buyerId = sysOrder.getBuyerId();
                    Long walletAccountId = enterpriseWalletAccount.getId();
                    if (buyerId.equals(walletAccountId)) {
                        Integer status = sysOrder.getStatus();
                        if (OrderStatusEnum.PENDING_PAYMENT.getStatus() == status) {
                            sysOrder.setStatus(OrderStatusEnum.PROCESSING_PAYMENT.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, orderId).eq(WalletTransactionFlow::getIsDelete, 0));
                            if (null == walletTransactionFlow) throw new YouyaException("订单交易流水不存在");
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.PROCESSING.getStatus());
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            log.info("企业名称：【{}】，企业id：【{}】，订单id：【{}】，完成支付操作", enterpriseName, enterpriseId, orderId);
                        } else {
                            log.info("企业名称：【{}】，企业id：【{}】，订单id：【{}】，当前不是待支付状态，不再进行更新操作", enterpriseName, enterpriseId, orderId);
                        }
                    }
                } else {
                    log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，不存在", enterpriseName, enterpriseId, orderId);
                }
            } else {
                log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，获取订单锁超时", enterpriseName, enterpriseId, orderId);
            }
        } catch (InterruptedException e) {
            log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，获取订单锁失败，原因：", enterpriseName, enterpriseId, orderId, e);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 企业充值通知
     * （通知频率为15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h - 总计 24h4m）
     *
     * @param request
     * @param response
     */
    @Override
    public void rechargeNotify(HttpServletRequest request, HttpServletResponse response) {
        log.info("收到友涯微信企业支付通知回调请求");
        String serial = request.getHeader("Wechatpay-Serial");
        if (StringUtils.isBlank(serial)) {
            log.error("微信支付通知缺失证书序列号");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失证书序列号");
            return;
        }
        if (serial.equals(WechatPayConstant.MERCHANT_SERIAL_NUMBER)) {
            log.error("微信支付通知证书序列号错误");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知证书序列号错误");
            return;
        }
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        if (StringUtils.isBlank(timestamp)) {
            log.error("微信支付通知缺失时间戳");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失时间戳");
            return;
        }
        log.info("微信支付通知请求头中参数Wechatpay-Timestamp原值为：【{}】", timestamp);
        long currentTimestamp = Instant.now().getEpochSecond();
        log.info("友涯系统当前时间Timestamp原值为：【{}】", currentTimestamp);
        long notifyTimestamp = Long.parseLong(timestamp);
        //将时间戳转换为Instant对象
        Instant instant1 = Instant.ofEpochSecond(currentTimestamp);
        Instant instant2 = Instant.ofEpochSecond(notifyTimestamp);
        //计算两个时间戳之间的Duration
        Duration duration = Duration.between(instant1, instant2);
        //获取相差的分钟数
        long minutesApart = duration.toMinutes();
        log.info("微信支付通知回调时间对比当前系统时间相差：【{}】分钟", minutesApart);
        if (minutesApart > 5) {
            log.error("微信支付通知时间对比友涯当前系统时间相差超过5分钟，不予处理");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知时间对比友涯当前系统时间相差超过5分钟，不予处理");
            return;
        }
        String nonce = request.getHeader("Wechatpay-Nonce");
        if (StringUtils.isBlank(nonce)) {
            log.error("微信支付通知缺失应答随机串");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失应答随机串");
            return;
        }
        String signature = request.getHeader("Wechatpay-Signature");
        if (StringUtils.isBlank(signature)) {
            log.error("微信支付通知缺少应答签名");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺少应答签名");
            return;
        }
        String requestBody;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder requestBodyBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBodyBuilder.append(line);
            }
            reader.close();
            requestBody = requestBodyBuilder.toString();
            log.info("获取友涯微信用户支付通知回调请求报文成功");
        } catch (Exception e) {
            log.error("获取友涯微信用户支付通知回调请求报文失败");
            writeToWechatPayNotifyResponseErrorMessage(response, "获取友涯微信用户支付通知回调请求报文失败");
            return;
        }
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(serial)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .body(requestBody)
                .build();
        Transaction transaction = null;
        try {
            transaction = WechatPayUtil.parse(requestParam);
        } catch (Exception e) {
            log.error("微信支付通知回调报文验签解密失败,原因：", e);
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知回调报文验签解密失败");
            return;
        }
        if (null == transaction) {
            log.error("微信支付通知回调报文为空");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知回调报文为空");
            return;
        }
        log.info("友涯微信企业支付通知回调请求验签解密成功，回调明文：【{}】", transaction);
        String appid = transaction.getAppid();
        String mchid = transaction.getMchid();
        if (StringUtils.isBlank(appid) || StringUtils.isBlank(mchid)) {
            log.error("微信支付通知appid或商户号缺失");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知appid或商户号缺失");
            return;
        }
        if (!appid.equals(WechatConstant.APP_ID) || !mchid.equals(WechatPayConstant.MERCHANT_ID)) {
            log.error("微信支付通知appid或商户号不匹配");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知appid或商户号不匹配");
            return;
        }
        String outTradeNo = transaction.getOutTradeNo();
        if (StringUtils.isBlank(outTradeNo)) {
            log.error("微信支付通知缺失商户订单号");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失商户订单号");
            return;
        }
        String tradeStateDesc = transaction.getTradeStateDesc();
        if (StringUtils.isBlank(tradeStateDesc)) {
            log.error("微信支付通知缺失交易状态描述");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失交易状态描述");
            return;
        }
        String transactionId = transaction.getTransactionId();
        if (StringUtils.isBlank(transactionId)) {
            log.error("微信支付通知缺失微信支付系统订单号");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失微信支付系统订单号");
            return;
        }
        String successTime = transaction.getSuccessTime();
        if (StringUtils.isBlank(successTime)) {
            log.error("微信支付通知缺失支付完成时间");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失支付完成时间");
            return;
        }
        String orderLockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, outTradeNo);
        RLock orderLock = redissonClient.getLock(orderLockKey);
        try {
            boolean tryOrderLock = orderLock.tryLock(3, TimeUnit.SECONDS);
            if (tryOrderLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, Long.valueOf(outTradeNo)).eq(SysOrder::getIsDelete, 0));
                if (null == sysOrder) {
                    log.error("友涯系统不存在此笔订单");
                    writeToWechatPayNotifyResponseErrorMessage(response, "友涯系统不存在此笔订单");
                    return;
                }
                Long sysOrderId = sysOrder.getId();
                WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, sysOrderId).eq(WalletTransactionFlow::getIsDelete, 0));
                if (null == walletTransactionFlow) {
                    log.error("友涯系统不存在此笔订单交易流水");
                    writeToWechatPayNotifyResponseErrorMessage(response, "友涯系统不存在此笔订单交易流水");
                    return;
                }
                sysOrder.setOutTransactionId(transactionId);
                Long buyerId = sysOrder.getBuyerId();
                String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, buyerId);
                RLock walletLock = redissonClient.getLock(walletLockKey);
                try {
                    boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
                    if (tryWalletLock) {
                        EnterpriseWalletAccount enterpriseWalletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getId, buyerId).eq(EnterpriseWalletAccount::getIsDelete, 0));
                        if (null == enterpriseWalletAccount) {
                            log.error("友涯企业钱包账户不存在");
                            writeToWechatPayNotifyResponseErrorMessage(response, "友涯企业钱包账户不存在");
                            return;
                        }
                        BigDecimal beforeBalance = enterpriseWalletAccount.getAccountBalance();
                        Transaction.TradeStateEnum tradeState = transaction.getTradeState();
                        //支付成功
                        if (Transaction.TradeStateEnum.SUCCESS.equals(tradeState)) {
                            //更新订单状态
                            BigDecimal actualAmount = sysOrder.getActualAmount();
                            TransactionAmount amount = transaction.getAmount();
                            if (null == amount) {
                                log.error("微信支付通知缺失订单金额信息");
                                writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失订单金额信息");
                                return;
                            }
                            Integer payerTotal = amount.getPayerTotal();
                            BigDecimal payerTotalAmount = new BigDecimal(payerTotal);
                            if (actualAmount.compareTo(payerTotalAmount) != 0) {
                                log.error("微信支付通知中支付金额与订单需要实付金额不一致");
                                writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失订单金额信息");
                                return;
                            }
                            sysOrder.setStatus(OrderStatusEnum.PAID.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            //更新账户金额
                            BigDecimal afterBalance = beforeBalance.add(payerTotalAmount);
                            enterpriseWalletAccount.setAccountBalance(afterBalance);
                            enterpriseWalletAccountMapper.updateById(enterpriseWalletAccount);
                            //更新账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc());
                            walletTransactionFlow.setBalanceBefore(beforeBalance);
                            walletTransactionFlow.setBalanceAfter(afterBalance);
                            walletTransactionFlow.setOutTransactionId(transactionId);
                            walletTransactionFlow.setCompletionDate(parseStringToLocalDateTime(successTime));
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            log.info("友涯订单id:【{}】，支付成功，账户余额和订单状态以及账户流水更新成功", sysOrderId);
                            //设置HTTP响应状态码为204（No Content）
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        } else if (Transaction.TradeStateEnum.PAYERROR.equals(tradeState)) {
                            //支付失败
                            //更新订单状态
                            sysOrder.setStatus(OrderStatusEnum.PAYMENT_FAILED.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            //更新账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.FAILED.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.FAILED.getStatusDesc());
                            walletTransactionFlow.setTransactionFailReason(tradeStateDesc);
                            walletTransactionFlow.setBalanceBefore(beforeBalance);
                            walletTransactionFlow.setBalanceAfter(beforeBalance);
                            walletTransactionFlow.setOutTransactionId(transactionId);
                            walletTransactionFlow.setCompletionDate(parseStringToLocalDateTime(successTime));
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            try {
                                WechatPayUtil.closeOrder(outTradeNo);
                                log.info("友涯订单id:【{}】，调用微信关闭订单接口成功", sysOrderId);
                            } catch (Exception e) {
                                log.error("友涯订单id:【{}】，调用微信关闭订单接口失败，原因：", sysOrderId, e);
                                log.error("友涯订单id:【{}】，支付失败，订单状态以及账户流水更新失败", sysOrderId);
                                writeToWechatPayNotifyResponseErrorMessage(response, "调用微信关闭订单接口失败");
                                return;
                            }
                            log.info("友涯订单id:【{}】，支付失败，订单状态以及账户流水更新成功", sysOrderId);
                            //设置HTTP响应状态码为204（No Content）
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        } else if (Transaction.TradeStateEnum.CLOSED.equals(tradeState)) {
                            //已关闭
                            //更新订单状态
                            sysOrder.setStatus(OrderStatusEnum.CLOSED.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            //更新账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.CLOSED.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.CLOSED.getStatusDesc());
                            walletTransactionFlow.setBalanceBefore(beforeBalance);
                            walletTransactionFlow.setBalanceAfter(beforeBalance);
                            walletTransactionFlow.setOutTransactionId(transactionId);
                            walletTransactionFlow.setCompletionDate(parseStringToLocalDateTime(successTime));
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            log.info("友涯订单id:【{}】，已关闭，订单状态以及账户流水更新成功", sysOrderId);
                        } else if (Transaction.TradeStateEnum.NOTPAY.equals(tradeState)) {
                            //未支付
                            //更新订单状态
                            sysOrder.setStatus(OrderStatusEnum.PENDING_PAYMENT.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            //更新账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.PENDING.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.PENDING.getStatusDesc());
                            walletTransactionFlow.setBalanceBefore(beforeBalance);
                            walletTransactionFlow.setBalanceAfter(beforeBalance);
                            walletTransactionFlow.setOutTransactionId(transactionId);
                            walletTransactionFlow.setCompletionDate(parseStringToLocalDateTime(successTime));
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            log.info("友涯订单id:【{}】，未支付，订单状态以及账户流水更新成功", sysOrderId);
                            //设置HTTP响应状态码为204（No Content）
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        } else if (Transaction.TradeStateEnum.REFUND.equals(tradeState)) {
                            //转入退款
                            ////更新订单状态
                            sysOrder.setStatus(OrderStatusEnum.REFUNDED.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            //更新账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.REFUNDED.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.REFUNDED.getStatusDesc());
                            walletTransactionFlow.setBalanceBefore(beforeBalance);
                            walletTransactionFlow.setBalanceAfter(beforeBalance);
                            walletTransactionFlow.setOutTransactionId(transactionId);
                            walletTransactionFlow.setCompletionDate(parseStringToLocalDateTime(successTime));
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            log.info("友涯订单id:【{}】，转入退款，订单状态以及账户流水更新成功", sysOrderId);
                            //设置HTTP响应状态码为204（No Content）
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        }
                    } else {
                        //获取钱包账户锁超时
                        log.error("友涯订单id:【{}】，获取钱包账户锁超时", sysOrderId);
                        writeToWechatPayNotifyResponseErrorMessage(response, "获取钱包账户锁超时");
                    }
                } catch (InterruptedException e) {
                    //获取钱包账户锁失败
                    log.error("友涯订单id:【{}】，获取钱包账户锁失败，原因：", sysOrderId, e);
                    log.error("友涯订单id:【{}】，订单状态以及账户流水更新失败", sysOrderId);
                    writeToWechatPayNotifyResponseErrorMessage(response, "获取钱包账户锁失败");
                } finally {
                    if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                        walletLock.unlock();
                    }
                }
            } else {
                //获取订单锁超时
                log.error("友涯订单id:【{}】，获取订单锁超时", outTradeNo);
                writeToWechatPayNotifyResponseErrorMessage(response, "获取订单锁超时");
            }
        } catch (InterruptedException e) {
            //获取订单锁失败
            log.error("友涯订单id:【{}】，获取订单锁失败，原因：", outTradeNo, e);
            log.error("友涯订单id:【{}】，订单状态以及账户流水更新失败", outTradeNo);
            writeToWechatPayNotifyResponseErrorMessage(response, "获取订单锁失败");
        } finally {
            if (orderLock != null && orderLock.isHeldByCurrentThread()) {
                orderLock.unlock();
            }
        }
    }

    /**
     * 写入微信支付通知响应错误消息
     *
     * @param response
     * @param message
     */
    private void writeToWechatPayNotifyResponseErrorMessage(HttpServletResponse response, String message) {
        //创建一个包含应答内容的JSON对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "FAIL");
        jsonObject.put("message", message);
        // 设置响应状态码为500
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        //设置响应内容类型为JSON
        response.setContentType("application/json;charset=UTF-8");
        //获取PrintWriter对象以便写入响应体
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //将JSON对象转换为字符串并写入响应
            assert out != null;
            out.print(jsonObject.toString());
            //刷新输出流
            out.flush();
            //关闭输出流
            out.close();
        }
    }

    /**
     * 查询企业充值结果
     *
     * @param rechargeResultDto
     * @return
     */
    @Override
    public Integer queryRechargeResult(QueryEnterpriseRechargeResultDto rechargeResultDto) {
        Long orderId = rechargeResultDto.getOrderId();
        SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
        if (null == sysOrder) throw new YouyaException("订单不存在");
        return sysOrder.getStatus();
    }

    /**
     * 查询企业订单列表
     *
     * @param queryOrderListDto
     * @return
     */
    @Override
    public Page<SysOrderVo> queryOrderList(QueryOrderListDto queryOrderListDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("当前账号未关联企业");
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        EnterpriseWalletAccount enterpriseWalletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getEnterpriseId, enterpriseId).eq(EnterpriseWalletAccount::getIsDelete, 0));
        if (null == enterpriseWalletAccount) throw new YouyaException("钱包账户不存在");
        Long walletAccountId = enterpriseWalletAccount.getId();
        Integer status = queryOrderListDto.getStatus();
        int pageNumber = queryOrderListDto.getPageNumber();
        int pageSize = queryOrderListDto.getPageSize();
        Long count = sysOrderMapper.selectCount(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getBuyerId, walletAccountId).eq((ObjectUtil.isNotNull(status)), SysOrder::getStatus, status).eq(SysOrder::getIsDelete, 0));
        List<SysOrderVo> list = sysOrderMapper.queryOrderList(walletAccountId, queryOrderListDto);
        Page<SysOrderVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 生成订单支付参数
     *
     * @param generatePaymentParametersDto
     * @return
     */
    @Override
    public JSONObject generatePaymentParameters(GeneratePaymentParametersDto generatePaymentParametersDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("当前账号未关联企业");
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        Integer authStatus = enterprise.getAuthStatus();
        if (EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus() != authStatus) throw new YouyaException("请等待企业审核通过后再使用");
        EnterpriseWalletAccount enterpriseWalletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getEnterpriseId, enterpriseId).eq(EnterpriseWalletAccount::getIsDelete, 0));
        if (null == enterpriseWalletAccount) throw new YouyaException("钱包账户不存在");
        Integer accountStatus = enterpriseWalletAccount.getStatus();
        if (WalletAccountStatusEnum.FROZEN.getStatus() == accountStatus) throw new YouyaException("钱包账户已被冻结，请联系客服");
        String enterpriseName = enterprise.getName();
        Long orderId = generatePaymentParametersDto.getOrderId();
        SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
        if (null == sysOrder) throw new YouyaException("订单不存在");
        Integer status = sysOrder.getStatus();
        if (OrderStatusEnum.PENDING_PAYMENT.getStatus() != status) throw new YouyaException("只有待支付订单可以获取支付参数");
        Long buyerId = sysOrder.getBuyerId();
        Long walletAccountId = enterpriseWalletAccount.getId();
        if (!buyerId.equals(walletAccountId)) throw new YouyaException("非法操作");
        String code = generatePaymentParametersDto.getCode();
        String openid = WeChatUtil.code2Session(code);
        if (StringUtils.isBlank(openid)) throw new YouyaException("用户微信openid获取失败");
        if (StringUtils.isBlank(enterpriseRechargeNotifyUrl)) throw new YouyaException("支付通知地址获取失败");
        Integer actualAmount = sysOrder.getActualAmount().intValue();
        PrepayWithRequestPaymentResponse response = WechatPayUtil.prepayWithRequestPayment(RECHARGE_DESCRIPTION, orderId.toString(), enterpriseRechargeNotifyUrl, actualAmount, openid);
        if (null == response) throw new YouyaException("充值下单失败，请稍后重试");
        log.info("企业名称：【{}】，企业id：【{}】，订单id：【{}】，生成调起支付参数成功，微信响应报文：【{}】", enterpriseName, enterpriseId, orderId, response);
        JSONObject result = new JSONObject();
        result.put("timeStamp", response.getTimeStamp());
        result.put("nonceStr", response.getNonceStr());
        result.put("package", response.getPackageVal());
        result.put("signType", response.getSignType());
        result.put("paySign", response.getPaySign());
        result.put("orderId", orderId);
        return result;
    }

    /**
     * 取消订单
     *
     * @param cancelOrderDto
     */
    @Override
    public void cancelOrder(CancelOrderDto cancelOrderDto) {
        log.info("收到企业取消订单请求");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("当前账号未关联企业");
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        String enterpriseName = enterprise.getName();
        Long orderId = cancelOrderDto.getOrderId();
        String orderLockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, orderId);
        RLock orderLock = redissonClient.getLock(orderLockKey);
        try {
            boolean tryOrderLock = orderLock.tryLock(3, TimeUnit.SECONDS);
            if (tryOrderLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
                if (null == sysOrder) throw new YouyaException("订单不存在");
                Integer status = sysOrder.getStatus();
                if (OrderStatusEnum.PENDING_PAYMENT.getStatus() != status) throw new YouyaException("只有待支付的订单才能取消");
                Long buyerId = sysOrder.getBuyerId();
                String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, buyerId);
                RLock walletLock = redissonClient.getLock(walletLockKey);
                try {
                    boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
                    if (tryWalletLock) {
                        EnterpriseWalletAccount enterpriseWalletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getEnterpriseId, enterpriseId).eq(EnterpriseWalletAccount::getIsDelete, 0));
                        if (null == enterpriseWalletAccount) throw new YouyaException("钱包账户不存在");
                        Long walletAccountId = enterpriseWalletAccount.getId();
                        if (!buyerId.equals(walletAccountId)) throw new YouyaException("非法操作");
                        BigDecimal accountBalance = enterpriseWalletAccount.getAccountBalance();
                        WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, orderId).eq(WalletTransactionFlow::getIsDelete, 0));
                        if (null == walletTransactionFlow) throw new YouyaException("系统不存在此笔订单交易流水");
                        sysOrder.setStatus(OrderStatusEnum.PAYMENT_CANCELED.getStatus());
                        sysOrderMapper.updateById(sysOrder);
                        walletTransactionFlow.setBalanceBefore(accountBalance);
                        walletTransactionFlow.setBalanceAfter(accountBalance);
                        walletTransactionFlow.setStatus(TransactionFlowStatusEnum.CANCELLED.getStatus());
                        walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.CANCELLED.getStatusDesc());
                        walletTransactionFlowMapper.updateById(walletTransactionFlow);
                        try {
                            WechatPayUtil.closeOrder(orderId.toString());
                            log.info("企业名称：【{}】，企业id：【{}】，订单id：【{}】，调用微信关闭订单接口成功", enterpriseName, enterpriseId, orderId);
                        } catch (Exception e) {
                            log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，调用微信关闭订单接口失败，原因：【{}】", enterpriseName, enterpriseId, orderId, e);
                            log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，取消失败", enterpriseName, enterpriseId, orderId, e);
                            throw new YouyaException("网络异常，请稍后重试");
                        }
                        log.info("企业名称：【{}】，企业id：【{}】，订单id：【{}】，取消成功", enterpriseName, enterpriseId, orderId);
                    } else {
                        log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，获取钱包账户锁超时", enterpriseName, enterpriseId, orderId);
                        throw new YouyaException("网络异常，请稍后重试");
                    }
                } catch (InterruptedException e) {
                    log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，获取钱包账户锁失败，原因：", enterpriseName, enterpriseId, orderId, e);
                    throw new YouyaException("网络异常，请稍后重试");
                } finally {
                    if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                        walletLock.unlock();
                    }
                }
            } else {
                log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，获取订单锁超时", enterpriseName, enterpriseId, orderId);
                throw new YouyaException("网络异常，请稍后重试");
            }
        } catch (InterruptedException e) {
            log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，获取订单锁失败，原因：", enterpriseName, enterpriseId, orderId, e);
            throw new YouyaException("网络异常，请稍后重试");
        } catch (YouyaException e) {
            log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，取消订单失败", enterpriseName, enterpriseId, orderId);
            throw e;
        } catch (Exception e) {
            log.error("企业名称：【{}】，企业id：【{}】，订单id：【{}】，取消订单失败，原因：", enterpriseName, enterpriseId, orderId, e);
            throw new YouyaException("网络异常，请稍后重试");
        } finally {
            if (orderLock != null && orderLock.isHeldByCurrentThread()) {
                orderLock.unlock();
            }
        }
    }

    /**
     * 关闭订单
     *
     * @param orderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeEnterpriseOrder(Long orderId) {
        log.info("企业订单id:【{}】开始进行关闭处理", orderId);
        String lockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, orderId);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean tryLock = lock.tryLock(3, TimeUnit.SECONDS);
            if (tryLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
                if (null != sysOrder) {
                    Integer status = sysOrder.getStatus();
                    if (OrderStatusEnum.PAYMENT_TIMEOUT.getStatus() == status) {
                        WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, orderId).eq(WalletTransactionFlow::getIsDelete, 0));
                        if (null != walletTransactionFlow) {
                            sysOrder.setStatus(OrderStatusEnum.CLOSED.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.CLOSED.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.CLOSED.getStatusDesc());
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            try {
                                WechatPayUtil.closeOrder(orderId.toString());
                                log.info("友涯订单id:【{}】，调用微信关闭订单接口成功", orderId);
                            } catch (Exception e) {
                                log.error("友涯订单id:【{}】，调用微信关闭订单接口失败，原因：", orderId, e);
                                log.error("友涯订单id:【{}】，关闭处理失败", orderId);
                                throw new YouyaException("调用微信关闭订单接口失败");
                            }
                            log.info("企业订单id:【{}】关闭处理成功", orderId);
                        } else {
                            log.info("企业订单id:【{}】未找到对应交易流水，停止处理", orderId);
                        }
                    } else {
                        log.info("企业订单id:【{}】不是待支付状态，停止处理", orderId);
                    }
                }
            } else {
                log.error("企业订单id：【{}】,获取订单锁超时", orderId);
                throw new YouyaException("获取订单锁超时，关闭订单处理失败");
            }
        } catch (InterruptedException e) {
            log.error("企业订单id：【{}】,获取订单锁失败，原因：", orderId, e);
            throw new YouyaException("获取订单锁失败，关闭订单处理失败");
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 查询企业已关闭订单列表
     *
     * @param queryClosedOrderListDto
     * @return
     */
    @Override
    public Page<SysOrderVo> queryClosedOrderList(QueryClosedOrderListDto queryClosedOrderListDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("当前账号未关联企业");
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未创建");
        EnterpriseWalletAccount enterpriseWalletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getEnterpriseId, enterpriseId).eq(EnterpriseWalletAccount::getIsDelete, 0));
        if (null == enterpriseWalletAccount) throw new YouyaException("钱包账户不存在");
        Long walletAccountId = enterpriseWalletAccount.getId();
        int pageNumber = queryClosedOrderListDto.getPageNumber();
        int pageSize = queryClosedOrderListDto.getPageSize();
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.PAYMENT_FAILED.getStatus());
        statusList.add(OrderStatusEnum.PAYMENT_TIMEOUT.getStatus());
        statusList.add(OrderStatusEnum.PAYMENT_CANCELED.getStatus());
        statusList.add(OrderStatusEnum.REFUNDING.getStatus());
        statusList.add(OrderStatusEnum.REFUNDED.getStatus());
        statusList.add(OrderStatusEnum.CLOSED.getStatus());
        Long count = sysOrderMapper.selectCount(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getBuyerId, walletAccountId).in(SysOrder::getStatus, statusList).eq(SysOrder::getIsDelete, 0));
        List<SysOrderVo> list = sysOrderMapper.queryClosedOrderList(walletAccountId, queryClosedOrderListDto);
        Page<SysOrderVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 将字符串转换为时间
     *
     * @param var
     * @return
     */
    private static LocalDateTime parseStringToLocalDateTime(String var) {
        // 创建一个可以解析ISO-8601格式时间字符串的formatter
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        // 解析为OffsetDateTime
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(var, formatter);
        // 将OffsetDateTime转换为LocalDateTime，忽略时区信息
        return offsetDateTime.toLocalDateTime();
    }
}
