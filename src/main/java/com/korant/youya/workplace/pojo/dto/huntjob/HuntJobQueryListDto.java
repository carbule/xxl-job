package com.korant.youya.workplace.pojo.dto.huntjob;

import com.korant.youya.workplace.pojo.PageParam;
import lombok.Data;

/**
 * @ClassName HuntJobQueryListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/16 11:34
 * @Version 1.0
 */
@Data
public class HuntJobQueryListDto extends PageParam {

    /**
     * 地区编码
     */
    private String districtCode;

    /**
     * 职位编码
     */
    private String positionCode;
}
