package com.korant.youya.workplace.pojo.dto.job;

import com.korant.youya.workplace.pojo.PageParam;
import lombok.Data;

/**
 * @ClassName JobQueryHomePageListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/4 11:51
 * @Version 1.0
 */
@Data
public class JobQueryHomePageListDto extends PageParam {

    /**
     * 市级编码
     */
    private String cityCode;

    /**
     * 职位编码
     */
    private String positionCode;
}
