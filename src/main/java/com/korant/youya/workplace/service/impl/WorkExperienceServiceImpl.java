package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.WorkExperienceMapper;
import com.korant.youya.workplace.pojo.SessionLocal;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceQueryListDto;
import com.korant.youya.workplace.pojo.po.WorkExperience;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceListVo;
import com.korant.youya.workplace.service.WorkExperienceService;
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
     * @Author Duan-zhixiao
     * @Description 查询工作履历信息列表
     * @Date 15:53 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public Page<WorkExperienceListVo> queryList(WorkExperienceQueryListDto listDto) {

        Long userId = SessionLocal.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = workExperienceMapper.selectCount(new LambdaQueryWrapper<WorkExperience>().eq(WorkExperience::getUid, userId).eq(WorkExperience::getIsDelete, 0));
        List<WorkExperienceListVo> list = workExperienceMapper.queryWorkExperienceListByUserId(userId, pageNumber, pageSize);
        Page<WorkExperienceListVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;

    }

    /**
     * @Author Duan-zhixiao
     * @Description 创建工作履历信息
     * @Date 15:53 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public void create(WorkExperienceCreateDto workExperienceCreateDto) {

        Long userId = SessionLocal.getUserId();
        WorkExperience workExperience = new WorkExperience();
        BeanUtils.copyProperties(workExperienceCreateDto, workExperience);
        workExperience.setUid(userId);
        workExperienceMapper.insert(workExperience);

    }

    /**
     * @Author Duan-zhixiao
     * @Description 修改工作履历信息
     * @Date 15:53 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public void modify(WorkExperienceModifyDto workExperienceModifyDto) {

        WorkExperience workExperience = workExperienceMapper.selectById(workExperienceModifyDto.getId());
        if (workExperience == null) throw new YouyaException("工作履历信息不存在!");
        BeanUtils.copyProperties(workExperienceModifyDto, workExperience);
        workExperienceMapper.updateById(workExperience);

    }

    /**
     * @Author Duan-zhixiao
     * @Description 查询工作履历信息详情
     * @Date 15:53 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public WorkExperienceDetailVo detail(Long id) {
        return workExperienceMapper.detail(id);
    }

    /**
     * @Author Duan-zhixiao
     * @Description 删除工作履历信息
     * @Date 15:53 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public void delete(Long id) {

        WorkExperience workExperience = workExperienceMapper.selectById(id);
        if (workExperience == null) throw new YouyaException("工作履历信息不存在!");
        workExperience.setIsDelete(1);
        workExperienceMapper.updateById(workExperience);

    }
}
