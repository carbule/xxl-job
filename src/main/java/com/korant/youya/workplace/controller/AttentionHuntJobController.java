package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.attentionhuntjob.AttentionHuntJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.vo.attentionhuntjob.AttentionHuntJobPersonalVo;
import com.korant.youya.workplace.service.AttentionHuntJobService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 关注求职表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/attentionHuntJob")
public class AttentionHuntJobController {

    @Resource
    private AttentionHuntJobService attentionHuntJobService;

    /**
     * 查询用户求职关注列表
     *
     * @param personalListDto
     * @return
     */
    @PostMapping("/queryPersonalList")
    public R<?> queryPersonalList(@RequestBody @Valid AttentionHuntJobQueryPersonalListDto personalListDto) {
        Page<AttentionHuntJobPersonalVo> page = attentionHuntJobService.queryPersonalList(personalListDto);
        return R.success(page);
    }

    /**
     * 取消求职关注
     *
     * @param id
     * @return
     */
    @GetMapping("/cancel/{id}")
    public R<?> cancel(@PathVariable("id") Long id) {
        attentionHuntJobService.cancel(id);
        return R.ok();
    }
}
