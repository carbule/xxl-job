package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceQueryListDto;
import com.korant.youya.workplace.pojo.po.WorkExperience;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceEnterpriseVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceListVo;

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
     * 查询工作履历信息列表
     *
     * @param listDto
     * @return
     */
    Page<WorkExperienceListVo> queryList(WorkExperienceQueryListDto listDto);

    /**
     * 创建工作履历信息
     *
     * @return
     */
    void create(WorkExperienceCreateDto workExperienceCreateDto);

    /**
     * 修改工作履历信息
     *
     * @param
     * @return
     */
    void modify(WorkExperienceModifyDto workExperienceModifyDto);

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
     * @param
     * @return
     */
    void delete(Long id);

    /**
     * 查询工作履历所有公司
     *
     * @param
     * @return
     */
    List<WorkExperienceEnterpriseVo> queryEnterpriseList();
}
