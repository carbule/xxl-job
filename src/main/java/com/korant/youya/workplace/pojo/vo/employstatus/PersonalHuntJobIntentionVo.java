package com.korant.youya.workplace.pojo.vo.employstatus;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

import java.util.List;

/**
 * @ClassName PersonalHuntJobIntentionVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 14:38
 * @Version 1.0
 */
@Data
public class PersonalHuntJobIntentionVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 状态
     */
    @Dict(categoryCode = "employ_status")
    private Integer status;

    /**
     * 意向职位集
     */
    private List<ExpectedPositionVo> expectedPositionVoList;

    /**
     * 意向区域集
     */
    private List<ExpectedWorkAreaVo> expectedWorkAreaVoList;
}
