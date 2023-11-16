package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceQueryListDto;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateCreateDto;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateModifyDto;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateQueryListDto;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceListVo;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateDetailDto;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateListDto;
import com.korant.youya.workplace.service.HonorCertificateService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 荣誉证书表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/honorCertificate")
public class HonorCertificateController {

    @Resource
    private HonorCertificateService honorCertificateService;

    /**
     * 查询荣誉证书信息列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    public R<?> queryList(@RequestBody @Valid HonorCertificateQueryListDto listDto) {
        Page<HonorCertificateListDto> page = honorCertificateService.queryList(listDto);
        return R.success(page);
    }

    /**
     * 创建荣誉证书信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid HonorCertificateCreateDto honorCertificateCreateDto) {
        honorCertificateService.create(honorCertificateCreateDto);
        return R.ok();
    }

    /**
     * 修改荣誉证书信息
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid HonorCertificateModifyDto honorCertificateModifyDto) {
        honorCertificateService.modify(honorCertificateModifyDto);
        return R.ok();
    }

    /**
     * 查询荣誉证书信息详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public R<?> detail(@PathVariable("id") Long id) {
        HonorCertificateDetailDto honorCertificateDetailDto = honorCertificateService.detail(id);
        return R.success(honorCertificateService);
    }

    /**
     * 删除荣誉证书信息
     *
     * @param
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        honorCertificateService.delete(id);
        return R.ok();
    }

}
