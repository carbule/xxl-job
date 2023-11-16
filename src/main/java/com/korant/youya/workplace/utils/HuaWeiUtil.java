package com.korant.youya.workplace.utils;

import com.alibaba.fastjson.JSONObject;
import com.cloud.apigateway.sdk.utils.Client;
import com.cloud.apigateway.sdk.utils.Request;
import com.korant.youya.workplace.constants.Constant;
import com.korant.youya.workplace.exception.YouyaException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @ClassName HuaWeiUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2023/7/26 17:17
 * @Version 1.0
 */
@Slf4j
public class HuaWeiUtil {

    public static final String AppKey = "0f0dd9f71f0443268d7bf3135adf782c";
    public static final String AppSecret = "ef3786129f964c8a9c1f591075da1777";

    /**
     * 发送短信验证码
     *
     * @param code
     * @param mobile
     * @return
     */
    public static boolean sendVerificationCode(String code, String mobile) {
        String[] param = {code, mobile};
        for (String s : param) {
            if (StringUtils.isBlank(s)) throw new YouyaException("短信验证码参数缺失");
        }
        String template = "【友涯】验证码：%s，有效期10分钟。如非本人操作，请忽略。";
        String content = String.format(template, code);
        Request request = new Request();
        try {
            request.setKey(AppKey);
            request.setSecret(AppSecret);
            request.setMethod(HttpMethod.GET.name());
            request.setUrl("https://codesms.apistore.huaweicloud.com/chuangxin/yzmdxjk?content=" + content + "&mobile=" + mobile);
            request.addHeader("Content-Type", "text/plain");
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        CloseableHttpClient client = null;
        try {
            // Sign the request.
            HttpRequestBase signedRequest = Client.sign(request, Constant.SIGNATURE_ALGORITHM_SDK_HMAC_SHA256);
            // Do not verify ssl certificate
            client = (CloseableHttpClient) SSLCipherSuiteUtil.createHttpClient(Constant.INTERNATIONAL_PROTOCOL);
            HttpResponse response = client.execute(signedRequest);
            // Print the body of the response.
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 获取响应状态
                String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info("短信验证码接口响应数据：{}", responseStr);
                int status = response.getStatusLine().getStatusCode();
                if (status == HttpStatus.SC_OK) {
                    // 获取响应数据
                    if (StringUtils.isNotBlank(responseStr)) {
                        JSONObject jsonObject = JSONObject.parseObject(responseStr);
                        int successCounts = jsonObject.getIntValue("SuccessCounts");
                        String returnStatus = jsonObject.getString("ReturnStatus");
                        String message = jsonObject.getString("Message");
                        if (1 == successCounts && "Success".equals(returnStatus) && "ok".equals(message)) {
                            log.info("手机号:{},短信验证码发送成功!", mobile);
                            return true;
                        }
                        log.error("手机号:{},短信验证码发送失败!", mobile);
                    }
                } else {
                    log.error("短信验证码接口失败，状态码：" + status);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 实名认证
     *
     * @param idcard
     * @param mobile
     * @param name
     * @return
     */
    public static boolean realNameAuth(String idcard, String mobile, String name) {
        String[] param = {idcard, mobile, name};
        for (String s : param) {
            if (StringUtils.isBlank(s)) throw new YouyaException("实名认证参数缺失");
        }
        Request request = new Request();
        try {
            request.setKey(AppKey);
            request.setSecret(AppSecret);
            request.setMethod(HttpMethod.GET.name());
            request.setUrl("http://pck.apistore.huaweicloud.com/efficient/cellphone?idCard=" + idcard + "&mobile=" + mobile + "&realName=" + name);
            request.addHeader("Content-Type", "text/plain");
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        CloseableHttpClient client = null;
        try {
            // Sign the request.
            HttpRequestBase signedRequest = Client.sign(request, Constant.SIGNATURE_ALGORITHM_SDK_HMAC_SHA256);
            // Do not verify ssl certificate
            client = (CloseableHttpClient) SSLCipherSuiteUtil.createHttpClient(Constant.INTERNATIONAL_PROTOCOL);
            HttpResponse response = client.execute(signedRequest);
            // Print the body of the response.
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 获取响应状态
                String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info("实名认证接口响应数据：{}", responseStr);
                int status = response.getStatusLine().getStatusCode();
                if (status == HttpStatus.SC_OK) {
                    // 获取响应数据
                    if (StringUtils.isNotBlank(responseStr)) {
                        JSONObject jsonObject = JSONObject.parseObject(responseStr);
                        int error_code = jsonObject.getIntValue("error_code");
                        JSONObject result = jsonObject.getJSONObject("result");
                        String VerificationResult = result.getString("VerificationResult");
                        if (0 == error_code && "1".equals(VerificationResult)) {
                            log.info("手机号:{},实名认证成功!真实姓名:{},身份证号:{}", mobile, name, idcard);
                            return true;
                        }
                        log.error("手机号:{},实名认证失败!", mobile);
                    }
                } else {
                    log.error("实名认证接口失败，状态码：" + status);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 根据文件url识别营业执照内容
     *
     * @param url
     */
    public static JSONObject getLicenseContentByUrl(String url) {
        JSONObject paramObj = new JSONObject();
        paramObj.put("image", url);
        Request request = new Request();
        try {
            request.setKey(AppKey);
            request.setSecret(AppSecret);
            request.setMethod(HttpMethod.POST.name());
            request.setUrl("https://buslicense.apistore.huaweicloud.com/buslicense");
            request.addHeader("Content-Type", "text/plain");
            request.setBody(paramObj.toJSONString());
            return getLicenseContentByOcr(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据文件识别营业执照内容
     *
     * @param file
     */
    public static JSONObject getLicenseContentByFile(MultipartFile file) {
        String imageBase64 = "";
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new ByteArrayOutputStream();
            int read;
            byte[] buf = new byte[1024];
            while ((read = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, read);
            }
            outputStream.flush();
            byte[] data = outputStream.toByteArray();
            String fileBase64 = Base64.getEncoder().encodeToString(data);
            String contentType = file.getContentType();
            log.info("contentType:{}", contentType);
            imageBase64 = "data:" + contentType + ";base64," + fileBase64;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONObject paramObj = new JSONObject();
        paramObj.put("image", imageBase64);
        Request request = new Request();
        try {
            request.setKey(AppKey);
            request.setSecret(AppSecret);
            request.setMethod(HttpMethod.POST.name());
            request.setUrl("https://buslicense.apistore.huaweicloud.com/buslicense");
            request.addHeader("Content-Type", "text/plain");
            request.setBody(paramObj.toJSONString());
            return getLicenseContentByOcr(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取营业执照json
     *
     * @param request
     * @return
     */
    public static JSONObject getLicenseContentByOcr(Request request) {
        CloseableHttpClient client = null;
        try {
            // Sign the request.
            HttpRequestBase signedRequest = Client.sign(request, Constant.SIGNATURE_ALGORITHM_SDK_HMAC_SHA256);
            // Do not verify ssl certificate
            client = (CloseableHttpClient) SSLCipherSuiteUtil.createHttpClient(Constant.INTERNATIONAL_PROTOCOL);
            HttpResponse response = client.execute(signedRequest);
            // Print the body of the response.
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 获取响应状态
                String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info("识别ocr营业执照接口响应数据：{}", responseStr);
                int status = response.getStatusLine().getStatusCode();
                if (status == HttpStatus.SC_OK) {
                    // 获取响应数据
                    if (StringUtils.isNotBlank(responseStr)) {
                        JSONObject jsonObject = JSONObject.parseObject(responseStr);
                        String code = jsonObject.getString("code");
                        if ("1".equals(code)) {
                            return jsonObject.getJSONObject("result");
                        } else {
                            String msg = jsonObject.getString("msg");
                            log.error("ocr营业执照认证失败,状态码:{},失败信息:{}", code, msg);
                        }
                    }
                } else {
                    log.error("识别ocr营业执照接口失败，状态码：" + status);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
//        boolean auth = realNameAuth("320621199710065918", "18052042163", "陈毅强");
//        System.out.println(auth);
        sendVerificationCode("123456", "18052042163");
    }
}
