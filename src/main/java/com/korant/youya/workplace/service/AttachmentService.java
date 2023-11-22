package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentCreateDto;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentModifyDto;
import com.korant.youya.workplace.pojo.po.Attachment;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentDetailVo;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentListVo;

import java.util.List;

/**
 * <p>
 * 其他附件表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface AttachmentService extends IService<Attachment> {

    /**
     * 查询其他附件信息列表
     *
     * @param
     * @return
     */
    List<AttachmentListVo> queryList();

    /**
     * 创建其他附件信息
     *
     * @return
     */
    void create(AttachmentCreateDto attachmentCreateDto);

    /**
     * 修改其他附件信息
     *
     * @return
     */
    void modify(AttachmentModifyDto attachmentModifyDto);

    /**
     * 查询其他附件信息详情
     *
     * @return
     */
    AttachmentDetailVo detail(Long id);

    /**
     * 删除其他附件信息
     *
     */
    void delete(Long id);

}
