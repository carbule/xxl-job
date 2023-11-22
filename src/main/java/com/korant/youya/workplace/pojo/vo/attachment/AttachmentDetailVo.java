package com.korant.youya.workplace.pojo.vo.attachment;

import lombok.Data;

/**
 * @Date 2023/11/16 17:25
 * @PackageName:com.korant.youya.workplace.pojo.vo.attachment
 * @ClassName: AttachmentDetailVo
 * @Description:
 * @Version 1.0
 */
@Data
public class AttachmentDetailVo {

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
