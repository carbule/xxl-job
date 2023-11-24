package com.korant.youya.workplace.pojo.vo.enterprisetodo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Date 2023/11/22 14:27
 * @PackageName:com.korant.youya.workplace.pojo.vo.enterprisetodo
 * @ClassName: EnterpriseTodoListVo
 * @Description:
 * @Version 1.0
 */
@Data
public class EnterpriseTodoListVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 事项类型 1-hr认证 2-员工认证
     */
    private Integer eventType;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名字
     */
    private String firstName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 工号
     */
    private String employeeId;

    /**
     * 身份证
     */
    private String identityCard;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
