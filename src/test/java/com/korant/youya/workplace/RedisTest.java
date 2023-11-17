package com.korant.youya.workplace;

import com.alibaba.fastjson.JSONObject;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName RedisConfig
 * @Description redis测试类
 * @Author chenyiqiang
 * @Date 2023/7/18 12:02
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void set() {
        User user = new User();
        user.setId(1828828L);
        user.setPhone("18052042163");
        String s = JSONObject.toJSONString(user);
        redisUtil.set("6666", s);
    }

    @Test
    public void get() {
        Object o = redisUtil.get("6666");
        User user = JSONObject.parseObject(o.toString(), User.class);
        System.out.println(user.getId());
        System.out.println(user.getPhone());
    }
}
