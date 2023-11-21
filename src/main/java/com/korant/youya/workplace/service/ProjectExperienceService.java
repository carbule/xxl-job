package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceQueryListDto;
import com.korant.youya.workplace.pojo.po.ProjectExperience;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceListVo;

/**
 * <p>
 * 项目经验表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface ProjectExperienceService extends IService<ProjectExperience> {

    Page<ProjectExperienceListVo> queryList(ProjectExperienceQueryListDto listDto);

    void create(ProjectExperienceCreateDto projectExperienceCreateDto);

    void modify(ProjectExperienceModifyDto projectExperienceModifyDto);

    ProjectExperienceDetailVo detail(Long id);

    void delete(Long id);
}
