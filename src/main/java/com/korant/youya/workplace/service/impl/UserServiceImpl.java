package com.korant.youya.workplace.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.user.UserAccountStatus;
import com.korant.youya.workplace.enums.user.UserAuthenticationStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.UserMapper;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.user.UserLoginByPasswordDto;
import com.korant.youya.workplace.pojo.dto.user.UserLoginBySMSVerificationCodeDto;
import com.korant.youya.workplace.pojo.dto.user.UserLoginByWechatCodeDto;
import com.korant.youya.workplace.pojo.dto.user.VerificationCodeDto;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.service.UserService;
import com.korant.youya.workplace.utils.HuaWeiUtil;
import com.korant.youya.workplace.utils.JwtUtil;
import com.korant.youya.workplace.utils.RedisUtil;
import com.korant.youya.workplace.utils.WeChatUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * <p>
 * 友涯用户表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    private static final String DEFAULT_AVATAR = "https://resources.youyai.cn/picture/avatar.jpg";

    /**
     * 微信登陆
     *
     * @param wechatCodeDto
     * @return
     */
    @Override
    public String loginByWechatCode(UserLoginByWechatCodeDto wechatCodeDto) {
        String code = wechatCodeDto.getCode();
        log.info("code:{}", code);
        String accessToken = WeChatUtil.getAccessToken();
        log.info("accessToken:{}", accessToken);
        if (StringUtils.isBlank(accessToken)) throw new YouyaException("accessToken为空");
        String phoneNumber = WeChatUtil.getPhoneNumber(accessToken, code);
        if (StringUtils.isBlank(phoneNumber)) throw new YouyaException("手机号为空");
        Object o = redisUtil.get(String.format(RedisConstant.YY_USER_ID, phoneNumber));
        if (null == o) {
            LoginUser loginUser = userMapper.selectUserToLoginByPhoneNumber(phoneNumber);
            //用户不存在则默认注册
            if (null == loginUser) {
                loginUser = register(phoneNumber);
            }
            Integer accountStatus = loginUser.getAccountStatus();
            if (UserAccountStatus.FROZEN.getStatus() == accountStatus) throw new YouyaException("账号已被冻结,详情请咨询客服");
            Long id = loginUser.getId();
            String token = JwtUtil.createToken(id);
            String key = String.format(RedisConstant.YY_USER_TOKEN, id);
            //todo token暂时不设置过期时间
//        redisUtil.set(key, token, 7200);
            redisUtil.set(key, token);
            String idKey = String.format(RedisConstant.YY_USER_ID, phoneNumber);
            redisUtil.set(idKey, id.toString());
            String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
            redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));
            return token;
        } else {
            String userId = o.toString();
            String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
            Object cacheObj = redisUtil.get(cacheKey);
            if (null == cacheObj) {
                LoginUser loginUser = userMapper.selectUserToLoginByPhoneNumber(phoneNumber);
                //用户不存在则默认注册
                if (null == loginUser) {
                    loginUser = register(phoneNumber);
                }
                Integer accountStatus = loginUser.getAccountStatus();
                if (UserAccountStatus.FROZEN.getStatus() == accountStatus) throw new YouyaException("账号已被冻结,详情请咨询客服");
                Long id = loginUser.getId();
                String token = JwtUtil.createToken(id);
                String key = String.format(RedisConstant.YY_USER_TOKEN, id);
                //todo token暂时不设置过期时间
//        redisUtil.set(key, token, 7200);
                redisUtil.set(key, token);
                redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));
                return token;
            } else {
                LoginUser userCache = JSONObject.parseObject(cacheObj.toString(), LoginUser.class);
                Integer accountStatus = userCache.getAccountStatus();
                if (UserAccountStatus.FROZEN.getStatus() == accountStatus) throw new YouyaException("账号已被冻结,详情请咨询客服");
                String token = JwtUtil.createToken(Long.valueOf(userId));
                String key = String.format(RedisConstant.YY_USER_TOKEN, userId);
                //todo token暂时不设置过期时间
//        redisUtil.set(key, token, 7200);
                redisUtil.set(key, token);
                return token;
            }
        }
    }

    /**
     * 短信验证码登陆
     *
     * @param smsVerificationCodeDto
     * @return
     */
    @Override
    public String loginBySMSVerificationCode(UserLoginBySMSVerificationCodeDto smsVerificationCodeDto) {
        String phoneNumber = smsVerificationCodeDto.getPhoneNumber();
        String code = smsVerificationCodeDto.getCode();
        String codeKey = String.format(RedisConstant.YY_PHONE_CODE, phoneNumber);
        Object codeCache = redisUtil.get(codeKey);
        if (null == codeCache) throw new YouyaException("验证码已过期");
        String cacheCode = String.valueOf(codeCache);
        if (cacheCode.equals(code)) {
            Object o = redisUtil.get(String.format(RedisConstant.YY_USER_ID, phoneNumber));
            if (null == o) {
                LoginUser loginUser = userMapper.selectUserToLoginByPhoneNumber(phoneNumber);
                //用户不存在则默认注册
                if (null == loginUser) {
                    loginUser = register(phoneNumber);
                }
                Integer accountStatus = loginUser.getAccountStatus();
                if (UserAccountStatus.FROZEN.getStatus() == accountStatus) throw new YouyaException("账号已被冻结,详情请咨询客服");
                Long id = loginUser.getId();
                String token = JwtUtil.createToken(id);
                String key = String.format(RedisConstant.YY_USER_TOKEN, id);
                //todo token暂时不设置过期时间
//        redisUtil.set(key, token, 7200);
                redisUtil.set(key, token);
                String idKey = String.format(RedisConstant.YY_USER_ID, phoneNumber);
                redisUtil.set(idKey, id.toString());
                String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
                redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));
                return token;
            } else {
                String userId = o.toString();
                String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
                Object cacheObj = redisUtil.get(cacheKey);
                if (null == cacheObj) {
                    LoginUser loginUser = userMapper.selectUserToLoginByPhoneNumber(phoneNumber);
                    //用户不存在则默认注册
                    if (null == loginUser) {
                        loginUser = register(phoneNumber);
                    }
                    Integer accountStatus = loginUser.getAccountStatus();
                    if (UserAccountStatus.FROZEN.getStatus() == accountStatus)
                        throw new YouyaException("账号已被冻结,详情请咨询客服");
                    Long id = loginUser.getId();
                    String token = JwtUtil.createToken(id);
                    String key = String.format(RedisConstant.YY_USER_TOKEN, id);
                    //todo token暂时不设置过期时间
//        redisUtil.set(key, token, 7200);
                    redisUtil.set(key, token);
                    redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));
                    return token;
                } else {
                    LoginUser userCache = JSONObject.parseObject(cacheObj.toString(), LoginUser.class);
                    Integer accountStatus = userCache.getAccountStatus();
                    if (UserAccountStatus.FROZEN.getStatus() == accountStatus)
                        throw new YouyaException("账号已被冻结,详情请咨询客服");
                    String token = JwtUtil.createToken(Long.valueOf(userId));
                    String key = String.format(RedisConstant.YY_USER_TOKEN, userId);
                    //todo token暂时不设置过期时间
//        redisUtil.set(key, token, 7200);
                    redisUtil.set(key, token);
                    return token;
                }
            }
        } else throw new YouyaException("验证码错误");
    }

    /**
     * 密码登陆
     *
     * @param passwordDto
     * @return
     */
    @Override
    public String loginByPassword(UserLoginByPasswordDto passwordDto) {
        return null;
    }

    /**
     * 根据手机号注册用户
     *
     * @param phoneNumber
     * @return
     */
    private LoginUser register(String phoneNumber) {
        boolean exists = userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getPhone, phoneNumber));
        if (exists) {
            throw new YouyaException("手机号已经注册");
        }
        User user = new User();
        user.setPhone(phoneNumber)
                .setAuthenticationStatus(UserAuthenticationStatusEnum.NOT_CERTIFIED.getType())
                .setAccountStatus(UserAccountStatus.UNFROZEN.getStatus())
                .setNamePublicStatus(0)
                .setPhonePublicStatus(0)
                .setWechatPublicStatus(0)
                .setQqPublicStatus(0)
                .setEmailPublicStatus(0)
                .setAddressPublicStatus(0)
                .setAvatar(DEFAULT_AVATAR);
        userMapper.insert(user);
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user, loginUser);
        return loginUser;
    }

    /**
     * 获取登陆短信二维码
     *
     * @param codeDto
     */
    @Override
    public void getVerificationCode(VerificationCodeDto codeDto) {
        String phone = codeDto.getPhone();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone).eq(User::getIsDelete, 0));
        if (null != user) {
            Integer accountStatus = user.getAccountStatus();
            if (UserAccountStatus.FROZEN.getStatus() == accountStatus) throw new YouyaException("账号已被冻结,详情请咨询客服");
        }
        String key = String.format(RedisConstant.YY_PHONE_CODE, phone);
        Object o = redisUtil.get(key);
        if (null == o) {
            String code = verificationCodeGenerator();
            if (!HuaWeiUtil.sendVerificationCode(code, phone)) throw new YouyaException("短信验证码发送失败，请稍后再试");
//            if (!redisUtil.set(key, code, 600)) throw new YouyaException("获取验证码失败，请稍后再试");
            //todo 暂时不过期
            if (!redisUtil.set(key, code)) throw new YouyaException("获取验证码失败，请稍后再试");
        } else throw new YouyaException("验证码10分钟内有效，请勿频繁获取短信验证码");
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getId();
        String key = String.format(RedisConstant.YY_USER_TOKEN, id);
        redisUtil.del(key);
    }

    /**
     * 获取6位随机数字验证码
     *
     * @return
     */
    private static String verificationCodeGenerator() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }
}
