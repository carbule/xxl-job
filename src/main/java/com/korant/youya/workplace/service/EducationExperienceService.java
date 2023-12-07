package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceModifyDto;
import com.korant.youya.workplace.pojo.po.EducationExperience;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceDetailVo;

/**
 * <p>
 * 教育经历表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EducationExperienceService extends IService<EducationExperience> {

    /**
     * 创建教育经历信息
     *
     * @param createDto
     */
    void create(EducationExperienceCreateDto createDto);

    /**
     * 修改教育经历信息
     *
     * @param modifyDto
     */
    void modify(EducationExperienceModifyDto modifyDto);

    /**
     * 查询教育经历信息详情
     *
     * @param id
     * @return
     */
    EducationExperienceDetailVo detail(Long id);

    /**
     * 删除教育经历信息
     *
     * @param id
     */
    void delete(Long id);
}
