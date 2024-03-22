package com.korant.youya.workplace.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @ClassName CalculationUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/21 10:56
 * @Version 1.0
 */
public class CalculationUtil {

    /**
     * 执行乘法并截断到两位小数
     *
     * @param bd1
     * @param bd2
     * @return
     */
    public static BigDecimal multiplyAndTruncateToTwoDecimalPlaces(BigDecimal bd1, BigDecimal bd2) {
        return bd1.multiply(bd2).setScale(2, RoundingMode.DOWN);
    }

    /**
     * 执行除法并截断到两位小数
     *
     * @param dividend
     * @param divisor
     * @return
     */
    public static BigDecimal divideAndTruncateToTwoDecimalPlaces(BigDecimal dividend, BigDecimal divisor) {
        // 确保除数不为零
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Divisor cannot be zero");
        }
        // 执行除法运算
        return dividend.divide(divisor, 2, RoundingMode.DOWN);
    }

    /**
     * 判断是否是负数
     *
     * @param decimal
     * @return
     */
    public static boolean isNegativeNumber(BigDecimal decimal) {
        int sigNum = decimal.signum();
        return sigNum == -1;
    }

    /**
     * 去除小数后无用零位返回小数位
     *
     * @param decimal
     * @return
     */
    public static int getScaleAfterStrippingTrailingZeros(BigDecimal decimal) {
        BigDecimal stripTrailingZeros = decimal.stripTrailingZeros();
        return stripTrailingZeros.scale();
    }

    public static void main(String[] args) {
        BigDecimal decimal1 = multiplyAndTruncateToTwoDecimalPlaces(new BigDecimal("0.93"), new BigDecimal("0.07"));
        System.out.println(decimal1);
        BigDecimal decimal2 = divideAndTruncateToTwoDecimalPlaces(new BigDecimal("10"), new BigDecimal("3"));
        System.out.println(decimal2);
    }
}
