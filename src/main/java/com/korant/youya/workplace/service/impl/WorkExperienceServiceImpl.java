package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.WorkExperienceMapper;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceModifyDto;
import com.korant.youya.workplace.pojo.po.WorkExperience;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePersonalVo;
import com.korant.youya.workplace.service.WorkExperienceService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工作履历表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class WorkExperienceServiceImpl extends ServiceImpl<WorkExperienceMapper, WorkExperience> implements WorkExperienceService {

    @Resource
    private WorkExperienceMapper workExperienceMapper;

    /**
     * 查询个人工作履历列表
     *
     * @return
     */
    @Override
    public List<WorkExperiencePersonalVo> queryPersonalList() {
        return workExperienceMapper.queryPersonalList(SpringSecurityUtil.getUserId());
    }

    /**
     * 创建工作履历信息
     *
     * @param createDto
     */
    @Override
    public void create(WorkExperienceCreateDto createDto) {
        Long userId = SpringSecurityUtil.getUserId();
        WorkExperience workExperience = new WorkExperience();
        BeanUtils.copyProperties(createDto, workExperience);
        workExperience.setUid(userId);
        workExperienceMapper.insert(workExperience);
    }

    /**
     * 修改工作履历信息
     *
     * @param modifyDto
     */
    @Override
    public void modify(WorkExperienceModifyDto modifyDto) {
        workExperienceMapper.modify(modifyDto);
    }

    /**
     * 查询工作履历信息详情
     *
     * @param id
     * @return
     */
    @Override
    public WorkExperienceDetailVo detail(Long id) {
        return workExperienceMapper.detail(id);
    }

    /**
     * 删除工作履历信息
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        WorkExperience workExperience = workExperienceMapper.selectOne(new LambdaQueryWrapper<WorkExperience>().eq(WorkExperience::getId, id).eq(WorkExperience::getIsDelete, 0));
        if (null == workExperience) throw new YouyaException("工作履历不存在");
        workExperience.setIsDelete(1);
        workExperienceMapper.updateById(workExperience);
    }
}
