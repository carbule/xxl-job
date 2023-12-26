package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.userhistoricallocation.UserHistoricalLocationCreateDto;
import com.korant.youya.workplace.pojo.vo.userhistoricallocation.UserHistoricalLocationVo;
import com.korant.youya.workplace.service.UserHistoricalLocationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户历史定位信息 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-26
 */
@RestController
@RequestMapping("/userHistoricalLocation")
public class UserHistoricalLocationController {

    @Resource
    private UserHistoricalLocationService userHistoricalLocationService;

    /**
     * 创建用户历史定位信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid UserHistoricalLocationCreateDto createDto) {
        userHistoricalLocationService.create(createDto);
        return R.ok();
    }

    /**
     * 查询用户最新定位
     *
     * @return
     */
    @GetMapping("/queryLatestLocation")
    public R<?> queryLatestLocation() {
        UserHistoricalLocationVo historicalLocationVo = userHistoricalLocationService.queryLatestLocation();
        return R.success(historicalLocationVo);
    }
}
