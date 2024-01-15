package com.korant.youya.workplace.pojo.vo.jobqrcode;

import lombok.Data;

import java.util.List;

/**
 * @ClassName JobSharingVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/15 11:39
 * @Version 1.0
 */
@Data
public class JobSharingVo {

    /**
     * 分享数量
     */
    private Integer shareQuantity;

    /**
     * 职位分享列表
     */
    private List<JobQrCodeVo> qrCodeVoList;
}
