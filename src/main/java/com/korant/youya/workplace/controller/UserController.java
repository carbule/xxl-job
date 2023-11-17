package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.user.UserLoginByPasswordDto;
import com.korant.youya.workplace.pojo.dto.user.UserLoginBySMSVerificationCodeDto;
import com.korant.youya.workplace.pojo.dto.user.UserLoginByWechatCodeDto;
import com.korant.youya.workplace.pojo.dto.user.VerificationCodeDto;
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
        String token = userService.loginByWechatCode(wechatCodeDto);
        return R.success(token);
    }

    /**
     * 短信验证码登陆
     *
     * @param smsVerificationCodeDto
     * @return
     */
    @PostMapping("/loginBySMSVerificationCode")
    public R<?> loginBySMSVerificationCode(@RequestBody @Valid UserLoginBySMSVerificationCodeDto smsVerificationCodeDto) {
        String token = userService.loginBySMSVerificationCode(smsVerificationCodeDto);
        return R.success(token);
    }

    /**
     * 密码登陆
     *
     * @param passwordDto
     * @return
     */
    @PostMapping("/loginByPassword")
    public R<?> loginByPassword(@RequestBody @Valid UserLoginByPasswordDto passwordDto) {
        String token = userService.loginByPassword(passwordDto);
        return R.success(token);
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
     * 用户登出
     *
     * @return
     */
    @GetMapping("/logout")
    public R<?> logout() {
        userService.logout();
        return R.ok();
    }
}
