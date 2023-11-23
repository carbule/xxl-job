package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.userprivacy.UserPrivacyModifyDto;
import com.korant.youya.workplace.pojo.po.UserPrivacy;
import com.korant.youya.workplace.pojo.vo.userprivacy.UserPersonalInfoPrivacyVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户个人信息隐私设置 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-22
 */
public interface UserPrivacyMapper extends BaseMapper<UserPrivacy> {

    /**
     * 查询个人信息隐私设置
     *
     * @param userId
     * @return
     */
    UserPersonalInfoPrivacyVo queryPersonalInfoPrivacy(@Param("userId") Long userId);

    /**
     * 修改个人信息隐私设置
     *
     * @param modifyDto
     * @return
     */
    int modify(@Param("modifyDto") UserPrivacyModifyDto modifyDto);
}
