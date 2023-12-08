package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentCreateDto;
import com.korant.youya.workplace.service.AttachmentService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 其他附件表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Resource
    private AttachmentService attachmentService;

    /**
     * 创建其他附件信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid AttachmentCreateDto createDto) {
        attachmentService.create(createDto);
        return R.ok();
    }

    /**
     * 删除其他附件信息
     *
     * @param
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        attachmentService.delete(id);
        return R.ok();
    }
}
