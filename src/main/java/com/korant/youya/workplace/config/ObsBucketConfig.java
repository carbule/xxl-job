package com.korant.youya.workplace.config;

import com.korant.youya.workplace.properties.ObsBucketConfigurationProperties;
import com.korant.youya.workplace.properties.ObsBucketProperties;
import com.korant.youya.workplace.utils.ObsUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.HashMap;

/**
 * @ClassName ObsBucketConfig
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/3 15:25
 * @Version 1.0
 */
@Configuration
@Slf4j
public class ObsBucketConfig {

    private static final HashMap<String, ObsBucketProperties> bucketMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Collection<ObsBucketProperties> values = bucketConfigurationProperties.getBucketProperties().values();
        if (!values.isEmpty()) {
            for (ObsBucketProperties value : values) {
                bucketMap.put(value.getCode(), value);
            }
        }
    }

    /**
     * 根据code获取bucketName
     *
     * @param code
     * @return
     */
    public static String getBucketName(String code) {
        ObsBucketProperties obsBucketProperties = bucketMap.get(code);
        return obsBucketProperties.getName();
    }

    /**
     * 根据code获取cdn地址
     *
     * @param code
     * @return
     */
    public static String getCdn(String code) {
        ObsBucketProperties obsBucketProperties = bucketMap.get(code);
        return obsBucketProperties.getCdn();
    }

    @Resource
    private ObsBucketConfigurationProperties bucketConfigurationProperties;

    @PostConstruct
    public void initBucket() {
        Collection<ObsBucketProperties> bucketProperties = bucketConfigurationProperties.getBucketProperties().values();
        if (!bucketProperties.isEmpty()) {
            log.info("读取到obsBucket配置");
            for (ObsBucketProperties bucketProperty : bucketProperties) {
                String name = bucketProperty.getName();
                try {
                    if (!ObsUtil.bucketExists(name)) {
                        //todo 暂时不写
                    } else {
                        log.info("obs bucket:[{}]已存在不需要创建", name);
                    }
                } catch (Exception e) {
                    log.error("obs bucket:[{}]创建失败，异常信息:", name, e);
                }
            }
        }
    }
}
