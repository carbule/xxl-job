package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.ProjectExperienceMapper;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceQueryListDto;
import com.korant.youya.workplace.pojo.po.ProjectExperience;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceListVo;
import com.korant.youya.workplace.service.ProjectExperienceService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private  ProjectExperienceMapper projectExperienceMapper;

    /**
     * @Author Duan-zhixiao
     * @Description 查询项目经验信息列表
     * @Date 16:16 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public Page<ProjectExperienceListVo> queryList(ProjectExperienceQueryListDto listDto) {

        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = projectExperienceMapper.queryCountByUserId(userId);
        List<ProjectExperienceListVo> list = projectExperienceMapper.queryProjectExperienceListByUserId(userId, pageNumber, pageSize);
        Page<ProjectExperienceListVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;

    }

    /**
     * @Author Duan-zhixiao
     * @Description 创建项目经验信息
     * @Date 16:16 2023/11/16
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ProjectExperienceCreateDto projectExperienceCreateDto) {

        ProjectExperience projectExperience = new ProjectExperience();
        BeanUtils.copyProperties(projectExperienceCreateDto, projectExperience);
        projectExperienceMapper.insert(projectExperience);

    }

    /**
     * @Author Duan-zhixiao
     * @Description 修改项目经验信息
     * @Date 16:16 2023/11/16
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ProjectExperienceModifyDto projectExperienceModifyDto) {

        ProjectExperience projectExperience = projectExperienceMapper.selectById(projectExperienceModifyDto.getId());
        if (projectExperience == null) throw new YouyaException("项目经验信息不存在！");
        BeanUtils.copyProperties(projectExperienceModifyDto, projectExperience);
        projectExperienceMapper.updateById(projectExperience);

    }

    /**
     * @Author Duan-zhixiao
     * @Description 查询项目经验信息详情
     * @Date 16:16 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public ProjectExperienceDetailVo detail(Long id) {
        return projectExperienceMapper.detail(id);
    }

    /**
     * @Author Duan-zhixiao
     * @Description 删除项目经验信息
     * @Date 16:16 2023/11/16
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        ProjectExperience projectExperience = projectExperienceMapper.selectById(id);
        if (projectExperience == null) throw new YouyaException("项目经验信息不存在！");
        projectExperience.setIsDelete(1);
        projectExperienceMapper.updateById(projectExperience);

    }
}
