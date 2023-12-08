package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.AttachmentMapper;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentCreateDto;
import com.korant.youya.workplace.pojo.po.Attachment;
import com.korant.youya.workplace.service.AttachmentService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 其他附件表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements AttachmentService {

    @Resource
    private AttachmentMapper attachmentMapper;

    /**
     * 创建其他附件信息
     *
     * @param createDto
     */
    @Override
    public void create(AttachmentCreateDto createDto) {
        Long userId = SpringSecurityUtil.getUserId();
        Long count = attachmentMapper.selectCount(new LambdaQueryWrapper<Attachment>().eq(Attachment::getUid, userId).eq(Attachment::getIsDelete, 0));
        if (count >= 3L) throw new YouyaException("附件总数不得超过3个");
        Attachment attachment = new Attachment();
        BeanUtils.copyProperties(createDto, attachment);
        attachment.setUid(userId);
        attachmentMapper.insert(attachment);
    }

    /**
     * 删除其他附件信息
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Attachment attachment = attachmentMapper.selectOne(new LambdaQueryWrapper<Attachment>().eq(Attachment::getId, id).eq(Attachment::getIsDelete, 0));
        if (null == attachment) throw new YouyaException("附件不存在");
        attachment.setIsDelete(1);
        attachmentMapper.updateById(attachment);
    }
}
