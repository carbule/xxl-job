package com.korant.youya.workplace.pojo.dto.honorcertificate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 17:08
 * @PackageName:com.korant.youya.workplace.pojo.dto.honorcertificate
 * @ClassName: HonorCertificateModifyDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class HonorCertificateModifyDto {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 证书名称
     */
    @NotBlank(message = "证书名称不能为空")
    private String certificateName;

    /**
     * 获得时间
     */
    @NotBlank(message = "获得时间不能为空")
    private LocalDate obtainTime;

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