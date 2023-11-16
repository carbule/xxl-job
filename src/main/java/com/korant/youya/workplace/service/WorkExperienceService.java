package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceQueryListDto;
import com.korant.youya.workplace.pojo.po.WorkExperience;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceListVo;

/**
 * <p>
 * 工作履历表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface WorkExperienceService extends IService<WorkExperience> {

    Page<WorkExperienceListVo> queryList(WorkExperienceQueryListDto listDto);

    void create(WorkExperienceCreateDto workExperienceCreateDto);

    void modify(WorkExperienceModifyDto workExperienceModifyDto);

    WorkExperienceDetailVo detail(Long id);

    void delete(Long id);
}
