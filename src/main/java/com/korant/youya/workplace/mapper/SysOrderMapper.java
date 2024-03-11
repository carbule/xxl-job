package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.sysorder.QueryOrderListDto;
import com.korant.youya.workplace.pojo.po.SysOrder;
import com.korant.youya.workplace.pojo.vo.sysorder.SysOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统订单表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-02-22
 */
public interface SysOrderMapper extends BaseMapper<SysOrder> {

    /**
     * 查询订单列表
     *
     * @param walletAccountId
     * @param queryOrderListDto
     * @return
     */
    List<SysOrderVo> queryOrderList(@Param("walletAccountId") Long walletAccountId, @Param("queryOrderListDto") QueryOrderListDto queryOrderListDto);
}
