package com.korant.youya.workplace.mapper;

import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName CandidateMapper
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 17:05
 * @Version 1.0
 */
public interface CandidateMapper {

    /**
     * 查询候选人数量
     *
     * @param userId
     * @param listDto
     * @return
     */
    int queryListCount(@Param("userId") Long userId, @Param("listDto") CandidateQueryListDto listDto);

    /**
     * 查询候选人列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    List<CandidateVo> queryList(@Param("userId") Long userId, @Param("listDto") CandidateQueryListDto listDto);
}
