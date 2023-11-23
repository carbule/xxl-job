package com.korant.youya.workplace.pojo.vo.userblockedenterprise;

import lombok.Data;

import java.util.List;

/**
 * @ClassName PersonalBlockedEnterpriseVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/23 15:40
 * @Version 1.0
 */
@Data
public class PersonalBlockedEnterpriseVo {

    /**
     * 是否屏蔽当前公司
     */
    private Integer isBlockedCurrentEnterprise;

    /**
     * 
     */
    private List<BlockedEnterprise> blockedEnterprises;
}
