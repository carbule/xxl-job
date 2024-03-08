package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName EnterpriseWalletVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/8 10:36
 * @Version 1.0
 */
@Data
public class EnterpriseWalletVo {

    /**
     * 账户总额
     */
    private BigDecimal accountBalance;

    /**
     * 冻结金额
     */
    private BigDecimal freezeAmount;

    /**
     * 可用余额
     */
    private BigDecimal availableBalance;
}
