package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName ModifyUserAvatarDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/30 15:06
 * @Version 1.0
 */
@Data
public class ModifyUserAvatarDto {

    /**
     * 头像
     */
    @NotBlank(message = "头像不能为空")
    private String avatar;
}
