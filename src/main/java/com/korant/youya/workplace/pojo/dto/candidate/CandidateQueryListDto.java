package com.korant.youya.workplace.pojo.dto.candidate;

import com.korant.youya.workplace.pojo.PageParam;
import lombok.Data;

/**
 * @ClassName CandidateQueryListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 16:41
 * @Version 1.0
 */
@Data
public class CandidateQueryListDto extends PageParam {

    /**
     * 职位编码
     */
    private String positionCode;
}
