package com.korant.youya.workplace.pojo;

/**
 * @author chenyiqiang
 * @date 2023-07-26
 */
public class SessionLocal {

    private static final ThreadLocal<Long> local = new ThreadLocal<>();

    public static void setUserId(Long id) {
        local.set(id);
    }

    public static Long getUserId() {
        return local.get();
    }

    public static void removeUserId() {
        local.remove();
    }
}
