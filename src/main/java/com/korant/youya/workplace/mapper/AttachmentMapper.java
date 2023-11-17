package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.po.Attachment;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentDetailVo;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 其他附件表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface AttachmentMapper extends BaseMapper<Attachment> {

    /**
     * 查询其他附件信息列表
     *
     * @param
     * @return
     */
    List<AttachmentListVo> queryList(@Param("userId") Long userId);

    /**
     * 查询其他附件信息详情
     *
     * @param
     * @return
     */
    AttachmentDetailVo detail(@Param("id") Long id);

}
