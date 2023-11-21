package com.korant.youya.workplace.pojo.vo.expectedworkarea;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 14:59
 * @PackageName:com.korant.youya.workplace.pojo.dto.expectedworkarea
 * @ClassName: ExpectedWorkAreaInfoDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ExpectedWorkAreaInfoVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 状态id
     */
    private Long statusId;

    /**
     * 国家编码
     */
    private String countryCode;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 市级编码
     */
    private String cityCode;

    /**
     * 行政区编码
     */
    private String districtCode;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 行政区名称
     */
    private String districtName;

}
