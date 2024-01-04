package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateDetailVo;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateRecruitmentRecordsVo;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateVo;
import com.korant.youya.workplace.service.CandidateService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName CandidateController
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 16:39
 * @Version 1.0
 */
@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Resource
    private CandidateService candidateService;

    /**
     * 查询候选人列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    public R<?> queryList(@RequestBody @Valid CandidateQueryListDto listDto) {
        Page<CandidateVo> page = candidateService.queryList(listDto);
        return R.success(page);
    }

    /**
     * 查询候选人详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public R<?> detail(@PathVariable("id") Long id) {
        CandidateDetailVo detailVo = candidateService.detail(id);
        return R.success(detailVo);
    }

    /**
     * 查询候选人招聘记录
     *
     * @param id
     * @return
     */
    @GetMapping("/queryRecruitmentRecords/{id}")
    public R<?> queryRecruitmentRecords(@PathVariable("id") Long id) {
        CandidateRecruitmentRecordsVo recruitmentRecordsVo = candidateService.queryRecruitmentRecords(id);
        return R.success(recruitmentRecordsVo);
    }
}
