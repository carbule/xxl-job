package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.EducationExperience;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 教育经历表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EducationExperienceMapper extends BaseMapper<EducationExperience> {

    /**
     * 查询教育经历信息列表
     *
     * @param
     * @return
     */
    List<EducationExperienceListVo> queryEducationExperienceListByUserId(@Param("userId") Long userId, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);

    /**
     * 查询教育经历信息详情
     *
     * @param
     * @return
     */
    EducationExperienceDetailVo detail(@Param("id") Long id);
}
