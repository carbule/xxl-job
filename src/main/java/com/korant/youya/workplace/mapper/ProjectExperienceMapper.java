package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.ProjectExperience;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceListVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 项目经验表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface ProjectExperienceMapper extends BaseMapper<ProjectExperience> {

    /**
     * @Author Duan-zhixiao
     * @Description 查询项目经验信息列表总数
     * @Date 16:25 2023/11/16
     * @Param
     * @return
     **/
    Long queryCountByUserId(@Param("userId") Long userId);

    /**
     * @Author Duan-zhixiao
     * @Description 查询项目经验信息列表
     * @Date 16:25 2023/11/16
     * @Param
     * @return
     **/
    List<ProjectExperienceListVo> queryProjectExperienceListByUserId(@Param("userId") Long userId, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);

    /**
     * @Author Duan-zhixiao
     * @Description 查询项目经验信息详情
     * @Date 16:34 2023/11/16
     * @Param
     * @return
     **/
    WorkExperienceDetailVo detail(@Param("id") Long id);
}
