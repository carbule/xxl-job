package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.pojo.vo.employstatus.EmployStatusVo;
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
    public R<?> status() {
        EmployStatusVo employStatusVo = employStatusService.status();
        return R.success(employStatusService);
    }

    /**
     * 修改求职状态
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid EmployStatusModifyDto employStatusModifyDto) {
        employStatusService.modify(employStatusModifyDto);
        return R.ok();
    }

}
