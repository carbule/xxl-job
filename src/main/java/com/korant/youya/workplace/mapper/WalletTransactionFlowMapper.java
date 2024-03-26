package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.wallettransactionflow.QueryAccountTransactionFlowListDto;
import com.korant.youya.workplace.pojo.po.WalletTransactionFlow;
import com.korant.youya.workplace.pojo.vo.wallettransactionflow.AccountTransactionFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 友涯钱包账户交易流水表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-02-22
 */
public interface WalletTransactionFlowMapper extends BaseMapper<WalletTransactionFlow> {

    /**
     * 查询钱包账户交易流水
     *
     * @param walletAccountId
     * @param queryAccountTransactionFlowListDto
     * @return
     */
    List<AccountTransactionFlowVo> queryAccountTransactionFlow(@Param("walletAccountId") Long walletAccountId, @Param("queryAccountTransactionFlowListDto") QueryAccountTransactionFlowListDto queryAccountTransactionFlowListDto);
}
