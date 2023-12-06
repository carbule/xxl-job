package com.korant.youya.workplace.pojo.dto.employstatus;

import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionModifyDto;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaModifyDto;
import lombok.Data;

import java.util.List;

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
    private List<ExpectedPositionModifyDto> positionModifyDtoList;

    /**
     * 期望工作区域
     */
    private List<ExpectedWorkAreaModifyDto> workAreaModifyDtoList;
}
