package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.EnterpriseWalletAccount;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 企业钱包账户表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-02-22
 */
public interface EnterpriseWalletAccountMapper extends BaseMapper<EnterpriseWalletAccount> {

    /**
     * 根据企业id查询钱包id
     *
     * @param enterpriseId
     * @return
     */
    Long queryWalletIdByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}
