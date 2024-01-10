package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.internalrecommend.InternalRecommendQueryListDto;
import com.korant.youya.workplace.pojo.po.InternalRecommend;
import com.korant.youya.workplace.pojo.vo.internalrecommend.InternalRecommendDetailVo;
import com.korant.youya.workplace.pojo.vo.internalrecommend.InternalRecommendVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 内部推荐表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface InternalRecommendMapper extends BaseMapper<InternalRecommend> {

    /**
     * 查询用户被推荐职位数量
     *
     * @param userId
     * @return
     */
    int queryListCount(@Param("userId") Long userId);

    /**
     * 查询用户被推荐职位列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    List<InternalRecommendVo> queryList(@Param("userId") Long userId, @Param("listDto") InternalRecommendQueryListDto listDto);

    /**
     * 查询用户被推荐职位详情
     *
     * @param id
     * @return
     */
    InternalRecommendDetailVo detail(@Param("id") Long id);

    /**
     * 根据招聘流程实例id查找hr
     *
     * @param recruitProcessInstanceId
     * @return
     */
    Long selectHRByInstanceId(@Param("instanceId") Long recruitProcessInstanceId);

    /**
     * 根据招聘流程实例id查找候选人
     *
     * @param recruitProcessInstanceId
     * @return
     */
    Long selectApplicantByInstanceId(@Param("instanceId") Long recruitProcessInstanceId);
}
