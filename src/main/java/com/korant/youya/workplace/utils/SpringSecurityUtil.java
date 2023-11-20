package com.korant.youya.workplace.utils;

import com.korant.youya.workplace.pojo.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @ClassName SpringSecurityUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/20 13:55
 * @Version 1.0
 */
public class SpringSecurityUtil {

    /**
     * 获取用户信息
     *
     * @return
     */
    public static LoginUser getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (LoginUser) authentication.getPrincipal();
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getId();
    }
}
