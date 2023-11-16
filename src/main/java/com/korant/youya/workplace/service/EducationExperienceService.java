package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceQueryListDto;
import com.korant.youya.workplace.pojo.po.EducationExperience;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceListVo;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceListVo;

/**
 * <p>
 * 教育经历表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EducationExperienceService extends IService<EducationExperience> {

    Page<EducationExperienceListVo> queryList(EducationExperienceQueryListDto listDto);

    void create(EducationExperienceCreateDto educationExperienceCreateDto);

    void modify(EducationExperienceModifyDto educationExperienceModifyDto);

    EducationExperienceDetailVo detail(Long id);

    void delete(Long id);
}
