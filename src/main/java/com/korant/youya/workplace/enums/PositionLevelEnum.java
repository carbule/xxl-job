package com.korant.youya.workplace.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 职业层级
 * </p>
 *
 * @author zhouzhifeng
 * @since 2024/3/19 16:19
 */
@AllArgsConstructor
@Getter
public enum PositionLevelEnum {

    /**
     * 所属行业
     */
    LEVEL_1(1),

    /**
     * 职业类型
     */
    LEVEL_2(2),

    /**
     * 专业领域
     */
    LEVEL_3(3),

    /**
     * 职位
     */
    LEVEL_4(4);

    private final int value;
}
