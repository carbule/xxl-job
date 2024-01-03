package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.internalrecommend.InternalRecommendQueryListDto;
import com.korant.youya.workplace.pojo.vo.internalrecommend.InternalRecommendDetailVo;
import com.korant.youya.workplace.pojo.vo.internalrecommend.InternalRecommendVo;
import com.korant.youya.workplace.service.InternalRecommendService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 查询用户被推荐职位详情
     *
     * @param id
     * @return
     */
    @PostMapping("/detail/{id}")
    public R<?> detail(@PathVariable("id") Long id) {
        InternalRecommendDetailVo detailVo = internalRecommendService.detail(id);
        return R.success(detailVo);
    }
}
