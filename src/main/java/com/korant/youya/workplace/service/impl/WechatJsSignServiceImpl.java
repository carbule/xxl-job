package com.korant.youya.workplace.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.korant.youya.workplace.pojo.dto.wechatjssign.WechatJsSignDto;
import com.korant.youya.workplace.service.WechatJsSignService;
import com.korant.youya.workplace.utils.SignUtil;
import com.korant.youya.workplace.utils.WeChatUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName WechatJsSignServiceImpl
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/21 11:00
 * @Version 1.0
 */
@Service
public class WechatJsSignServiceImpl implements WechatJsSignService {

    /**
     * 获取微信JSapi签名
     *
     * @param wechatJsSignDto
     * @return
     */
    @Override
    public JSONObject sign(WechatJsSignDto wechatJsSignDto) {
        String url = wechatJsSignDto.getUrl();
        String jsapiTicket = WeChatUtil.getJsapiTicket();
        Map<String, Object> sign = SignUtil.sign(jsapiTicket, url);
        return new JSONObject(sign);
    }
}
