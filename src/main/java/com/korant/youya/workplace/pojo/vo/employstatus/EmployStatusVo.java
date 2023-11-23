package com.korant.youya.workplace.pojo.vo.employstatus;

import com.korant.youya.workplace.annotations.Dict;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaInfoVo;
import lombok.Data;

import java.util.List;

/**
 * @Date 2023/11/17 14:16
 * @PackageName:com.korant.youya.workplace.pojo.vo.employstatus
 * @ClassName: EmployStatusVo
 * @Description:
 * @Version 1.0
 */
@Data
public class EmployStatusVo {

    /**
     * id
     */
    private Long id;

    /**
     * 求职状态
     */
    @Dict(categoryCode = "employ_status")
    private Integer status;

    /**
     * 求职意向-意向职位
     */
    private List<ExpectedPositionInfoVo> expectedPositionInfoVoList;

    /**
     * 求职意向-期望工作区域
     */
    private List<ExpectedWorkAreaInfoVo> expectedWorkAreaInfoVoList;


}
