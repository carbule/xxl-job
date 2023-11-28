package com.korant.youya.workplace.pojo.dto.huntjob;

import com.korant.youya.workplace.pojo.PageParam;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName HuntJobQueryPersonalListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/28 16:49
 * @Version 1.0
 */
@Data
public class HuntJobQueryPersonalListDto extends PageParam {

    /**
     * 求职状态
     */
    @NotNull(message = "求职状态不能为空")
    private Integer status;
}
