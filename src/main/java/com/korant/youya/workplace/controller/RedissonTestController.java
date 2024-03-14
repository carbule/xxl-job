package com.korant.youya.workplace.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedissonTestController
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/14 14:53
 * @Version 1.0
 */
@RestController
@RequestMapping("/redisson")
@Slf4j
public class RedissonTestController {

    @Resource
    private RedissonClient redissonClient;

    /**
     * redisson压测
     *
     * @return
     */
    @GetMapping("/stressTest")
    public void stressTest() {
        String lockKey = "lock:12345";
        RLock lock = redissonClient.getLock(lockKey);
        long id = Thread.currentThread().getId();
        String name = Thread.currentThread().getName();
        try {
            boolean tryLock = lock.tryLock(5, 5, TimeUnit.SECONDS);
            if (tryLock) {
                log.info("线程id:【{}】，线程名称:【{}】,获取分布式锁成功", id, name);
                Thread.sleep(20000);
            } else {
                log.error("线程id:【{}】，线程名称:【{}】,获取分布式锁失败超时", id, name);
            }
        } catch (Exception e) {
            log.error("线程id:【{}】，线程名称:【{}】,获取分布式锁失败,异常信息:", id, name, e);
        } finally {
            log.info("线程id:【{}】，线程名称:【{}】,进入finally程序块", id, name);
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("线程id:【{}】，线程名称:【{}】,释放分布式锁成功", id, name);
            }
        }
    }
}
