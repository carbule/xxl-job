package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.jobqrcode.JobUnlimitedQRCodeDto;
import com.korant.youya.workplace.pojo.po.JobQrCode;
import com.korant.youya.workplace.pojo.vo.jobqrcode.JobQrcodeData;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 * 职位推荐二维码表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface JobQrCodeService extends IService<JobQrCode> {

    /**
     * 获取小程序职位二维码
     *
     * @param unlimitedQRCodeDto
     * @param response
     */
    void getUnlimitedQRCode(JobUnlimitedQRCodeDto unlimitedQRCodeDto, HttpServletResponse response) throws IOException;

    /**
     * 根据二维码id获取二维码数据
     *
     * @param id
     * @return
     */
    JobQrcodeData getData(Long id);

    /**
     * 上传分享图片
     *
     * @param file
     * @return
     */
    String uploadShareImage(MultipartFile file);

    /**
     * 删除分享图片
     *
     * @param objectKey
     */
    void deleteShareImage(String objectKey);
}
