package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.HuntJobMapper;
import com.korant.youya.workplace.mapper.HuntJobQrCodeMapper;
import com.korant.youya.workplace.pojo.dto.huntjobqrcode.UnlimitedQRCodeDto;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.pojo.po.HuntJobQrCode;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobQrcodeData;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.ReferrerInfoVo;
import com.korant.youya.workplace.service.HuntJobQrCodeService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import com.korant.youya.workplace.utils.WeChatUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * <p>
 * 求职推荐二维码表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class HuntJobQrCodeServiceImpl extends ServiceImpl<HuntJobQrCodeMapper, HuntJobQrCode> implements HuntJobQrCodeService {

    @Resource
    private HuntJobQrCodeMapper huntJobQrCodeMapper;

    @Resource
    private HuntJobMapper huntJobMapper;

    @Value("${env_version}")
    private String env_version;

    /**
     * 获取小程序求职二维码
     *
     * @param unlimitedQRCodeDto
     * @param response
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getUnlimitedQRCode(UnlimitedQRCodeDto unlimitedQRCodeDto, HttpServletResponse response) throws IOException {
        ServletOutputStream out = null;
        Integer isShare = unlimitedQRCodeDto.getIsShare();
        if (0 != isShare && 1 != isShare) throw new YouyaException("非法的禁止推荐状态参数");
        Long huntId = unlimitedQRCodeDto.getHuntId();
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, huntId).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        Long userId = SpringSecurityUtil.getUserId();
        String scene;
        Long qrId = unlimitedQRCodeDto.getQrId();
        if (isShare == 1) {
            boolean exists = huntJobQrCodeMapper.exists(new LambdaQueryWrapper<HuntJobQrCode>().eq(HuntJobQrCode::getId, qrId).eq(HuntJobQrCode::getIsDelete, 0));
            if (!exists) throw new YouyaException("分享信息不存在");
            HuntJobQrCode qrCode = new HuntJobQrCode();
            qrCode.setPid(qrId);
            qrCode.setReferee(userId);
            qrCode.setIsShare(isShare);
            qrCode.setHuntId(huntId);
            huntJobQrCodeMapper.insert(qrCode);
            scene = "qrCodeId" + "=" + qrCode.getId();
        } else {
            if (null == qrId) {
                HuntJobQrCode qrCode = new HuntJobQrCode();
                qrCode.setReferee(userId);
                qrCode.setIsShare(isShare);
                qrCode.setHuntId(huntId);
                huntJobQrCodeMapper.insert(qrCode);
                scene = "qrCodeId" + "=" + qrCode.getId();
            } else {
                boolean exists = huntJobQrCodeMapper.exists(new LambdaQueryWrapper<HuntJobQrCode>().eq(HuntJobQrCode::getId, qrId).eq(HuntJobQrCode::getIsDelete, 0));
                if (!exists) throw new YouyaException("分享信息不存在");
                scene = "qrCodeId" + "=" + qrId;
            }
        }
        try {
            String page = unlimitedQRCodeDto.getPage();
            String accessToken = WeChatUtil.getMiniProgramAccessToken();
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
            byte[] unlimitedQRCode = WeChatUtil.getUnlimitedQRCode(url, scene, page, env_version);
            response.addHeader("Content-Disposition", "attachment;filename=" + url);
            response.addHeader("Content-Length", "" + unlimitedQRCode.length);
            response.setHeader("filename", url);
            response.setContentType("application/octet-stream");
            out = response.getOutputStream();
            out.write(unlimitedQRCode);
        } finally {
            assert out != null;
            out.close();
        }
    }

    /**
     * 根据二维码id获取二维码数据
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobQrcodeData getData(Long id) {
        return null;
    }

    /**
     * 获取推荐人信息
     *
     * @param id
     * @return
     */
    @Override
    public ReferrerInfoVo getReferrerInfo(Long id) {
        return null;
    }
}
