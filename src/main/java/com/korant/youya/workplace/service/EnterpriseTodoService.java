package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.enterprisetodo.EnterpriseTodoCreateDto;
import com.korant.youya.workplace.pojo.dto.enterprisetodo.EnterpriseTodoListDto;
import com.korant.youya.workplace.pojo.po.EnterpriseTodo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseEmployeeListVo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseTodoDetailVo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseTodoListVo;

/**
 * <p>
 * 企业代办事项表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EnterpriseTodoService extends IService<EnterpriseTodo> {

    /**
     * 创建加入公司申请
     *
     * @param
     * @return
     */
    void create(EnterpriseTodoCreateDto enterpriseTodoCreateDto);

    /**
     * 获取当前用户的加入公司申请
     *
     * @return
     */
    EnterpriseTodoDetailVo getEnterpriseTodoByUser();

    /**
     * 管理员查看加入公司申请列表
     *
     * @param
     * @return
     */
    Page<EnterpriseTodoListVo> queryApprovalList(EnterpriseTodoListDto enterpriseTodoListDto);

    /**
     * 管理员查看成员列表
     *
     * @param
     * @return
     */
    Page<EnterpriseEmployeeListVo> queryEmployeeList(EnterpriseTodoListDto enterpriseTodoListDto);

    /**
     * 同意用户的加入公司申请
     *
     * @return
     */
    void pass(Long id);

    /**
     * 拒绝用户的加入公司申请
     *
     * @return
     */
    void refuse(Long id);

}
