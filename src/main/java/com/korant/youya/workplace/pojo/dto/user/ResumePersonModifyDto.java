package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/20 17:42
 * @PackageName:com.korant.youya.workplace.pojo.dto.user
 * @ClassName: ResumePersonModifyDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ResumePersonModifyDto {

    /**
     * 用户头像
     */
    @NotBlank(message = "头像不能为空")
    private String avatar;

    /**
     * 用户姓氏
     */
    @NotBlank(message = "姓氏不能为空")
    private String lastName;

    /**
     * 用户名字
     */
    @NotBlank(message = "名字不能为空")
    private String firstName;

    /**
     * 用户性别
     */
    @NotBlank(message = "性别不能为空")
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

}
