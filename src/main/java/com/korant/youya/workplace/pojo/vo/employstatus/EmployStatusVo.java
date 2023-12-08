package com.korant.youya.workplace.pojo.vo.employstatus;

import com.korant.youya.workplace.annotations.Dict;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaVo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName EmployStatusVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/8 16:48
 * @Version 1.0
 */
@Data
public class EmployStatusVo {

    /**
     * 主键
     */
    private Long statusId;

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
