package com.korant.youya.workplace.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.enterprise.EnterpriseAuthStatusEnum;
import com.korant.youya.workplace.enums.enterprisetodo.EnterpriseTodoEventTypeEnum;
import com.korant.youya.workplace.enums.enterprisetodo.EnterpriseTodoOperateEnum;
import com.korant.youya.workplace.enums.role.RoleEnum;
import com.korant.youya.workplace.enums.user.UserAccountStatusEnum;
import com.korant.youya.workplace.enums.user.UserAuthenticationStatusEnum;
import com.korant.youya.workplace.enums.userprivacy.NameVisibleTypeEnum;
import com.korant.youya.workplace.enums.userprivacy.OtherInfoVisibleTypeEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.user.*;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.user.*;
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
    private EnterpriseMapper enterpriseMapper;

    @Resource
    private UserEnterpriseMapper userEnterpriseMapper;

    @Resource
    private EnterpriseTodoMapper enterpriseTodoMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

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
        String accessToken = WeChatUtil.getMiniProgramAccessToken();
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
            redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser), 7200);
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
                redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser), 7200);
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
                redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser), 7200);
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
                    redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser), 7200);
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
            if (!redisUtil.set(key, code, 600)) throw new YouyaException("获取验证码失败，请稍后再试");
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
     * 查询个人基本信息
     *
     * @return
     */
    @Override
    public UserPersonalBasicInfoVo queryPersonalBasicInfo() {
        Long userId = SpringSecurityUtil.getUserId();
        return userMapper.queryPersonalBasicInfo(userId);
    }

    /**
     * 修改个人基本信息
     *
     * @param modifyDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyUserPersonalBasicInfo(ModifyUserPersonalBasicInfoDto modifyDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        if (UserAuthenticationStatusEnum.CERTIFIED.getStatus() == loginUser.getAuthenticationStatus()) {
            String lastName = loginUser.getLastName();
            String firstName = loginUser.getFirstName();
            if (!lastName.equals(modifyDto.getLastName()) || !firstName.equals(modifyDto.getFirstName()))
                throw new YouyaException("已完成实名认证,姓名不可更改");
        }
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
                        userNameVisibleInfo.setLastName(modifyDto.getLastName());
                        userNameVisibleInfo.setFirstName(modifyDto.getFirstName());
                        break;
                    case 2:
                        userNameVisibleInfo.setFirstName(null);
                        userNameVisibleInfo.setLastName(modifyDto.getLastName());
                        break;
                    case 3:
                        userNameVisibleInfo.setLastName(null);
                        userNameVisibleInfo.setFirstName(modifyDto.getFirstName());
                        break;
                    default:
                        break;
                }
                userNameVisibleInfoMapper.updateById(userNameVisibleInfo);
            }
        }
        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
        if (!redisUtil.del(cacheKey)) throw new YouyaException("修改个人基本信息失败");
        userMapper.modifyUserPersonalBasicInfo(userId, modifyDto);
    }

    /**
     * 查询用户联系方式
     *
     * @return
     */
    @Override
    public UserContactInfoVo queryUserContactInfo() {
        Long userId = SpringSecurityUtil.getUserId();
        return userMapper.queryUserContactInfo(userId);
    }

    /**
     * 修改用户联系方式
     *
     * @param modifyDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyUserContactInfo(ModifyUserContactInfoDto modifyDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
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
                    everyoneVisibleInfo.setPhone(loginUser.getPhone());
                    break;
                case 2:
                    recruiterVisibleInfo.setPhone(loginUser.getPhone());
                    break;
                default:
                    break;
            }
            switch (wechatPublicStatus) {
                case 1:
                    everyoneVisibleInfo.setWechatId(modifyDto.getWechatId());
                    break;
                case 2:
                    recruiterVisibleInfo.setWechatId(modifyDto.getWechatId());
                    break;
                default:
                    break;
            }
            switch (qqPublicStatus) {
                case 1:
                    everyoneVisibleInfo.setQq(modifyDto.getQq());
                    break;
                case 2:
                    recruiterVisibleInfo.setQq(modifyDto.getQq());
                    break;
                default:
                    break;
            }
            switch (emailPublicStatus) {
                case 1:
                    everyoneVisibleInfo.setEmail(modifyDto.getEmail());
                    break;
                case 2:
                    recruiterVisibleInfo.setEmail(modifyDto.getEmail());
                    break;
                default:
                    break;
            }
            switch (addressPublicStatus) {
                case 1:
                    everyoneVisibleInfo.setCountryCode(CHINA_CODE);
                    everyoneVisibleInfo.setProvinceCode(modifyDto.getProvinceCode());
                    everyoneVisibleInfo.setCityCode(modifyDto.getCityCode());
                    everyoneVisibleInfo.setDistrictCode(modifyDto.getDistrictCode());
                    everyoneVisibleInfo.setAddress(modifyDto.getAddress());
                    break;
                case 2:
                    recruiterVisibleInfo.setCountryCode(CHINA_CODE);
                    recruiterVisibleInfo.setProvinceCode(modifyDto.getProvinceCode());
                    recruiterVisibleInfo.setCityCode(modifyDto.getCityCode());
                    recruiterVisibleInfo.setDistrictCode(modifyDto.getDistrictCode());
                    recruiterVisibleInfo.setAddress(modifyDto.getAddress());
                    break;
                default:
                    break;
            }
            everyoneVisibleInfoMapper.updateById(everyoneVisibleInfo);
            recruiterVisibleInfoMapper.updateById(recruiterVisibleInfo);
        }
        modifyDto.setCountryCode(CHINA_CODE);
        userMapper.modifyUserContactInfo(userId, modifyDto);
    }

    /**
     * 查询登录用户信息
     *
     * @return
     */
    @Override
    public LoginUserVo queryLoginUserInfo() {
        LoginUser userInfo = SpringSecurityUtil.getUserInfo();
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(userInfo, loginUserVo);
        return loginUserVo;
    }

    /**
     * 简历详情
     *
     * @return
     */
    @Override
    public ResumeDetailVo resumeDetail() {
        Long userId = SpringSecurityUtil.getUserId();
        return userMapper.resumeDetail(userId);
    }

    /**
     * 简历预览
     *
     * @return
     */
    @Override
    public ResumePreviewVo resumePreview() {
        Long userId = SpringSecurityUtil.getUserId();
        return userMapper.resumePreview(userId);
    }

    /**
     * 申请关联企业
     *
     * @param applyAffiliatedEnterpriseDto
     */
    @Override
    public void affiliatedEnterprise(ApplyAffiliatedEnterpriseDto applyAffiliatedEnterpriseDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Integer authenticationStatus = loginUser.getAuthenticationStatus();
        if (!authenticationStatus.equals(UserAuthenticationStatusEnum.CERTIFIED.getStatus()))
            throw new YouyaException("请先完成实名认证");
        Long userId = loginUser.getId();
        UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getId, userId).eq(UserEnterprise::getIsDelete, 0));
        if (null != userEnterprise) throw new YouyaException("您已关联企业，无法申请");
        Long enterpriseId = applyAffiliatedEnterpriseDto.getEnterpriseId();
        if (enterpriseMapper.exists(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getAuthStatus, EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus()).eq(Enterprise::getIsDelete, 0)))
            throw new YouyaException("企业未创建");
        if (enterpriseTodoMapper.exists(new LambdaQueryWrapper<EnterpriseTodo>().eq(EnterpriseTodo::getEnterpriseId, enterpriseId).eq(EnterpriseTodo::getUid, userId).eq(EnterpriseTodo::getOperate, EnterpriseTodoOperateEnum.PENDING_REVIEW.getOperate()).eq(EnterpriseTodo::getIsDelete, 0)))
            throw new YouyaException("您当前已有关联申请等待审核中");
        EnterpriseTodo enterpriseTodo = new EnterpriseTodo();
        enterpriseTodo.setEnterpriseId(enterpriseId).setUid(userId).setEventType(EnterpriseTodoEventTypeEnum.EMPLOYEE.getType()).setOperate(EnterpriseTodoOperateEnum.PENDING_REVIEW.getOperate());
        enterpriseTodoMapper.insert(enterpriseTodo);
    }

    /**
     * 解除关联企业
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relieveAffiliated() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        String role = loginUser.getRole();
        if (StringUtils.isBlank(role)) throw new YouyaException("您当前未关联企业");
        if (RoleEnum.ADMIN.getRole().equals(role)) {
            throw new YouyaException(200, "抱歉，您是该公司管理员，请切换至公司端解除关联！");
        } else if (RoleEnum.HR.getRole().equals(role)) {
            throw new YouyaException(200, "抱歉，您是该公司HR，请切换至公司端解除关联！");
        } else {
            Long userId = loginUser.getId();
            Long enterpriseId = loginUser.getEnterpriseId();
            UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getEnterpriseId, enterpriseId).eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getIsDelete, 0));
            if (null != userEnterprise) {
                userEnterprise.setIsDelete(1);
                userEnterpriseMapper.updateById(userEnterprise);
            }
            UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, userId).eq(UserRole::getIsDelete, 0));
            if (null != userRole) {
                userRole.setIsDelete(1);
                userRoleMapper.updateById(userRole);
            }
            String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
            if (!redisUtil.del(cacheKey)) throw new YouyaException("解除关联企业失败，请稍后再试");
        }
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
