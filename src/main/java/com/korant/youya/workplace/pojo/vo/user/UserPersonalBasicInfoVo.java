package com.korant.youya.workplace.pojo.vo.user;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName UserPersonalBasicInfoVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/6 14:50
 * @Version 1.0
 */
@Data
public class UserPersonalBasicInfoVo {

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
     * 用户性别
     */
    private Integer gender;

    /**
     * 用户生日
     */
    private LocalDate birthday;

    /**
     * 用户开始工作时间
     */
    private LocalDate startWorkingTime;

    /**
     * 政治面貌
     */
    @Dict(categoryCode = "political_outlook")
    private Integer politicalOutlook;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 自我评价
     */
    private String selfEvaluation;

    /**
     * 是否关联企业
     */
    private Integer isAssociated;
}
