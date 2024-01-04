package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.mapper.CandidateMapper;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateDetailVo;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateRecruitmentRecordsVo;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateVo;
import com.korant.youya.workplace.service.CandidateService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CandidateServiceImpl
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 16:40
 * @Version 1.0
 */
@Service
public class CandidateServiceImpl implements CandidateService {

    @Resource
    private CandidateMapper candidateMapper;

    /**
     * 查询候选人列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<CandidateVo> queryList(CandidateQueryListDto listDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        int count = candidateMapper.queryListCount(userId, listDto);
        List<CandidateVo> list = candidateMapper.queryList(userId, listDto);
        Page<CandidateVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询候选人详情
     *
     * @param id
     * @return
     */
    @Override
    public CandidateDetailVo detail(Long id) {
        return candidateMapper.detail(id);
    }

    /**
     * 查询候选人招聘记录
     *
     * @param id
     * @return
     */
    @Override
    public CandidateRecruitmentRecordsVo queryRecruitmentRecords(Long id) {
        return candidateMapper.queryRecruitmentRecords(id);
    }
}
