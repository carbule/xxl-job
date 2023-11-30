package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobModifyDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryListDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.pojo.vo.huntjob.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 求职表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface HuntJobMapper extends BaseMapper<HuntJob> {

    /**
     * 查询首页求职数量
     *
     * @param userId
     * @param enterpriseId
     * @param listDto
     * @return
     */
    int queryHomePageListCount(@Param("userId") Long userId, @Param("enterpriseId") Long enterpriseId, @Param("listDto") HuntJobQueryListDto listDto);

    /**
     * 查询首页求职列表
     *
     * @param userId
     * @param enterpriseId
     * @param listDto
     * @return
     */
    List<HuntJobListOnHomePageVo> queryListOnHomePage(@Param("userId") Long userId, @Param("enterpriseId") Long enterpriseId, @Param("listDto") HuntJobQueryListDto listDto);

    /**
     * 根据求职id查询首页求职信息详情
     *
     * @param userId
     * @param id
     * @return
     */
    HuntJobDetailOnHomePageVo queryDetailOnHomePageById(@Param("userId") Long userId, @Param("id") Long id);

    /**
     * 查询用户个人求职列表
     *
     * @param userId
     * @param status
     * @param personalListDto
     * @return
     */
    List<HuntJobPersonalListVo> queryPersonalList(@Param("userId") Long userId, @Param("status") Integer status, @Param("personalListDto") HuntJobQueryPersonalListDto personalListDto);

    /**
     * 查询个人意向职位列表
     *
     * @param userId
     * @return
     */
    List<PersonalExpectedPositionListVo> queryPersonalExpectedPositionList(@Param("userId") Long userId);

    /**
     * 查询个人意向区域列表
     *
     * @param userId
     * @return
     */
    List<PersonalExpectedWorkAreaListVo> queryPersonalExpectedWorkAreaList(@Param("userId") Long userId);

    /**
     * 求职预览
     *
     * @param userId
     * @return
     */
    HuntJobPublishPreviewVo publishPreview(@Param("userId") Long userId);

    /**
     * 根据id查询求职信息详情
     *
     * @param id
     * @return
     */
    HuntJobDetailVo detail(@Param("id") Long id);

    /**
     * 修改求职信息
     *
     * @param modifyDto
     * @return
     */
    int modify(@Param("modifyDto") HuntJobModifyDto modifyDto);

    /**
     * 根据id预览求职详细信息
     *
     * @param id
     * @return
     */
    HuntJobDetailsPreviewVo detailsPreview(@Param("userId") Long userId, @Param("id") Long id);
}
