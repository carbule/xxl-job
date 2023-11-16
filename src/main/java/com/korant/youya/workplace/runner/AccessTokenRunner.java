package com.korant.youya.workplace.runner;

import com.korant.youya.workplace.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName AccessTokenRunner
 * @Description
 * @Author chenyiqiang
 * @Date 2023/9/7 13:55
 * @Version 1.0
 */
@Component
@Slf4j
public class AccessTokenRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        WeChatUtil.refreshAccessToken();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                int maxRetries = 3;
                int retryCount = 0;
                while (retryCount < maxRetries) {
                    try {
                        WeChatUtil.refreshAccessToken();
                        break;
                    } catch (Exception e) {
                        log.error("定时刷新AccessToken失败,异常信息:", e);
                        retryCount++;
                        if (retryCount == maxRetries) {
                            log.error("定时刷新AccessToken方法调用已达到最大重试次数");
                        } else {
                            log.error("正在进行第 " + retryCount + " 次重试");
                        }
                    }
                }
            }
        };
        // 设置定时器，任务执行完成后等待5分钟，然后再执行下一次任务
        executorService.scheduleWithFixedDelay(task, 5, 5, TimeUnit.MINUTES);
    }
}
