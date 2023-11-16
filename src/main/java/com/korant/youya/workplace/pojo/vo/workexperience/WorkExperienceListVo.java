package com.korant.youya.workplace.pojo.vo.workexperience;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 15:49
 * @PackageName:com.korant.youya.workplace.pojo.vo.workexperience
 * @ClassName: WorkExperienceListVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class WorkExperienceListVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 工作内容
     */
    private String content;

    /**
     * 工作业绩
     */
    private String performance;

}
