package com.korant.youya.workplace.pojo.vo.candidate;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName CandidateVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 16:42
 * @Version 1.0
 */
@Data
public class CandidateVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 申请人id
     */
    private Long applicant;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名字
     */
    private String firstName;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 招聘流程实列id
     */
    private Long recruitProcessInstanceId;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 最低薪资
     */
    private Integer minSalary;

    /**
     * 最高薪资
     */
    private Integer maxSalary;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;
}
