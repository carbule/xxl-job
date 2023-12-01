package com.korant.youya.workplace.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.enterprisetodo.EnterpriseTodoOperateStatus;
import com.korant.youya.workplace.enums.role.RoleEnum;
import com.korant.youya.workplace.enums.user.UserAuthenticationStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.enterprisetodo.EnterpriseTodoCreateDto;
import com.korant.youya.workplace.pojo.dto.enterprisetodo.EnterpriseTodoListDto;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByUserVo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseEmployeeListVo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseTodoDetailVo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseTodoListVo;
import com.korant.youya.workplace.service.EnterpriseTodoService;
import com.korant.youya.workplace.utils.RedisUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 企业代办事项表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class EnterpriseTodoServiceImpl extends ServiceImpl<EnterpriseTodoMapper, EnterpriseTodo> implements EnterpriseTodoService {

    @Resource
    private  EnterpriseTodoMapper enterpriseTodoMapper;

    @Resource
    private EnterpriseMapper enterpriseMapper;

    @Resource
    private UserEnterpriseMapper userEnterpriseMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RedisUtil redisUtil;


    /**
     * 创建加入公司申请
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(EnterpriseTodoCreateDto enterpriseTodoCreateDto) {

        Long userId = SpringSecurityUtil.getUserId();
        User user = userMapper.selectById(userId);
        if (user.getAuthenticationStatus() ==  UserAuthenticationStatusEnum.NOT_CERTIFIED.getStatus())
            throw new YouyaException("请先进行实名认证！");
        Long role = roleMapper.getRoleByUserAndEnterprise(userId, enterpriseTodoCreateDto.getEnterpriseId());
        if (role != null && role == 2) throw new YouyaException("管理员无法提交加入申请！");
        boolean enterpriseExists = enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseTodoCreateDto.getEnterpriseId()).eq(Enterprise::getIsDelete, 0));
        if (!enterpriseExists) throw new YouyaException("您申请的企业不存在！");
        boolean exists = enterpriseTodoMapper.exists(new LambdaQueryWrapper<EnterpriseTodo>().eq(EnterpriseTodo::getUid, userId).eq(EnterpriseTodo::getIsDelete, 0));
        if (exists) throw new YouyaException("您已提交过加入申请，请勿重复提交！");
        boolean userEnterpriseExists = userEnterpriseMapper.exists(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getEnterpriseId, enterpriseTodoCreateDto.getEnterpriseId()).eq(UserEnterprise::getIsDelete, 0));
        if (userEnterpriseExists) throw new YouyaException("您已加入过公司，无法再次加入公司！");
        EnterpriseTodo enterpriseTodo = new EnterpriseTodo();
        BeanUtils.copyProperties(enterpriseTodoCreateDto, enterpriseTodo);
        enterpriseTodo.setUid(userId);
        enterpriseTodo.setOperate(EnterpriseTodoOperateStatus.OPERATE_IN_PROGRESS.getStatus());
        enterpriseTodoMapper.insert(enterpriseTodo);

    }

    /**
     * 获取当前用户的加入公司申请
     *
     * @return
     */
    @Override
    public EnterpriseTodoDetailVo getEnterpriseTodoByUser() {

        Long userId = SpringSecurityUtil.getUserId();
        EnterpriseTodoDetailVo enterpriseTodoDetailVo = enterpriseTodoMapper.getEnterpriseTodoByUser(userId);
        //是否是移交过管理员的hr
        if (enterpriseTodoDetailVo == null){
            UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>()
                    .eq(UserEnterprise::getUid, userId)
                    .eq(UserEnterprise::getIsDelete, 0));
            if (userEnterprise != null){
                //获取当前公司信息及管理员信息
                enterpriseTodoDetailVo = enterpriseMapper.getAdminAndEnterpriseInfo(userEnterprise.getEnterpriseId());
            }
        }
        return enterpriseTodoDetailVo;

    }

    /**
     * 管理员查看加入公司申请列表
     *
     * @param
     * @return
     */
    @Override
    public Page<EnterpriseTodoListVo> queryApprovalList(EnterpriseTodoListDto enterpriseTodoListDto) {

        Long userId = SpringSecurityUtil.getUserId();
        //获取当前用户属于哪个公司
        EnterpriseInfoByUserVo enterprise = enterpriseMapper.queryEnterpriseInfoByUser(userId);
        if (enterprise == null) throw new YouyaException("您当前未创建公司！");

        int pageNumber = enterpriseTodoListDto.getPageNumber();
        int pageSize = enterpriseTodoListDto.getPageSize();
        Long count = enterpriseTodoMapper.selectCount(new LambdaQueryWrapper<EnterpriseTodo>()
                .eq(EnterpriseTodo::getEnterpriseId, enterprise.getId())
                .eq(EnterpriseTodo::getIsDelete, 0)
                .eq(EnterpriseTodo::getOperate, EnterpriseTodoOperateStatus.OPERATE_IN_PROGRESS.getStatus()));
        List<EnterpriseTodoListVo> list = enterpriseTodoMapper.queryApprovalList(enterprise.getId(), EnterpriseTodoOperateStatus.OPERATE_IN_PROGRESS.getStatus(), pageNumber, pageSize);
        Page<EnterpriseTodoListVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;

    }

    /**
     * 管理员查看成员列表
     *
     * @param
     * @return
     */
    @Override
    public Page<EnterpriseEmployeeListVo> queryEmployeeList(EnterpriseTodoListDto enterpriseTodoListDto) {

        Long userId = SpringSecurityUtil.getUserId();
        //获取当前用户属于哪个公司
        EnterpriseInfoByUserVo enterprise = enterpriseMapper.queryEnterpriseInfoByUser(userId);
        if (enterprise == null) throw new YouyaException("您当前未创建公司！");

        int pageNumber = enterpriseTodoListDto.getPageNumber();
        int pageSize = enterpriseTodoListDto.getPageSize();
        Long count = enterpriseTodoMapper.selectCount(new LambdaQueryWrapper<EnterpriseTodo>()
                .eq(EnterpriseTodo::getEnterpriseId, enterprise.getId())
                .eq(EnterpriseTodo::getIsDelete, 0)
                .eq(EnterpriseTodo::getEventType, 1)
                .eq(EnterpriseTodo::getOperate, EnterpriseTodoOperateStatus.OPERATE_SUCCESS.getStatus()));
        List<EnterpriseEmployeeListVo> list = enterpriseTodoMapper.queryEmployeeList(enterprise.getId(), pageNumber, pageSize);
        Page<EnterpriseEmployeeListVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;

    }

    /**
     * 同意用户的加入公司申请
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(Long id) {

        EnterpriseTodo enterpriseTodo = enterpriseTodoMapper.selectOne(new LambdaQueryWrapper<EnterpriseTodo>().eq(EnterpriseTodo::getId, id).eq(EnterpriseTodo::getIsDelete, 0));
        if (enterpriseTodo == null) throw  new YouyaException("加入申请不存在！");
        enterpriseTodoMapper.update(null,
                new LambdaUpdateWrapper<EnterpriseTodo>()
                        .eq(EnterpriseTodo::getId, id)
                        .set(EnterpriseTodo::getOperate, EnterpriseTodoOperateStatus.OPERATE_SUCCESS.getStatus()));

        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, enterpriseTodo.getUid());
        LoginUser loginUser = JSONObject.parseObject(String.valueOf(redisUtil.get(cacheKey)), LoginUser.class);

        UserEnterprise userEnterprise = new UserEnterprise();
        userEnterprise.setEnterpriseId(enterpriseTodo.getEnterpriseId());
        userEnterprise.setUid(enterpriseTodo.getUid());
        userEnterpriseMapper.insert(userEnterprise);
        loginUser.setEnterpriseId(enterpriseTodo.getEnterpriseId());

        //TODO 角色权限未知
        //如果是hr申请 则给他分配hr角色
        if (enterpriseTodo.getEventType() == 1){
            UserRole userRole = new UserRole();
            userRole.setUid(enterpriseTodo.getUid());
            userRole.setRid(1L);
            userRoleMapper.insert(userRole);
            loginUser.setRole(RoleEnum.HR.getRole());
        }

        //更新用户缓存
        redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));

    }

    /**
     * 拒绝用户的加入公司申请
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refuse(Long id) {

        boolean exists = enterpriseTodoMapper.exists(new LambdaQueryWrapper<EnterpriseTodo>().eq(EnterpriseTodo::getId, id).eq(EnterpriseTodo::getIsDelete, 0));
        if (!exists) throw  new YouyaException("加入申请不存在！");
        enterpriseTodoMapper.update(null,
                new LambdaUpdateWrapper<EnterpriseTodo>()
                        .eq(EnterpriseTodo::getId, id)
                        .set(EnterpriseTodo::getOperate, EnterpriseTodoOperateStatus.OPERATE_FAIL.getStatus())
                        .set(EnterpriseTodo::getIsDelete, 1));

    }

}
