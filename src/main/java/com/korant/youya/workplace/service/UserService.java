package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.user.*;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.pojo.vo.user.*;

/**
 * <p>
 * 友涯用户表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface UserService extends IService<User> {

    /**
     * 微信登陆
     *
     * @param wechatCodeDto
     * @return
     */
    UserLoginVo loginByWechatCode(UserLoginByWechatCodeDto wechatCodeDto);

    /**
     * 短信验证码登陆
     *
     * @param smsVerificationCodeDto
     * @return
     */
    UserLoginVo loginBySMSVerificationCode(UserLoginBySMSVerificationCodeDto smsVerificationCodeDto);

    /**
     * 密码登陆
     *
     * @param passwordDto
     * @return
     */
    UserLoginVo loginByPassword(UserLoginByPasswordDto passwordDto);

    /**
     * 注册用户
     *
     * @param phoneNumber
     * @return
     */
    LoginUser register(String phoneNumber);

    /**
     * 获取登陆短信二维码
     *
     * @param codeDto
     */
    void getVerificationCode(VerificationCodeDto codeDto);

    /**
     * 实名认证
     *
     * @param realNameAuthDto
     */
    void realNameAuthentication(UserRealNameAuthenticationDto realNameAuthDto);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 用户注销
     */
    void cancel();

    /**
     * 修改用户头像
     *
     * @param modifyUserAvatarDto
     */
    void modifyUserAvatar(ModifyUserAvatarDto modifyUserAvatarDto);

    /**
     * 查询个人基本信息
     *
     * @return
     */
    UserPersonalBasicInfoVo queryPersonalBasicInfo();

    /**
     * 修改个人基本信息
     *
     * @param modifyDto
     */
    void modifyUserPersonalBasicInfo(ModifyUserPersonalBasicInfoDto modifyDto);

    /**
     * 查询用户联系方式
     *
     * @return
     */
    UserContactInfoVo queryUserContactInfo();

    /**
     * 修改联系方式
     *
     * @param modifyDto
     */
    void modifyUserContactInfo(ModifyUserContactInfoDto modifyDto);

    /**
     * 查询登录用户信息
     *
     * @return
     */
    LoginUserVo queryLoginUserInfo();

    /**
     * 简历详情
     *
     * @return
     */
    ResumeDetailVo resumeDetail();

    /**
     * 简历预览
     *
     * @return
     */
    ResumePreviewVo resumePreview();

    /**
     * 申请关联企业
     *
     * @param applyAffiliatedEnterpriseDto
     */
    void affiliatedEnterprise(ApplyAffiliatedEnterpriseDto applyAffiliatedEnterpriseDto);

    /**
     * 解除关联企业
     */
    void relieveAffiliated();
}
