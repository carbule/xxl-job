package com.korant.youya.workplace.pojo.vo.expectedposition;

import lombok.Data;

/**
 * @Date 2023/11/23 13:53
 * @PackageName:com.korant.youya.workplace.pojo.vo.expectedposition
 * @ClassName: ExpectedPositionInfoByPositionCodeVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ExpectedPositionInfoByPositionCodeVo {

    /**
     * 用户id
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
     * 个性签名
     */
    private String personalSignature;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 最低薪资
     */
    private Integer minSalary;

    /**
     * 最高薪资
     */
    private Integer maxSalary;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 行政区名称
     */
    private String districtName;

}
