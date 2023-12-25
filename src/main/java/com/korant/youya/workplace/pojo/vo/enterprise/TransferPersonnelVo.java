package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

/**
 * @ClassName TransferPersonnelVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/14 13:52
 * @Version 1.0
 */
@Data
public class TransferPersonnelVo {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名字
     */
    private String firstName;

    /**
     * 用户手机号
     */
    private String phone;
}
