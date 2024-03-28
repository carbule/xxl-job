package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.PageData;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.jobqrcode.JobQrCodeQueryListDto;
import com.korant.youya.workplace.pojo.dto.jobqrcode.JobUnlimitedQRCodeDto;
import com.korant.youya.workplace.pojo.vo.jobqrcode.JobQrCodeDetailVo;
import com.korant.youya.workplace.pojo.vo.jobqrcode.JobQrcodeData;
import com.korant.youya.workplace.pojo.vo.jobqrcode.JobSharingVo;
import com.korant.youya.workplace.service.JobQrCodeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 * 职位推荐二维码表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/jobQrCode")
public class JobQrCodeController {

    @Resource
    private JobQrCodeService jobQrCodeService;

    /**
     * 查询职位分享列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    @ExplanationDict
    public R<?> queryList(@RequestBody @Valid JobQrCodeQueryListDto listDto) {
        PageData<JobSharingVo> pageVo = jobQrCodeService.queryList(listDto);
        return R.success(pageVo);
    }

    /**
     * 查询分享职位详情
     *
     * @param id
     * @return
     */
    @GetMapping("/queryJobDetail/{id}")
    @ExplanationDict
    public R<?> queryJobDetail(@PathVariable("id") Long id) {
        JobQrCodeDetailVo detail = jobQrCodeService.queryJobDetail(id);
        return R.success(detail);
    }

    /**
     * 获取小程序职位二维码
     *
     * @param unlimitedQRCodeDto
     * @param response
     * @return
     */
    @PostMapping("/getUnlimitedQRCode")
    //todo 序列化有问题 明天看
    public void getUnlimitedQRCode(@RequestBody @Valid JobUnlimitedQRCodeDto unlimitedQRCodeDto, HttpServletResponse response) throws IOException {
        jobQrCodeService.getUnlimitedQRCode(unlimitedQRCodeDto, response);
    }

    /**
     * 上传分享图片
     *
     * @param
     * @returnb'gen
     */
    @PostMapping("/uploadShareImage")
    public R<?> uploadShareImage(MultipartFile file) {
        String imageUrl = jobQrCodeService.uploadShareImage(file);
        return R.success(imageUrl);
    }

    /**
     * 根据二维码id获取二维码数据
     *
     * @param id
     * @return
     */
    @GetMapping("/getData/{id}")
    public R<?> getData(@PathVariable("id") Long id) {
        JobQrcodeData data = jobQrCodeService.getData(id);
        return R.success(data);
    }
}
