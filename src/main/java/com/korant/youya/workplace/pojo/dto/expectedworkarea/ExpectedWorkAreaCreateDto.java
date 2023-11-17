package com.korant.youya.workplace.pojo.dto.expectedworkarea;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 15:03
 * @PackageName:com.korant.youya.workplace.pojo.dto.expectedworkarea
 * @ClassName: ExpectedWorkAreaIncreaseDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ExpectedWorkAreaCreateDto {

    /**
     * 状态id
     */
    @NotNull(message = "状态id不能为空")
    private Long statusId;

    /**
     * 国家id
     */
    @NotNull(message = "国家id不能为空")
    private Long countryId;

    /**
     * 省份id
     */
    @NotNull(message = "省份id不能为空")
    private Long provinceId;

    /**
     * 市级id
     */
    @NotNull(message = "市级id不能为空")
    private Long cityId;

    /**
     * 行政区id
     */
    private Long districtId;

}
