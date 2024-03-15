package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.UserWalletAccount;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户钱包账户表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-02-22
 */
public interface UserWalletAccountMapper extends BaseMapper<UserWalletAccount> {

    /**
     * 根据用户id查询钱包账户id
     *
     * @param userId
     * @return
     */
    Long queryWalletIdByUserId(@Param("userId") Long userId);
}
