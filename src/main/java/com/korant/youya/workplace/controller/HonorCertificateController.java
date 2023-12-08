package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateCreateDto;
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
     * 创建荣誉证书信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid HonorCertificateCreateDto createDto) {
        honorCertificateService.create(createDto);
        return R.ok();
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
