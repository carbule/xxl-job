package com.korant.youya.workplace.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @ClassName ObsBucketProperties
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/3 15:15
 * @Version 1.0
 */
@Data
@Component
public class ObsBucketProperties {

    /**
     * bucket名称
     */
    private String name;

    /**
     * bucket编码
     */
    private String code;

    /**
     * bucket策略
     */
    private String policy;

    /**
     * cdn地址
     */
    private String cdn;
}
