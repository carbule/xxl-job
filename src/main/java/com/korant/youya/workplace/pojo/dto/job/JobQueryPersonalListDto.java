package com.korant.youya.workplace.pojo.dto.job;

import com.korant.youya.workplace.pojo.PageParam;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName JobQueryPersonalListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/4 11:53
 * @Version 1.0
 */
@Data
public class JobQueryPersonalListDto extends PageParam {

    /**
     * 状态
     */
    @NotNull(message = "职位状态不能为空")
    private Integer status;
}
