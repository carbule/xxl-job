package com.korant.youya.workplace.pojo.vo.attachment;

import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 17:26
 * @PackageName:com.korant.youya.workplace.pojo.vo.attachment
 * @ClassName: AttachmentListVo
 * @Description: TODO
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
