package com.korant.youya.workplace.pojo.vo.huntjob;

import lombok.Data;

/**
 * @ClassName EnterpriseHRVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 15:33
 * @Version 1.0
 */
@Data
public class EnterpriseHRVo {

    /**
     * 主键
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
}
