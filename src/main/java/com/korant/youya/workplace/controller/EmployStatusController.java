package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.service.EmployStatusService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
