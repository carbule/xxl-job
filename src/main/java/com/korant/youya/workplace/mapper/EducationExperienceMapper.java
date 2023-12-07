package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceModifyDto;
import com.korant.youya.workplace.pojo.po.EducationExperience;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceDetailVo;
import org.apache.ibatis.annotations.Param;

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
     * 修改教育经历信息
     *
     * @param modifyDto
     * @return
     */
    int modify(@Param("modifyDto") EducationExperienceModifyDto modifyDto);

    /**
     * 查询教育经历信息详情
     *
     * @param id
     * @return
     */
    EducationExperienceDetailVo detail(@Param("id") Long id);
}
