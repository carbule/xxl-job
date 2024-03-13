package com.korant.youya.workplace.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.sysorder.CancelOrderDto;
import com.korant.youya.workplace.pojo.dto.sysorder.GeneratePaymentParametersDto;
import com.korant.youya.workplace.pojo.dto.sysorder.QueryOrderListDto;
import com.korant.youya.workplace.pojo.dto.user.*;
import com.korant.youya.workplace.pojo.vo.sysorder.SysOrderVo;
import com.korant.youya.workplace.pojo.vo.user.*;
import com.korant.youya.workplace.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
    public R<?> loginByWechatCode(@RequestBody @Valid WechatCodeDto wechatCodeDto) {
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

    /**
     * 修改用户头像
     *
     * @return
     */
    @PostMapping("/modifyUserAvatar")
    public R<?> modifyUserAvatar(@RequestBody @Valid ModifyUserAvatarDto modifyUserAvatarDto) {
        userService.modifyUserAvatar(modifyUserAvatarDto);
        return R.ok();
    }

    /**
     * 查询个人基本信息
     *
     * @param
     * @return
     */
    @GetMapping("/queryPersonalBasicInfo")
    @ExplanationDict
    public R<?> queryPersonalBasicInfo() {
        UserPersonalBasicInfoVo personalBasicInfoVo = userService.queryPersonalBasicInfo();
        return R.success(personalBasicInfoVo);
    }

    /**
     * 修改个人基本信息
     *
     * @param
     * @return
     */
    @PostMapping("/modifyPersonalBasicInfo")
    public R<?> modifyPersonalInfo(@RequestBody @Valid ModifyUserPersonalBasicInfoDto modifyDto) {
        userService.modifyUserPersonalBasicInfo(modifyDto);
        return R.ok();
    }

    /**
     * 查询用户联系方式
     *
     * @param
     * @return
     */
    @GetMapping("/queryUserContactInfo")
    public R<?> queryUserContactInfo() {
        UserContactInfoVo contactInfoVo = userService.queryUserContactInfo();
        return R.success(contactInfoVo);
    }

    /**
     * 修改联系方式
     *
     * @param
     * @return
     */
    @PostMapping("/modifyContactInfo")
    public R<?> modifyContactInfo(@RequestBody @Valid ModifyUserContactInfoDto modifyDto) {
        userService.modifyUserContactInfo(modifyDto);
        return R.ok();
    }

    /**
     * 查询登录用户信息
     *
     * @param
     * @return
     */
    @GetMapping("/queryLoginUserInfo")
    public R<?> queryCurrentUserInfo() {
        LoginUserVo loginUserVo = userService.queryLoginUserInfo();
        return R.success(loginUserVo);
    }

    /**
     * 简历详情
     *
     * @param
     * @return
     */
    @GetMapping("/resumeDetail")
    @ExplanationDict
    public R<?> resumeDetail() {
        ResumeDetailVo resumeDetailVo = userService.resumeDetail();
        return R.success(resumeDetailVo);
    }

    /**
     * 简历预览
     *
     * @param
     * @return
     */
    @GetMapping("/resumePreview")
    @ExplanationDict
    public R<?> resumePreview() {
        ResumePreviewVo resumePreviewVo = userService.resumePreview();
        return R.success(resumePreviewVo);
    }

    /**
     * 申请关联企业
     *
     * @return
     */
    @PostMapping("/applyAffiliatedEnterprise")
    public R<?> applyAffiliatedEnterprise(@RequestBody @Valid ApplyAffiliatedEnterpriseDto applyAffiliatedEnterpriseDto) {
        userService.affiliatedEnterprise(applyAffiliatedEnterpriseDto);
        return R.ok();
    }

    /**
     * 解除关联企业
     *
     * @return
     */
    @GetMapping("/relieveAffiliated")
    public R<?> relieveAffiliated() {
        userService.relieveAffiliated();
        return R.ok();
    }

    /**
     * 查询用户钱包信息
     *
     * @return
     */
    @GetMapping("/queryUserWalletInfo")
    public R<?> queryUserWalletInfo() {
        UserWalletVo userWalletVo = userService.queryUserWalletInfo();
        return R.success(userWalletVo);
    }

    /**
     * 检查用户是否有有效的微信Openid
     *
     * @return
     */
    @GetMapping("/checkIfUserHasValidWechatOpenid")
    public R<?> checkIfUserHasValidWechatOpenid() {
        boolean result = userService.checkIfUserHasValidWechatOpenid();
        return R.success(result);
    }

    /**
     * 更新用户微信openid
     *
     * @param wechatCodeDto
     * @return
     */
    @PostMapping("/updateUserWechatOpenId")
    public R<?> updateUserWechatOpenId(@RequestBody @Valid WechatCodeDto wechatCodeDto) {
        userService.updateUserWechatOpenId(wechatCodeDto);
        return R.ok();
    }

    /**
     * 用户微信充值
     *
     * @param userRechargeDto
     * @return
     */
    @PostMapping("/recharge")
    public R<?> recharge(@RequestBody @Valid UserRechargeDto userRechargeDto) {
        JSONObject result = userService.recharge(userRechargeDto);
        return R.success(result);
    }

    /**
     * 用户完成支付
     *
     * @return
     */
    @PostMapping("/completePayment")
    public R<?> completePayment(@RequestBody @Valid UserCompletePaymentDto completePaymentDto) {
        userService.completePayment(completePaymentDto);
        return R.ok();
    }

    /**
     * 用户充值通知
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/rechargeNotify")
    public void rechargeNotify(HttpServletRequest request, HttpServletResponse response) {
        userService.rechargeNotify(request, response);
    }

    /**
     * 查询用户充值结果
     *
     * @return
     */
    @PostMapping("/queryRechargeResult")
    public R<?> queryRechargeResult(@RequestBody @Valid UserQueryRechargeResultDto rechargeResultDto) {
        Integer status = userService.queryRechargeResult(rechargeResultDto);
        return R.success(status);
    }

    /**
     * 查询用户支付宝账号
     *
     * @return
     */
    @GetMapping("queryUserAlipayAccount")
    public R<?> queryUserAlipayAccount() {
        UserAlipayAccountVo userAlipayAccountVo = userService.queryUserAlipayAccount();
        return R.success(userAlipayAccountVo);
    }

    /**
     * 发送支付宝账号绑定验证码
     *
     * @return
     */
    @PostMapping("sendAlipayAccountBindingVerificationCode")
    public R<?> sendAlipayAccountBindingVerificationCode(@RequestBody @Valid BindingVerificationCodeDto verificationCodeDto) {
        userService.sendAlipayAccountBindingVerificationCode(verificationCodeDto);
        return R.ok();
    }

    /**
     * 校验支付宝账号绑定验证码
     *
     * @return
     */
    @PostMapping("checkVerificationCode")
    public R<?> checkVerificationCode(@RequestBody @Valid CheckVerificationCodeDto checkVerificationCodeDto) {
        userService.checkVerificationCode(checkVerificationCodeDto);
        return R.ok();
    }

    /**
     * 绑定支付宝账号
     *
     * @param realNameAuthDto
     * @return
     */
    @PostMapping("/bindAlipayAccount")
    public R<?> bindAlipayAccount(@RequestBody @Valid BindAlipayAccountDto realNameAuthDto) {
        userService.bindAlipayAccount(realNameAuthDto);
        return R.ok();
    }

    /**
     * 查询用户钱包账户可用余额
     *
     * @return
     */
    @GetMapping("/queryAccountAvailableBalance")
    public R<?> queryAccountAvailableBalance() {
        BigDecimal availableBalance = userService.queryAccountAvailableBalance();
        return R.success(availableBalance);
    }

    /**
     * 解绑支付宝账号
     *
     * @return
     */
    @GetMapping("unbindAlipayAccount")
    public R<?> unbindAlipayAccount() {
        userService.unbindAlipayAccount();
        return R.ok();
    }

    /**
     * 用户钱包提现
     *
     * @param withdrawalDto
     * @return
     */
    @PostMapping("/withdrawal")
    public R<?> withdrawal(@RequestBody @Valid WithdrawalDto withdrawalDto) {
        return userService.withdrawal(withdrawalDto);
    }

    /**
     * 查询用户订单列表
     *
     * @param queryOrderListDto
     * @return
     */
    @PostMapping("/queryOrderList")
    public R<?> queryOrderList(@RequestBody @Valid QueryOrderListDto queryOrderListDto) {
        Page<SysOrderVo> page = userService.queryOrderList(queryOrderListDto);
        return R.success(page);
    }

    /**
     * 生成订单支付参数
     *
     * @param generatePaymentParametersDto
     * @return
     */
    @PostMapping("/generatePaymentParameters")
    public R<?> generatePaymentParameters(@RequestBody @Valid GeneratePaymentParametersDto generatePaymentParametersDto) {
        JSONObject result = userService.generatePaymentParameters(generatePaymentParametersDto);
        return R.success(result);
    }

    /**
     * 取消订单
     *
     * @param cancelOrderDto
     * @return
     */
    @PostMapping("/cancelOrder")
    public R<?> cancelOrder(@RequestBody @Valid CancelOrderDto cancelOrderDto) {
        userService.cancelOrder(cancelOrderDto);
        return R.ok();
    }
}
