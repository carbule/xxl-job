package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.Role;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * @Author Duan-zhixiao
     * @Description 获取用户在企业中的角色类型
     * @Date 17:22 2023/11/20
     * @Param
     * @return
     **/
    Long getRoleByUserAndEnterprise(@Param("userId") Long userId, @Param("id") Long id);
}
