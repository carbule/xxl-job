package com.korant.youya.workplace.pojo.vo.huntjobqrcode;

import lombok.Data;

import java.util.List;

/**
 * @ClassName HuntJobRecommendVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/15 11:32
 * @Version 1.0
 */
@Data
public class HuntJobRecommendVo {

    /**
     * 推荐数量
     */
    private Integer recommendedQuantity;

    /**
     * 求职推荐列表
     */
    private List<HuntJobQrCodeVo> qrCodeVoList;
}
