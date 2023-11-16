package com.korant.youya.workplace.pojo.dto.expectedworkarea;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 15:06
 * @PackageName:com.korant.youya.workplace.pojo.dto.expectedworkarea
 * @ClassName: ExpectedWorkAreaModifyDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ExpectedWorkAreaModifyDto {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空")
    private Long id;

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

    /**
     * 详细地址
     */
    private String detailedAddress;

}
