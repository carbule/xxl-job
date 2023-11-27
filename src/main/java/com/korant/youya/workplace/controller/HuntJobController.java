package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobCreateDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobModifyDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryListDto;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobDetailOnHomePageVo;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobDetailVo;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobListOnHomePageVo;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobPreviewVo;
import com.korant.youya.workplace.service.HuntJobService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 求职表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/huntJob")
public class HuntJobController {

    @Resource
    private HuntJobService huntJobService;

    /**
     * 查询首页求职信息列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryListOnHomePage")
    public R<?> queryListOnHomePage(@RequestBody @Valid HuntJobQueryListDto listDto) {
        Page<HuntJobListOnHomePageVo> page = huntJobService.queryListOnHomePage(listDto);
        return R.success(page);
    }

    /**
     * 根据求职id查询首页求职信息详情
     *
     * @param id
     * @return
     */
    @GetMapping("/queryDetailOnHomePageById/{id}")
    public R<?> queryDetailOnHomePageById(@PathVariable("id") Long id) {
        HuntJobDetailOnHomePageVo detailOnHomePageVo = huntJobService.queryDetailOnHomePageById(id);
        return R.success(detailOnHomePageVo);
    }

    /**
     * 收藏或取消收藏求职信息
     *
     * @param id
     * @return
     */
    @GetMapping("/collect/{id}")
    public R<?> collect(@PathVariable("id") Long id) {
        huntJobService.collect(id);
        return R.ok();
    }

    /**
     * 校验用户信息
     *
     * @return
     */
    @GetMapping("/checkUserInfo")
    public R<?> checkUserInfo() {
        huntJobService.checkUserInfo();
        return R.ok();
    }

    /**
     * 求职预览
     *
     * @return
     */
    @GetMapping("/preview")
    @ExplanationDict
    public R<?> preview() {
        HuntJobPreviewVo previewVo = huntJobService.preview();
        return R.success(previewVo);
    }

    /**
     * 创建求职信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid HuntJobCreateDto createDto) {
        huntJobService.create(createDto);
        return R.ok();
    }

    /**
     * 修改求职信息
     *
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid HuntJobModifyDto modifyDto) {
        huntJobService.modify(modifyDto);
        return R.ok();
    }

    /**
     * 查询求职信息详情
     *
     * @param
     * @return
     */
    @GetMapping("/detail/{id}")
    public R<?> detail(@PathVariable("id") Long id) {
        HuntJobDetailVo detailVo = huntJobService.detail(id);
        return R.success(detailVo);
    }

    /**
     * 删除求职信息
     *
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        huntJobService.delete(id);
        return R.ok();
    }

}
