package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.WorkExperienceMapper;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceQueryListDto;
import com.korant.youya.workplace.pojo.po.WorkExperience;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceListVo;
import com.korant.youya.workplace.service.WorkExperienceService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @Description 查询工作履历信息列表
     * @Param
     * @return
     **/
    @Override
    public Page<WorkExperienceListVo> queryList(WorkExperienceQueryListDto listDto) {

        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = workExperienceMapper.selectCount(new LambdaQueryWrapper<WorkExperience>().eq(WorkExperience::getUid, userId).eq(WorkExperience::getIsDelete, 0));
        List<WorkExperienceListVo> list = workExperienceMapper.queryWorkExperienceListByUserId(userId, pageNumber, pageSize);
        Page<WorkExperienceListVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;

    }

    /**
     * @Description 创建工作履历信息
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(WorkExperienceCreateDto workExperienceCreateDto) {

        Long userId = SpringSecurityUtil.getUserId();
        WorkExperience workExperience = new WorkExperience();
        BeanUtils.copyProperties(workExperienceCreateDto, workExperience);
        workExperience.setUid(userId);
        workExperienceMapper.insert(workExperience);

    }

    /**
     * @Description 修改工作履历信息
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(WorkExperienceModifyDto workExperienceModifyDto) {

        WorkExperience workExperience = workExperienceMapper.selectById(workExperienceModifyDto.getId());
        if (workExperience == null) throw new YouyaException("工作履历信息不存在!");
        workExperienceMapper.modify(workExperienceModifyDto);

    }

    /**
     * @Description 查询工作履历信息详情
     * @Param
     * @return
     **/
    @Override
    public WorkExperienceDetailVo detail(Long id) {
        return workExperienceMapper.detail(id);
    }

    /**
     * @Description 删除工作履历信息
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        WorkExperience workExperience = workExperienceMapper.selectById(id);
        if (workExperience == null) throw new YouyaException("工作履历信息不存在!");
        workExperienceMapper.update(null,
                new LambdaUpdateWrapper<WorkExperience>()
                        .eq(WorkExperience::getId, id)
                        .set(WorkExperience::getIsDelete, 1));

    }
}
