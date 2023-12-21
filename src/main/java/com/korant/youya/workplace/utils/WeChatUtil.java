package com.korant.youya.workplace.utils;

import com.alibaba.fastjson.JSONObject;
import com.korant.youya.workplace.exception.YouyaException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

/**
 * @ClassName WeChatUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2023/7/21 15:48
 * @Version 1.0
 */
@Slf4j
public class WeChatUtil {

    //小程序appId
    private final static String APP_ID = "wxac76b91987ca9f96";
    //小程序appSecret
    private final static String APP_SECRET = "280f7c2a134eb1577dfa0f75aed8a1b9";
    //知明贤公众号
    private final static String OFFICIAL_ACCOUNT = "wx0583bd04a25d0695";
    //知明贤公众号secret
    private final static String OFFICIAL_ACCOUNT_SECRET = "b51329f481bf3a0a081eb61654ccd631";


    //小程序accessToken
    private static String MINI_PROGRAM_ACCESS_TOKEN;
    //公众号accessToken
    private static String OFFICIAL_ACCOUNT_ACCESS_TOKEN;
    //JsApiTicket
    private static String JSAPI_TICKET;


    public static String getMiniProgramAccessToken() {
        return MINI_PROGRAM_ACCESS_TOKEN;
    }

    public static String getOfficialAccountAccessToken() {
        return OFFICIAL_ACCOUNT_ACCESS_TOKEN;
    }

    public static String getJsapiTicket() {
        return JSAPI_TICKET;
    }

    /**
     * 获取小程序接口调用凭据
     * access_token 的有效期目前为 2 个小时，需定时刷新，重复获取将导致上次获取的 access_token 失效；
     *
     * @return
     */
    public static String refreshMiniProgramAccessToken() {
        String GRANT_TYPE = "client_credential";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("grant_type", GRANT_TYPE);
        paramMap.put("appid", APP_ID);
        paramMap.put("secret", APP_SECRET);
        paramMap.put("force_refresh", false);
        String response = HttpClientUtil.sentPost("https://api.weixin.qq.com/cgi-bin/stable_token", paramMap);
        if (StringUtils.isNotBlank(response)) {
            log.info("[getAccessToken] response:{}", response);
            JSONObject jsonObject = JSONObject.parseObject(response);
            String access_token = jsonObject.getString("access_token");
            int expires_in = jsonObject.getIntValue("expires_in");
            if (StringUtils.isNotBlank(access_token)) {
                MINI_PROGRAM_ACCESS_TOKEN = access_token;
                return access_token;
            }
        }
        throw new YouyaException("获取微信接口调用凭据失败");
    }

    /**
     * 获取公众号接口调用凭据
     * access_token 的有效期目前为 2 个小时，需定时刷新，重复获取将导致上次获取的 access_token 失效；
     *
     * @return
     */
    public static String refreshOfficialAccountAccessToken() {
        String GRANT_TYPE = "client_credential";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("grant_type", GRANT_TYPE);
        paramMap.put("appid", OFFICIAL_ACCOUNT);
        paramMap.put("secret", OFFICIAL_ACCOUNT_SECRET);
        paramMap.put("force_refresh", false);
        String response = HttpClientUtil.sentPost("https://api.weixin.qq.com/cgi-bin/stable_token", paramMap);
        if (StringUtils.isNotBlank(response)) {
            log.info("[getAccessToken] response:{}", response);
            JSONObject jsonObject = JSONObject.parseObject(response);
            String access_token = jsonObject.getString("access_token");
            int expires_in = jsonObject.getIntValue("expires_in");
            if (StringUtils.isNotBlank(access_token)) {
                OFFICIAL_ACCOUNT_ACCESS_TOKEN = access_token;
                return access_token;
            }
        }
        throw new YouyaException("获取微信接口调用凭据失败");
    }

