package com.korant.youya.workplace.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.user.UserAccountStatusEnum;
import com.korant.youya.workplace.enums.user.UserAuthenticationStatusEnum;
import com.korant.youya.workplace.enums.userprivacy.NameVisibleTypeEnum;
import com.korant.youya.workplace.enums.userprivacy.OtherInfoVisibleTypeEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.user.*;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.user.ResumeContactInfoVo;
import com.korant.youya.workplace.pojo.vo.user.ResumePersonInfoVo;
import com.korant.youya.workplace.pojo.vo.user.UserLoginVo;
import com.korant.youya.workplace.service.UserService;
import com.korant.youya.workplace.utils.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserPrivacyMapper userPrivacyMapper;

    @Resource
    private UserNameVisibleInfoMapper userNameVisibleInfoMapper;

    @Resource
    private EveryoneVisibleInfoMapper everyoneVisibleInfoMapper;

    @Resource
    private RecruiterVisibleInfoMapper recruiterVisibleInfoMapper;

    @Resource
    private RedisUtil redisUtil;

    private static final String DEFAULT_AVATAR = "https://resources.youyai.cn/icon/male.svg";

    private static final String CHINA_CODE = "100000";

    /**
     * 微信登陆
     *
     * @param wechatCodeDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVo loginByWechatCode(UserLoginByWechatCodeDto wechatCodeDto) {
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
                loginUser = userService.register(phoneNumber);
            }
            Integer accountStatus = loginUser.getAccountStatus();
            if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus) throw new YouyaException("账号已被冻结,详情请咨询客服");
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
            UserLoginVo userLoginVo = new UserLoginVo();
            userLoginVo.setToken(token);
            userLoginVo.setRole(loginUser.getRole());
            return userLoginVo;
        } else {
            String userId = o.toString();
            String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
            Object cacheObj = redisUtil.get(cacheKey);
            if (null == cacheObj) {
                LoginUser loginUser = userMapper.selectUserToLoginByPhoneNumber(phoneNumber);
                //用户不存在则默认注册
                if (null == loginUser) {
                    loginUser = userService.register(phoneNumber);
                }
                Integer accountStatus = loginUser.getAccountStatus();
                if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                    throw new YouyaException("账号已被冻结,详情请咨询客服");
                Long id = loginUser.getId();
                String token = JwtUtil.createToken(id);
                String key = String.format(RedisConstant.YY_USER_TOKEN, id);
                //todo token暂时不设置过期时间
//        redisUtil.set(key, token, 7200);
                redisUtil.set(key, token);
                redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));
                UserLoginVo userLoginVo = new UserLoginVo();
                userLoginVo.setToken(token);
                userLoginVo.setRole(loginUser.getRole());
                return userLoginVo;
            } else {
                LoginUser loginUser = JSONObject.parseObject(cacheObj.toString(), LoginUser.class);
                Integer accountStatus = loginUser.getAccountStatus();
                if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                    throw new YouyaException("账号已被冻结,详情请咨询客服");
                String token = JwtUtil.createToken(Long.valueOf(userId));
                String key = String.format(RedisConstant.YY_USER_TOKEN, userId);
                //todo token暂时不设置过期时间
//        redisUtil.set(key, token, 7200);
                redisUtil.set(key, token);
                UserLoginVo userLoginVo = new UserLoginVo();
                userLoginVo.setToken(token);
                userLoginVo.setRole(loginUser.getRole());
                return userLoginVo;
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
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVo loginBySMSVerificationCode(UserLoginBySMSVerificationCodeDto smsVerificationCodeDto) {
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
                    loginUser = userService.register(phoneNumber);
                }
                Integer accountStatus = loginUser.getAccountStatus();
                if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                    throw new YouyaException("账号已被冻结,详情请咨询客服");
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
                UserLoginVo userLoginVo = new UserLoginVo();
                userLoginVo.setToken(token);
                userLoginVo.setRole(loginUser.getRole());
                return userLoginVo;
            } else {
                String userId = o.toString();
                String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
                Object cacheObj = redisUtil.get(cacheKey);
                if (null == cacheObj) {
                    LoginUser loginUser = userMapper.selectUserToLoginByPhoneNumber(phoneNumber);
                    //用户不存在则默认注册
                    if (null == loginUser) {
                        loginUser = userService.register(phoneNumber);
                    }
                    Integer accountStatus = loginUser.getAccountStatus();
                    if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                        throw new YouyaException("账号已被冻结,详情请咨询客服");
                    Long id = loginUser.getId();
                    String token = JwtUtil.createToken(id);
                    String key = String.format(RedisConstant.YY_USER_TOKEN, id);
                    //todo token暂时不设置过期时间
//        redisUtil.set(key, token, 7200);
                    redisUtil.set(key, token);
                    redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));
                    UserLoginVo userLoginVo = new UserLoginVo();
                    userLoginVo.setToken(token);
                    userLoginVo.setRole(loginUser.getRole());
                    return userLoginVo;
                } else {
                    LoginUser loginUser = JSONObject.parseObject(cacheObj.toString(), LoginUser.class);
                    Integer accountStatus = loginUser.getAccountStatus();
                    if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                        throw new YouyaException("账号已被冻结,详情请咨询客服");
                    String token = JwtUtil.createToken(Long.valueOf(userId));
                    String key = String.format(RedisConstant.YY_USER_TOKEN, userId);
                    //todo token暂时不设置过期时间
//        redisUtil.set(key, token, 7200);
                    redisUtil.set(key, token);
                    UserLoginVo userLoginVo = new UserLoginVo();
                    userLoginVo.setToken(token);
                    userLoginVo.setRole(loginUser.getRole());
                    return userLoginVo;
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
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVo loginByPassword(UserLoginByPasswordDto passwordDto) {
        return null;
    }

    /**
     * 根据手机号注册用户
     *
     * @param phoneNumber
     * @return
     */
    public LoginUser register(String phoneNumber) {
        User user = new User();
        user.setPhone(phoneNumber)
                .setAuthenticationStatus(UserAuthenticationStatusEnum.NOT_CERTIFIED.getStatus())
                .setAccountStatus(UserAccountStatusEnum.UNFROZEN.getStatus())
                .setAvatar(DEFAULT_AVATAR);
        userMapper.insert(user);
        Long id = user.getId();
        UserPrivacy userPrivacy = new UserPrivacy();
        userPrivacy.setUid(id)
                .setNamePublicStatus(NameVisibleTypeEnum.FULL_NAME.getValue())
                .setPhonePublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_ONESELF.getValue())
                .setWechatPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_ONESELF.getValue())
                .setQqPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_ONESELF.getValue())
                .setEmailPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_ONESELF.getValue())
                .setAddressPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_ONESELF.getValue());
        userPrivacyMapper.insert(userPrivacy);
        UserNameVisibleInfo userNameVisibleInfo = new UserNameVisibleInfo();
        userNameVisibleInfo.setUid(id);
        userNameVisibleInfoMapper.insert(userNameVisibleInfo);
        EveryoneVisibleInfo everyoneVisibleInfo = new EveryoneVisibleInfo();
        everyoneVisibleInfo.setUid(id);
        everyoneVisibleInfoMapper.insert(everyoneVisibleInfo);
        RecruiterVisibleInfo recruiterVisibleInfo = new RecruiterVisibleInfo();
        recruiterVisibleInfo.setUid(id);
        recruiterVisibleInfoMapper.insert(recruiterVisibleInfo);
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
            if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus) throw new YouyaException("账号已被冻结,详情请咨询客服");
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
     * 实名认证
     *
     * @param realNameAuthDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void realNameAuthentication(UserRealNameAuthenticationDto realNameAuthDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Integer authenticationStatus = loginUser.getAuthenticationStatus();
        if (0 == authenticationStatus) {
            String lastName = realNameAuthDto.getLastName();
            String firstName = realNameAuthDto.getFirstName();
            String idcard = realNameAuthDto.getIdcard();
            String name = lastName + firstName;
            String phone = loginUser.getPhone();
            if (!HuaWeiUtil.realNameAuth(idcard, phone, name)) throw new YouyaException("实名认证失败");
            Long id = loginUser.getId();
            User user = userMapper.selectById(id);
            user.setLastName(lastName);
            user.setFirstName(firstName);
            user.setIdentityCard(idcard);
            user.setAuthenticationStatus(UserAuthenticationStatusEnum.CERTIFIED.getStatus());
            userMapper.updateById(user);
            String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
            redisUtil.del(cacheKey);
        } else {
            throw new YouyaException("请勿重复认证");
        }
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        Long id = SpringSecurityUtil.getUserId();
        String key = String.format(RedisConstant.YY_USER_TOKEN, id);
        redisUtil.del(key);
    }

    /**
     * 用户注销
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel() {
        Long id = SpringSecurityUtil.getUserId();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, id).eq(User::getIsDelete, 0));
        if (null == user) throw new YouyaException("用户未注册或已注销");
        String phone = user.getPhone();
        String idKey = String.format(RedisConstant.YY_USER_ID, phone);
        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
        String tokenKey = String.format(RedisConstant.YY_USER_TOKEN, id);
        if (!redisUtil.del(idKey) || !redisUtil.del(cacheKey) || !redisUtil.del(tokenKey))
            throw new YouyaException("注销失败,请稍后再试");
        user.setIsDelete(1);
        userMapper.updateById(user);
    }

    /**
     * @return
     * @Description 在线简历-查询个人信息
     * @Param
     **/
    @Override
    public ResumePersonInfoVo resumePersonDetail() {
        Long userId = SpringSecurityUtil.getUserId();
        return userMapper.resumePersonDetail(userId);
    }

    /**
     * @return
     * @Description 在线简历-修改个人信息
     * @Param
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resumePersonModify(ResumePersonModifyDto resumePersonModifyDto) {
        Long userId = SpringSecurityUtil.getUserId();
        UserPrivacy userPrivacy = userPrivacyMapper.selectOne(new LambdaQueryWrapper<UserPrivacy>().eq(UserPrivacy::getUid, userId).eq(UserPrivacy::getIsDelete, 0));
        if (null == userPrivacy) {
            userPrivacy = new UserPrivacy();
            userPrivacy.setUid(userId)
                    .setNamePublicStatus(NameVisibleTypeEnum.FULL_NAME.getValue())
                    .setPhonePublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_ONESELF.getValue())
                    .setWechatPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_ONESELF.getValue())
                    .setQqPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_ONESELF.getValue())
                    .setEmailPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_ONESELF.getValue())
                    .setAddressPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_ONESELF.getValue());
            userPrivacyMapper.insert(userPrivacy);
        } else {
            UserNameVisibleInfo userNameVisibleInfo = userNameVisibleInfoMapper.selectOne(new LambdaQueryWrapper<UserNameVisibleInfo>().eq(UserNameVisibleInfo::getUid, userId).eq(UserNameVisibleInfo::getIsDelete, 0));
            if (null == userNameVisibleInfo) {
                userNameVisibleInfo = new UserNameVisibleInfo();
                userNameVisibleInfo.setUid(userId);
                userNameVisibleInfoMapper.insert(userNameVisibleInfo);
            } else {
                Integer namePublicStatus = userPrivacy.getNamePublicStatus();
                switch (namePublicStatus) {
                    case 1:
                        userNameVisibleInfo.setLastName(resumePersonModifyDto.getLastName());
                        userNameVisibleInfo.setFirstName(resumePersonModifyDto.getFirstName());
                        break;
                    case 2:
                        userNameVisibleInfo.setFirstName(null);
                        userNameVisibleInfo.setLastName(resumePersonModifyDto.getLastName());
                        break;
                    case 3:
                        userNameVisibleInfo.setLastName(null);
                        userNameVisibleInfo.setFirstName(resumePersonModifyDto.getFirstName());
                        break;
                    default:
                        break;
                }
                userNameVisibleInfoMapper.updateById(userNameVisibleInfo);
            }
        }
        userMapper.resumePersonModify(userId, resumePersonModifyDto);

    }

    /**
     * 在线简历-查询联系方式
     *
     * @return
     */
    @Override
    public ResumeContactInfoVo resumeContactDetail() {
        Long userId = SpringSecurityUtil.getUserId();
        return userMapper.resumeContactDetail(userId);
    }

    /**
     * 在线简历-编辑联系方式
     *
     * @return
     */
    @Override
    public void modifyResumeContactDetail(ResumeContactModifyDto resumeContactModifyDto) {
        Long userId = SpringSecurityUtil.getUserId();
        UserPrivacy userPrivacy = userPrivacyMapper.selectOne(new LambdaQueryWrapper<UserPrivacy>().eq(UserPrivacy::getUid, userId).eq(UserPrivacy::getIsDelete, 0));
        if (null == userPrivacy) {
            userPrivacy = new UserPrivacy();
            userPrivacy.setUid(userId)
                    .setNamePublicStatus(1)
                    .setPhonePublicStatus(3)
                    .setWechatPublicStatus(3)
                    .setQqPublicStatus(3)
                    .setEmailPublicStatus(3)
                    .setAddressPublicStatus(3);
            userPrivacyMapper.insert(userPrivacy);
        } else {
            EveryoneVisibleInfo everyoneVisibleInfo = everyoneVisibleInfoMapper.selectOne(new LambdaQueryWrapper<EveryoneVisibleInfo>().eq(EveryoneVisibleInfo::getUid, userId).eq(EveryoneVisibleInfo::getIsDelete, 0));
            if (null == everyoneVisibleInfo) {
                everyoneVisibleInfo = new EveryoneVisibleInfo();
                everyoneVisibleInfo.setUid(userId);
                everyoneVisibleInfoMapper.insert(everyoneVisibleInfo);
            }
            RecruiterVisibleInfo recruiterVisibleInfo = recruiterVisibleInfoMapper.selectOne(new LambdaQueryWrapper<RecruiterVisibleInfo>().eq(RecruiterVisibleInfo::getUid, userId).eq(RecruiterVisibleInfo::getIsDelete, 0));
            if (null == recruiterVisibleInfo) {
                recruiterVisibleInfo = new RecruiterVisibleInfo();
                recruiterVisibleInfo.setUid(userId);
                recruiterVisibleInfoMapper.insert(recruiterVisibleInfo);
            }
            Integer phonePublicStatus = userPrivacy.getPhonePublicStatus();
            Integer wechatPublicStatus = userPrivacy.getWechatPublicStatus();
            Integer qqPublicStatus = userPrivacy.getQqPublicStatus();
            Integer emailPublicStatus = userPrivacy.getEmailPublicStatus();
            Integer addressPublicStatus = userPrivacy.getAddressPublicStatus();
            switch (phonePublicStatus) {
                case 1:
                    everyoneVisibleInfo.setPhone(resumeContactModifyDto.getPhone());
                    break;
                case 2:
                    recruiterVisibleInfo.setPhone(resumeContactModifyDto.getPhone());
                    break;
                default:
                    break;
            }
            switch (wechatPublicStatus) {
                case 1:
                    everyoneVisibleInfo.setWechatId(resumeContactModifyDto.getWechatId());
                    break;
                case 2:
                    recruiterVisibleInfo.setWechatId(resumeContactModifyDto.getWechatId());
                    break;
                default:
                    break;
            }
            switch (qqPublicStatus) {
                case 1:
                    everyoneVisibleInfo.setQq(resumeContactModifyDto.getQq());
                    break;
                case 2:
                    recruiterVisibleInfo.setQq(resumeContactModifyDto.getQq());
                    break;
                default:
                    break;
            }
            switch (emailPublicStatus) {
                case 1:
                    everyoneVisibleInfo.setEmail(resumeContactModifyDto.getEmail());
                    break;
                case 2:
                    recruiterVisibleInfo.setEmail(resumeContactModifyDto.getEmail());
                    break;
                default:
                    break;
            }
            switch (addressPublicStatus) {
                case 1:
                    everyoneVisibleInfo.setCountryCode(CHINA_CODE);
                    everyoneVisibleInfo.setProvinceCode(resumeContactModifyDto.getProvinceCode());
                    everyoneVisibleInfo.setCityCode(resumeContactModifyDto.getCityCode());
                    everyoneVisibleInfo.setDistrictCode(resumeContactModifyDto.getDistrictCode());
                    everyoneVisibleInfo.setAddress(resumeContactModifyDto.getAddress());
                    break;
                case 2:
                    recruiterVisibleInfo.setCountryCode(CHINA_CODE);
                    recruiterVisibleInfo.setProvinceCode(resumeContactModifyDto.getProvinceCode());
                    recruiterVisibleInfo.setCityCode(resumeContactModifyDto.getCityCode());
                    recruiterVisibleInfo.setDistrictCode(resumeContactModifyDto.getDistrictCode());
                    recruiterVisibleInfo.setAddress(resumeContactModifyDto.getAddress());
                    break;
                default:
                    break;
            }
            everyoneVisibleInfoMapper.updateById(everyoneVisibleInfo);
            recruiterVisibleInfoMapper.updateById(recruiterVisibleInfo);
        }
        resumeContactModifyDto.setCountryCode(CHINA_CODE);
        userMapper.modifyResumeContactDetail(userId, resumeContactModifyDto);

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
