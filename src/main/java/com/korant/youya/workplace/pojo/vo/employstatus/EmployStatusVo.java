package com.korant.youya.workplace.pojo.vo.employstatus;

import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/17 14:16
 * @PackageName:com.korant.youya.workplace.pojo.vo.employstatus
 * @ClassName: EmployStatusVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class EmployStatusVo {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 求职状态
     */
    private Integer status;

}
