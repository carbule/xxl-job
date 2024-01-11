package com.korant.youya.workplace.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName HttpClientUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/1 13:56
 * @Version 1.0
 */
@Slf4j
public class HttpClientUtil {

    public static HttpClient httpClient;

    public static final int TIMEOUT = 10;

    static {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(TIMEOUT))
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

    /**
     * 发送GET请求
     */
    public static String sentGet(String url) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(TIMEOUT))
                .GET()
                .build();

        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        if (null != httpResponse) {
            int statusCode = httpResponse.statusCode();
            if (HttpStatus.OK.value() == statusCode) {
                return httpResponse.body();
            }
            return null;
        }
        return null;
    }

    /**
     * 发送GET请求
     */
    public static String sentGet(String url, Map<String, Object> paramMap) {

        String paramString = paramMap.entrySet().stream()
                .map(e -> {
                    return e.getKey() + "=" + URLEncoder.encode(e.getValue().toString(), StandardCharsets.UTF_8);
                })
                .collect(Collectors.joining("&"));

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(url + "?" + paramString))
                .timeout(Duration.ofSeconds(TIMEOUT))
                .method(HttpMethod.GET.name(), HttpRequest.BodyPublishers.ofString(JSON.toJSONString(paramMap)))
                .build();

        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        if (null != httpResponse) {
            int statusCode = httpResponse.statusCode();
            if (HttpStatus.OK.value() == statusCode) {
                return httpResponse.body();
            }
            return null;
        }
        return null;
    }

    /**
     * 发送Post请求
     */
    public static String sentPost(String url, Map<String, Object> paramMap) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(TIMEOUT))
                .POST(HttpRequest.BodyPublishers.ofString(JSON.toJSONString(paramMap)))
                .build();
        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        if (null != httpResponse) {
            int statusCode = httpResponse.statusCode();
            if (HttpStatus.OK.value() == statusCode) {
                return httpResponse.body();
            }
            return null;
        }
        return null;
    }

    /**
     * 获取小程序二维码图片二进制数组
     *
     * @param url
     * @param paramObj
     * @return
     */
    public static byte[] getPictureBase64ByPostMethod(String url, JSONObject paramObj) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(TIMEOUT))
                .POST(HttpRequest.BodyPublishers.ofString(paramObj.toJSONString()))
                .build();
        HttpResponse<byte[]> httpResponse = null;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        if (null != httpResponse) {
            int statusCode = httpResponse.statusCode();
            if (HttpStatus.OK.value() == statusCode) {
                return httpResponse.body();
            }
            return null;
        }
        return null;
    }
}
