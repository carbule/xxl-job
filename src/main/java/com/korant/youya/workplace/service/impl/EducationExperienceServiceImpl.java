package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.EducationExperienceMapper;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceQueryListDto;
import com.korant.youya.workplace.pojo.po.EducationExperience;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceListVo;
import com.korant.youya.workplace.service.EducationExperienceService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * 查询教育经历信息列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<EducationExperienceListVo> queryList(EducationExperienceQueryListDto listDto) {

        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = educationExperienceMapper.selectCount(new LambdaQueryWrapper<EducationExperience>().eq(EducationExperience::getUid, userId).eq(EducationExperience::getIsDelete, 0));
        List<EducationExperienceListVo> list = educationExperienceMapper.queryEducationExperienceListByUserId(userId, pageNumber, pageSize);
        Page<EducationExperienceListVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;

    }

    /**
     * 创建教育经历信息
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(EducationExperienceCreateDto educationExperienceCreateDto) {

        Long userId = SpringSecurityUtil.getUserId();
        EducationExperience educationExperience = new EducationExperience();
        BeanUtils.copyProperties(educationExperienceCreateDto, educationExperience);
        educationExperience.setUid(userId);
        educationExperienceMapper.insert(educationExperience);

    }

    /**
     * 修改教育经历信息
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(EducationExperienceModifyDto educationExperienceModifyDto) {

        EducationExperience educationExperience = educationExperienceMapper.selectById(educationExperienceModifyDto.getId());
        if (educationExperience == null) throw new YouyaException("教育经历不存在!");
        educationExperienceMapper.modify(educationExperienceModifyDto);

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
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        EducationExperience educationExperience = educationExperienceMapper.selectById(id);
        if (educationExperience == null) throw new YouyaException("教育经历不存在!");
        educationExperienceMapper.update(new EducationExperience(),
                new LambdaUpdateWrapper<EducationExperience>()
                        .eq(EducationExperience::getId, id)
                        .set(EducationExperience::getIsDelete, 1));

    }
}
