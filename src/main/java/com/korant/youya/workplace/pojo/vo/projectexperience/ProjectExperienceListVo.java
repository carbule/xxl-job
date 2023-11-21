package com.korant.youya.workplace.pojo.vo.projectexperience;

import com.korant.youya.workplace.pojo.PageParam;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 16:11
 * @PackageName:com.korant.youya.workplace.pojo.vo.projectexperience
 * @ClassName: ProjectExperienceListVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ProjectExperienceListVo extends PageParam {

    /**
     * 主键
     */
    private Long id;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目角色
     */
    private String projectRole;

    /**
     * 项目内容
     */
    private String projectContent;

}
