package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.dto.talentpool.AssociateDto;
import com.korant.youya.workplace.pojo.dto.talentpool.QueryPublishedJobListDto;
import com.korant.youya.workplace.pojo.dto.talentpool.TalentPoolQueryListDto;
import com.korant.youya.workplace.pojo.vo.talentpool.PublishedJobVo;
import com.korant.youya.workplace.pojo.vo.talentpool.TalentDetailVo;
import com.korant.youya.workplace.pojo.vo.talentpool.TalentPoolVo;
import com.korant.youya.workplace.pojo.vo.talentpool.TalentRecruitmentRecordsVo;

/**
 * @ClassName TalentPoolService
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/4 17:37
 * @Version 1.0
 */
public interface TalentPoolService {

    /**
     * 查询人才库列表
     *
     * @param listDto
     * @return
     */
    Page<TalentPoolVo> queryList(TalentPoolQueryListDto listDto);

    /**
     * 查询人才详情
     *
     * @param id
     * @return
     */
    TalentDetailVo detail(Long id);

    /**
     * 查询人才招聘记录
     *
     * @param id
     * @return
     */
    TalentRecruitmentRecordsVo queryRecruitmentRecords(Long id);

    /**
     * 查询已发布职位
     *
     * @param listDto
     * @return
     */
    Page<PublishedJobVo> queryPublishedJobList(QueryPublishedJobListDto listDto);

    /**
     * 关联职位
     *
     * @param associateDto
     */
    void associate(AssociateDto associateDto);
}
