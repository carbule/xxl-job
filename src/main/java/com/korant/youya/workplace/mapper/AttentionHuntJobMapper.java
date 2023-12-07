package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.attentionhuntjob.AttentionHuntJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.AttentionHuntJob;
import com.korant.youya.workplace.pojo.vo.attentionhuntjob.AttentionHuntJobPersonalVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 关注求职表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface AttentionHuntJobMapper extends BaseMapper<AttentionHuntJob> {

    /**
     * 查询用户求职关注列表
     *
     * @param userId
     * @param personalListDto
     * @return
     */
    List<AttentionHuntJobPersonalVo> queryPersonalList(@Param("userId") Long userId, @Param("personalListDto") AttentionHuntJobQueryPersonalListDto personalListDto);
}
