package com.korant.youya.workplace.utils;

import com.alibaba.fastjson.JSONObject;
import com.korant.youya.workplace.pojo.Location;

import java.util.HashMap;

/**
 * @ClassName TencentMapUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/20 20:25
 * @Version 1.0
 */
public class TencentMapUtil {

    private static final String KEY = "LGKBZ-6WEWJ-HLMFY-DUP23-FLSZE-MCB2O";

    /**
     * 文字地址转换经纬度
     *
     * @param address
     * @return
     */
    public static Location geocode(String address) {
        String url = "https://apis.map.qq.com/ws/geocoder/v1/";
        HashMap<String, Object> map = new HashMap<>();
        map.put("address", address);
        map.put("key", KEY);
        String res = HttpClientUtil.sentGet(url, map);
        JSONObject jsonObject = JSONObject.parseObject(res);
        if (null != jsonObject) {
            JSONObject result = jsonObject.getJSONObject("result");
            if (null != result){
                JSONObject location = result.getJSONObject("location");
                if (null != location){
                    return location.toJavaObject(Location.class);
                }
            }
        }
        return null;
    }

    /**
     * {
     * "status": 0,
     * "message": "query ok",
     * "result": {
     * "title": "清辉园",
     * "location": {
     * "lng": 118.706299,
     * "lat": 31.965662
     * },
     * "ad_info": {
     * "adcode": "320105"
     * },
     * "address_components": {
     * "province": "江苏省",
     * "city": "南京市",
     * "district": "建邺区",
     * "street": "",
     * "street_number": ""
     * },
     * "similarity": 0.99,
     * "deviation": 1000,
     * "reliability": 7,
     * "level": 10
     * }
     * }
     *
     * @param args
     */
    public static void main(String[] args) {
        Location s = geocode("江苏省建邺区清辉园");
        System.out.println(s);
    }
}
