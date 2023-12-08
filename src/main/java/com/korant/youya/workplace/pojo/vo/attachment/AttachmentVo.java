package com.korant.youya.workplace.pojo.vo.attachment;

import lombok.Data;

/**
 * @ClassName AttachmentVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/8 16:27
 * @Version 1.0
 */
@Data
public class AttachmentVo {

    /**
     * 主键
     */
    private Long aId;

    /**
     * 文件名称
     */
    private String attachmentFileName;

    /**
     * md5文件名称
     */
    private String attachmentMd5FileName;
}
