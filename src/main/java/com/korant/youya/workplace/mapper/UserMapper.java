package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.user.ModifyUserContactInfoDto;
import com.korant.youya.workplace.pojo.dto.user.ModifyUserPersonalBasicInfoDto;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.pojo.vo.user.ResumeDetailVo;
import com.korant.youya.workplace.pojo.vo.user.ResumePreviewVo;
import com.korant.youya.workplace.pojo.vo.user.UserContactInfoVo;
import com.korant.youya.workplace.pojo.vo.user.UserPersonalBasicInfoVo;
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

    /**
     * 查询个人基本信息
     *
     * @param userId
     * @return
     */
    UserPersonalBasicInfoVo queryPersonalBasicInfo(@Param("userId") Long userId);

    /**
     * 修改个人基本信息
     *
     * @param userId
     * @param modifyDto
     * @return
     */
    int modifyUserPersonalBasicInfo(@Param("userId") Long userId, @Param("modifyDto") ModifyUserPersonalBasicInfoDto modifyDto);

    /**
     * 查询用户联系方式
     *
     * @param userId
     * @return
     */
    UserContactInfoVo queryUserContactInfo(@Param("userId") Long userId);

    /**
     * 修改用户联系方式
     *
     * @param userId
     * @param modifyDto
     * @return
     */
    int modifyUserContactInfo(@Param("userId") Long userId, @Param("modifyDto") ModifyUserContactInfoDto modifyDto);

    /**
     * 简历详情
     *
     * @param userId
     * @return
     */
    ResumeDetailVo resumeDetail(@Param("userId") Long userId);

    /**
     * 简历预览
     *
     * @param userId
     * @return
     */
    ResumePreviewVo resumePreview(@Param("userId") Long userId);
}
