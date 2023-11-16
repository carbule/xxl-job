package com.korant.youya.workplace.pojo.vo.attachment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 17:25
 * @PackageName:com.korant.youya.workplace.pojo.vo.attachment
 * @ClassName: AttachmentDetailVo
 * @Description: TODO
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
