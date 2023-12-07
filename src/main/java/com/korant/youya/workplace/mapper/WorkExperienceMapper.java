package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceModifyDto;
import com.korant.youya.workplace.pojo.po.WorkExperience;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePersonalVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工作履历表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface WorkExperienceMapper extends BaseMapper<WorkExperience> {

    /**
     * 查询个人工作履历列表
     *
     * @param userId
     * @return
     */
    List<WorkExperiencePersonalVo> queryPersonalList(@Param("userId") Long userId);

    /**
     * 修改工作履历信息
     *
     * @param modifyDto
     * @return
     */
    int modify(@Param("modifyDto") WorkExperienceModifyDto modifyDto);

    /**
     * 查询工作履历信息详情
     *
     * @param id
     * @return
     */
    WorkExperienceDetailVo detail(@Param("id") Long id);
}
