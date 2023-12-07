package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceModifyDto;
import com.korant.youya.workplace.pojo.po.WorkExperience;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePersonalVo;

import java.util.List;

/**
 * <p>
 * 工作履历表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface WorkExperienceService extends IService<WorkExperience> {

    /**
     * 查询个人工作履历列表
     *
     * @return
     */
    List<WorkExperiencePersonalVo> queryPersonalList();

    /**
     * 创建工作履历信息
     *
     * @param createDto
     */
    void create(WorkExperienceCreateDto createDto);

    /**
     * 修改工作履历信息
     *
     * @param modifyDto
     */
    void modify(WorkExperienceModifyDto modifyDto);

    /**
     * 查询工作履历信息详情
     *
     * @param id
     * @return
     */
    WorkExperienceDetailVo detail(Long id);

    /**
     * 删除工作履历信息
     *
     * @param id
     */
    void delete(Long id);
}
