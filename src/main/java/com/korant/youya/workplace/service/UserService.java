package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.user.*;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.pojo.vo.user.ResumeContactInfoVo;
import com.korant.youya.workplace.pojo.vo.user.UserLoginVo;
import com.korant.youya.workplace.pojo.vo.user.ResumePersonInfoVo;

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

    ResumePersonInfoVo resumePersonDetail();

    void resumePersonModify(ResumePersonModifyDto resumePersonModifyDto);

    ResumeContactInfoVo resumeContactDetail();
}
