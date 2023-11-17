package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.WorkExperience;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceListVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePreviewVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工作履历表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface WorkExperienceMapper extends BaseMapper<WorkExperience> {

    /**
     * @Author Duan-zhixiao
     * @Description 查询工作履历信息列表
     * @Date 15:59 2023/11/16
     * @Param
     * @return
     **/
    List<WorkExperienceListVo> queryWorkExperienceListByUserId(@Param("userId") Long userId, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);

    /**
     * @Author Duan-zhixiao
     * @Description 查询工作履历信息详情
     * @Date 16:07 2023/11/16
     * @Param
     * @return
     **/
    WorkExperienceDetailVo detail(@Param("id") Long id);

    /**
     * @Author Duan-zhixiao
     * @Description 查询工作履历信息及其项目经验列表
     * @Date 15:31 2023/11/17
     * @Param
     * @return
     **/
    List<WorkExperiencePreviewVo> selectWorkExperienceAndProjectExperienceByUserId(@Param("userId") Long userId);
}
