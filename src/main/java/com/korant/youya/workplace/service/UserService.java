package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.user.UserLoginByPasswordDto;
import com.korant.youya.workplace.pojo.dto.user.UserLoginBySMSVerificationCodeDto;
import com.korant.youya.workplace.pojo.dto.user.UserLoginByWechatCodeDto;
import com.korant.youya.workplace.pojo.dto.user.VerificationCodeDto;
import com.korant.youya.workplace.pojo.po.User;

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
    String loginByWechatCode(UserLoginByWechatCodeDto wechatCodeDto);

    /**
     * 短信验证码登陆
     *
     * @param smsVerificationCodeDto
     * @return
     */
    String loginBySMSVerificationCode(UserLoginBySMSVerificationCodeDto smsVerificationCodeDto);

    /**
     * 密码登陆
     *
     * @param passwordDto
     * @return
     */
    String loginByPassword(UserLoginByPasswordDto passwordDto);

    /**
     * 获取登陆短信二维码
     *
     * @param codeDto
     */
    void getVerificationCode(VerificationCodeDto codeDto);

    /**
     * 用户登出
     */
    void logout();
}