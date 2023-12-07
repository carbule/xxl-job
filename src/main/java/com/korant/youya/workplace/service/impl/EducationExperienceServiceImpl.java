package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.EducationExperienceMapper;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceModifyDto;
import com.korant.youya.workplace.pojo.po.EducationExperience;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceDetailVo;
import com.korant.youya.workplace.service.EducationExperienceService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 教育经历表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class EducationExperienceServiceImpl extends ServiceImpl<EducationExperienceMapper, EducationExperience> implements EducationExperienceService {

    @Resource
    private EducationExperienceMapper educationExperienceMapper;

    /**
     * 创建教育经历信息
     *
     * @param createDto
     */
    @Override
    public void create(EducationExperienceCreateDto createDto) {
        Long userId = SpringSecurityUtil.getUserId();
        EducationExperience educationExperience = new EducationExperience();
        BeanUtils.copyProperties(createDto,educationExperience);
        educationExperience.setUid(userId);
        educationExperienceMapper.insert(educationExperience);
    }

    /**
     * 修改教育经历信息
     *
     * @param modifyDto
     */
    @Override
    public void modify(EducationExperienceModifyDto modifyDto) {
        educationExperienceMapper.modify(modifyDto);
    }

    /**
     * 查询教育经历信息详情
     *
     * @param id
     * @return
     */
    @Override
    public EducationExperienceDetailVo detail(Long id) {
        return educationExperienceMapper.detail(id);
    }

    /**
     * 删除教育经历信息
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        EducationExperience educationExperience = educationExperienceMapper.selectOne(new LambdaQueryWrapper<EducationExperience>().eq(EducationExperience::getId, id).eq(EducationExperience::getIsDelete, 0));
        if (null == educationExperience) throw new YouyaException("教育经历不存在");
        educationExperience.setIsDelete(1);
        educationExperienceMapper.updateById(educationExperience);
    }
}
