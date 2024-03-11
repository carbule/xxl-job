package com.korant.youya.workplace.pojo.dto.sysorder;

import com.korant.youya.workplace.pojo.PageParam;
import lombok.Data;

/**
 * @ClassName QueryOrderListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/11 16:09
 * @Version 1.0
 */
@Data
public class QueryOrderListDto extends PageParam {

    /**
     * 订单状态
     */
    private Integer status;
}
