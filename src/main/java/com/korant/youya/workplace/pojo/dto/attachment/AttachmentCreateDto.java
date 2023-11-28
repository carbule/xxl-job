package com.korant.youya.workplace.pojo.dto.attachment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Date 2023/11/16 17:34
 * @ClassName: AttachmentCreateDto
 * @Description:
 * @Version 1.0
 */
@Data
public class AttachmentCreateDto {

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
