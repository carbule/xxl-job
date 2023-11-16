package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.jobintention.JobIntentionModifyDto;
import com.korant.youya.workplace.pojo.vo.jobIntention.JobIntentionVo;
import com.korant.youya.workplace.service.JobIntentionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 求职意向 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-15
 */
@RestController
@RequestMapping("/jobIntention")
public class JobIntentionController {

    @Resource
    private JobIntentionService jobIntentionService;

    /**
     * 查询求职状态
     *
     * @return
     */
    @GetMapping("/status")
    public R<?> status() {
        JobIntentionVo jobIntentionVo = jobIntentionService.status();
        return R.success(jobIntentionVo);
    }

    /**
     * 修改求职状态
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid JobIntentionModifyDto jobIntentionModifyDto) {
        jobIntentionService.modify(jobIntentionModifyDto);
        return R.ok();
    }

}
