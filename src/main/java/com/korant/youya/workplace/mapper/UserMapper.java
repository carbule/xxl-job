package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.user.ResumeContactModifyDto;
import com.korant.youya.workplace.pojo.dto.user.ResumePersonModifyDto;
import com.korant.youya.workplace.pojo.po.User;
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

    /**
     * 根据id查询登录用户
     *
     * @param id
     * @return
     */
    LoginUser selectUserToLoginById(@Param("id") Long id);

    /**
     * 根据手机号查询登录用户
     *
     * @param phoneNumber
     * @return
     */
    LoginUser selectUserToLoginByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    Integer resumePersonModify(@Param("userId") Long userId, @Param("resumePersonModifyDto") ResumePersonModifyDto resumePersonModifyDto);

    Integer modifyResumeContactDetail(@Param("userId") Long userId, @Param("resumeContactModifyDto") ResumeContactModifyDto resumeContactModifyDto);
}
