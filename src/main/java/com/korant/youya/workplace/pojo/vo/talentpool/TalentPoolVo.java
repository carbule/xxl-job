package com.korant.youya.workplace.pojo.vo.talentpool;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName TalentPoolVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/8 14:30
 * @Version 1.0
 */
@Data
public class TalentPoolVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 申请人头像
     */
    private String applicantAvatar;

    /**
     * 申请人姓氏
     */
    private String applicantLastName;

    /**
     * 申请人名
     */
    private String applicantFirstName;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 推荐人头像
     */
    private String refereeAvatar;

    /**
     * 推荐人姓氏
     */
    private String refereeLastName;

    /**
     * 推荐人名
     */
    private String refereeFirstName;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 推荐时间
     */
    private LocalDateTime recommendedTime;
}
