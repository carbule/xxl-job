package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.huntjobqrcode.UnlimitedQRCodeDto;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobQrcodeData;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.ReferrerInfoVo;
import com.korant.youya.workplace.service.HuntJobQrCodeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <p>
 * 求职推荐二维码表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/huntJobQrCode")
public class HuntJobQrCodeController {

    @Resource
    private HuntJobQrCodeService huntJobQrCodeService;

    /**
     * 获取小程序求职二维码
     *
     * @param unlimitedQRCodeDto
     * @param response
     * @return
     */
    @PostMapping("/getUnlimitedQRCode")
    public void getUnlimitedQRCode(@RequestBody @Valid UnlimitedQRCodeDto unlimitedQRCodeDto, HttpServletResponse response) throws IOException {
        huntJobQrCodeService.getUnlimitedQRCode(unlimitedQRCodeDto, response);
    }

    /**
     * 根据二维码id获取二维码数据
     *
     * @param id
     * @return
     */
    @GetMapping("/getData/{id}")
    public R<?> getData(@PathVariable("id") Long id) {
        HuntJobQrcodeData data = huntJobQrCodeService.getData(id);
        return R.success(data);
    }

    /**
     * 获取推荐人信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getReferrerInfo/{id}")
    public R<?> getReferrerInfo(@PathVariable("id") Long id) {
        ReferrerInfoVo data = huntJobQrCodeService.getReferrerInfo(id);
        return R.success(data);
    }
}
