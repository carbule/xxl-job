package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.vo.interview.InterviewVo;
import com.korant.youya.workplace.service.InterviewService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 面试记录表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@RestController
@RequestMapping("/interview")
public class InterviewController {

    @Resource
    private InterviewService interviewService;

    /**
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    public R<?> queryList(@RequestBody @Valid InterviewQueryListDto listDto) {
        Page<InterviewVo> page = interviewService.queryList(listDto);
        return R.success(page);
    }
}
