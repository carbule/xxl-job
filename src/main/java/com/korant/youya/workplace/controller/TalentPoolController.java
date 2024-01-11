package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.talentpool.*;
import com.korant.youya.workplace.pojo.vo.talentpool.*;
import com.korant.youya.workplace.service.TalentPoolService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName TalentPoolController
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/4 17:35
 * @Version 1.0
 */
@RestController
@RequestMapping("/talentPool")
public class TalentPoolController {

    @Resource
    private TalentPoolService talentPoolService;

    /**
     * 查询人才库列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    public R<?> queryList(@RequestBody @Valid TalentPoolQueryListDto listDto) {
        Page<TalentPoolVo> page = talentPoolService.queryList(listDto);
        return R.success(page);
    }

    /**
     * 查询人才详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    @ExplanationDict
    public R<?> detail(@PathVariable("id") Long id) {
        TalentDetailVo detailVo = talentPoolService.detail(id);
        return R.success(detailVo);
    }

    /**
     * 查询人才招聘记录
     *
     * @param id
     * @return
     */
    @GetMapping("/queryRecruitmentRecords/{id}")
    public R<?> queryRecruitmentRecords(@PathVariable("id") Long id) {
        TalentRecruitmentRecordsVo recruitmentRecordsVo = talentPoolService.queryRecruitmentRecords(id);
        return R.success(recruitmentRecordsVo);
    }

    /**
     * 查询已发布职位
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryPublishedJobList")
    @ExplanationDict
    public R<?> queryPublishedJobList(@RequestBody @Valid QueryPublishedJobListDto listDto) {
        Page<PublishedJobVo> page = talentPoolService.queryPublishedJobList(listDto);
        return R.success(page);
    }

    /**
     * 关联职位
     *
     * @param associateDto
     * @return
     */
    @PostMapping("/associate")
    public R<?> associate(@RequestBody @Valid AssociateDto associateDto) {
        talentPoolService.associate(associateDto);
        return R.ok();
    }

    /**
     * 创建面试邀约
     *
     * @param createInterviewDto
     * @return
     */
    @PostMapping("/createInterview")
    public R<?> createInterview(@RequestBody @Valid TalentPoolCreateInterviewDto createInterviewDto) {
        talentPoolService.createInterview(createInterviewDto);
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
        TalentPoolInterviewDetailVo detailVo = talentPoolService.interviewDetail(id);
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
        talentPoolService.cancelInterview(id);
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
        talentPoolService.confirmInterview(id);
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
        talentPoolService.deleteInterview(id);
        return R.ok();
    }

    /**
     * 创建入职邀约
     *
     * @param createOnboardingDto
     * @return
     */
    @PostMapping("/createOnboarding")
    public R<?> createOnboarding(@RequestBody @Valid TalentPoolCreateOnboardingDto createOnboardingDto) {
        talentPoolService.createOnboarding(createOnboardingDto);
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
        TalentPoolOnboardingDetailVo detailVo = talentPoolService.onboardingDetail(id);
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
        talentPoolService.cancelOnboarding(id);
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
        talentPoolService.confirmOnboarding(id);
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
        talentPoolService.deleteOnboarding(id);
        return R.ok();
    }

    /**
     * 创建转正邀约
     *
     * @param createConfirmationDto
     * @return
     */
    @PostMapping("/createConfirmation")
    public R<?> createConfirmation(@RequestBody @Valid TalentPoolCreateConfirmationDto createConfirmationDto) {
        talentPoolService.createConfirmation(createConfirmationDto);
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
        TalentPoolConfirmationDetailVo detailVo = talentPoolService.confirmationDetail(id);
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
        talentPoolService.cancelConfirmation(id);
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
        talentPoolService.confirmConfirmation(id);
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
        talentPoolService.deleteConfirmation(id);
        return R.ok();
    }
}
