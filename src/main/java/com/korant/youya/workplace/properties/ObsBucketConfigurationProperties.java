package com.korant.youya.workplace.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @ClassName ObsBucketConfigurationProperties
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/3 15:15
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "obs-bucket")
public class ObsBucketConfigurationProperties {

    private HashMap<String, ObsBucketProperties> bucketProperties;
}
