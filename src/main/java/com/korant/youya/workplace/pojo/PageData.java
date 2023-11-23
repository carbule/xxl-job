package com.korant.youya.workplace.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName PageData
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/25 17:43
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class PageData<T> {

    protected T data;

    protected long total;

    protected long size;

    protected long current;

    public PageData() {
        this.total = 0L;
        this.size = 10L;
        this.current = 1L;
    }

}
