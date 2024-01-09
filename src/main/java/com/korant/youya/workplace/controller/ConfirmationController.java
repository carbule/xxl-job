package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.vo.confirmation.ConfirmationVo;
import com.korant.youya.workplace.service.ConfirmationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 转正记录表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@RestController
@RequestMapping("/confirmation")
public class ConfirmationController {

    @Resource
    private ConfirmationService confirmationService;

    /**
     * 查询转正邀请列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    public R<?> queryList(@RequestBody @Valid ConfirmationQueryListDto listDto) {
        Page<ConfirmationVo> page = confirmationService.queryList(listDto);
        return R.success(page);
    }
}
