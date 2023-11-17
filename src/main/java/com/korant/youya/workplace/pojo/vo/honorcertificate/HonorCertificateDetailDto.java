package com.korant.youya.workplace.pojo.vo.honorcertificate;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 17:07
 * @PackageName:com.korant.youya.workplace.pojo.vo.honorcertificate
 * @ClassName: HonorCertificateDetailDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class HonorCertificateDetailDto {

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
    private LocalDate obtainTime;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * md5文件名称
     */
    private String md5FileName;

}
