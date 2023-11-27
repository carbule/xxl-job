package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.UserBlockedEnterpriseMapper;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.PageData;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.QueryEnterpriseByNameDto;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.QueryPersonalBlockedEnterpriseDto;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.UserBlockedEnterpriseCreateDto;
import com.korant.youya.workplace.pojo.po.UserBlockedEnterprise;
import com.korant.youya.workplace.pojo.vo.userblockedenterprise.PersonalBlockedEnterpriseVo;
import com.korant.youya.workplace.pojo.vo.userblockedenterprise.QueryEnterpriseByNameVo;
import com.korant.youya.workplace.service.UserBlockedEnterpriseService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户屏蔽企业信息表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-23
 */
@Service
public class UserBlockedEnterpriseServiceImpl extends ServiceImpl<UserBlockedEnterpriseMapper, UserBlockedEnterprise> implements UserBlockedEnterpriseService {

    @Resource
    private UserBlockedEnterpriseMapper userBlockedEnterpriseMapper;

    /**
     * 查询个人屏蔽的企业
     *
     * @param personalBlockedEnterpriseDto
     * @return
     */
    @Override
    public PageData<PersonalBlockedEnterpriseVo> queryPersonalBlockedEnterprise(QueryPersonalBlockedEnterpriseDto personalBlockedEnterpriseDto) {
        LoginUser userInfo = SpringSecurityUtil.getUserInfo();
        Long userId = userInfo.getId();
        Long enterpriseId = userInfo.getEnterpriseId();
        int pageNumber = personalBlockedEnterpriseDto.getPageNumber();
        int pageSize = personalBlockedEnterpriseDto.getPageSize();
        Long count = userBlockedEnterpriseMapper.selectCount(new LambdaQueryWrapper<UserBlockedEnterprise>().eq(UserBlockedEnterprise::getUid, userId).eq(UserBlockedEnterprise::getIsDelete, 0));
        PersonalBlockedEnterpriseVo personalBlockedEnterpriseVo = userBlockedEnterpriseMapper.queryPersonalBlockedEnterprise(userId, enterpriseId, personalBlockedEnterpriseDto);
        PageData<PersonalBlockedEnterpriseVo> pageData = new PageData<>();
        pageData.setTotal(count).setData(personalBlockedEnterpriseVo).setCurrent(pageNumber).setSize(pageSize);
        return pageData;
    }

    /**
     * 根据企业名称查询企业
     *
     * @param queryEnterpriseByNameDto
     * @return
     */
    @Override
    public QueryEnterpriseByNameVo queryEnterpriseByName(QueryEnterpriseByNameDto queryEnterpriseByNameDto) {
        return userBlockedEnterpriseMapper.queryEnterpriseByName(queryEnterpriseByNameDto);
    }

    /**
     * 屏蔽当前所在企业
     */
    @Override
    public void blockCurrentEnterprise() {
        LoginUser user = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = user.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("暂未关联企业");
        Long userId = user.getId();
        UserBlockedEnterprise userBlockedEnterprise = userBlockedEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserBlockedEnterprise>().eq(UserBlockedEnterprise::getUid, userId).eq(UserBlockedEnterprise::getEnterpriseId, enterpriseId).eq(UserBlockedEnterprise::getIsDelete, 0));
        if (null == userBlockedEnterprise) {
            UserBlockedEnterprise blockedEnterprise = new UserBlockedEnterprise();
            blockedEnterprise.setUid(userId).setEnterpriseId(enterpriseId);
            userBlockedEnterpriseMapper.insert(blockedEnterprise);
        } else {
            userBlockedEnterprise.setIsDelete(1);
            userBlockedEnterpriseMapper.updateById(userBlockedEnterprise);
        }
    }

    /**
     * 创建屏蔽企业
     *
     * @param userBlockedEnterpriseCreateDto
     */
    @Override
    public void create(UserBlockedEnterpriseCreateDto userBlockedEnterpriseCreateDto) {
        Long userId = SpringSecurityUtil.getUserId();
        Long enterpriseId = userBlockedEnterpriseCreateDto.getEnterpriseId();
        boolean exists = userBlockedEnterpriseMapper.exists(new LambdaQueryWrapper<UserBlockedEnterprise>().eq(UserBlockedEnterprise::getUid, userId).eq(UserBlockedEnterprise::getEnterpriseId, enterpriseId).eq(UserBlockedEnterprise::getIsDelete, 0));
        if (exists) throw new YouyaException("该企业已屏蔽，请勿重复添加");
        UserBlockedEnterprise userBlockedEnterprise = new UserBlockedEnterprise();
        userBlockedEnterprise.setUid(userId);
        userBlockedEnterprise.setEnterpriseId(enterpriseId);
        userBlockedEnterpriseMapper.insert(userBlockedEnterprise);
    }

    /**
     * 删除屏蔽企业
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        UserBlockedEnterprise userBlockedEnterprise = userBlockedEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserBlockedEnterprise>().eq(UserBlockedEnterprise::getId, id).eq(UserBlockedEnterprise::getIsDelete, 0));
        if (null == userBlockedEnterprise) throw new YouyaException("企业屏蔽信息不存在");
        userBlockedEnterprise.setIsDelete(1);
        userBlockedEnterpriseMapper.updateById(userBlockedEnterprise);
    }
}
