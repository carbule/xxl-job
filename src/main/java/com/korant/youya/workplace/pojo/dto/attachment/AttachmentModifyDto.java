package com.korant.youya.workplace.pojo.dto.attachment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 17:34
 * @PackageName:com.korant.youya.workplace.pojo.dto.attachment
 * @ClassName: AttachmentModifyDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class AttachmentModifyDto {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空")
    private Long id;

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
