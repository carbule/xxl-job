package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.job.JobCreateDto;
import com.korant.youya.workplace.pojo.dto.job.JobModifyDto;
import com.korant.youya.workplace.pojo.po.Job;
import com.korant.youya.workplace.pojo.vo.job.JobDetailVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface JobService extends IService<Job> {

    /**
     * 创建职位信息
     *
     * @param createDto
     */
    void create(JobCreateDto createDto);

    /**
     * 修改职位信息
     *
     * @param modifyDto
     */
    void modify(JobModifyDto modifyDto);

    /**
     * 根据id查询职位信息详情
     *
     * @param id
     * @return
     */
    JobDetailVo detail(Long id);

    /**
     * 根据id删除职位
     *
     * @param id
     */
    void delete(Long id);
}
