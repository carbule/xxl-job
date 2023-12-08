package com.korant.youya.workplace.pojo.vo.honorcertificate;

import lombok.Data;

/**
 * @ClassName HonorCertificateDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/8 10:35
 * @Version 1.0
 */
@Data
public class HonorCertificateDetailVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 证书名称
     */
    private String certificateName;

    /**
     * 获得时间
     */
    private String obtainTime;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * md5文件名称
     */
    private String md5FileName;
}
