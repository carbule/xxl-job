package com.korant.youya.workplace.pojo.dto.expectedposition;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/17 14:28
 * @PackageName:com.korant.youya.workplace.pojo.dto.expectedposition
 * @ClassName: ExpectedPositionCreateDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ExpectedPositionCreateDto {

    /**
     * 行业编码
     */
    @NotNull(message = "行业编码不能为空")
    private Long industryCode;

    /**
     * 领域编码
     */
    @NotNull(message = "领域编码不能为空")
    private Long sectorCode;

    /**
     * 职位编码
     */
    @NotNull(message = "职位编码不能为空")
    private Long positionCode;

    /**
     * 最低薪资
     */
    @NotNull(message = "最低薪资不能为空")
    private Integer minSalary;

    /**
     * 最高薪资
     */
    @NotNull(message = "最高薪资不能为空")
    private Integer maxSalary;

}
