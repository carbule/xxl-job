package com.korant.youya.workplace.pojo.vo.jobIntention;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 11:26
 * @PackageName:com.korant.youya.workplace.pojo.vo.jobIntention
 * @ClassName: JobIntention
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class JobIntentionVo {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 求职状态
     */
    private Integer status;
}
