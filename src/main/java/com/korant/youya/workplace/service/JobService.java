package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.job.JobCreateDto;
import com.korant.youya.workplace.pojo.dto.job.JobModifyDto;
import com.korant.youya.workplace.pojo.dto.job.JobQueryHomePageListDto;
import com.korant.youya.workplace.pojo.dto.job.JobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.Job;
import com.korant.youya.workplace.pojo.vo.job.*;
import jakarta.servlet.http.HttpServletRequest;

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
     * 查询首页职位信息列表
     *
     * @param listDto
     * @param request
     * @return
     */
    Page<JobHomePageListVo> queryHomePageList(JobQueryHomePageListDto listDto, HttpServletRequest request);

    /**
     * 根据求职id查询首页职位信息详情
     *
     * @param id
     * @return
     */
    JobHomePageDetailVo queryHomePageDetailById(Long id);

    /**
     * 根据职位信息中的企业id查询企业信息详情
     *
     * @param id
     * @return
     */
    EnterDetailVo queryEnterpriseDetailById(Long id);

    /**
     * 收藏或取消收藏职位信息
     *
     * @param id
     */
    void collect(Long id);

    /**
     * 查询用户个人职位列表
     *
     * @param personalListDto
     * @return
     */
    Page<JobPersonalVo> queryPersonalList(JobQueryPersonalListDto personalListDto);

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
     * 根据id发布职位
     *
     * @param id
     */
    void publish(Long id);

    /**
     * 根据id关闭职位
     *
     * @param id
     */
    void close(Long id);

    /**
     * 根据id删除职位
     *
     * @param id
     */
    void delete(Long id);
}
