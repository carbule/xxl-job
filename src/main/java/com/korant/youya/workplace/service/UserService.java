package com.korant.youya.workplace.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.sysorder.CancelOrderDto;
import com.korant.youya.workplace.pojo.dto.sysorder.GeneratePaymentParametersDto;
import com.korant.youya.workplace.pojo.dto.sysorder.QueryClosedOrderListDto;
import com.korant.youya.workplace.pojo.dto.sysorder.QueryOrderListDto;
import com.korant.youya.workplace.pojo.dto.user.*;
import com.korant.youya.workplace.pojo.dto.wallettransactionflow.QueryAccountTransactionFlowListDto;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.pojo.po.UserWalletAccount;
import com.korant.youya.workplace.pojo.po.WalletTransactionFlow;
import com.korant.youya.workplace.pojo.po.WalletWithdrawalRecord;
import com.korant.youya.workplace.pojo.vo.sysorder.SysOrderVo;
import com.korant.youya.workplace.pojo.vo.user.*;
import com.korant.youya.workplace.pojo.vo.wallettransactionflow.AccountTransactionFlowVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;

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
    UserLoginVo loginByWechatCode(WechatCodeDto wechatCodeDto);

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

    /**
     * 查询用户钱包信息
     *
     * @return
     */
    UserWalletVo queryUserWalletInfo();

    /**
     * 检查用户是否有有效的微信Openid
     *
     * @return
     */
    boolean checkIfUserHasValidWechatOpenid();

    /**
     * 更新用户微信openid
     *
     * @param wechatCodeDto
     * @return
     */
    void updateUserWechatOpenId(WechatCodeDto wechatCodeDto);

    /**
     * 用户充值
     *
     * @param userRechargeDto
     * @return
     */
    JSONObject recharge(UserRechargeDto userRechargeDto);

    /**
     * 订单超时处理
     *
     * @param orderId
     */
    void orderTimeoutProcessing(Long orderId);

    /**
     * 用户完成支付
     *
     * @param completePaymentDto
     */
    void completePayment(UserCompletePaymentDto completePaymentDto);

    /**
     * 用户充值通知
     *
     * @param request
     * @param response
     */
    void rechargeNotify(HttpServletRequest request, HttpServletResponse response);

    /**
     * 查询充值结果
     *
     * @param rechargeResultDto
     * @return
     */
    Integer queryRechargeResult(UserQueryRechargeResultDto rechargeResultDto);

    /**
     * 用户订单付款查询
     *
     * @param orderId
     */
    void userOrderPaymentInquiry(Long orderId);

    /**
     * 查询用户支付宝账号
     *
     * @return
     */
    UserAlipayAccountVo queryUserAlipayAccount();

    /**
     * 发送支付宝账号绑定验证码
     *
     * @param verificationCodeDto
     */
    void sendAlipayAccountBindingVerificationCode(BindingVerificationCodeDto verificationCodeDto);

    /**
     * 校验支付宝账号绑定验证码
     *
     * @param checkVerificationCodeDto
     */
    void checkVerificationCode(CheckVerificationCodeDto checkVerificationCodeDto);

    /**
     * 绑定支付宝账号
     *
     * @param bindAlipayAccountDto
     */
    void bindAlipayAccount(BindAlipayAccountDto bindAlipayAccountDto);

    /**
     * 查询用户钱包账户可用余额
     *
     * @return
     */
    BigDecimal queryAccountAvailableBalance();

    /**
     * 解绑支付宝账号
     */
    void unbindAlipayAccount();

    /**
     * 用户钱包提现
     *
     * @param withdrawalDto
     */
    R<?> withdrawal(WithdrawalDto withdrawalDto);

    /**
     * 创建账户提现相关信息
     *
     * @param walletWithdrawalRecord
     * @param walletTransactionFlow
     */
    void createAccountWithdrawalRelatedInfo(WalletWithdrawalRecord walletWithdrawalRecord, WalletTransactionFlow walletTransactionFlow);

    /**
     * 更新账户提现相关信息
     *
     * @param walletWithdrawalRecord
     * @param userWalletAccount
     * @param walletTransactionFlow
     */
    void updateAccountWithdrawalRelatedInfo(WalletWithdrawalRecord walletWithdrawalRecord, UserWalletAccount userWalletAccount, WalletTransactionFlow walletTransactionFlow);

    /**
     * 查询用户订单列表
     *
     * @param queryOrderListDto
     * @return
     */
    Page<SysOrderVo> queryOrderList(QueryOrderListDto queryOrderListDto);

    /**
     * 生成订单支付参数
     *
     * @param generatePaymentParametersDto
     * @return
     */
    JSONObject generatePaymentParameters(GeneratePaymentParametersDto generatePaymentParametersDto);

    /**
     * 取消订单
     *
     * @param cancelOrderDto
     */
    void cancelOrder(CancelOrderDto cancelOrderDto);

    /**
     * 关闭订单
     *
     * @param orderId
     */
    void closeUserOrder(Long orderId);

    /**
     * 查询用户已关闭订单列表
     *
     * @param queryClosedOrderListDto
     * @return
     */
    Page<SysOrderVo> queryClosedOrderList(QueryClosedOrderListDto queryClosedOrderListDto);

    /**
     * 查询用户钱包账户交易流水
     *
     * @param queryAccountTransactionFlowListDto
     * @return
     */
    Page<AccountTransactionFlowVo> queryAccountTransactionFlow(QueryAccountTransactionFlowListDto queryAccountTransactionFlowListDto);
}
