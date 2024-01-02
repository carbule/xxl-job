package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.applyjob.ApplyJobQueryListDto;
import com.korant.youya.workplace.pojo.po.ApplyJob;
import com.korant.youya.workplace.pojo.vo.applyjob.ApplyJobVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 职位申请表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface ApplyJobMapper extends BaseMapper<ApplyJob> {

    /**
     * 查询用户已申请职位列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    List<ApplyJobVo> queryList(@Param("userId") Long userId, @Param("listDto") ApplyJobQueryListDto listDto);
}
