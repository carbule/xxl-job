package com.korant.youya.workplace.pojo;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName PageParam
 * @Description
 * @Author chenyiqiang
 * @Date 2023/7/31 10:07
 * @Version 1.0
 */
public class PageParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 4086992924280882445L;

    private int pageNumber = 1;

    private int pageSize = 10;

    public PageParam(int pageSize, int pageNumber) {
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public PageParam() {
    }

    public PageParam pageParam() {
        return new PageParam(this.pageSize, this.pageNumber);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageNumber(int pageNumber) {
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            pageSize = 10;
        }
        this.pageSize = pageSize;
    }

    @Deprecated
    public void setPage(int pageNumber) {
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        this.pageNumber = pageNumber;
    }

    public int getPage() {
        return this.pageNumber;
    }

    public int getLimitOffset() {
        return (pageNumber - 1) * pageSize;
    }

    public int getLimitRows() {
        return pageSize;
    }
}
