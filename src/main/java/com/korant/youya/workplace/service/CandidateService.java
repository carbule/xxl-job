package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateVo;

/**
 * @ClassName CandidateService
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 16:39
 * @Version 1.0
 */
public interface CandidateService {

    /**
     * 查询候选人列表
     *
     * @param listDto
     * @return
     */
    Page<CandidateVo> queryList(CandidateQueryListDto listDto);
}
