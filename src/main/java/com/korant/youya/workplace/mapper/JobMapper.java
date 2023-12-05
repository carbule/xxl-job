package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.job.JobModifyDto;
import com.korant.youya.workplace.pojo.dto.job.JobQueryHomePageListDto;
import com.korant.youya.workplace.pojo.po.Job;
import com.korant.youya.workplace.pojo.vo.job.JobHomePageDetailVo;
import com.korant.youya.workplace.pojo.vo.job.JobHomePageListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface JobMapper extends BaseMapper<Job> {

    /**
     * 查询首页职位数量
     *
     * @param userId
     * @param listDto
     * @return
     */
    int queryHomePageListCount(@Param("userId") Long userId, @Param("listDto") JobQueryHomePageListDto listDto);

    /**
     * 查询首页职位列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    List<JobHomePageListVo> queryHomePageList(@Param("userId") Long userId, @Param("listDto") JobQueryHomePageListDto listDto);

    /**
     * 根据求职id查询首页职位信息详情
     *
     * @param userId
     * @param id
     * @return
     */
    JobHomePageDetailVo queryHomePageDetailById(@Param("userId") Long userId, @Param("id") Long id);

    /**
     * 修改职位信息
     *
     * @param modifyDto
     * @return
     */
    int modify(@Param("modifyDto") JobModifyDto modifyDto);
}
