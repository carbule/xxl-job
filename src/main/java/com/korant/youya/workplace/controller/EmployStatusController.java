package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.pojo.vo.employstatus.PersonalHuntJobIntentionVo;
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
     * 查询个人求职意向
     *
     * @return
     */
    @GetMapping("/queryPersonalHuntJobIntention")
    @ExplanationDict
    public R<?> queryPersonalHuntJobIntention() {
        PersonalHuntJobIntentionVo personalHuntJobIntentionVo = employStatusService.queryPersonalHuntJobIntention();
        return R.success(personalHuntJobIntentionVo);
    }

    /**
     * 修改求职意向
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid EmployStatusModifyDto modifyDto) {
        employStatusService.modify(modifyDto);
        return R.ok();
    }
}
