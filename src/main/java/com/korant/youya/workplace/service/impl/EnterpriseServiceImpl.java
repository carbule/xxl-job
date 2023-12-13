package com.korant.youya.workplace.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.config.ObsBucketConfig;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.enterprise.EnterpriseAuthStatusEnum;
import com.korant.youya.workplace.enums.enterprisetodo.EnterpriseTodoEventTypeEnum;
import com.korant.youya.workplace.enums.enterprisetodo.EnterpriseTodoOperateEnum;
import com.korant.youya.workplace.enums.role.RoleEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.enterprise.*;
import com.korant.youya.workplace.service.EnterpriseService;
import com.korant.youya.workplace.utils.HuaWeiUtil;
import com.korant.youya.workplace.utils.ObsUtil;
import com.korant.youya.workplace.utils.RedisUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import com.obs.services.model.PutObjectResult;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements EnterpriseService {

    @Resource
    private EnterpriseMapper enterpriseMapper;

    @Resource
    private UserEnterpriseMapper userEnterpriseMapper;

    @Resource
    private EnterpriseTodoMapper enterpriseTodoMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private JobMapper jobMapper;

    @Resource
    private RedisUtil redisUtil;

    public static final String ENTERPRISE_BUCKET = "enterprise";

    private static final String DEFAULT_LOGO = "https://resources.youyai.cn/picture/firmLogo.jpg";

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
     * 查询待审批列表
     *
     * @param queryPendingApprovalListDto
     * @return
     */
    @Override
    public Page<EnterprisePendingApprovalVo> queryPendingApprovalList(QueryPendingApprovalListDto queryPendingApprovalListDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) return null;
        int pageNumber = queryPendingApprovalListDto.getPageNumber();
        int pageSize = queryPendingApprovalListDto.getPageSize();
        Long count = enterpriseTodoMapper.selectCount(new LambdaQueryWrapper<EnterpriseTodo>().eq(EnterpriseTodo::getEnterpriseId, enterpriseId).eq(EnterpriseTodo::getOperate, EnterpriseTodoOperateEnum.PENDING_REVIEW.getOperate()).eq(EnterpriseTodo::getIsDelete, 0));
        List<EnterprisePendingApprovalVo> list = enterpriseMapper.queryPendingApprovalList(enterpriseId, queryPendingApprovalListDto);
        Page<EnterprisePendingApprovalVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 同意事项
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void agree(Long id) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        EnterpriseTodo enterpriseTodo = enterpriseTodoMapper.selectOne(new LambdaQueryWrapper<EnterpriseTodo>().eq(EnterpriseTodo::getId, id).eq(EnterpriseTodo::getIsDelete, 0));
        if (null == enterpriseTodo) throw new YouyaException("审批事项不存在");
        if (!enterpriseTodo.getEnterpriseId().equals(enterpriseId)) throw new YouyaException("非法操作");
        if (EnterpriseTodoOperateEnum.PENDING_REVIEW.getOperate() != enterpriseTodo.getOperate())
            throw new YouyaException("事项已审批");
        Integer eventType = enterpriseTodo.getEventType();
        Long uid = enterpriseTodo.getUid();
        if (EnterpriseTodoEventTypeEnum.HR.getType() == eventType) {
            Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, RoleEnum.HR.getRole()).eq(Role::getIsDelete, 0));
            if (null == role) throw new YouyaException("预设角色缺失");
            UserRole userRole = new UserRole();
            userRole.setUid(uid).setRid(role.getId());
            userRoleMapper.insert(userRole);
            String cacheKey = String.format(RedisConstant.YY_USER_CACHE, uid);
            redisUtil.del(cacheKey);
        }
        UserEnterprise userEnterprise = new UserEnterprise();
        userEnterprise.setUid(uid).setEnterpriseId(enterpriseId);
        userEnterpriseMapper.insert(userEnterprise);
        enterpriseTodo.setOperate(EnterpriseTodoOperateEnum.AGREE.getOperate());
        enterpriseTodoMapper.updateById(enterpriseTodo);
    }

    /**
     * 拒绝事项
     *
     * @param id
     */
    @Override
    public void refuse(Long id) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        EnterpriseTodo enterpriseTodo = enterpriseTodoMapper.selectOne(new LambdaQueryWrapper<EnterpriseTodo>().eq(EnterpriseTodo::getId, id).eq(EnterpriseTodo::getIsDelete, 0));
        if (null == enterpriseTodo) throw new YouyaException("审批事项不存在");
        if (!enterpriseTodo.getEnterpriseId().equals(enterpriseId)) throw new YouyaException("非法操作");
        if (EnterpriseTodoOperateEnum.PENDING_REVIEW.getOperate() != enterpriseTodo.getOperate())
            throw new YouyaException("事项已审批");
        enterpriseTodo.setOperate(EnterpriseTodoOperateEnum.REFUSE.getOperate());
        enterpriseTodoMapper.updateById(enterpriseTodo);
    }

    /**
     * 撤销申请
     *
     * @param enterpriseId
     */
    @Override
    public void revoke(Long enterpriseId) {
        Long userId = SpringSecurityUtil.getUserId();
        EnterpriseTodo enterpriseTodo = enterpriseTodoMapper.selectOne(new LambdaQueryWrapper<EnterpriseTodo>().eq(EnterpriseTodo::getEnterpriseId, enterpriseId).eq(EnterpriseTodo::getUid, userId).eq(EnterpriseTodo::getIsDelete, 0));
        if (null == enterpriseTodo) throw new YouyaException("加入申请不存在");
        Integer operate = enterpriseTodo.getOperate();
        if (EnterpriseTodoOperateEnum.AGREE.getOperate() == operate) throw new YouyaException("申请已同意无法撤销");
        enterpriseTodo.setIsDelete(1);
        enterpriseTodoMapper.updateById(enterpriseTodo);
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
            String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
            if (!redisUtil.del(cacheKey)) throw new YouyaException("强制移除员工失败，请稍后再试");
            userEnterprise.setIsDelete(1);
            userEnterpriseMapper.updateById(userEnterprise);
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
        Long userId = SpringSecurityUtil.getUserId();
        if (userEnterpriseMapper.exists(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getIsDelete, 0)))
            throw new YouyaException("您已绑定过企业");
        String socialCreditCode = createDto.getSocialCreditCode();
        boolean exists = enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getSocialCreditCode, socialCreditCode).eq(Enterprise::getIsDelete, 0));
        if (exists) throw new YouyaException("该企业已被创建");
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
    public void join(EnterpriseJoinDto joinDto) {
        Long userId = SpringSecurityUtil.getUserId();
        if (userEnterpriseMapper.exists(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getIsDelete, 0)))
            throw new YouyaException("您已绑定过企业");
        if (enterpriseTodoMapper.exists(new LambdaQueryWrapper<EnterpriseTodo>().eq(EnterpriseTodo::getUid, userId).eq(EnterpriseTodo::getEventType, EnterpriseTodoEventTypeEnum.HR.getType()).eq(EnterpriseTodo::getIsDelete, 0)))
            throw new YouyaException("您当前有待审核的认证");
        Long enterpriseId = joinDto.getEnterpriseId();
        if (!enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getAuthStatus, EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus()).eq(Enterprise::getIsDelete, 0)))
            throw new YouyaException("企业未创建");
        EnterpriseTodo enterpriseTodo = new EnterpriseTodo();
        enterpriseTodo.setEnterpriseId(enterpriseId).setUid(userId).setEventType(EnterpriseTodoEventTypeEnum.HR.getType()).setOperate(EnterpriseTodoOperateEnum.PENDING_REVIEW.getOperate());
        enterpriseTodoMapper.insert(enterpriseTodo);
    }

    /**
     * 修改企业logo
     *
     * @param modifyLogoDto
     */
    @Override
    public void modifyLogo(EnterpriseModifyLogoDto modifyLogoDto) {
        enterpriseMapper.modifyLogo(modifyLogoDto);
    }

    /**
     * 修改企业
     *
     * @param modifyDto
     */
    @Override
    public void modify(EnterpriseModifyDto modifyDto) {
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
            EnterpriseTodo enterpriseTodo = enterpriseTodoMapper.selectOne(new LambdaQueryWrapper<EnterpriseTodo>().eq(EnterpriseTodo::getUid, userId).eq(EnterpriseTodo::getEventType, EnterpriseTodoEventTypeEnum.HR.getType()).eq(EnterpriseTodo::getIsDelete, 0));
            //未加入
            if (null != enterpriseTodo) {
                return enterpriseMapper.queryEnterpriseInfoByHR(userId, enterpriseTodo.getEnterpriseId());
            } else {
                //没有认证也没有加入
                return null;
            }
        } else {
            String role = loginUser.getRole();
            //管理员
            if (RoleEnum.ADMIN.getRole().equals(role)) {
                return enterpriseMapper.queryEnterpriseInfoByAdmin(userEnterprise.getEnterpriseId());
            } else if (RoleEnum.HR.getRole().equals(role)) {
                //hr
                return enterpriseMapper.queryEnterpriseInfoByHR(userId, userEnterprise.getEnterpriseId());
            } else {
                //普通员工
                return null;
            }
        }
    }

    /**
     * 查询企业结构信息
     *
     * @return
     */
    @Override
    //todo 未写完
    public EnterpriseStructureInfoVo queryEnterpriseStructureInfo() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        String role = loginUser.getRole();
        //管理员
        if (RoleEnum.ADMIN.getRole().equals(role)) {
            return enterpriseMapper.queryEnterpriseStructureInfoByAdmin(enterpriseId);
        } else {
            //hr
            return enterpriseMapper.queryEnterpriseStructureInfoByHR(enterpriseId);
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
}
