package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.po.Confirmation;
import com.korant.youya.workplace.pojo.vo.confirmation.ConfirmationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 转正记录表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface ConfirmationMapper extends BaseMapper<Confirmation> {

    /**
     * 查询转正邀请列表
     *
     * @param listDto
     * @return
     */
    List<ConfirmationVo> queryList(@Param("listDto") ConfirmationQueryListDto listDto);
}
