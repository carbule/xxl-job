package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.internalrecommend.InternalRecommendQueryListDto;
import com.korant.youya.workplace.pojo.vo.internalrecommend.InternalRecommendVo;
import com.korant.youya.workplace.service.InternalRecommendService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 内部推荐表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@RestController
@RequestMapping("/internalRecommend")
public class InternalRecommendController {

    @Resource
    private InternalRecommendService internalRecommendService;

    /**
     * 查询用户被推荐职位列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    public R<?> queryList(@RequestBody @Valid InternalRecommendQueryListDto listDto) {
        Page<InternalRecommendVo> page = internalRecommendService.queryList(listDto);
        return R.success(page);
    }
}
