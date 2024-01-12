package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.job.*;
import com.korant.youya.workplace.pojo.vo.job.*;
import com.korant.youya.workplace.service.JobService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Resource
    private JobService jobService;

    /**
     * 查询首页职位信息列表
     *
     * @param listDto
     * @param request
     * @return
     */
    @PostMapping("/queryHomePageList")
    @ExplanationDict
    public R<?> queryHomePageList(@RequestBody @Valid JobQueryHomePageListDto listDto, HttpServletRequest request) {
        Page<JobHomePageListVo> page = jobService.queryHomePageList(listDto, request);
        return R.success(page);
    }

    /**
     * 根据求职id查询首页职位信息详情
     *
     * @param id
     * @return
     */
    @GetMapping("/queryHomePageDetailById/{id}")
    @ExplanationDict
    public R<?> queryHomePageDetailById(@PathVariable("id") Long id) {
        JobHomePageDetailVo homePageDetailVo = jobService.queryHomePageDetailById(id);
        return R.success(homePageDetailVo);
    }

    /**
     * 根据职位信息中的企业id查询企业信息详情
     *
     * @param id
     * @return
     */
    @GetMapping("/queryEnterpriseDetailById/{id}")
    @ExplanationDict
    public R<?> queryEnterpriseDetailById(@PathVariable("id") Long id) {
        EnterDetailVo enterpriseDetailVo = jobService.queryEnterpriseDetailById(id);
        return R.success(enterpriseDetailVo);
    }

    /**
     * 根据职位id发起职位申请
     *
     * @param applyDto
     * @return
     */
    @PostMapping("/apply")
    public R<?> apply(@RequestBody @Valid JobApplyDto applyDto) {
        jobService.apply(applyDto);
        return R.ok();
    }

    /**
     * 收藏或取消收藏职位信息
     *
     * @param id
     * @return
     */
    @GetMapping("/collect/{id}")
    public R<?> collect(@PathVariable("id") Long id) {
        jobService.collect(id);
        return R.ok();
    }

    /**
     * 根据id查询分享信息
     *
     * @param id
     * @return
     */
    @GetMapping("/queryShareInfo/{id}")
    public R<?> queryShareInfo(@PathVariable("id") Long id) {
        JobShareInfo shareInfo = jobService.queryShareInfo(id);
        return R.success(shareInfo);
    }

    /**
     * 查询用户个人职位列表
     *
     * @param personalListDto
     * @return
     */
    @PostMapping("/queryPersonalList")
    @ExplanationDict
    public R<?> queryPersonalList(@RequestBody @Valid JobQueryPersonalListDto personalListDto) {
        Page<JobPersonalVo> page = jobService.queryPersonalList(personalListDto);
        return R.success(page);
    }

    /**
     * 创建职位信息
     *
     * @return
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('hr','admin')")
    public R<?> create(@RequestBody @Valid JobCreateDto createDto) {
        jobService.create(createDto);
        return R.ok();
    }

    /**
     * 修改职位信息
     *
     * @return
     */
    @PostMapping("/modify")
    @PreAuthorize("hasAnyRole('hr','admin')")
    public R<?> modify(@RequestBody @Valid JobModifyDto modifyDto) {
        jobService.modify(modifyDto);
        return R.ok();
    }

    /**
     * 根据id查询职位信息详情
     *
     * @return
     */
    @GetMapping("/detail/{id}")
    @ExplanationDict
    public R<?> detail(@PathVariable("id") Long id) {
        JobDetailVo jobDetailVo = jobService.detail(id);
        return R.success(jobDetailVo);
    }

    /**
     * 根据id发布职位
     *
     * @return
     */
    @GetMapping("/publish/{id}")
    public R<?> publish(@PathVariable("id") Long id) {
        jobService.publish(id);
        return R.ok();
    }

    /**
     * 根据id关闭职位
     *
     * @return
     */
    @GetMapping("/close/{id}")
    public R<?> close(@PathVariable("id") Long id) {
        jobService.close(id);
        return R.ok();
    }

    /**
     * 根据id删除职位
     *
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        jobService.delete(id);
        return R.ok();
    }
}
