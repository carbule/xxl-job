package com.korant.youya.workplace.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.config.ObsBucketConfig;
import com.korant.youya.workplace.enums.enterprise.EnterpriseAuthStatusEnum;
import com.korant.youya.workplace.enums.user.UserAuthenticationStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.EnterpriseMapper;
import com.korant.youya.workplace.mapper.UserEnterpriseMapper;
import com.korant.youya.workplace.mapper.UserMapper;
import com.korant.youya.workplace.mapper.UserRoleMapper;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.po.Enterprise;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.pojo.po.UserEnterprise;
import com.korant.youya.workplace.pojo.po.UserRole;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseDetailVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByNameVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByUserVo;
import com.korant.youya.workplace.service.EnterpriseService;
import com.korant.youya.workplace.utils.HuaWeiUtil;
import com.korant.youya.workplace.utils.ObsUtil;
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
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

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
        User user = userMapper.selectById(userId);
        if (user.getAuthenticationStatus() ==  UserAuthenticationStatusEnum.NOT_CERTIFIED.getStatus())
            throw new YouyaException("请先进行实名认证！");
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

        //企业人员关联
        UserEnterprise userEnterprise = new UserEnterprise();
        userEnterprise.setUid(userId);
        userEnterprise.setEnterpriseId(enterprise.getId());
        userEnterpriseMapper.insert(userEnterprise);

        //TODO 注册企业的人自动成为管理员
        UserRole userRole = new UserRole();
        userRole.setUid(userId);
        userRole.setRid(2L);
        userRoleMapper.insert(userRole);

    }

    /**
     * @Description 修改企业logo
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyLogo(EnterpriseModifyLogoDto modifyLogoDto) {

        boolean exists = enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, modifyLogoDto.getId()).eq(Enterprise::getIsDelete, 0));
        if (!exists) throw new YouyaException("企业不存在！");
        enterpriseMapper.modifyLogo(modifyLogoDto);

    }

    /**
     * @Description 修改企业
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(EnterpriseModifyDto modifyDto) {

        boolean exists = enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, modifyDto.getId()).eq(Enterprise::getIsDelete, 0));
        if (!exists) throw new YouyaException("企业不存在！");
        modifyDto.setCountryCode("100000");
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
    @Transactional(rollbackFor = Exception.class)
    public void changeEnterpriseInfo(EnterpriseChangeDto changeDto) {

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
        enterpriseMapper.changeEnterpriseInfo(changeDto);

    }

    /**
     * @Description 根据企业名称查询企业
     * @Param
     * @return
     **/
    @Override
    public List<EnterpriseInfoByNameVo> getEnterpriseByName(EnterpriseQueryListDto enterpriseQueryListDto) {

        return enterpriseMapper.getEnterpriseByName(enterpriseQueryListDto.getName(), EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus());

    }

}
