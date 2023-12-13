package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName EnterprisePendingApprovalVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 14:11
 * @Version 1.0
 */
@Data
public class EnterprisePendingApprovalVo {

    /**
     * 申请事项id
     */
    private Long id;

    /**
     * 用户头像
     */
    private String avatar;

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

    /**
     * 事项类型 1-hr认证 2-员工认证
     */
    private Integer eventType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
