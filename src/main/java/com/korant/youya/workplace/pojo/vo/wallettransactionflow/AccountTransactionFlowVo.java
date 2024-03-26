package com.korant.youya.workplace.pojo.vo.wallettransactionflow;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName AccountTransactionFlowVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/26 10:05
 * @Version 1.0
 */
@Data
public class AccountTransactionFlowVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 交易类型
     */
    private Integer transactionType;

    /**
     * 交易方向 1-入账 2-出账
     */
    private Integer transactionDirection;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 货币代码
     */
    private String currency;

    /**
     * 交易简要描述
     */
    private String description;

    /**
     * 交易发起日期
     */
    private LocalDateTime initiationDate;

    /**
     * 交易状态
     */
    private Integer status;
}
