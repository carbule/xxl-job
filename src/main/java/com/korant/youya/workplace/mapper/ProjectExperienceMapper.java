package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceModifyDto;
import com.korant.youya.workplace.pojo.po.ProjectExperience;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceDetailVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 项目经验表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface ProjectExperienceMapper extends BaseMapper<ProjectExperience> {

    /**
     * 修改项目经验信息
     *
     * @param modifyDto
     * @return
     */
    int modify(@Param("modifyDto") ProjectExperienceModifyDto modifyDto);

    /**
     * 查询项目经验信息详情
     *
     * @param id
     * @return
     */
    ProjectExperienceDetailVo detail(@Param("id") Long id);
}
