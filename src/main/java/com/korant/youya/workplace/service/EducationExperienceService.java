package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceQueryListDto;
import com.korant.youya.workplace.pojo.po.EducationExperience;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceListVo;

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
     * 查询教育经历信息列表
     *
     * @param listDto
     * @return
     */
    Page<EducationExperienceListVo> queryList(EducationExperienceQueryListDto listDto);

    /**
     * 创建教育经历信息
     *
     * @return
     */
    void create(EducationExperienceCreateDto educationExperienceCreateDto);

    /**
     * 修改教育经历信息
     *
     * @param
     * @return
     */
    void modify(EducationExperienceModifyDto educationExperienceModifyDto);

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
     * @param
     * @return
     */
    void delete(Long id);
}
