package com.korant.youya.workplace.pojo.dto.attachment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 17:34
 * @PackageName:com.korant.youya.workplace.pojo.dto.attachment
 * @ClassName: AttachmentCreateDto
 * @Description: TODO
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
