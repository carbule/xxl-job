package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.ProjectExperience;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * @Description 查询项目经验信息列表总数
     * @Param
     * @return
     **/
    Long queryCountByUserId(@Param("userId") Long userId);

    /**
     * @Description 查询项目经验信息列表
     * @Param
     * @return
     **/
    List<ProjectExperienceListVo> queryProjectExperienceListByUserId(@Param("userId") Long userId, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);

    /**
     * @Description 查询项目经验信息详情
     * @Param
     * @return
     **/
    ProjectExperienceDetailVo detail(@Param("id") Long id);
}
