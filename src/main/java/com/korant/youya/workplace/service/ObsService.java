package com.korant.youya.workplace.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName ObsService
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/3 10:59
 * @Version 1.0
 */
public interface ObsService {

    /**
     * 上传公共文件
     *
     * @param fileCode
     * @param file
     * @return
     */
    String putPublicObject(String fileCode, MultipartFile file);

    /**
     * 上传私有文件
     *
     * @param fileCode
     * @param file
     * @return
     */
    String putPrivateObject(String fileCode, MultipartFile file);

    /**
     * 下载文件
     *
     * @param fileCode
     * @param fileName
     * @param response
     */
    void getObject(String fileCode, String fileName, HttpServletResponse response) throws IOException;

    /**
     * 获取文件签名Url
     *
     * @param fileCode
     * @param fileName
     * @return
     */
    String getSignedUrl(String fileCode, String fileName);

    /**
     * 删除文件
     *
     * @param fileCode
     * @param fileName
     */
    void deleteObject(String fileCode, String fileName);
}
