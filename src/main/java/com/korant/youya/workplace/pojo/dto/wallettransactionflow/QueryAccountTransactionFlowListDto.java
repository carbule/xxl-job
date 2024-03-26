package com.korant.youya.workplace.pojo.dto.wallettransactionflow;

import com.korant.youya.workplace.pojo.PageParam;
import lombok.Data;

/**
 * @ClassName QueryAccountTransactionFlowListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/26 10:04
 * @Version 1.0
 */
@Data
public class QueryAccountTransactionFlowListDto extends PageParam {

    /**
     * 交易类型
     */
    private Integer transactionType;
}
