package com.korant.youya.workplace.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.config.ObsBucketConfig;
import com.korant.youya.workplace.enums.enterprise.EnterpriseAuthStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseDetailVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByNameVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByUserVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceListVo;
import com.korant.youya.workplace.service.EnterpriseService;
import com.korant.youya.workplace.utils.HuaWeiUtil;
import com.korant.youya.workplace.utils.ObsUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import com.obs.services.model.PutObjectResult;
import jakarta.annotation.Resource;
import jakarta.validation.ReportAsSingleViolation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.MethodWrapper;
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
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserEnterpriseMapper userEnterpriseMapper;

    public static final String ENTERPRISE_BUCKET = "enterprise";

    private static final String DEFAULT_LOGO = "https://resources.youyai.cn/picture/firmLogo.jpg";

    /**
     * @Description 获取营业执照信息
     * @Param
     * @return
     **/
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
     * @Description 创建企业
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(EnterpriseCreateDto createDto) {

        Long userId = SpringSecurityUtil.getUserId();
        if (userEnterpriseMapper.exists(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getIsDelete, 0)))
            throw new YouyaException("您已绑定过企业");
        String name = createDto.getName();
        boolean exists = enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getName, name).eq(Enterprise::getIsDelete, 0));
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
     * @Description 修改企业logo
     * @Param
     * @return
     **/
    @Override
    public void modifyLogo(EnterpriseModifyLogoDto modifyLogoDto) {

        enterpriseMapper.modifyLogo(modifyLogoDto);

    }

    /**
     * @Description 修改企业
     * @Param
     * @return
     **/
    @Override
    public void modify(EnterpriseModifyDto modifyDto) {

        enterpriseMapper.modify(modifyDto);

    }

    /**
     * @Description 根据当前登陆用户查询企业信息
     * @Param
     * @return
     **/
    @Override
    public EnterpriseInfoByUserVo queryEnterpriseInfoByUser() {

        Long userId = SpringSecurityUtil.getUserId();
        return enterpriseMapper.queryEnterpriseInfoByUser(userId);

    }

    /**
     * @Description 查询企业详细信息
     * @Param
     * @return
     **/
    @Override
    public EnterpriseDetailVo detail(Long id) {

        EnterpriseDetailVo detailVo = enterpriseMapper.detail(id);
        if (null == detailVo) throw new YouyaException("企业未注册");
        String businessLicense = detailVo.getBusinessLicense();
        String encode = URLEncoder.encode(businessLicense, StandardCharsets.UTF_8);
        String cdnPath = "https://ent.youyai.cn/" + encode;
        detailVo.setBusinessLicense(cdnPath);
        return detailVo;

    }

    /**
     * @Description 变更企业
     * @Param
     * @return
     **/
    @Override
    public void changeEnterpriseInfo(EnterpriseChangeDto changeDto) {

        Long userId = SpringSecurityUtil.getUserId();
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, changeDto.getId()).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未注册");
        //校验企业成立时间
        LocalDate establishDate = changeDto.getEstablishDate();
        Date date = Date.from(establishDate.atStartOfDay().atZone(ZoneId.of("Asia/Shanghai")).toInstant());
        long between = DateUtil.between(date, new Date(), DateUnit.DAY);
        if (between < 365) throw new YouyaException("成立时间未届满一年的企业不可注册");
        //校验统一社会信用代码是否一致
        String socialCreditCode = changeDto.getSocialCreditCode();
        if (!socialCreditCode.equals(enterprise.getSocialCreditCode())) throw new YouyaException("统一社会信用代码不一致，请重新上传");
        //校验企业认证状态
        Integer authStatus = enterprise.getAuthStatus();
        if (null == authStatus || EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus() != authStatus)
            throw new YouyaException("企业认证成功后才能进行变更操作");
        String name = changeDto.getName();
        if (name.equals(enterprise.getName())) throw new YouyaException("请勿重复提交企业实名信息");
        LambdaUpdateWrapper<Enterprise> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Enterprise::getId, changeDto.getId());
        Enterprise e = new Enterprise();
        BeanUtils.copyProperties(changeDto, e);
        e.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_IN_PROGRESS.getStatus());
        enterpriseMapper.update(e, updateWrapper);

        //你自动成为创建公司的管理员
        if(userRoleMapper.exists(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, userId).eq(UserRole::getIsDelete, 0))){
            userRoleMapper.update(new UserRole(), new LambdaUpdateWrapper<UserRole>().eq(UserRole::getUid, userId).set(UserRole::getRid, 2L));
        }
        else{
            UserRole userRole = new UserRole();
            userRole.setUid(userId);
            userRole.setRid(2L);
            userRoleMapper.insert(userRole);
        }

    }

    /**
     * @Description 根据企业名称查询企业
     * @Param
     * @return
     **/
    @Override
    public Page<EnterpriseInfoByNameVo> getEnterpriseByName(EnterpriseQueryListDto enterpriseQueryListDto) {

        int pageNumber = enterpriseQueryListDto.getPageNumber();
        int pageSize = enterpriseQueryListDto.getPageSize();
        Long count = enterpriseMapper.selectCount(new LambdaQueryWrapper<Enterprise>().like(Enterprise::getName, enterpriseQueryListDto.getName()).eq(Enterprise::getIsDelete, 0));
        List<EnterpriseInfoByNameVo> list = enterpriseMapper.getEnterpriseByName(enterpriseQueryListDto.getName(), pageNumber, pageSize);
        Page<EnterpriseInfoByNameVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;

    }

    /**
     * @Description 解除关联企业绑定
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void Unbinding(Long id) {

        //判断当前用户是否为企业管理员
        Long userId = SpringSecurityUtil.getUserId();
        Long role = roleMapper.getRoleByUserAndEnterprise(userId, id);
        if (role == 2) throw new YouyaException("管理员无法再次解绑！");
        userEnterpriseMapper.update(new UserEnterprise(),
                new LambdaUpdateWrapper<UserEnterprise>()
                        .eq(UserEnterprise::getUid, userId)
                        .eq(UserEnterprise::getEnterpriseId, id)
                        .set(UserEnterprise::getIsDelete, 1));
    }

}
