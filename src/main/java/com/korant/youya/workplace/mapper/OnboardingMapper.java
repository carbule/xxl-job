package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.po.Onboarding;
import com.korant.youya.workplace.pojo.vo.onboarding.OnboardingVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 入职记录表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface OnboardingMapper extends BaseMapper<Onboarding> {

    /**
     * 查询入职邀请列表
     *
     * @param listDto
     * @return
     */
    List<OnboardingVo> queryList(@Param("listDto") OnboardingQueryListDto listDto);
}
