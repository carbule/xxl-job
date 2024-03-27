package com.korant.youya.workplace.utils;

import com.korant.youya.workplace.exception.YouyaException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @ClassName IdGenerationUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/27 9:53
 * @Version 1.0
 */
public class IdGenerationUtil {

    private static final String TRANSACTION_FLOW_CODE = "800";

    /**
     * 生成订单id
     *
     * @param consumerCode
     * @param businessCode
     * @return
     */
    public static String generateOrderId(String consumerCode, String businessCode) {
        if (StringUtils.isBlank(consumerCode)) throw new YouyaException("消费方编码缺失");
        if (StringUtils.isBlank(businessCode)) throw new YouyaException("业务编码缺失");
        StringBuilder builder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String currentDateTimeStr = now.format(formatter);
        builder.append(consumerCode).append(businessCode).append(currentDateTimeStr);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            //生成0到9的随机数
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }


    /**
     * 生成交易流水id
     *
     * @param businessCode
     * @return
     */
    public static String generateTransactionFlowId(String businessCode) {
        if (StringUtils.isBlank(businessCode)) throw new YouyaException("业务编码缺失");
        StringBuilder builder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String currentDateTimeStr = now.format(formatter);
        builder.append(TRANSACTION_FLOW_CODE).append(businessCode).append(currentDateTimeStr);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            //生成0到9的随机数
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
