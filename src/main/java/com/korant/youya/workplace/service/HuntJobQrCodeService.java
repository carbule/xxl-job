package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.PageData;
import com.korant.youya.workplace.pojo.dto.huntjobqrcode.HuntJobUnlimitedQRCodeDto;
import com.korant.youya.workplace.pojo.dto.huntjobqrcode.huntJobQrCodeQueryListDto;
import com.korant.youya.workplace.pojo.po.HuntJobQrCode;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobQrcodeData;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobRecommendVo;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobRecruitmentProgressVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

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
     * 查询求职推荐列表
     *
     * @param listDto
     * @return
     */
    PageData<HuntJobRecommendVo> queryList(huntJobQrCodeQueryListDto listDto);

    /**
     * 查询求职招聘进度
     *
     * @param id
     * @return
     */
    HuntJobRecruitmentProgressVo queryRecruitmentProgress(Long id);

    /**
     * 获取小程序求职二维码
     *
     * @param unlimitedQRCodeDto
     * @param response
     */
    void getUnlimitedQRCode(HuntJobUnlimitedQRCodeDto unlimitedQRCodeDto, HttpServletResponse response) throws IOException;

    /**
     * 上传分享图片
     *
     * @param file
     * @return
     */
    String uploadShareImage(MultipartFile file);

    /**
     * 根据二维码id获取二维码数据
     *
     * @param id
     * @return
     */
    HuntJobQrcodeData getData(Long id);

    /**
     * 删除分享图片
     *
     * @param objectKey
     */
    void deleteShareImage(String objectKey);
}
