package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName ModifyUserPersonalBasicInfoDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/6 14:47
 * @Version 1.0
 */
@Data
public class ModifyUserPersonalBasicInfoDto {

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
    @NotNull(message = "性别不能为空")
    private Integer gender;

    /**
     * 用户生日
     */
    private LocalDate birthday;

    /**
     * 用户开始工作时间
     */
    @NotBlank(message = "开始工作时间不能为空")
    @Pattern(regexp = "^(\\d{4})-(\\d{2})$", message = "年月参数格式错误，应为 'yyyy-MM' 格式")
    private String startWorkingTime;

    /**
     * 政治面貌
     */
    private Integer politicalOutlook;

    /**
     * 个性签名
     */
    @Size(max = 30, message = "最多不得超过30字")
    private String personalSignature;

    /**
     * 自我评价
     */
    @Size(max = 50, message = "最多不得超过50字")
    private String selfEvaluation;
}
