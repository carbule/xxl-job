package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.talentpool.AssociateDto;
import com.korant.youya.workplace.pojo.dto.talentpool.QueryPublishedJobListDto;
import com.korant.youya.workplace.pojo.dto.talentpool.TalentPoolQueryListDto;
import com.korant.youya.workplace.pojo.vo.talentpool.PublishedJobVo;
import com.korant.youya.workplace.pojo.vo.talentpool.TalentDetailVo;
import com.korant.youya.workplace.pojo.vo.talentpool.TalentPoolVo;
import com.korant.youya.workplace.pojo.vo.talentpool.TalentRecruitmentRecordsVo;
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
}
