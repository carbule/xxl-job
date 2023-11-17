package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentCreateDto;
import com.korant.youya.workplace.pojo.dto.attachment.AttachmentModifyDto;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentDetailVo;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentListVo;
import com.korant.youya.workplace.service.AttachmentService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 查询其他附件信息列表
     *
     * @param
     * @return
     */
    @PostMapping("/queryList")
    public R<?> queryList() {
        List<AttachmentListVo> attachmentListVo = attachmentService.queryList();
        return R.success(attachmentListVo);
    }

    /**
     * 创建其他附件信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid AttachmentCreateDto attachmentCreateDto) {
        attachmentService.create(attachmentCreateDto);
        return R.ok();
    }

    /**
     * 修改其他附件信息
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid AttachmentModifyDto attachmentModifyDto) {
        attachmentService.modify(attachmentModifyDto);
        return R.ok();
    }

    /**
     * 查询其他附件信息详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public R<?> detail(@PathVariable("id") Long id) {
        AttachmentDetailVo attachmentDetailVo = attachmentService.detail(id);
        return R.success(attachmentDetailVo);
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
