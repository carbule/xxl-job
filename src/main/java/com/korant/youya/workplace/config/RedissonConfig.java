package com.korant.youya.workplace.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName RedissonConfig
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/14 14:03
 * @Version 1.0
 */
@Configuration
@Slf4j
public class RedissonConfig {

    @Resource
    private RedisProperties redisProperties;

    private final int TIMEOUT = 3000;
    private final int CONNECT_TIMEOUT = 300;
    private static final String ADDRESS_PREFIX = "redis://";

    @Bean
    public RedissonClient initBean() {
        //哨兵模式
        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        if (Objects.nonNull(sentinel)) {
            log.info("redis is sentinel mode");
            return redissonSentinel();
        }
        //集群模式
        RedisProperties.Cluster cluster = redisProperties.getCluster();
        if (Objects.nonNull(cluster)) {
            log.info("redis is cluster mode");
            return redissonCluster();
        }
        //单机模式
        String host = redisProperties.getHost();
        if (StringUtils.isNotBlank(host)) {
            log.info("redis is single mode");
            return redissonSingle();
        }
        log.error("redisson config can not support this redis mode");
        return null;
    }

    /**
     * 哨兵模式
     */
    private RedissonClient redissonSentinel() {
        String masterName = redisProperties.getSentinel().getMaster();
        List<String> nodes = redisProperties.getSentinel().getNodes();
        String password = redisProperties.getPassword();
        //声明一个配置类
        Config config = new Config();
        SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
        sentinelServersConfig.setTimeout(TIMEOUT);
        sentinelServersConfig.setConnectTimeout(CONNECT_TIMEOUT);
        //扫描间隔
        sentinelServersConfig.setScanInterval(2000);
        //判断密码
        if (StringUtils.isNotBlank(password)) {
            sentinelServersConfig.setPassword(password);
        }
        sentinelServersConfig.setMasterName(masterName);
        for (String node : nodes) {
            sentinelServersConfig.addSentinelAddress(ADDRESS_PREFIX + node);
        }
        //添加redis节点
        return Redisson.create(config);
    }

    /**
     * 集群模式
     */
    private RedissonClient redissonCluster() {
        List<String> nodes = redisProperties.getCluster().getNodes();
        String password = redisProperties.getPassword();
        //声明一个配置类
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        clusterServersConfig.setTimeout(TIMEOUT);
        clusterServersConfig.setConnectTimeout(CONNECT_TIMEOUT);
        //扫描间隔
        clusterServersConfig.setScanInterval(2000);
        //判断密码
        if (StringUtils.isNotBlank(password)) {
            clusterServersConfig.setPassword(password);
        }
        //添加redis节点
        for (String node : nodes) {
            clusterServersConfig.addNodeAddress(ADDRESS_PREFIX + node);
        }
        return Redisson.create(config);
    }

    /**
     * 单机模式
     */
    private RedissonClient redissonSingle() {
        String host = redisProperties.getHost();
        String password = redisProperties.getPassword();
        int port = redisProperties.getPort();
        //声明一个配置类
        Config config = new Config();
        int pingConnectionInterval = 60000;
        int connectionMinimumIdleSize = 10;
        int connectionPoolSize = 64;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(ADDRESS_PREFIX + host + ":" + port)
                .setTimeout(TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setPingConnectionInterval(pingConnectionInterval)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize);
        //判断密码
        if (StringUtils.isNotBlank(password)) {
            serverConfig.setPassword(password);
        }
        return Redisson.create(config);
    }
}
