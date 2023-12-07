package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.attentionjob.AttentionJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.vo.attentionjob.AttentionJobPersonalVo;
import com.korant.youya.workplace.service.AttentionJobService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 关注职位表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/attentionJob")
public class AttentionJobController {

    @Resource
    private AttentionJobService attentionJobService;

    /**
     * 查询用户职位关注列表
     *
     * @param personalListDto
     * @return
     */
    @PostMapping("/queryPersonalList")
    @ExplanationDict
    public R<?> queryPersonalList(@RequestBody @Valid AttentionJobQueryPersonalListDto personalListDto) {
        Page<AttentionJobPersonalVo> page = attentionJobService.queryPersonalList(personalListDto);
        return R.success(page);
    }

    /**
     * 取消职位关注
     *
     * @param id
     * @return
     */
    @GetMapping("/cancel/{id}")
    public R<?> cancel(@PathVariable("id") Long id) {
        attentionJobService.cancel(id);
        return R.ok();
    }
}
