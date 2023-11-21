package com.korant.youya.workplace.pojo.vo.user;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/20 16:27
 * @PackageName:com.korant.youya.workplace.pojo.vo.user
 * @ClassName: resumePersonInfoVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class resumePersonInfoVo {

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
    private LocalDateTime birthday;

    /**
     * 政治面貌
     */
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
