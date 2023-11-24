package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.EnterpriseTodo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseEmployeeListVo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseTodoDetailVo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseTodoListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 企业代办事项表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EnterpriseTodoMapper extends BaseMapper<EnterpriseTodo> {

    /**
     * 获取当前用户的加入公司申请
     *
     * @return
     */
    EnterpriseTodoDetailVo getEnterpriseTodoByUser(Long userId);

    /**
     * 管理员查看加入公司申请列表
     *
     * @param
     * @return
     */
    List<EnterpriseTodoListVo> queryApprovalList(@Param("id") Long id, @Param("status") int status, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);

    /**
     * 管理员查看成员列表
     *
     * @param
     * @return
     */
    List<EnterpriseEmployeeListVo> queryEmployeeList(@Param("id") Long id, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);
}
