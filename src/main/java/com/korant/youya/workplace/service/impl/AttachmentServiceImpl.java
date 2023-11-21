package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.AttachmentMapper;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentCreateDto;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentModifyDto;
import com.korant.youya.workplace.pojo.po.Attachment;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentDetailVo;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentListVo;
import com.korant.youya.workplace.service.AttachmentService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * 查询其他附件信息列表
     *
     * @param
     * @return
     */
    @Override
    public List<AttachmentListVo> queryList() {

        Long userId = SpringSecurityUtil.getUserId();
        return attachmentMapper.queryList(userId);

    }

    /**
     * 创建其他附件信息
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AttachmentCreateDto attachmentCreateDto) {

        Long userId = SpringSecurityUtil.getUserId();
        Attachment attachment = new Attachment();
        BeanUtils.copyProperties(attachmentCreateDto, attachment);
        attachment.setUid(userId);
        attachmentMapper.insert(attachment);

    }

    /**
     * 修改其他附件信息
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(AttachmentModifyDto attachmentModifyDto) {

        Attachment attachment = attachmentMapper.selectById(attachmentModifyDto.getId());
        if (attachment == null) throw new YouyaException("附件信息不存在！");
        BeanUtils.copyProperties(attachmentModifyDto, attachment);
        attachmentMapper.updateById(attachment);

    }

    /**
     * 查询其他附件信息详情
     *
     * @param id
     * @return
     */
    @Override
    public AttachmentDetailVo detail(Long id) {

        return attachmentMapper.detail(id);

    }

    /**
     * 删除其他附件信息
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        Attachment attachment = attachmentMapper.selectById(id);
        if (attachment == null) throw new YouyaException("附件信息不存在！");
        attachment.setIsDelete(1);
        attachmentMapper.updateById(attachment);

    }
}
