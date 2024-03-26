package com.korant.youya.workplace.pojo.vo.sysorder;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName SysOrderVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/11 11:27
 * @Version 1.0
 */
@Data
public class SysOrderVo {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品描述
     */
    private String productDescription;

    /**
     * 下单日期
     */
    private LocalDateTime orderDate;

    /**
     * 实付金额
     */
    private BigDecimal actualAmount;

    /**
     * 订单状态
     */
    private Integer status;
}
