package com.korant.youya.workplace.pojo.dto.honorcertificate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.YearMonth;

/**
 * @ClassName HonorCertificateCreateDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/8 10:34
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
    @NotNull(message = "获得时间不能为空")
    private YearMonth obtainTime;

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
