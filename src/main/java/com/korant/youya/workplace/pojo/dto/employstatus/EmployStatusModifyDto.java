package com.korant.youya.workplace.pojo.dto.employstatus;

import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionCreateDto;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaCreateDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/17 14:25
 * @PackageName:com.korant.youya.workplace.pojo.dto.employstatus
 * @ClassName: EmployStatusModifyDto
 * @Description:
 * @Version 1.0
 */
@Data
public class EmployStatusModifyDto {

    /**
     * 求职状态
     */
    @NotNull(message = "求职状态不能为空")
    private Integer status;

    /**
     * 意向职位
     */
    private ExpectedPositionCreateDto[] expectedPositionCreateDtos;

    /**
     * 期望工作区域
     */
    private ExpectedWorkAreaCreateDto[] expectedWorkAreaCreateDtos;

}
