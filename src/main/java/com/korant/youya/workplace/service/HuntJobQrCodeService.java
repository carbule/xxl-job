package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.huntjobqrcode.UnlimitedQRCodeDto;
import com.korant.youya.workplace.pojo.po.HuntJobQrCode;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobQrcodeData;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.ReferrerInfoVo;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <p>
 * 求职推荐二维码表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface HuntJobQrCodeService extends IService<HuntJobQrCode> {

    /**
     * 获取小程序求职二维码
     *
     * @param unlimitedQRCodeDto
     * @param response
     */
    void getUnlimitedQRCode(UnlimitedQRCodeDto unlimitedQRCodeDto, HttpServletResponse response) throws IOException;

    /**
     * 根据二维码id获取二维码数据
     *
     * @param id
     * @return
     */
    HuntJobQrcodeData getData(Long id);

    /**
     * 获取推荐人信息
     *
     * @param id
     * @return
     */
    ReferrerInfoVo getReferrerInfo(Long id);
}
