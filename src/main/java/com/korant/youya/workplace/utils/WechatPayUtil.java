package com.korant.youya.workplace.utils;

import com.korant.youya.workplace.constants.WechatConstant;
import com.korant.youya.workplace.constants.WechatPayConstant;
import com.korant.youya.workplace.exception.YouyaException;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;

/**
 * @ClassName WechatPayUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2024/2/28 13:59
 * @Version 1.0
 */
public class WechatPayUtil {

    private static RSAAutoCertificateConfig rsaAutoCertificateConfig = null;
    private static JsapiService jsapiService = null;

    static {
        rsaAutoCertificateConfig = new RSAAutoCertificateConfig.Builder()
                .merchantId(WechatPayConstant.MERCHANT_ID)
                .privateKey(WechatPayConstant.PRIVATE_KEY)
                .merchantSerialNumber(WechatPayConstant.MERCHANT_SERIAL_NUMBER)
                .apiV3Key(WechatPayConstant.API_V3_KEY)
                .build();
        jsapiService = new JsapiService.Builder().config(rsaAutoCertificateConfig).build();
    }

    /**
     * 小程序下单
     *
     * @param description 商品描述
     * @param outTradeNo  商户订单号
     * @param notifyUrl   通知地址
     * @param totalAmount 订单总金额
     * @param openid      微信用户openid
     * @return
     */
    public static String prepay(String description, String outTradeNo, String notifyUrl, Integer totalAmount, String openid) {
        PrepayRequest request = new PrepayRequest();
        //公众号id
        request.setAppid(WechatConstant.APP_ID);
        //直连商户号
        request.setMchid(WechatPayConstant.MERCHANT_ID);
        //商品描述
        request.setDescription(description);
        //商户订单号
        request.setOutTradeNo(outTradeNo);
        //交易结束时间
//        request.setTimeExpire();
        //通知地址
        request.setNotifyUrl(notifyUrl);
        //订单金额
        Amount amount = new Amount();
        //总金额
        amount.setTotal(totalAmount);
        //货币类型
        amount.setCurrency("CNY");
        request.setAmount(amount);
        //支付者
        Payer payer = new Payer();
        //微信用户openid
        payer.setOpenid(openid);
        request.setPayer(payer);

        PrepayResponse response = jsapiService.prepay(request);
        if (null != response) {
            return response.getPrepayId();
        }
        return null;
    }

    /**
     * 小程序下单并生成调起支付参数
     *
     * @param description
     * @param outTradeNo
     * @param notifyUrl
     * @param totalAmount
     * @param openid
     * @return
     */
    public static PrepayWithRequestPaymentResponse prepayWithRequestPayment(String description, String outTradeNo, String notifyUrl, Integer totalAmount, String openid) {
        JsapiServiceExtension service = new JsapiServiceExtension.Builder().config(rsaAutoCertificateConfig).build();
        //跟之前下单示例一样，填充预下单参数
        PrepayRequest request = new PrepayRequest();
        //公众号id
        request.setAppid(WechatConstant.APP_ID);
        //直连商户号
        request.setMchid(WechatPayConstant.MERCHANT_ID);
        //商品描述
        request.setDescription(description);
        //商户订单号
        request.setOutTradeNo(outTradeNo);
        //交易结束时间
//        request.setTimeExpire();
        //通知地址
        request.setNotifyUrl(notifyUrl);
        //订单金额
        Amount amount = new Amount();
        //总金额
        amount.setTotal(totalAmount);
        //货币类型
        amount.setCurrency("CNY");
        request.setAmount(amount);
        //支付者
        Payer payer = new Payer();
        //微信用户openid
        payer.setOpenid(openid);
        request.setPayer(payer);

        //response包含了调起支付所需的所有参数，可直接用于前端调起支付
        return service.prepayWithRequestPayment(request);
    }

    /**
     * 验签解密
     *
     * @param requestParam
     * @return
     */
    public static Transaction parse(RequestParam requestParam) {
        //初始化 NotificationParser
        NotificationParser parser = new NotificationParser(rsaAutoCertificateConfig);

        try {
            //以支付通知回调为例，验签、解密并转换成 Transaction
            return parser.parse(requestParam, Transaction.class);
        } catch (ValidationException e) {
            //签名验证失败，返回 401 UNAUTHORIZED 状态码
            throw new YouyaException(401, "UNAUTHORIZED");
        }
    }

    /**
     * 根据微信支付订单号查询订单状态
     *
     * @param transactionId
     * @return
     */
    public static Transaction queryOrderById(String transactionId) {
        QueryOrderByIdRequest request = new QueryOrderByIdRequest();
        //直连商户号
        request.setMchid(WechatPayConstant.MERCHANT_ID);
        //微信支付订单号
        request.setTransactionId(transactionId);

        return jsapiService.queryOrderById(request);
    }

    /**
     * 根据商户订单号查询订单状态
     *
     * @param outTradeNo
     * @return
     */
    public static Transaction queryOrderByOutTradeNo(String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        //直连商户号
        request.setMchid(WechatPayConstant.MERCHANT_ID);
        //商户订单号
        request.setOutTradeNo(outTradeNo);

        return jsapiService.queryOrderByOutTradeNo(request);
    }

    /**
     * 根据商户订单号关闭订单
     *
     * @param outTradeNo
     */
    public static void closeOrder(String outTradeNo) {
        CloseOrderRequest request = new CloseOrderRequest();
        //直连商户号
        request.setMchid(WechatPayConstant.MERCHANT_ID);
        //商户订单号
        request.setOutTradeNo(outTradeNo);

        jsapiService.closeOrder(request);
    }

    public static void main(String[] args) {
//        String prepay = prepay("测试商品", "1111112222", "https://baidu.com", 100, "o7sjA6-dB6gGsYGcL2BfemKELOBE");
//        System.out.println(prepay);
        closeOrder("1111112222");
    }
}
