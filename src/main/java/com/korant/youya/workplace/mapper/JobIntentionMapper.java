package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.JobIntention;
import com.korant.youya.workplace.pojo.vo.jobIntention.JobIntentionVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 求职意向 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-15
 */
public interface JobIntentionMapper extends BaseMapper<JobIntention> {

    JobIntentionVo status(@Param("userId") Long userId);
}
