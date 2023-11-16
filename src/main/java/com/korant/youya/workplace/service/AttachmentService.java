package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentCreateDto;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentModifyDto;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentQueryListDto;
import com.korant.youya.workplace.pojo.po.Attachment;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentDetailVo;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentListVo;

/**
 * <p>
 * 其他附件表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface AttachmentService extends IService<Attachment> {

    Page<AttachmentListVo> queryList(AttachmentQueryListDto listDto);

    void create(AttachmentCreateDto attachmentCreateDto);

    void modify(AttachmentModifyDto attachmentModifyDto);

    AttachmentDetailVo detail(Long id);

    void delete(Long id);

}
