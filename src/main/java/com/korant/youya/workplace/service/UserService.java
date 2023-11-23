package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.user.*;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.pojo.vo.user.ResumeContactInfoVo;
import com.korant.youya.workplace.pojo.vo.user.ResumePersonInfoVo;
import com.korant.youya.workplace.pojo.vo.user.UserLoginVo;

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
     * 在线简历-查询个人信息
     *
     * @return
     */
    ResumePersonInfoVo resumePersonDetail();

    /**
     * 在线简历-修改个人信息
     *
     * @param
     * @return
     */
    void resumePersonModify(ResumePersonModifyDto resumePersonModifyDto);

    /**
     * 在线简历-查询联系方式
     *
     * @return
     */
    ResumeContactInfoVo resumeContactDetail();

    /**
     * 在线简历-编辑联系方式
     *
     * @return
     */
    void modifyResumeContactDetail(ResumeContactModifyDto resumeContactModifyDto);
}
