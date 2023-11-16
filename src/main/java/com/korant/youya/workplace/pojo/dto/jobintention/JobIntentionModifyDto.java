package com.korant.youya.workplace.pojo.dto.jobintention;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 11:26
 * @PackageName:com.korant.youya.workplace.pojo.dto.jobIntention
 * @ClassName: JobIntention
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class JobIntentionModifyDto {

    /**
     * 求职状态
     */
    @NotBlank(message = "求职状态不能为空")
    private Integer status;
}
