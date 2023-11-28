package com.korant.youya.workplace.pojo.dto.huntjob;

import com.korant.youya.workplace.pojo.PageParam;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/27 10:15
 * @ClassName: HuntJobQueryListDto
 * @Version 1.0
 */
@Data
public class HuntJobQueryListDto extends PageParam {

    /**
     * 职位编码
     */
    @NotNull(message = "职位编码不能为空")
    private String positionCode;

}
