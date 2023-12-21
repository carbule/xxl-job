package com.korant.youya.workplace.controller;

import com.alibaba.fastjson.JSONObject;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.wechatjssign.WechatJsSignDto;
import com.korant.youya.workplace.service.WechatJsSignService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WechatJsSignController
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/21 10:59
 * @Version 1.0
 */
@RestController
@RequestMapping("/wechat/js")
public class WechatJsSignController {

    @Resource
    private WechatJsSignService wechatJsSignService;

    /**
     * 获取微信JSapi签名
     *
     * @param wechatJsSignDto
     * @return
     */
    @PostMapping("/sign")
    public R<?> loginByWechatCode(@RequestBody @Valid WechatJsSignDto wechatJsSignDto) {
        JSONObject jsonObject = wechatJsSignService.sign(wechatJsSignDto);
        return R.success(jsonObject);
    }
}
