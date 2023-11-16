package com.korant.youya.workplace.utils;

import com.korant.youya.workplace.exception.YouyaException;
import com.obs.services.BasicObsCredentialsProvider;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.internal.utils.ServiceUtils;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName ObsUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/2 19:23
 * @Version 1.0
 */
@Slf4j
public class ObsUtil {

    //endPoint节点
    public static final String END_POINT = "https://obs.cn-east-3.myhuaweicloud.com";

    //华东-上海一
    public static final String LOCATION = "cn-east-3";

    public static final String ACCESS_KEY = "ERHOQFONH3TMYMXBYA8Q";

    public static final String SECRET_KEY = "HXkS5616SMxFDkM3oP6WRGNHiamv3VchK5OlCrpZ";

    private static final ObsClient obsClient = new ObsClient(new BasicObsCredentialsProvider(ACCESS_KEY, SECRET_KEY), END_POINT);

    /**
     * 创建bucket
     *
     * @param bucketName
     */
    public static void createBucket(String bucketName) {
        CreateBucketRequest request = new CreateBucketRequest();
        request.setBucketName(bucketName);
        // 设置桶访问权限为公共读，默认是私有读写
        request.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
        // 设置桶的存储类型为归档存储
        request.setBucketStorageClass(StorageClassEnum.STANDARD);
        // 设置桶区域位置(以区域为华北-北京四为例)
        request.setLocation(LOCATION);
        // 指定创建多AZ桶，如果不设置，默认创建单AZ桶
        request.setAvailableZone(AvailableZoneEnum.MULTI_AZ);
        // 创建桶
        try {
            // 创建桶成功
            ObsBucket bucket = obsClient.createBucket(request);
            System.out.println(bucket.getRequestId());
        } catch (ObsException e) {
            // 创建桶失败
            System.out.println("HTTP Code: " + e.getResponseCode());
            System.out.println("Error Code:" + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());

            System.out.println("Request ID:" + e.getErrorRequestId());
            System.out.println("Host ID:" + e.getErrorHostId());
        }
    }

    /**
     * 列举bucket
     *
     * @return
     */
    public static List<ObsBucket> listBuckets() {
        // 列举桶
        ListBucketsRequest request = new ListBucketsRequest();
        request.setQueryLocation(true);
        List<ObsBucket> buckets = obsClient.listBuckets(request);
        for (ObsBucket bucket : buckets) {
            System.out.println("BucketName:" + bucket.getBucketName());
            System.out.println("CreationDate:" + bucket.getCreationDate());
            System.out.println("Location:" + bucket.getLocation());
        }
        return buckets;
    }

    /**
     * 删除bucket
     *
     * @param bucketName
     */
    public static void deleteBucket(String bucketName) {
        obsClient.deleteBucket(bucketName);
    }

    /**
     * 判断bucket是否已存在
     *
     * @param bucketName
     * @return
     */
    public static boolean bucketExists(String bucketName) {
        return obsClient.headBucket(bucketName);
    }

    /**
     * 获取bucket元数据
     *
     * @param bucketName
     * @return
     */
    public static BucketMetadataInfoResult getBucketMetadata(String bucketName) {
        BucketMetadataInfoRequest request = new BucketMetadataInfoRequest(bucketName);
        // 获取桶元数据
        BucketMetadataInfoResult result = obsClient.getBucketMetadata(request);
        System.out.println("bucketType" + "\t:" + result.getBucketType());
        System.out.println("location" + "\t:" + result.getLocation());
        System.out.println("bucketStorageClass" + "\t:" + result.getBucketStorageClass());
        System.out.println("obsVersion" + "\t:" + result.getObsVersion());
        System.out.println("defaultStorageClass" + "\t:" + result.getDefaultStorageClass());
        System.out.println("allowOrigin" + "\t:" + result.getAllowOrigin());
        System.out.println("maxAge" + "\t:" + result.getMaxAge());
        System.out.println("allowHeaders" + "\t:" + result.getAllowHeaders());
        System.out.println("allowMethods" + "\t:" + result.getAllowMethods());
        System.out.println("exposeHeaders" + "\t:" + result.getExposeHeaders());
        return result;
    }

    /**
     * 判断对象是否存在
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public static boolean doesObjectExist(String bucketName, String objectName) {
        return obsClient.doesObjectExist(bucketName, objectName);
    }

    /**
     * 获取文件base64名称
     *
     * @param file
     * @return
     */
    public static String getFileBase64Name(MultipartFile file) {
        String fileName = "";
        BufferedInputStream bis = null;
        String base64Md5;
        try {
            bis = new BufferedInputStream(file.getInputStream());
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[16384];
            int bytesRead;
            while ((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
                messageDigest.update(buffer, 0, bytesRead);
            }
            base64Md5 = ServiceUtils.toBase64(messageDigest.digest());
            System.out.println("base64Md5计算值：" + base64Md5);
            String originalFilename = file.getOriginalFilename();
            String fileSuffix = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
            String s = base64Md5.replaceAll("/", "");
            fileName = s + fileSuffix;
        } catch (Exception e) {
            log.error("obs上传文件时生成MD5文件名称失败，异常信息：", e);
        } finally {
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }

    /**
     * 上传文件
     *
     * @param bucketName
     * @param file
     */
    public static PutObjectResult putObject(String bucketName, String objectName, MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String contentType = file.getContentType();
            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(objectName);
            request.setInput(inputStream);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            request.setMetadata(objectMetadata);
            PutObjectResult putObjectResult = obsClient.putObject(request);
            log.info("putObjectResult:{}", putObjectResult);
            return putObjectResult;
        } catch (YouyaException ye) {
            throw ye;
        } catch (Exception e) {
            log.error("obs上传文件失败，异常信息：", e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 对象流式下载
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public static byte[] getObject(String bucketName, String objectName) {
        ObsObject obsObject = obsClient.getObject(bucketName, objectName);
        // 读取对象内容
        InputStream input = null;
        ByteArrayOutputStream bos = null;
        int len;
        byte[] b = new byte[1024];
        try {
            input = obsObject.getObjectContent();
            bos = new ByteArrayOutputStream();
            while ((len = input.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            log.error("获取obs对象流失败，异常信息：", e);
        } finally {
            try {
                assert input != null;
                input.close();
                assert bos != null;
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取文件签名Url
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public static String getSignedUrl(String bucketName, String objectName) {
        TemporarySignatureRequest request = new TemporarySignatureRequest();
        request.setRequestDate(new Date());
        request.setExpires(7200);
        request.setBucketName(bucketName);
        request.setObjectKey(objectName);
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        return response.getSignedUrl();
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param objectName
     */
    public static DeleteObjectResult deleteObject(String bucketName, String objectName) {
        return obsClient.deleteObject(bucketName, objectName);
    }

    public static void main(String[] args) {
//        createBucket("youya-test");
//        System.out.println(listBuckets());
//        deleteBucket("youya-test");
//        System.out.println(bucketExists("youya-test"));
//        getBucketMetadata("youya-avatar");
        String signedUrl = getSignedUrl("youya-enterprise", "hQXgHJ2adYeA1DGTx4IuoQ==.jpg");
        String s = "hQXgHJ2adYeA1DGTx4IuoQ==.jpg";
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8);
        System.out.println(encode);
        System.out.println(signedUrl);
    }
}
