package com.korant.youya.workplace.pojo.dto.enterprisetodo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/22 9:45
 * @ClassName: EnterpriseTodoCreateDto
 * @Description:
 * @Version 1.0
 */
@Data
public class EnterpriseTodoCreateDto {

    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;

    /**
     * 工号
     */
    @NotNull(message = "工号不能为空")
    private String employeeId;

    /**
     * 事项类型 1-hr认证 2-员工认证
     */
    @NotNull(message = "事项类型不能为空")
    private Integer eventType;

}
