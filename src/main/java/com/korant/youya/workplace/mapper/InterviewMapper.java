package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.po.Interview;
import com.korant.youya.workplace.pojo.vo.interview.InterviewVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 面试记录表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface InterviewMapper extends BaseMapper<Interview> {

    /**
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    List<InterviewVo> queryList(@Param("listDto") InterviewQueryListDto listDto);
}
