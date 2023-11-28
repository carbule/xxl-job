package com.korant.youya.workplace.pojo.dto.honorcertificate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Date 2023/11/16 17:08
 * @ClassName: HonorCertificateCreateDto
 * @Description:
 * @Version 1.0
 */
@Data
public class HonorCertificateCreateDto {

    /**
     * 证书名称
     */
    @NotBlank(message = "证书名称不能为空")
    private String certificateName;

    /**
     * 获得时间
     */
    @NotBlank(message = "获得时间不能为空")
    private String obtainTime;

    /**
     * 文件名称
     */
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    /**
     * md5文件名称
     */
    @NotBlank(message = "md5文件名称不能为空")
    private String md5FileName;

}
