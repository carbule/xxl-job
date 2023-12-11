package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.ProjectExperienceMapper;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceModifyDto;
import com.korant.youya.workplace.pojo.po.ProjectExperience;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceDetailVo;
import com.korant.youya.workplace.service.ProjectExperienceService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目经验表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class ProjectExperienceServiceImpl extends ServiceImpl<ProjectExperienceMapper, ProjectExperience> implements ProjectExperienceService {

    @Resource
    public ProjectExperienceMapper projectExperienceMapper;

    /**
     * 创建项目经验信息
     *
     * @param createDto
     */
    @Override
    public void create(ProjectExperienceCreateDto createDto) {
        Long userId = SpringSecurityUtil.getUserId();
        ProjectExperience projectExperience = new ProjectExperience();
        BeanUtils.copyProperties(createDto, projectExperience);
        projectExperience.setUid(userId);
        projectExperienceMapper.insert(projectExperience);
    }

    /**
     * 修改项目经验信息
     *
     * @param modifyDto
     */
    @Override
    public void modify(ProjectExperienceModifyDto modifyDto) {
        projectExperienceMapper.modify(modifyDto);
    }

    /**
     * 查询项目经验信息详情
     *
     * @param id
     * @return
     */
    @Override
    public ProjectExperienceDetailVo detail(Long id) {
        return projectExperienceMapper.detail(id);
    }

    /**
     * 删除项目经验信息
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        ProjectExperience projectExperience = projectExperienceMapper.selectOne(new LambdaQueryWrapper<ProjectExperience>().eq(ProjectExperience::getId, id).eq(ProjectExperience::getIsDelete, 0));
        if (null == projectExperience) throw new YouyaException("项目经验信息不存在");
        projectExperience.setIsDelete(1);
        projectExperienceMapper.updateById(projectExperience);
    }
}
