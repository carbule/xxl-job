package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.job.JobCreateDto;
import com.korant.youya.workplace.pojo.dto.job.JobModifyDto;
import com.korant.youya.workplace.pojo.vo.job.JobDetailVo;
import com.korant.youya.workplace.service.JobService;
import jakarta.annotation.Resource;
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
    @PostMapping("/detail/{id}")
    public R<?> detail(@PathVariable("id") Long id) {
        JobDetailVo jobDetailVo = jobService.detail(id);
        return R.success(jobDetailVo);
    }

    /**
     * 根据id删除职位
     *
     * @return
     */
    @PostMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        jobService.delete(id);
        return R.ok();
    }
}
