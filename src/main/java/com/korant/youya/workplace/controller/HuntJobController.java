package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobCreateDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobModifyDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryListDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.vo.huntjob.*;
import com.korant.youya.workplace.service.HuntJobService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ExplanationDict
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
     * 查询用户个人求职列表
     *
     * @param personalListDto
     * @return
     */
    @PostMapping("/queryPersonalList")
    public R<?> queryPersonalList(@RequestBody @Valid HuntJobQueryPersonalListDto personalListDto) {
        Page<HuntJobPersonalListVo> page = huntJobService.queryPersonalList(personalListDto);
        return R.success(page);
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
     * 查询个人意向职位列表
     *
     * @return
     */
    @GetMapping("/queryPersonalExpectedPositionList")
    public R<?> queryPersonalExpectedPositionList() {
        List<PersonalExpectedPositionListVo> positionList = huntJobService.queryPersonalExpectedPositionList();
        return R.success(positionList);
    }

    /**
     * 查询个人意向区域列表
     *
     * @return
     */
    @GetMapping("/queryPersonalExpectedWorkAreaList")
    public R<?> queryPersonalExpectedWorkAreaList() {
        List<PersonalExpectedWorkAreaListVo> workAreaList = huntJobService.queryPersonalExpectedWorkAreaList();
        return R.success(workAreaList);
    }

    /**
     * 求职发布预览
     *
     * @return
     */
    @GetMapping("/publishPreview")
    @ExplanationDict
    public R<?> publishPreview() {
        HuntJobPublishPreviewVo publishPreviewVo = huntJobService.publishPreview();
        return R.success(publishPreviewVo);
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
     * 根据id预览求职详细信息
     *
     * @param
     * @return
     */
    @GetMapping("/detailsPreview/{id}")
    public R<?> detailsPreview(@PathVariable("id") Long id) {
        HuntJobDetailsPreviewVo detailsPreviewVo = huntJobService.detailsPreview(id);
        return R.success(detailsPreviewVo);
    }

    /**
     * 根据id查询求职信息详情
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
     * 根据id关闭职位
     *
     * @param
     * @return
     */
    @GetMapping("/close/{id}")
    public R<?> close(@PathVariable("id") Long id) {
        huntJobService.close(id);
        return R.ok();
    }

    /**
     * 根据id发布职位
     *
     * @param
     * @return
     */
    @GetMapping("/release/{id}")
    public R<?> release(@PathVariable("id") Long id) {
        huntJobService.release(id);
        return R.ok();
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
