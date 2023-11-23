package com.korant.youya.workplace.pojo.dto.expectedposition;

import com.korant.youya.workplace.pojo.PageParam;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/23 13:49
 * @PackageName:com.korant.youya.workplace.pojo.dto.expectedposition
 * @ClassName: ExpectedPositionQueryDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ExpectedPositionQueryDto extends PageParam {

    /**
     * 职位编码
     */
    @NotNull(message = "职位编码不能为空")
    private String positionCode;

}
