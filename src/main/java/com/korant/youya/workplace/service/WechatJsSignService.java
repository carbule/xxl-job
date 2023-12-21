package com.korant.youya.workplace.service;

import com.alibaba.fastjson.JSONObject;
import com.korant.youya.workplace.pojo.dto.wechatjssign.WechatJsSignDto;

/**
 * @ClassName WechatJsSignService
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/21 11:00
 * @Version 1.0
 */
public interface WechatJsSignService {

    /**
     * 获取微信JSapi签名
     *
     * @param wechatJsSignDto
     * @return
     */
    JSONObject sign(WechatJsSignDto wechatJsSignDto);
}
