package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.job.JobModifyDto;
import com.korant.youya.workplace.pojo.dto.job.JobQueryHomePageListDto;
import com.korant.youya.workplace.pojo.dto.job.JobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.Job;
import com.korant.youya.workplace.pojo.vo.job.*;
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
     * @param listDto
     * @return
     */
    int queryHomePageListCount(@Param("listDto") JobQueryHomePageListDto listDto);

    /**
     * 查询首页职位列表
     *
     * @param listDto
     * @return
     */
    List<JobHomePageListVo> queryHomePageList(@Param("listDto") JobQueryHomePageListDto listDto);

    /**
     * 查询首页职位数量
     *
     * @param userId
     * @param listDto
     * @return
     */
    int queryHomePageListCountByUserId(@Param("userId") Long userId, @Param("listDto") JobQueryHomePageListDto listDto);

    /**
     * 查询首页职位列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    List<JobHomePageListVo> queryHomePageListByUserId(@Param("userId") Long userId, @Param("listDto") JobQueryHomePageListDto listDto);

    /**
     * 根据求职id查询首页职位信息详情
     *
     * @param userId
     * @param id
     * @return
     */
    JobHomePageDetailVo queryHomePageDetailById(@Param("userId") Long userId, @Param("id") Long id);

    /**
     * 根据职位信息中的企业id查询企业信息详情
     *
     * @param id
     * @return
     */
    EnterDetailVo queryEnterpriseDetailById(@Param("id") Long id);

    /**
     * 查询用户个人职位列表
     *
     * @param userId
     * @param enterpriseId
     * @param personalListDto
     * @return
     */
    List<JobPersonalVo> queryPersonalList(@Param("userId") Long userId, @Param("enterpriseId") Long enterpriseId, @Param("personalListDto") JobQueryPersonalListDto personalListDto);

    /**
     * 修改职位信息
     *
     * @param modifyDto
     * @return
     */
    int modify(@Param("modifyDto") JobModifyDto modifyDto);

    /**
     * 根据id查询职位信息详情
     *
     * @param id
     * @return
     */
    JobDetailVo detail(@Param("id") Long id);

    /**
     * 强制移交职位信息
     *
     * @param id
     * @param userId
     */
    int compulsoryTransferJob(@Param("id") Long id, @Param("userId") Long userId);
}
