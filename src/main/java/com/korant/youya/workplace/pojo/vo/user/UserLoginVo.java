package com.korant.youya.workplace.pojo.vo.user;

import lombok.Data;

/**
 * @ClassName UserLoginVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/20 10:00
 * @Version 1.0
 */
@Data
public class UserLoginVo {

    /**
     * token
     */
    private String token;

    /**
     * 用户角色
     */
    private String role;
}
