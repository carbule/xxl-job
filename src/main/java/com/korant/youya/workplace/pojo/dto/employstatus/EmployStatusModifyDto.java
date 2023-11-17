package com.korant.youya.workplace.pojo.dto.employstatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/17 14:25
 * @PackageName:com.korant.youya.workplace.pojo.dto.employstatus
 * @ClassName: EmployStatusModifyDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class EmployStatusModifyDto {

    /**
     * 求职状态
     */
    @NotNull(message = "求职状态不能为空")
    private Integer status;

}
