package com.korant.youya.workplace.utils;

import cn.hutool.core.util.ObjectUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundAccountQueryRequest;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.korant.youya.workplace.constants.AliPayConstant;
import com.korant.youya.workplace.exception.YouyaException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @ClassName AlipayUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/4 15:17
 * @Version 1.0
 */
public class AlipayUtil {

    private static DefaultAlipayClient alipayClient;

    private static final String URL = "https://openapi.alipay.com/gateway.do";

    private static final String ACCOUNT_TYPE = "ACCTRANS_ACCOUNT";

    private static final String BIZ_SCENE = "DIRECT_TRANSFER";

    private static final String PRODUCT_CODE = "TRANS_ACCOUNT_NO_PWD";

    //收款方的标识ID
    //当 identity_type=ALIPAY_USER_ID 时，填写支付宝用户 UID。示例值：2088123412341234。
    //当 identity_type=ALIPAY_LOGON_ID 时，填写支付宝登录号。示例值：186xxxxxxxx。
    private static final String IDENTITY_TYPE = "ALIPAY_LOGON_ID";

    static {
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置网关地址
        alipayConfig.setServerUrl(URL);
        //设置应用ID
        alipayConfig.setAppId(AliPayConstant.APP_ID);
        //设置应用私钥
        alipayConfig.setPrivateKey(AliPayConstant.PRIVATE_KEY);
        //设置请求格式，固定值json
        alipayConfig.setFormat("json");
        //设置字符集
        alipayConfig.setCharset("UTF-8");
        //设置签名类型
        alipayConfig.setSignType("RSA2");
        //设置应用公钥证书路径
        alipayConfig.setAppCertPath(AliPayConstant.APP_CERT_PATH);
        //设置支付宝公钥证书路径
        alipayConfig.setAlipayPublicCertPath(AliPayConstant.ALIPAY_PUBLIC_CERT_PATH);
        //设置支付宝根证书路径
        alipayConfig.setRootCertPath(AliPayConstant.ROOT_CERT_PATH);
        //构造client
        try {
            alipayClient = new DefaultAlipayClient(alipayConfig);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 账户余额查询
     *
     * @return
     */
    public static AlipayFundAccountQueryResponse fundAccountQuery() {
        AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
        AlipayFundAccountQueryModel model = new AlipayFundAccountQueryModel();
        model.setAlipayUserId(AliPayConstant.PID);
        model.setAccountType(ACCOUNT_TYPE);
        request.setBizModel(model);
        try {
            return alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转账
     *
     * @param orderTitle
     * @param outBizNo
     * @param transAmount
     * @param identity
     * @param name
     */
    public static AlipayFundTransUniTransferResponse transfer(String orderTitle, String outBizNo, BigDecimal transAmount, String identity, String name) {
        if (StringUtils.isBlank(orderTitle)) throw new YouyaException("转账业务标题缺失");
        if (ObjectUtil.isNull(outBizNo)) throw new YouyaException("商家转账订单号缺失");
        if (ObjectUtil.isNull(transAmount)) throw new YouyaException("转账总金额缺失");
        if (StringUtils.isBlank(identity)) throw new YouyaException("转账收款方账号缺失");
        if (StringUtils.isBlank(name)) throw new YouyaException("转账收款方名字缺失");
        // 构造请求参数以调用接口
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        //设置转账业务的标题(必填)
        model.setOrderTitle(orderTitle);
        //设置描述特定的业务场景(固定参数)
        model.setBizScene(BIZ_SCENE);
        //设置转账业务请求的扩展参数(非必填)
//        model.setBusinessParams("{\"payer_show_name_use_alias\":\"true\"}");
        //设置业务备注(非必填)
//        model.setRemark("201905代发");
        //设置商家侧唯一订单号(必填)
        model.setOutBizNo(outBizNo);
        //设置订单总金额(必填)
        model.setTransAmount(transAmount.toString());
        //设置业务产品码(固定参数)
        model.setProductCode(PRODUCT_CODE);
        //设置收款方信息(必填)
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity(identity);
        payeeInfo.setName(name);
        payeeInfo.setIdentityType(IDENTITY_TYPE);
        model.setPayeeInfo(payeeInfo);
        request.setBizModel(model);
        try {
            return alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转账单据查询
     * <p>
     * 查询入参 out_biz_no、order_id 与 pay_fund_order_id 三者不可同时为空，三者优先级为 pay_fund_order_id > order_id > out_biz_no，
     * 高优先级与低优先级参数同时给出时，用高优先级参数查询，忽略低优先级参数
     *
     * @param payFundOrderId
     * @param alipayOrderId
     * @param orderId
     * @return
     */
    public static AlipayFundTransCommonQueryResponse fundTransCommonQuery(String payFundOrderId, String alipayOrderId, String orderId) {
        if (StringUtils.isBlank(payFundOrderId) && StringUtils.isBlank(alipayOrderId) && StringUtils.isBlank(orderId))
            throw new YouyaException("转账单据查询参数缺失");
        // 构造请求参数以调用接口
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        AlipayFundTransCommonQueryModel model = new AlipayFundTransCommonQueryModel();
        // 设置描述特定的业务场景
        //如果传递了out_biz_no 则该字段为必传。单笔无密转账固定为DIRECT_TRANSFER
        model.setBizScene(BIZ_SCENE);
        // 设置支付宝支付资金流水号
        model.setPayFundOrderId(payFundOrderId);
        // 设置销售产品码
        //如果传了 out_biz_no，则该字段必传。单笔无密转账固定为TRANS_ACCOUNT_NO_PWD
        model.setProductCode(PRODUCT_CODE);
        // 设置商户转账唯一订单号
        model.setOutBizNo(orderId);
        // 设置支付宝转账单据号
        model.setOrderId(alipayOrderId);
        request.setBizModel(model);
        try {
            return alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        AlipayFundAccountQueryResponse alipayFundAccountQueryResponse = fundAccountQuery();
        assert alipayFundAccountQueryResponse != null;
        System.out.println(alipayFundAccountQueryResponse.getAvailableAmount());
    }
}
