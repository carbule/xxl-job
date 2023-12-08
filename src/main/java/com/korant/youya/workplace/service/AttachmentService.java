package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentCreateDto;
import com.korant.youya.workplace.pojo.po.Attachment;

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
     * 创建其他附件信息
     *
     * @param createDto
     */
    void create(AttachmentCreateDto createDto);

    /**
     * 删除其他附件信息
     *
     * @param id
     */
    void delete(Long id);
}
