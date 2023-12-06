package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.user.*;
import com.korant.youya.workplace.pojo.vo.user.UserLoginVo;
import com.korant.youya.workplace.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 友涯用户表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 微信登陆
     *
     * @param wechatCodeDto
     * @return
     */
    @PostMapping("/loginByWechatCode")
    public R<?> loginByWechatCode(@RequestBody @Valid UserLoginByWechatCodeDto wechatCodeDto) {
        UserLoginVo loginVo = userService.loginByWechatCode(wechatCodeDto);
        return R.success(loginVo);
    }

    /**
     * 短信验证码登陆
     *
     * @param smsVerificationCodeDto
     * @return
     */
    @PostMapping("/loginBySMSVerificationCode")
    public R<?> loginBySMSVerificationCode(@RequestBody @Valid UserLoginBySMSVerificationCodeDto smsVerificationCodeDto) {
        UserLoginVo loginVo = userService.loginBySMSVerificationCode(smsVerificationCodeDto);
        return R.success(loginVo);
    }

    /**
     * 密码登陆
     *
     * @param passwordDto
     * @return
     */
    @PostMapping("/loginByPassword")
    public R<?> loginByPassword(@RequestBody @Valid UserLoginByPasswordDto passwordDto) {
        UserLoginVo loginVo = userService.loginByPassword(passwordDto);
        return R.success(loginVo);
    }

    /**
     * 获取登陆短信二维码
     *
     * @param codeDto
     * @return
     */
    @PostMapping("/getVerificationCode")
    public R<?> getVerificationCode(@RequestBody @Valid VerificationCodeDto codeDto) {
        userService.getVerificationCode(codeDto);
        return R.ok();
    }

    /**
     * 实名认证
     *
     * @param realNameAuthDto
     * @return
     */
    @PostMapping("/realNameAuth")
    public R<?> realNameAuthentication(@RequestBody @Valid UserRealNameAuthenticationDto realNameAuthDto) {
        userService.realNameAuthentication(realNameAuthDto);
        return R.ok();
    }

    /**
     * 用户登出
     *
     * @return
     */
    @GetMapping("/logout")
    public R<?> logout() {
        userService.logout();
        return R.ok();
    }

    /**
     * 用户注销
     *
     * @return
     */
    @GetMapping("/cancel")
    public R<?> cancel() {
        userService.cancel();
        return R.ok();
    }
}
