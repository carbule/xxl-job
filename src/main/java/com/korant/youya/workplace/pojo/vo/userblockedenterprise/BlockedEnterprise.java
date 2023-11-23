package com.korant.youya.workplace.pojo.vo.userblockedenterprise;

import lombok.Data;

/**
 * @ClassName BlockedEnterprise
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/23 15:57
 * @Version 1.0
 */
@Data
public class BlockedEnterprise {



    /**
     * 屏蔽id
     */
    private Long blockId;

    /**
     * 企业名称
     */
    private String enterpriseName;
}
