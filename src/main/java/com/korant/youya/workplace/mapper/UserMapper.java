package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.pojo.vo.user.ResumePersonInfoVo;
import com.korant.youya.workplace.pojo.vo.user.ResumePersonPreviewVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 友涯用户表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface UserMapper extends BaseMapper<User> {

    LoginUser selectUserToLoginById(@Param("id") Long id);

    LoginUser selectUserToLoginByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    ResumePersonInfoVo resumePersonDetail(@Param("userId") Long userId);

    ResumePersonPreviewVo resumePersonPreview(@Param("userId") Long userId);
}
