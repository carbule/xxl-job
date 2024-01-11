package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateConfirmationDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateInterviewDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateOnboardingDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.vo.candidate.*;
import com.korant.youya.workplace.service.CandidateService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 查询已发布职位分类
     *
     * @return
     */
    @GetMapping("/queryPublishedJobCategoryList")
    public R<?> queryPublishedJobCategoryList() {
        List<PublishedJobCategoryVo> list = candidateService.queryPublishedJobCategoryList();
        return R.success(list);
    }

    /**
     * 查询候选人详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    @ExplanationDict
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

    /**
     * 创建面试邀约
     *
     * @param createInterviewDto
     * @return
     */
    @PostMapping("/createInterview")
    public R<?> createInterview(@RequestBody @Valid CandidateCreateInterviewDto createInterviewDto) {
        candidateService.createInterview(createInterviewDto);
        return R.ok();
    }

    /**
     * 查询面试邀约详情
     *
     * @param id
     * @return
     */
    @GetMapping("/interviewDetail/{id}")
    public R<?> interviewDetail(@PathVariable("id") Long id) {
        CandidateInterviewDetailVo detailVo = candidateService.interviewDetail(id);
        return R.success(detailVo);
    }

    /**
     * 取消面试邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/cancelInterview/{id}")
    public R<?> cancelInterview(@PathVariable("id") Long id) {
        candidateService.cancelInterview(id);
        return R.ok();
    }

    /**
     * 确认完成面试邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/confirmInterview/{id}")
    public R<?> confirmInterview(@PathVariable("id") Long id) {
        candidateService.confirmInterview(id);
        return R.ok();
    }

    /**
     * 删除面试邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteInterview/{id}")
    public R<?> deleteInterview(@PathVariable("id") Long id) {
        candidateService.deleteInterview(id);
        return R.ok();
    }

    /**
     * 创建入职邀约
     *
     * @param createOnboardingDto
     * @return
     */
    @PostMapping("/createOnboarding")
    public R<?> createOnboarding(@RequestBody @Valid CandidateCreateOnboardingDto createOnboardingDto) {
        candidateService.createOnboarding(createOnboardingDto);
        return R.ok();
    }

    /**
     * 查询入职邀约详情
     *
     * @param id
     * @return
     */
    @GetMapping("/onboardingDetail/{id}")
    public R<?> onboardingDetail(@PathVariable("id") Long id) {
        CandidateOnboardingDetailVo detailVo = candidateService.onboardingDetail(id);
        return R.success(detailVo);
    }

    /**
     * 取消入职邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/cancelOnboarding/{id}")
    public R<?> cancelOnboarding(@PathVariable("id") Long id) {
        candidateService.cancelOnboarding(id);
        return R.ok();
    }

    /**
     * 确认完成入职邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/confirmOnboarding/{id}")
    public R<?> confirmOnboarding(@PathVariable("id") Long id) {
        candidateService.confirmOnboarding(id);
        return R.ok();
    }

    /**
     * 删除入职邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteOnboarding/{id}")
    public R<?> deleteOnboarding(@PathVariable("id") Long id) {
        candidateService.deleteOnboarding(id);
        return R.ok();
    }

    /**
     * 创建转正邀约
     *
     * @param createConfirmationDto
     * @return
     */
    @PostMapping("/createConfirmation")
    public R<?> createConfirmation(@RequestBody @Valid CandidateCreateConfirmationDto createConfirmationDto) {
        candidateService.createConfirmation(createConfirmationDto);
        return R.ok();
    }

    /**
     * 查询转正邀约详情
     *
     * @param id
     * @return
     */
    @GetMapping("/confirmationDetail/{id}")
    public R<?> confirmationDetail(@PathVariable("id") Long id) {
        CandidateConfirmationDetailVo detailVo = candidateService.confirmationDetail(id);
        return R.success(detailVo);
    }

    /**
     * 取消转正邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/cancelConfirmation/{id}")
    public R<?> cancelConfirmation(@PathVariable("id") Long id) {
        candidateService.cancelConfirmation(id);
        return R.ok();
    }

    /**
     * 确认完成转正邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/confirmConfirmation/{id}")
    public R<?> confirmConfirmation(@PathVariable("id") Long id) {
        candidateService.confirmConfirmation(id);
        return R.ok();
    }

    /**
     * 删除转正邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteConfirmation/{id}")
    public R<?> deleteConfirmation(@PathVariable("id") Long id) {
        candidateService.deleteConfirmation(id);
        return R.ok();
    }
}
