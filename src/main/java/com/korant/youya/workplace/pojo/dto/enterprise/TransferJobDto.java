package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName TransferJobDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/27 15:54
 * @Version 1.0
 */
@Data
public class TransferJobDto {

    /**
     * 职位id
     */
    @NotNull(message = "职位id不能为空")
    private Long jobId;

    /**
     * 转让人id
     */
    @NotNull(message = "转让人id不能为空")
    private Long userId;
}
