package com.korant.youya.workplace.pojo.dto.employstatus;

import com.korant.youya.workplace.pojo.ValidList;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionModifyDto;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaModifyDto;
import jakarta.validation.Valid;
import lombok.Data;

/**
 * @ClassName EmployStatusModifyDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/6 17:26
 * @Version 1.0
 */
@Data
public class EmployStatusModifyDto {

    /**
     * 求职状态
     */
    private Integer status;

    /**
     * 意向职位
     */
    @Valid
    private ValidList<ExpectedPositionModifyDto> positionModifyDtoList;

    /**
     * 期望工作区域
     */
    @Valid
    private ValidList<ExpectedWorkAreaModifyDto> workAreaModifyDtoList;
}