    /**
     * 获取微信Jsapi调用凭据
     * access_token 的有效期目前为 2 个小时，需定时刷新，重复获取将导致上次获取的 access_token 失效；
     *
     * @return
     */
    public static String refreshJsapiTicket() {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("access_token", OFFICIAL_ACCOUNT_ACCESS_TOKEN);
        paramMap.put("type", "jsapi");
        String response = HttpClientUtil.sentGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket", paramMap);
        if (StringUtils.isNotBlank(response)) {
            log.info("[getJsapiTicket] response:{}", response);
            JSONObject jsonObject = JSONObject.parseObject(response);
            String ticket = jsonObject.getString("ticket");
            int expires_in = jsonObject.getIntValue("expires_in");
            if (StringUtils.isNotBlank(ticket)) {
                JSAPI_TICKET = ticket;
                return ticket;
            }
        }
        throw new YouyaException("获取微信Jsapi调用凭据失败");
    }

    /**
     * 手机号验证
     *
     * @param access_token 通过getAccessToken()方法获取
     * @param code         每个code只能使用一次，code的有效期为5min
     * @return
     */
    public static String getPhoneNumber(String access_token, String code) {
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + access_token;
        JSONObject paramObj = new JSONObject();
        paramObj.put("code", code);
        String response = HttpClientUtil.sentPost(url, paramObj);
        log.info("[getPhoneNumber] response:{}", response);
        if (StringUtils.isNotBlank(response)) {
            JSONObject jsonObject = JSONObject.parseObject(response);
            int errcode = jsonObject.getIntValue("errcode");
            if (0 == errcode) {
                JSONObject phone_info = jsonObject.getJSONObject("phone_info");
                if (null != phone_info) {
                    //用户绑定的手机号（国外手机号会有区号）
                    String phoneNumber = phone_info.getString("phoneNumber");
                    //没有区号的手机号
                    String purePhoneNumber = phone_info.getString("purePhoneNumber");
                    if (StringUtils.isNotBlank(purePhoneNumber)) return purePhoneNumber;
                }
            } else if (40029 == errcode) {
                throw new YouyaException("code无效");
            } else {
                throw new YouyaException("微信系统繁忙");
            }
        }
        return null;
    }

    /**
     * 获取不限制的小程序码
     *
     * @param url
     * @param scene
     * @param page
     * @param env_version
     * @return
     */
    public static byte[] getUnlimitedQRCode(String url, String scene, String page, String env_version) {
        JSONObject paramObj = new JSONObject();
        //最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
        paramObj.put("scene", scene);
        //默认是主页，页面 page，例如 pages/index/index，根路径前不要填加 /，不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面。scancode_time为系统保留参数，不允许配置
        paramObj.put("page", page);
        //默认是true，检查page 是否存在，为 true 时 page 必须是已经发布的小程序存在的页面（否则报错）；为 false 时允许小程序未发布或者 page 不存在， 但page 有数量上限（60000个）请勿滥用。
        paramObj.put("check_path", false);
        //要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。
        paramObj.put("env_version", env_version);
        return HttpClientUtil.getPictureBase64ByPostMethod(url, paramObj);
    }

    public static void main(String[] args) {
//        String code = "ae49cfbb8870f74c883aca9bfdec2e5d2e0e63c2655483e746218e33476be9bf";
        //72_TIta8DY1Ahx5fhGhEhx97EwlelGmYoS0mMX3WrbZbacEop8MwYp48AcjqjjPNKr7pxS_fUhiVllRQvM_NvN8XsD1kvMorxWr3gXizc-BejuDcsqMP0nyC0p92WEBANdAJARQD
        //72_TIta8DY1Ahx5fhGhEhx97EwlelGmYoS0mMX3WrbZbacEop8MwYp48AcjqjjPNKr7pxS_fUhiVllRQvM_NvN8XsD1kvMorxWr3gXizc-BejuDcsqMP0nyC0p92WEBANdAJARQD
//        String accessToken = refreshMiniProgramAccessToken();
//        System.out.println(accessToken);
        String s = refreshOfficialAccountAccessToken();
        System.out.println(s);
    }
}
