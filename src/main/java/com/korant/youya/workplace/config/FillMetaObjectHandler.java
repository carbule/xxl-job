package com.korant.youya.workplace.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName MetaObjectHandler
 * @Description
 * @Author chenyiqiang
 * @Date 2023/7/24 17:26
 * @Version 1.0
 */
@Component
public class FillMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("isDelete", 0, metaObject);
    }

    /**
     * 修改操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
