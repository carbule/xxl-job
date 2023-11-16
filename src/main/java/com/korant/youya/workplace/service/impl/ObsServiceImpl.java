package com.korant.youya.workplace.service.impl;

import com.korant.youya.workplace.config.ObsBucketConfig;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.service.ObsService;
import com.korant.youya.workplace.utils.ObsUtil;
import com.obs.services.model.PutObjectResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName ObsServiceImpl
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/3 11:00
 * @Version 1.0
 */
@Service
@Slf4j
public class ObsServiceImpl implements ObsService {

    /**
     * 上传公共文件
     *
     * @param fileCode
     * @param file
     * @return
     */
    @Override
    public String putPublicObject(String fileCode, MultipartFile file) {
        if (null == file) throw new YouyaException("文件不能为空");
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) throw new YouyaException("文件名称不能为空");
        String contentType = file.getContentType();
        if (StringUtils.isBlank(contentType)) throw new YouyaException("文件类型不能为空");
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))
            throw new YouyaException("文件类型只能是jpg或者png");
        String bucketName = ObsBucketConfig.getBucketName(fileCode);
        String fileName = ObsUtil.getFileBase64Name(file);
        if (StringUtils.isBlank(fileName)) throw new YouyaException("上传文件失败");
        //对象存在
        if (ObsUtil.doesObjectExist(bucketName, fileName)) {
            String encode = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            return "https://" + ObsBucketConfig.getCdn(fileCode) + "/" + encode;
        } else {//对象不存在
            PutObjectResult result = ObsUtil.putObject(bucketName, fileName, file);
            if (null == result) throw new YouyaException("上传文件失败");
            String etag = result.getEtag();
            String objectUrl = result.getObjectUrl();
            String objectKey = result.getObjectKey();
            if (StringUtils.isBlank(etag) && StringUtils.isBlank(objectUrl)) throw new YouyaException("上传文件失败");
            String encode = URLEncoder.encode(objectKey, StandardCharsets.UTF_8);
            return "https://" + ObsBucketConfig.getCdn(fileCode) + "/" + encode;
        }
    }

    /**
     * 上传私有文件
     *
     * @param fileCode
     * @param file
     * @return
     */
    @Override
    public String putPrivateObject(String fileCode, MultipartFile file) {
        if (null == file) throw new YouyaException("文件不能为空");
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) throw new YouyaException("文件名称不能为空");
        String contentType = file.getContentType();
        if (StringUtils.isBlank(contentType)) throw new YouyaException("文件类型不能为空");
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("application/pdf"))
            throw new YouyaException("文件类型只能是jpg、png或者pdf");
        String bucketName = ObsBucketConfig.getBucketName(fileCode);
        String fileName = ObsUtil.getFileBase64Name(file);
        if (StringUtils.isBlank(fileName)) throw new YouyaException("上传文件失败");
        if (ObsUtil.doesObjectExist(bucketName, fileName)) {
            return fileName;
        } else {
            PutObjectResult result = ObsUtil.putObject(bucketName, fileName, file);
            if (null == result) throw new YouyaException("上传文件失败");
            String etag = result.getEtag();
            String objectKey = result.getObjectKey();
            if (StringUtils.isBlank(etag) && StringUtils.isBlank(objectKey)) throw new YouyaException("上传文件失败");
            return objectKey;
        }
    }

    /**
     * 下载文件
     *
     * @param fileCode
     * @param fileName
     * @param response
     */
    @Override
    public void getObject(String fileCode, String fileName, HttpServletResponse response) throws IOException {
        String bucketName = ObsBucketConfig.getBucketName(fileCode);
        byte[] data = ObsUtil.getObject(bucketName, fileName);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(data);
        } finally {
            assert outputStream != null;
            outputStream.close();
        }
    }

    /**
     * 获取文件签名Url
     *
     * @param fileCode
     * @param fileName
     * @return
     */
    @Override
    public String getSignedUrl(String fileCode, String fileName) {
        String bucketName = ObsBucketConfig.getBucketName(fileCode);
        return ObsUtil.getSignedUrl(bucketName, fileName);
    }

    /**
     * 删除文件
     *
     * @param fileCode
     * @param fileName
     */
    @Override
    public void deleteObject(String fileCode, String fileName) {
        String bucketName = ObsBucketConfig.getBucketName(fileCode);
        ObsUtil.deleteObject(bucketName, fileName);
    }
}
