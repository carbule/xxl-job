package com.korant.youya.workplace.pojo.vo.position;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 职位等级 VO
 * </p>
 *
 * @author zhouzhifeng
 * @since 2024/3/19 15:30
 */
@Data
@Accessors(chain = true)
public class PositionClassLevelVO {

    /**
     * 职业群名称
     */
    private String organizationLevel;

    /**
     * 职业等级列表
     */
    private List<ClassLevel> classLevels = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    public static class ClassLevel {

        /**
         * 职业等级名称
         */
        private String classLevel;

        /**
         * 职位编码
         */
        private String positionCode;

        /**
         * 职位名称
         */
        private String positionName;

        /**
         * 职位描述
         */
        private String description;
    }
}
