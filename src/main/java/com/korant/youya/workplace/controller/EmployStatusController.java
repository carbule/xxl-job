package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.pojo.vo.employstatus.EmployStatusVo;
import com.korant.youya.workplace.pojo.vo.employstatus.ResumePreviewVo;
import com.korant.youya.workplace.service.EmployStatusService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 求职状态表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
@RestController
@RequestMapping("/employStatus")
public class EmployStatusController {

    @Resource
    private EmployStatusService employStatusService;

    /**
     * 查询求职状态
     *
     * @return
     */
    @GetMapping("/status")
    @ExplanationDict
    public R<?> status() {
        EmployStatusVo employStatusVo = employStatusService.status();
        return R.success(employStatusVo);
    }

    /**
     * @Description 个人简历 预览页面
     * @Param
     * @return
     **/
    @GetMapping("/preview")
    public R<?> preview() {

        ResumePreviewVo resumePreviewVo = employStatusService.preview();
        return R.success(resumePreviewVo);

    }

    /**
     * 创建求职意向
     *
     * @param
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid EmployStatusModifyDto employStatusModifyDto) {
        employStatusService.create(employStatusModifyDto);
        return R.ok();
    }

}
