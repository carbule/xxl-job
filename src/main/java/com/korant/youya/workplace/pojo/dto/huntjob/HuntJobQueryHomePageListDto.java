package com.korant.youya.workplace.pojo.dto.huntjob;

import com.korant.youya.workplace.pojo.PageParam;
import lombok.Data;

/**
 * @ClassName HuntJobQueryHomePageListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/16 11:34
 * @Version 1.0
 */
@Data
public class HuntJobQueryHomePageListDto extends PageParam {

    /**
     * 市级编码
     */
    private String cityCode;

    /**
     * 职位编码
     */
    private String positionCode;
}
