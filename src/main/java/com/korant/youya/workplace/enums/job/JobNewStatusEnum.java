package com.korant.youya.workplace.enums.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhouzhifeng
 * @since 2024/4/1 13:32
 */
@AllArgsConstructor
@Getter
public enum JobNewStatusEnum {

    /**
     * 待审核
     */
    PENDING_APPROVAL,

    /**
     * 审核通过
     */
    APPROVED,

    /**
     * 审核不通过
     */
    REJECTED,

    /**
     * 已关闭
     */
    CLOSED;
}
