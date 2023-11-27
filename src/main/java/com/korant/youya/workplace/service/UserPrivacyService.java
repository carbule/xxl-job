package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.userprivacy.UserPrivacyModifyDto;
import com.korant.youya.workplace.pojo.po.UserPrivacy;
import com.korant.youya.workplace.pojo.vo.userprivacy.UserPersonalInfoPrivacyVo;

import java.util.List;

/**
 * <p>
 * 用户个人信息隐私设置 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-22
 */
public interface UserPrivacyService extends IService<UserPrivacy> {

    /**
     * 查询个人信息隐私设置
     *
     * @return
     */
    UserPersonalInfoPrivacyVo queryPersonalInfoPrivacy();

    /**
     * 修改个人隐私
     *
     * @param modifyDto
     */
    void modify(UserPrivacyModifyDto modifyDto);

    /**
     * 查询名字隐私设置类型
     */
    List<?> queryNameVisibleType();

    /**
     * 查询其他隐私设置类型
     *
     * @return
     */
    List<?> queryOtherInfoVisibleType();
}
