package com.korant.youya.workplace.pojo.dto.enterprise;

import com.korant.youya.workplace.pojo.PageParam;
import lombok.Data;

/**
 * @ClassName QueryTransferPersonnelListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/14 13:51
 * @Version 1.0
 */
@Data
public class QueryTransferPersonnelListDto extends PageParam {

    /**
     * hr姓名
     */
    private String name;
}