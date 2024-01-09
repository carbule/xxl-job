package com.korant.youya.workplace.pojo.dto.confirmation;

import com.korant.youya.workplace.pojo.PageParam;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName ConfirmationQueryListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/9 14:29
 * @Version 1.0
 */
@Data
public class ConfirmationQueryListDto extends PageParam {

    /**
     * 招聘流程实例id
     */
    @NotNull(message = "招聘流程实例id不能为空")
    private Long recruitProcessInstanceId;
}
