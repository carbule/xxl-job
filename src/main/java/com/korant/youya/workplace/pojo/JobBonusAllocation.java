package com.korant.youya.workplace.pojo;

import com.korant.youya.workplace.pojo.po.ApplyJob;
import com.korant.youya.workplace.pojo.po.InternalRecommend;
import com.korant.youya.workplace.pojo.po.Job;
import lombok.Data;

/**
 * @ClassName JobBonusAllocation
 * @Description
 * @Author chenyiqiang
 * @Date 2024/4/1 20:17
 * @Version 1.0
 */
@Data
public class JobBonusAllocation {

    /**
     * 求职
     */
    private Job job;

    /**
     * 任务类型 1- 候选人 2-人才库
     */
    private Integer taskType;

    /**
     * 职位申请
     */
    private ApplyJob applyJob;

    /**
     * 职位内推
     */
    private InternalRecommend internalRecommend;

    /**
     * 流程环节
     */
    private Integer processStep;
}
