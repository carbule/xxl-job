package com.korant.youya.workplace.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.config.ObsBucketConfig;
import com.korant.youya.workplace.constants.RedisConstant;
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
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.enterprise.*;
import com.korant.youya.workplace.properties.DelayProperties;
import com.korant.youya.workplace.properties.RabbitMqConfigurationProperties;
import com.korant.youya.workplace.service.EnterpriseService;
import com.korant.youya.workplace.utils.*;
import com.obs.services.model.PutObjectResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private RedisUtil redisUtil;

    @Resource
    private RabbitMqUtil rabbitMqUtil;

    @Value("${enterprise_qrcode_url}")
    private String enterpriseQrcodeUrl;

    @Resource
    RabbitMqConfigurationProperties mqConfigurationProperties;

    private static final String ENTERPRISE_BUCKET = "enterprise";

    private static final String ENTERPRISE_QRCODE_BUCKET = "activity";

    private static final String DEFAULT_LOGO = "https://resources.youyai.cn/svg/door-open.svg";

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
        if (null == file) throw new YouyaException("文件不能为空");
        String bucketName = ObsBucketConfig.getBucketName(ENTERPRISE_BUCKET);
        String fileName = ObsUtil.getFileBase64Name(file);
        if (StringUtils.isBlank(fileName)) throw new YouyaException("上传营业执照失败");
        if (!ObsUtil.doesObjectExist(bucketName, fileName)) {
            PutObjectResult result = ObsUtil.putObject(bucketName, fileName, file);
            if (null == result) throw new YouyaException("上传营业执照失败");
            String etag = result.getEtag();
            String objectKey = result.getObjectKey();
            if (StringUtils.isBlank(etag) && StringUtils.isBlank(objectKey)) throw new YouyaException("上传营业执照失败");
        }
        String signedUrl = ObsUtil.getSignedUrl(bucketName, fileName);
        String encode = URLEncoder.encode(signedUrl, StandardCharsets.UTF_8);
        JSONObject resultObj = HuaWeiUtil.getLicenseContentByUrl(encode);
        if (null == resultObj) throw new YouyaException("获取营业执照信息失败");
        resultObj.put("objectKey", fileName);
        resultObj.put("signedUrl", signedUrl);
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
            //todo 上生产之后放开 先写死一个
//            InputStream inputStream = QrCodeUtil.getQrCode(enterpriseQrcodeUrl + id, 180, 180);
            InputStream inputStream = QrCodeUtil.getQrCode(enterpriseQrcodeUrl + "123456789", 180, 180);
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
}
