package com.korant.youya.workplace.pojo.vo.attachment;

import lombok.Data;

/**
 * @Date 2023/11/16 17:26
 * @ClassName: AttachmentListVo
 * @Description:
 * @Version 1.0
 */
@Data
public class AttachmentListVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * md5文件名称
     */
    private String md5FileName;

}
