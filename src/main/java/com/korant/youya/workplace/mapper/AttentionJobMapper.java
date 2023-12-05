package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.attentionjob.AttentionJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.AttentionJob;
import com.korant.youya.workplace.pojo.vo.attentionjob.AttentionJobPersonalListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 关注职位表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface AttentionJobMapper extends BaseMapper<AttentionJob> {

    /**
     * 查询用户职位关注列表
     *
     * @param userId
     * @param personalListDto
     * @return
     */
    List<AttentionJobPersonalListVo> queryPersonalList(@Param("userId") Long userId, @Param("personalListDto") AttentionJobQueryPersonalListDto personalListDto);
}
