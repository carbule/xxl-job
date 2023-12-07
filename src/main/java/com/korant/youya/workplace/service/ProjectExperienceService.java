package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceModifyDto;
import com.korant.youya.workplace.pojo.po.ProjectExperience;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceDetailVo;

/**
 * <p>
 * 项目经验表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface ProjectExperienceService extends IService<ProjectExperience> {

    /**
     * 创建项目经验信息
     *
     * @param createDto
     */
    void create(ProjectExperienceCreateDto createDto);

    /**
     * 修改项目经验信息
     *
     * @param modifyDto
     */
    void modify(ProjectExperienceModifyDto modifyDto);

    /**
     * 查询项目经验信息详情
     *
     * @param id
     * @return
     */
    ProjectExperienceDetailVo detail(Long id);

    /**
     * 删除项目经验信息
     *
     * @param id
     */
    void delete(Long id);
}
