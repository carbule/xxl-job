package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.AttachmentMapper;
import com.korant.youya.workplace.pojo.po.Attachment;
import com.korant.youya.workplace.service.AttachmentService;
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
}
