package com.korant.youya.workplace.pojo.vo.employstatus;

import com.korant.youya.workplace.annotations.Dict;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionCreateDto;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaCreateDto;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaInfoVo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/17 14:16
 * @PackageName:com.korant.youya.workplace.pojo.vo.employstatus
 * @ClassName: EmployStatusVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class EmployStatusVo {

    /**
     * 求职状态
     */
    @Dict(categoryCode = "employ_status")
    private Integer status;

    private List<ExpectedPositionInfoVo> expectedPositionInfoVoList;

    private List<ExpectedWorkAreaInfoVo> expectedWorkAreaInfoVoList;


}
