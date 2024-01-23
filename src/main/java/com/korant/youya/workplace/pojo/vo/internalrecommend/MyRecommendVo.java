package com.korant.youya.workplace.pojo.vo.internalrecommend;

import lombok.Data;

import java.util.List;

/**
 * @ClassName MyRecommendVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/22 17:07
 * @Version 1.0
 */
@Data
public class MyRecommendVo {

    /**
     * 推荐数量
     */
    private Integer recommendedQuantity;

    /**
     * 求职推荐列表
     */
    private List<RecommendVo> recommendVoList;
}
