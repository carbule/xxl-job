package com.korant.youya.workplace.pojo.vo.internalrecommend;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName InternalRecommendInterviewVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/11 15:09
 * @Version 1.0
 */
@Data
public class InternalRecommendInterviewVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 面试方式
     */
    private Integer approach;

    /**
     * 面试时间
     */
    private LocalDateTime interTime;

    /**
     * 备注
     */
    private String note;

    /**
     * 接受状态 0-待接受 1-拒绝 2-接受
     */
    private Integer acceptanceStatus;

    /**
     * 完成状态 0-未完成 1-已取消 2-已完成
     */
    private Integer completionStatus;

    /**
     * hr头像
     */
    private String avatar;

    /**
     * hr姓氏
     */
    private String lastName;

    /**
     * hr名称
     */
    private String firstName;
}