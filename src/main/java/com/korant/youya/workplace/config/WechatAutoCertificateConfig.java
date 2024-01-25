package com.korant.youya.workplace.config;

import com.korant.youya.workplace.constants.WechatPayConstant;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName WechatAutoCertificateConfig
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/24 15:35
 * @Version 1.0
 */
@Configuration
public class WechatAutoCertificateConfig {

    @Bean
    public RSAAutoCertificateConfig rsaAutoCertificateConfig() {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(WechatPayConstant.MERCHANT_ID)
                .privateKey(WechatPayConstant.PRIVATE_KEY)
                .merchantSerialNumber(WechatPayConstant.MERCHANT_SERIAL_NUMBER)
                .apiV3Key(WechatPayConstant.API_V3_KEY)
                .build();
    }
}
