package com.korant.youya.workplace.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.config.ObsBucketConfig;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.HuntJobMapper;
import com.korant.youya.workplace.mapper.HuntJobQrCodeMapper;
import com.korant.youya.workplace.pojo.PageData;
import com.korant.youya.workplace.pojo.dto.graph.SharedDto;
import com.korant.youya.workplace.pojo.dto.huntjobqrcode.HuntJobUnlimitedQRCodeDto;
import com.korant.youya.workplace.pojo.dto.huntjobqrcode.huntJobQrCodeQueryListDto;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.pojo.po.HuntJobQrCode;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobQrcodeData;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobRecommendVo;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobRecruitmentProgressVo;
import com.korant.youya.workplace.properties.DelayProperties;
import com.korant.youya.workplace.properties.RabbitMqConfigurationProperties;
import com.korant.youya.workplace.service.GraphSharedService;
import com.korant.youya.workplace.service.HuntJobQrCodeService;
import com.korant.youya.workplace.utils.ObsUtil;
import com.korant.youya.workplace.utils.RabbitMqUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import com.korant.youya.workplace.utils.WeChatUtil;
import com.obs.services.model.PutObjectResult;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;

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

    @Resource
    private RabbitMqUtil rabbitMqUtil;

    @Resource
    private GraphSharedService graphSharedService;

    @Resource
    RabbitMqConfigurationProperties mqConfigurationProperties;

    @Value("${env_version}")
    private String env_version;

    private static final String HUNT_JOB_QRCODE_BUCKET = "activity";

    /**
     * 查询求职推荐列表
     *
     * @param listDto
     * @return
     */
    @Override
    public PageData<HuntJobRecommendVo> queryList(huntJobQrCodeQueryListDto listDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = huntJobQrCodeMapper.selectCount(new LambdaQueryWrapper<HuntJobQrCode>().eq(HuntJobQrCode::getReferee, userId).eq(HuntJobQrCode::getIsDelete, 0));
        HuntJobRecommendVo huntJobRecommendVo = huntJobQrCodeMapper.queryList(userId, listDto);
        PageData<HuntJobRecommendVo> page = new PageData<>();
        page.setData(huntJobRecommendVo).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询求职招聘进度
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobRecruitmentProgressVo queryRecruitmentProgress(Long id) {

        return null;
    }

    /**
     * 获取小程序求职二维码
     *
     * @param unlimitedQRCodeDto
     * @param response
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getUnlimitedQRCode(HuntJobUnlimitedQRCodeDto unlimitedQRCodeDto, HttpServletResponse response) throws IOException {
        ServletOutputStream out = null;
        Integer isShare = unlimitedQRCodeDto.getIsShare();
        if (0 != isShare && 1 != isShare) throw new YouyaException("非法的禁止推荐状态参数");
        Long huntId = unlimitedQRCodeDto.getHuntId();
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, huntId).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        Long userId = SpringSecurityUtil.getUserId();
        String scene;
        Long qrId = unlimitedQRCodeDto.getQrId();
        if (null == qrId) {
            HuntJobQrCode qrCode = new HuntJobQrCode();
            qrCode.setReferee(userId);
            qrCode.setIsShare(isShare);
            qrCode.setHuntId(huntId);
            huntJobQrCodeMapper.insert(qrCode);
            scene = "qrCodeId" + "=" + qrCode.getId();
        } else {
            if (isShare == 1) {
                HuntJobQrCode huntJobQrCode = huntJobQrCodeMapper.selectOne(new LambdaQueryWrapper<HuntJobQrCode>().eq(HuntJobQrCode::getId, qrId).eq(HuntJobQrCode::getIsDelete, 0));
                if (null == huntJobQrCode) throw new YouyaException("分享信息不存在");
                HuntJobQrCode qrCode = new HuntJobQrCode();
                qrCode.setPid(qrId);
                qrCode.setReferee(userId);
                qrCode.setIsShare(isShare);
                qrCode.setHuntId(huntId);
                huntJobQrCodeMapper.insert(qrCode);
                scene = "qrCodeId" + "=" + qrCode.getId();
                Long fromUserId = huntJobQrCode.getReferee();
                boolean result = graphSharedService.existShared(fromUserId, userId, huntId);
                if (!result) {
                    SharedDto sharedDto = new SharedDto();
                    sharedDto.setTargetId(huntId);
                    sharedDto.setFromUserId(fromUserId);
                    sharedDto.setToUserId(userId);
                    sharedDto.setIsHr(false);
                    sharedDto.setTimestamp(LocalDateTime.now());
                    Message message = new Message(JSONObject.toJSONString(sharedDto).getBytes(StandardCharsets.UTF_8));
                    rabbitMqUtil.sendMsg("youya.share", "huntjob.share", message);
                }
            } else {
                HuntJobQrCode huntJobQrCode = huntJobQrCodeMapper.selectOne(new LambdaQueryWrapper<HuntJobQrCode>().eq(HuntJobQrCode::getId, qrId).eq(HuntJobQrCode::getIsDelete, 0));
                if (null == huntJobQrCode) throw new YouyaException("分享信息不存在");
                if (huntJobQrCode.getIsShare() == 1) {
                    HuntJobQrCode qrCode = new HuntJobQrCode();
                    qrCode.setPid(qrId);
                    qrCode.setReferee(userId);
                    qrCode.setIsShare(0);
                    qrCode.setHuntId(huntId);
                    huntJobQrCodeMapper.insert(qrCode);
                    scene = "qrCodeId" + "=" + qrCode.getId();
                    Long fromUserId = huntJobQrCode.getReferee();
                    boolean result = graphSharedService.existShared(fromUserId, userId, huntId);
                    if (!result) {
                        SharedDto sharedDto = new SharedDto();
                        sharedDto.setTargetId(huntId);
                        sharedDto.setFromUserId(fromUserId);
                        sharedDto.setToUserId(userId);
                        sharedDto.setIsHr(false);
                        sharedDto.setTimestamp(LocalDateTime.now());
                        Message message = new Message(JSONObject.toJSONString(sharedDto).getBytes(StandardCharsets.UTF_8));
                        rabbitMqUtil.sendMsg("youya.share", "huntjob.share", message);
                    }
                } else {
                    scene = "qrCodeId" + "=" + qrId;
                }
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
     * 上传分享图片
     *
     * @param file
     * @return
     */
    @Override
    public String uploadShareImage(MultipartFile file) {
        if (null == file) throw new YouyaException("分享图片不能为空");
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new YouyaException("文件流获取失败");
        }
        String bucketName = ObsBucketConfig.getBucketName(HUNT_JOB_QRCODE_BUCKET);
        String path = "hunt_job_qrcode/";
        PutObjectResult result = ObsUtil.putObject(bucketName, path, "jpg", inputStream);
        if (null == result) throw new YouyaException("上传文件失败");
        String etag = result.getEtag();
        String objectUrl = result.getObjectUrl();
        String objectKey = result.getObjectKey();
        if (StringUtils.isBlank(etag) && StringUtils.isBlank(objectUrl)) throw new YouyaException("上传文件失败");
        HashMap<String, DelayProperties> delayProperties = mqConfigurationProperties.getDelayProperties();
        DelayProperties properties = delayProperties.get("hunt_job_shareImage");
        Message message = new Message(objectKey.getBytes(StandardCharsets.UTF_8));
        rabbitMqUtil.sendDelayedMsg(properties.getExchangeName(), properties.getRoutingKey(), message, 60);
        String encode = URLEncoder.encode(objectKey, StandardCharsets.UTF_8);
        return "https://" + ObsBucketConfig.getCdn(HUNT_JOB_QRCODE_BUCKET) + "/" + encode;
    }

    /**
     * 根据二维码id获取二维码数据
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobQrcodeData getData(Long id) {
        return huntJobQrCodeMapper.getData(id);
    }

    /**
     * 删除分享图片
     *
     * @param objectKey
     */
    @Override
    public void deleteShareImage(String objectKey) {
        String bucketName = ObsBucketConfig.getBucketName(HUNT_JOB_QRCODE_BUCKET);
        ObsUtil.deleteObject(bucketName, objectKey);
    }
}
