package com.korant.youya.workplace.pojo.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Date 2023/11/20 16:27
 * @ClassName: resumePersonInfoVo
 * @Description:
 * @Version 1.0
 */
@Data
public class ResumePersonInfoVo {

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
    @JsonFormat(pattern = "yyyy-MM",timezone="GMT+8")
    private LocalDate startWorkingTime;

    /**
     * 实名认证状态
     */
    private Integer authenticationStatus;

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
     * 关联公司
     */
    private String userEnterprise;

}