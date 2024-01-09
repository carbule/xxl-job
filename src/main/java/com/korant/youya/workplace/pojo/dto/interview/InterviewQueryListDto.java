package com.korant.youya.workplace.pojo.dto.interview;

import com.korant.youya.workplace.pojo.PageParam;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName InterviewQueryListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/9 11:05
 * @Version 1.0
 */
@Data
public class InterviewQueryListDto extends PageParam {

    /**
     * 招聘流程实例id
     */
    @NotNull(message = "招聘流程实例id不能为空")
    private Long recruitProcessInstanceId;
}
