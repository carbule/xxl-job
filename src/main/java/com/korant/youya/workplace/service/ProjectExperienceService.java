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

    /**
     * 查询项目经验信息列表
     *
     * @param listDto
     * @return
     */
    Page<ProjectExperienceListVo> queryList(ProjectExperienceQueryListDto listDto);

    /**
     * 创建项目经验信息
     *
     * @return
     */
    void create(ProjectExperienceCreateDto projectExperienceCreateDto);

    /**
     * 修改项目经验信息
     *
     * @param
     * @return
     */
    void modify(ProjectExperienceModifyDto projectExperienceModifyDto);

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
     * @param
     * @return
     */
    void delete(Long id);
}
