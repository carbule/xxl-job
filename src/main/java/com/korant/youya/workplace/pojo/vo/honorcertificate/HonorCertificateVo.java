package com.korant.youya.workplace.pojo.vo.honorcertificate;

import lombok.Data;

/**
 * @ClassName HonorCertificateVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/8 10:35
 * @Version 1.0
 */
@Data
public class HonorCertificateVo {

    /**
     * 主键
     */
    private Long hcId;

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
    private String honorCertificateFileName;

    /**
     * md5文件名称
     */
    private String honorCertificateMd5FileName;
}
