package com.korant.youya.workplace.pojo.vo.user;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName UserWalletVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/1 13:35
 * @Version 1.0
 */
@Data
public class UserWalletVo {

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
