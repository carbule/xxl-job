package com.korant.youya.workplace.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.constants.WechatConstant;
import com.korant.youya.workplace.constants.WechatPayConstant;
import com.korant.youya.workplace.enums.*;
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
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.sysorder.CancelOrderDto;
import com.korant.youya.workplace.pojo.dto.sysorder.GeneratePaymentParametersDto;
import com.korant.youya.workplace.pojo.dto.sysorder.QueryClosedOrderListDto;
import com.korant.youya.workplace.pojo.dto.sysorder.QueryOrderListDto;
import com.korant.youya.workplace.pojo.dto.user.*;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.sysorder.SysOrderVo;
import com.korant.youya.workplace.pojo.vo.user.*;
import com.korant.youya.workplace.properties.DelayProperties;
import com.korant.youya.workplace.properties.RabbitMqConfigurationProperties;
import com.korant.youya.workplace.service.UserService;
import com.korant.youya.workplace.utils.*;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.model.TransactionAmount;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
    private UserWalletAccountMapper userWalletAccountMapper;

    @Resource
    private SysVirtualProductMapper sysVirtualProductMapper;

    @Resource
    private SysOrderMapper sysOrderMapper;

    @Resource
    private WalletTransactionFlowMapper walletTransactionFlowMapper;

    @Resource
    private WalletWithdrawalRecordMapper walletWithdrawalRecordMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RabbitMqUtil rabbitMqUtil;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    RabbitMqConfigurationProperties mqConfigurationProperties;

    @Value("${user_recharge_notify_url}")
    private String userRechargeNotifyUrl;

    private static final String DEFAULT_AVATAR = "https://resources.youyai.cn/icon/male.svg";

    private static final String CHINA_CODE = "100000";

    private static final String USER_RECHARGE_PRODUCT_CODE = "user_recharge";

    private static final String RECHARGE_DESCRIPTION = "友涯用户充值";

    private static final String WALLET_ACCOUNT_WITHDRAWAL = "友涯用户钱包账户提现";

    //友涯支付宝账号余额不足短信通知人员手机号码
    private static final String ACCOUNTANT_PHONE = "15150586985";

    /**
     * 微信登陆
     *
     * @param wechatCodeDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVo loginByWechatCode(WechatCodeDto wechatCodeDto) {
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
                .setPhonePublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_RECRUITERS.getValue())
                .setWechatPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_RECRUITERS.getValue())
                .setQqPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_RECRUITERS.getValue())
                .setEmailPublicStatus(OtherInfoVisibleTypeEnum.VISIBLE_TO_RECRUITERS.getValue())
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
        UserWalletAccount userWalletAccount = new UserWalletAccount();
        userWalletAccount.setUid(id);
        userWalletAccount.setAvailableBalance(new BigDecimal(0));
        userWalletAccount.setFreezeAmount(new BigDecimal(0));
        userWalletAccount.setStatus(0);
        userWalletAccountMapper.insert(userWalletAccount);
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
            if (!HuaWeiUtil.sendVerificationCode(HuaWeiUtil.TEMPLATE_A, code, phone))
                throw new YouyaException("短信验证码发送失败，请稍后再试");
            if (!redisUtil.set(key, code, 600)) throw new YouyaException("短信验证码发送失败，请稍后再试");
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
            String name = lastName + firstName;
            String phone = loginUser.getPhone();
            if (!HuaWeiUtil.realNameAuth2(phone, name)) throw new YouyaException("实名认证失败");
            Long id = loginUser.getId();
            User user = userMapper.selectById(id);
            user.setLastName(lastName);
            user.setFirstName(firstName);
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
        redisUtil.del(idKey);
        redisUtil.del(cacheKey);
        redisUtil.del(tokenKey);
        user.setIsDelete(1);
        userMapper.updateById(user);
    }

    /**
     * 修改用户头像
     *
     * @param modifyUserAvatarDto
     */
    @Override
    public void modifyUserAvatar(ModifyUserAvatarDto modifyUserAvatarDto) {
        Long userId = SpringSecurityUtil.getUserId();
        userMapper.modifyUserAvatar(userId, modifyUserAvatarDto);
        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
        redisUtil.del(cacheKey);
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
                    everyoneVisibleInfo.setAddress(modifyDto.getAddress());
                    break;
                case 2:
                    recruiterVisibleInfo.setCountryCode(CHINA_CODE);
                    recruiterVisibleInfo.setProvinceCode(modifyDto.getProvinceCode());
                    recruiterVisibleInfo.setCityCode(modifyDto.getCityCode());
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
            throw new YouyaException("抱歉，您是该公司管理员，请切换至公司端解除关联！");
        } else if (RoleEnum.HR.getRole().equals(role)) {
            throw new YouyaException("抱歉，您是该公司HR，请切换至公司端解除关联！");
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
     * 查询用户钱包信息
     *
     * @return
     */
    @Override
    public UserWalletVo queryUserWalletInfo() {
        Long userId = SpringSecurityUtil.getUserId();
        UserWalletAccount userWalletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
        if (null == userWalletAccount) throw new YouyaException("钱包账户不存在");
        Integer status = userWalletAccount.getStatus();
        if (WalletAccountStatusEnum.FROZEN.getStatus() == status) throw new YouyaException("钱包账户已被冻结，请联系客服");
        //可用余额
        BigDecimal availableBalance = userWalletAccount.getAvailableBalance();
        //冻结金额
        BigDecimal freezeAmount = userWalletAccount.getFreezeAmount();
        //账户总额
        BigDecimal accountBalance = availableBalance.add(freezeAmount);
        UserWalletVo userWalletVo = new UserWalletVo();
        userWalletVo.setAvailableBalance(availableBalance.multiply(new BigDecimal("0.01")));
        userWalletVo.setFreezeAmount(freezeAmount.multiply(new BigDecimal("0.01")));
        userWalletVo.setAccountBalance(accountBalance.multiply(new BigDecimal("0.01")));
        return userWalletVo;
    }

    /**
     * 检查用户是否有有效的微信Openid
     *
     * @return
     */
    @Override
    public boolean checkIfUserHasValidWechatOpenid() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        String wechatOpenId = loginUser.getWechatOpenId();
        return StringUtils.isNotBlank(wechatOpenId);
    }

    /**
     * 更新用户微信openid
     *
     * @param wechatCodeDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserWechatOpenId(WechatCodeDto wechatCodeDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        String wechatOpenId = loginUser.getWechatOpenId();
        if (StringUtils.isBlank(wechatOpenId)) {
            String code = wechatCodeDto.getCode();
            String openid = WeChatUtil.code2Session(code);
            if (StringUtils.isBlank(openid)) throw new YouyaException("openid获取失败，请稍后重试");
            Long userId = loginUser.getId();
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId).eq(User::getIsDelete, 0));
            if (null == user) throw new YouyaException("用户未注册或已注销");
            user.setWechatOpenId(openid);
            userMapper.updateById(user);
            String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
            redisUtil.del(cacheKey);
        }
    }

    /**
     * 用户充值
     *
     * @param userRechargeDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject recharge(UserRechargeDto userRechargeDto) {
        log.info("收到用户充值请求");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        String phone = loginUser.getPhone();
        UserWalletAccount userWalletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
        if (null == userWalletAccount) throw new YouyaException("钱包账户不存在");
        Integer status = userWalletAccount.getStatus();
        if (WalletAccountStatusEnum.FROZEN.getStatus() == status) throw new YouyaException("钱包账户已被冻结，请联系客服");
        String code = userRechargeDto.getCode();
        Integer quantity = userRechargeDto.getQuantity();
        SysVirtualProduct virtualProduct = sysVirtualProductMapper.selectOne(new LambdaQueryWrapper<SysVirtualProduct>().eq(SysVirtualProduct::getCode, USER_RECHARGE_PRODUCT_CODE).eq(SysVirtualProduct::getIsDelete, 0));
        if (null == virtualProduct) throw new YouyaException("充值商品不存在");
        Long productId = virtualProduct.getId();
        BigDecimal price = virtualProduct.getPrice();
        BigDecimal multiply = price.multiply(new BigDecimal(quantity));
        int totalAmount = multiply.intValue();
        log.info("用户：【{}】，购买商品id：【{}】，单价：【{}】，数量：【{}】，总金额：【{}】", phone, productId, price, quantity, totalAmount);
        //todo 放开最低充值限制
        //if (totalAmount < 100) throw new YouyaException("最低充值金额为1元");
        Long accountId = userWalletAccount.getId();
        SysOrder sysOrder = new SysOrder();
        sysOrder.setSysProductId(productId).setBuyerId(accountId).setType(OrderTypeEnum.VIRTUAL_PRODUCT.getType()).setPaymentMethod(PaymentMethodTypeEnum.WECHAT_PAYMENT.getType()).setOrderDate(LocalDateTime.now())
                .setQuantity(quantity).setTotalAmount(multiply).setActualAmount(multiply).setCurrency(CurrencyTypeEnum.CNY.getType()).setStatus(OrderStatusEnum.PENDING_PAYMENT.getStatus());
        sysOrderMapper.insert(sysOrder);
        String openid = WeChatUtil.code2Session(code);
        if (StringUtils.isBlank(openid)) throw new YouyaException("用户微信openid获取失败");
        if (StringUtils.isBlank(userRechargeNotifyUrl)) throw new YouyaException("支付通知地址获取失败");
        Long orderId = sysOrder.getId();
        PrepayWithRequestPaymentResponse response = WechatPayUtil.prepayWithRequestPayment(RECHARGE_DESCRIPTION, orderId.toString(), userRechargeNotifyUrl, totalAmount, openid);
        if (null == response) throw new YouyaException("充值下单失败，请稍后重试");
        log.info("用户：【{}】，购买商品id：【{}】，小程序下单并生成调起支付参数成功，微信响应报文：【{}】", phone, productId, JSONObject.toJSONString(response));
        WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
        walletTransactionFlow.setAccountId(accountId).setProductId(productId).setOrderId(orderId).setTransactionType(TransactionTypeEnum.RECHARGE.getType()).setTransactionDirection(TransactionDirectionTypeEnum.CREDIT.getType()).setAmount(new BigDecimal(totalAmount)).setCurrency(CurrencyTypeEnum.CNY.getType())
                .setDescription(RECHARGE_DESCRIPTION).setInitiationDate(LocalDateTime.now()).setStatus(TransactionFlowStatusEnum.PENDING.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.PENDING.getStatusDesc());
        walletTransactionFlowMapper.insert(walletTransactionFlow);
        try {
            HashMap<String, DelayProperties> delayProperties = mqConfigurationProperties.getDelayProperties();
            DelayProperties properties = delayProperties.get("user_order_timeout");
            rabbitMqUtil.sendDelayedMsg(properties.getExchangeName(), properties.getRoutingKey(), orderId, 900);
        } catch (Exception e) {
            log.error("用户：【{}】，购买商品id：【{}】，推送至订单超时队列失败，原因：", phone, orderId, e);
            log.error("用户：【{}】，购买商品id：【{}】，下单失败", phone, orderId);
            throw new YouyaException("网络异常，请稍后重试");
        }
        JSONObject result = new JSONObject();
        result.put("timeStamp", response.getTimeStamp());
        result.put("nonceStr", response.getNonceStr());
        result.put("package", response.getPackageVal());
        result.put("signType", response.getSignType());
        result.put("paySign", response.getPaySign());
        result.put("orderId", orderId);
        log.info("用户：【{}】，购买商品id：【{}】，下单成功", phone, productId);
        return result;
    }

    /**
     * 订单超时处理
     *
     * @param orderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderTimeoutProcessing(Long orderId) {
        log.info("用户订单id:【{}】开始进行超时处理", orderId);
        String lockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, orderId);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean tryLock = lock.tryLock(3, TimeUnit.SECONDS);
            if (tryLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
                if (null != sysOrder) {
                    Integer status = sysOrder.getStatus();
                    if (OrderStatusEnum.PENDING_PAYMENT.getStatus() == status) {
                        Long buyerId = sysOrder.getBuyerId();
                        UserWalletAccount userWalletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getId, buyerId).eq(UserWalletAccount::getIsDelete, 0));
                        if (null != userWalletAccount) {
                            WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, orderId).eq(WalletTransactionFlow::getIsDelete, 0));
                            if (null != walletTransactionFlow) {
                                sysOrder.setStatus(OrderStatusEnum.PAYMENT_TIMEOUT.getStatus());
                                sysOrderMapper.updateById(sysOrder);
                                BigDecimal availableBalance = userWalletAccount.getAvailableBalance();
                                walletTransactionFlow.setBalanceBefore(availableBalance);
                                walletTransactionFlow.setBalanceAfter(availableBalance);
                                walletTransactionFlow.setStatus(TransactionFlowStatusEnum.EXPIRED.getStatus());
                                walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.EXPIRED.getStatusDesc());
                                walletTransactionFlowMapper.updateById(walletTransactionFlow);
                                try {
                                    HashMap<String, DelayProperties> delayProperties = mqConfigurationProperties.getDelayProperties();
                                    DelayProperties properties = delayProperties.get("close_user_order");
                                    rabbitMqUtil.sendDelayedMsg(properties.getExchangeName(), properties.getRoutingKey(), orderId, 600);
                                } catch (Exception e) {
                                    log.error("用户订单id：【{}】，推送至关闭订单队列失败，原因：", orderId, e);
                                    log.error("用户订单id:【{}】超时处理失败", orderId);
                                    throw new YouyaException("网络异常，请稍后重试");
                                }
                                log.info("用户订单id:【{}】超时处理成功", orderId);
                            } else {
                                log.info("用户订单id:【{}】未找到对应交易流水，停止处理", orderId);
                            }
                        } else {
                            log.info("用户订单id:【{}】未找到对应钱包账户，停止处理", orderId);
                        }
                    } else {
                        log.info("用户订单id:【{}】不是待支付状态，停止处理", orderId);
                    }
                }
            } else {
                log.error("用户订单id：【{}】,获取订单锁超时", orderId);
                throw new YouyaException("获取订单锁超时，订单超时处理失败");
            }
        } catch (InterruptedException e) {
            log.error("用户订单id：【{}】,获取订单锁失败，原因：", orderId, e);
            throw new YouyaException("获取订单锁失败，订单超时处理失败");
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 用户完成支付
     *
     * @param completePaymentDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    //todo 如果当前状态为待支付完成支付操作更改状态成功后将其推入到队列 每隔5分钟查询一次订单状态
    public void completePayment(UserCompletePaymentDto completePaymentDto) {
        log.info("收到用户完成支付请求");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        String phone = loginUser.getPhone();
        UserWalletAccount userWalletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
        if (null == userWalletAccount) throw new YouyaException("钱包账户不存在");
        Long orderId = completePaymentDto.getOrderId();
        String lockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, orderId);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean tryLock = lock.tryLock(3, TimeUnit.SECONDS);
            if (tryLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
                if (null != sysOrder) {
                    Long buyerId = sysOrder.getBuyerId();
                    Long walletAccountId = userWalletAccount.getId();
                    if (buyerId.equals(walletAccountId)) {
                        Integer status = sysOrder.getStatus();
                        if (OrderStatusEnum.PENDING_PAYMENT.getStatus() == status) {
                            sysOrder.setStatus(OrderStatusEnum.PROCESSING_PAYMENT.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, orderId).eq(WalletTransactionFlow::getIsDelete, 0));
                            if (null == walletTransactionFlow) throw new YouyaException("订单交易流水不存在");
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.PROCESSING.getStatus());
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            log.info("用户：【{}】，订单id：【{}】，完成支付操作", phone, orderId);
                        } else {
                            log.info("用户：【{}】，订单id：【{}】，当前不是待支付状态，不再进行更新操作", phone, orderId);
                        }
                    }
                } else {
                    log.error("用户：【{}】，订单id：【{}】，不存在", phone, orderId);
                }
            } else {
                log.error("用户：【{}】，订单id：【{}】，获取订单锁超时", phone, orderId);
            }
        } catch (InterruptedException e) {
            log.error("用户：【{}】，订单id：【{}】，获取订单锁失败，原因：", phone, orderId, e);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 用户充值通知
     * （通知频率为15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h - 总计 24h4m）
     *
     * @param request
     * @param response
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechargeNotify(HttpServletRequest request, HttpServletResponse response) {
        log.info("收到友涯微信用户支付通知回调请求");
        String serial = request.getHeader("Wechatpay-Serial");
        if (StringUtils.isBlank(serial)) {
            log.error("微信支付通知缺失证书序列号");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失证书序列号");
            return;
        }
        if (serial.equals(WechatPayConstant.MERCHANT_SERIAL_NUMBER)) {
            log.error("微信支付通知证书序列号错误");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知证书序列号错误");
            return;
        }
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        if (StringUtils.isBlank(timestamp)) {
            log.error("微信支付通知缺失时间戳");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失时间戳");
            return;
        }
        log.info("微信支付通知请求头中参数Wechatpay-Timestamp原值为：【{}】", timestamp);
        long currentTimestamp = Instant.now().getEpochSecond();
        log.info("友涯系统当前时间Timestamp原值为：【{}】", currentTimestamp);
        long notifyTimestamp = Long.parseLong(timestamp);
        //将时间戳转换为Instant对象
        Instant instant1 = Instant.ofEpochSecond(currentTimestamp);
        Instant instant2 = Instant.ofEpochSecond(notifyTimestamp);
        //计算两个时间戳之间的Duration
        Duration duration = Duration.between(instant1, instant2);
        //获取相差的分钟数
        long minutesApart = duration.toMinutes();
        log.info("微信支付通知回调时间对比当前系统时间相差：【{}】分钟", minutesApart);
        if (minutesApart > 5) {
            log.error("微信支付通知时间对比友涯当前系统时间相差超过5分钟，不予处理");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知时间对比友涯当前系统时间相差超过5分钟，不予处理");
            return;
        }
        String nonce = request.getHeader("Wechatpay-Nonce");
        if (StringUtils.isBlank(nonce)) {
            log.error("微信支付通知缺失应答随机串");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失应答随机串");
            return;
        }
        String signature = request.getHeader("Wechatpay-Signature");
        if (StringUtils.isBlank(signature)) {
            log.error("微信支付通知缺少应答签名");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺少应答签名");
            return;
        }
        String requestBody;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder requestBodyBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBodyBuilder.append(line);
            }
            reader.close();
            requestBody = requestBodyBuilder.toString();
            log.info("获取友涯微信用户支付通知回调请求报文成功");
        } catch (Exception e) {
            log.error("获取友涯微信用户支付通知回调请求报文失败");
            writeToWechatPayNotifyResponseErrorMessage(response, "获取友涯微信用户支付通知回调请求报文失败");
            return;
        }
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(serial)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .body(requestBody)
                .build();
        Transaction transaction = null;
        try {
            transaction = WechatPayUtil.parse(requestParam);
        } catch (Exception e) {
            log.error("微信支付通知回调报文验签解密失败,原因：", e);
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知回调报文验签解密失败");
            return;
        }
        if (null == transaction) {
            log.error("微信支付通知回调报文为空");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知回调报文为空");
            return;
        }
        log.info("友涯微信用户支付通知回调请求验签解密成功，回调明文：【{}】", JSONObject.toJSONString(transaction));
        String appid = transaction.getAppid();
        String mchid = transaction.getMchid();
        if (StringUtils.isBlank(appid) || StringUtils.isBlank(mchid)) {
            log.error("微信支付通知appid或商户号缺失");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知appid或商户号缺失");
            return;
        }
        if (!appid.equals(WechatConstant.APP_ID) || !mchid.equals(WechatPayConstant.MERCHANT_ID)) {
            log.error("微信支付通知appid或商户号不匹配");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知appid或商户号不匹配");
            return;
        }
        String outTradeNo = transaction.getOutTradeNo();
        if (StringUtils.isBlank(outTradeNo)) {
            log.error("微信支付通知缺失商户订单号");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失商户订单号");
            return;
        }
        String tradeStateDesc = transaction.getTradeStateDesc();
        if (StringUtils.isBlank(tradeStateDesc)) {
            log.error("微信支付通知缺失交易状态描述");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失交易状态描述");
            return;
        }
        String transactionId = transaction.getTransactionId();
        if (StringUtils.isBlank(transactionId)) {
            log.error("微信支付通知缺失微信支付系统订单号");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失微信支付系统订单号");
            return;
        }
        String successTime = transaction.getSuccessTime();
        if (StringUtils.isBlank(successTime)) {
            log.error("微信支付通知缺失支付完成时间");
            writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失支付完成时间");
            return;
        }
        String orderLockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, outTradeNo);
        RLock orderLock = redissonClient.getLock(orderLockKey);
        try {
            boolean tryOrderLock = orderLock.tryLock(3, TimeUnit.SECONDS);
            if (tryOrderLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, Long.valueOf(outTradeNo)).eq(SysOrder::getIsDelete, 0));
                if (null == sysOrder) {
                    log.error("友涯系统不存在此笔订单");
                    writeToWechatPayNotifyResponseErrorMessage(response, "友涯系统不存在此笔订单");
                    return;
                }
                Long sysOrderId = sysOrder.getId();
                WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, sysOrderId).eq(WalletTransactionFlow::getIsDelete, 0));
                if (null == walletTransactionFlow) {
                    log.error("友涯系统不存在此笔订单交易流水");
                    writeToWechatPayNotifyResponseErrorMessage(response, "友涯系统不存在此笔订单交易流水");
                    return;
                }
                sysOrder.setOutTransactionId(transactionId);
                Long buyerId = sysOrder.getBuyerId();
                String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, buyerId);
                RLock walletLock = redissonClient.getLock(walletLockKey);
                try {
                    boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
                    if (tryWalletLock) {
                        UserWalletAccount userWalletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getId, buyerId).eq(UserWalletAccount::getIsDelete, 0));
                        if (null == userWalletAccount) {
                            log.error("友涯用户钱包账户不存在");
                            writeToWechatPayNotifyResponseErrorMessage(response, "友涯用户钱包账户不存在");
                            return;
                        }
                        BigDecimal beforeBalance = userWalletAccount.getAvailableBalance();
                        Transaction.TradeStateEnum tradeState = transaction.getTradeState();
                        //支付成功
                        if (Transaction.TradeStateEnum.SUCCESS.equals(tradeState)) {
                            //更新订单状态
                            BigDecimal actualAmount = sysOrder.getActualAmount();
                            TransactionAmount amount = transaction.getAmount();
                            if (null == amount) {
                                log.error("微信支付通知缺失订单金额信息");
                                writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失订单金额信息");
                                return;
                            }
                            Integer payerTotal = amount.getPayerTotal();
                            BigDecimal payerTotalAmount = new BigDecimal(payerTotal);
                            if (actualAmount.compareTo(payerTotalAmount) != 0) {
                                log.error("微信支付通知中支付金额与订单需要实付金额不一致");
                                writeToWechatPayNotifyResponseErrorMessage(response, "微信支付通知缺失订单金额信息");
                                return;
                            }
                            sysOrder.setStatus(OrderStatusEnum.PAID.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            //更新账户金额
                            BigDecimal afterBalance = beforeBalance.add(payerTotalAmount);
                            userWalletAccount.setAvailableBalance(afterBalance);
                            userWalletAccountMapper.updateById(userWalletAccount);
                            //更新账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc());
                            walletTransactionFlow.setBalanceBefore(beforeBalance);
                            walletTransactionFlow.setBalanceAfter(afterBalance);
                            walletTransactionFlow.setOutTransactionId(transactionId);
                            walletTransactionFlow.setCompletionDate(parseStringToLocalDateTime(successTime));
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            log.info("友涯订单id:【{}】，支付成功，账户余额和订单状态以及账户流水更新成功", sysOrderId);
                            //设置HTTP响应状态码为204（No Content）
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        } else if (Transaction.TradeStateEnum.PAYERROR.equals(tradeState)) {
                            //支付失败
                            //更新订单状态
                            sysOrder.setStatus(OrderStatusEnum.PAYMENT_FAILED.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            //更新账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.FAILED.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.FAILED.getStatusDesc());
                            walletTransactionFlow.setTransactionFailReason(tradeStateDesc);
                            walletTransactionFlow.setBalanceBefore(beforeBalance);
                            walletTransactionFlow.setBalanceAfter(beforeBalance);
                            walletTransactionFlow.setOutTransactionId(transactionId);
                            walletTransactionFlow.setCompletionDate(parseStringToLocalDateTime(successTime));
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            try {
                                WechatPayUtil.closeOrder(outTradeNo);
                                log.info("友涯订单id:【{}】，调用微信关闭订单接口成功", sysOrderId);
                            } catch (Exception e) {
                                log.error("友涯订单id:【{}】，调用微信关闭订单接口失败，原因：", sysOrderId, e);
                                log.error("友涯订单id:【{}】，支付失败，订单状态以及账户流水更新失败", sysOrderId);
                                writeToWechatPayNotifyResponseErrorMessage(response, "调用微信关闭订单接口失败");
                                return;
                            }
                            log.info("友涯订单id:【{}】，支付失败，订单状态以及账户流水更新成功", sysOrderId);
                            //设置HTTP响应状态码为204（No Content）
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        } else if (Transaction.TradeStateEnum.CLOSED.equals(tradeState)) {
                            //已关闭
                            //更新订单状态
                            sysOrder.setStatus(OrderStatusEnum.CLOSED.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            //更新账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.CLOSED.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.CLOSED.getStatusDesc());
                            walletTransactionFlow.setBalanceBefore(beforeBalance);
                            walletTransactionFlow.setBalanceAfter(beforeBalance);
                            walletTransactionFlow.setOutTransactionId(transactionId);
                            walletTransactionFlow.setCompletionDate(parseStringToLocalDateTime(successTime));
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            log.info("友涯订单id:【{}】，已关闭，订单状态以及账户流水更新成功", sysOrderId);
                        } else if (Transaction.TradeStateEnum.NOTPAY.equals(tradeState)) {
                            //未支付
                            //更新订单状态
                            sysOrder.setStatus(OrderStatusEnum.PENDING_PAYMENT.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            //更新账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.PENDING.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.PENDING.getStatusDesc());
                            walletTransactionFlow.setBalanceBefore(beforeBalance);
                            walletTransactionFlow.setBalanceAfter(beforeBalance);
                            walletTransactionFlow.setOutTransactionId(transactionId);
                            walletTransactionFlow.setCompletionDate(parseStringToLocalDateTime(successTime));
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            log.info("友涯订单id:【{}】，未支付，订单状态以及账户流水更新成功", sysOrderId);
                            //设置HTTP响应状态码为204（No Content）
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        } else if (Transaction.TradeStateEnum.REFUND.equals(tradeState)) {
                            //转入退款
                            ////更新订单状态
                            sysOrder.setStatus(OrderStatusEnum.REFUNDED.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            //更新账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.REFUNDED.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.REFUNDED.getStatusDesc());
                            walletTransactionFlow.setBalanceBefore(beforeBalance);
                            walletTransactionFlow.setBalanceAfter(beforeBalance);
                            walletTransactionFlow.setOutTransactionId(transactionId);
                            walletTransactionFlow.setCompletionDate(parseStringToLocalDateTime(successTime));
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            log.info("友涯订单id:【{}】，转入退款，订单状态以及账户流水更新成功", sysOrderId);
                            //设置HTTP响应状态码为204（No Content）
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        }
                    } else {
                        //获取钱包账户锁超时
                        log.error("友涯订单id:【{}】，获取钱包账户锁超时", sysOrderId);
                        writeToWechatPayNotifyResponseErrorMessage(response, "获取钱包账户锁超时");
                    }
                } catch (InterruptedException e) {
                    //获取钱包账户锁失败
                    log.error("友涯订单id:【{}】，获取钱包账户锁失败，原因：", sysOrderId, e);
                    log.error("友涯订单id:【{}】，订单状态以及账户流水更新失败", sysOrderId);
                    writeToWechatPayNotifyResponseErrorMessage(response, "获取钱包账户锁失败");
                } finally {
                    if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                        walletLock.unlock();
                    }
                }
            } else {
                //获取订单锁超时
                log.error("友涯订单id:【{}】，获取订单锁超时", outTradeNo);
                writeToWechatPayNotifyResponseErrorMessage(response, "获取订单锁超时");
            }
        } catch (InterruptedException e) {
            //获取订单锁失败
            log.error("友涯订单id:【{}】，获取订单锁失败，原因：", outTradeNo, e);
            log.error("友涯订单id:【{}】，订单状态以及账户流水更新失败", outTradeNo);
            writeToWechatPayNotifyResponseErrorMessage(response, "获取订单锁失败");
        } finally {
            if (orderLock != null && orderLock.isHeldByCurrentThread()) {
                orderLock.unlock();
            }
        }
    }

    /**
     * 写入微信支付通知响应错误消息
     *
     * @param response
     * @param message
     */
    private void writeToWechatPayNotifyResponseErrorMessage(HttpServletResponse response, String message) {
        //创建一个包含应答内容的JSON对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "FAIL");
        jsonObject.put("message", message);
        // 设置响应状态码为500
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        //设置响应内容类型为JSON
        response.setContentType("application/json;charset=UTF-8");
        //获取PrintWriter对象以便写入响应体
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //将JSON对象转换为字符串并写入响应
            assert out != null;
            out.print(jsonObject.toString());
            //刷新输出流
            out.flush();
            //关闭输出流
            out.close();
        }
    }

    /**
     * 查询充值结果
     *
     * @param rechargeResultDto
     * @return
     */
    @Override
    public Integer queryRechargeResult(UserQueryRechargeResultDto rechargeResultDto) {
        Long orderId = rechargeResultDto.getOrderId();
        SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
        if (null == sysOrder) throw new YouyaException("订单不存在");
        return sysOrder.getStatus();
    }

    /**
     * 查询用户支付宝账号
     *
     * @return
     */
    @Override
    public UserAlipayAccountVo queryUserAlipayAccount() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        String alipayAccount = loginUser.getAlipayAccount();
        if (StringUtils.isBlank(alipayAccount)) return null;
        UserAlipayAccountVo userAlipayAccountVo = new UserAlipayAccountVo();
        userAlipayAccountVo.setAlipayAccount(alipayAccount);
        return userAlipayAccountVo;
    }

    /**
     * 发送支付宝账号绑定验证码
     *
     * @param verificationCodeDto
     */
    @Override
    public void sendAlipayAccountBindingVerificationCode(BindingVerificationCodeDto verificationCodeDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Integer accountStatus = loginUser.getAccountStatus();
        if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus) throw new YouyaException("账号已被冻结,详情请咨询客服");
        String alipayAccount = verificationCodeDto.getAlipayAccount();
        String key = String.format(RedisConstant.YY_ALIPAY_COUNT_CODE, alipayAccount);
        Object o = redisUtil.get(key);
        if (null == o) {
            String code = verificationCodeGenerator();
            if (!HuaWeiUtil.sendVerificationCode(HuaWeiUtil.TEMPLATE_B, code, alipayAccount))
                throw new YouyaException("短信验证码发送失败，请稍后再试");
            if (!redisUtil.set(key, code, 600)) throw new YouyaException("短信验证码发送失败，请稍后再试");
        } else throw new YouyaException("验证码10分钟内有效，请勿频繁获取短信验证码");
    }

    /**
     * 校验支付宝账号绑定验证码
     *
     * @param checkVerificationCodeDto
     */
    @Override
    public void checkVerificationCode(CheckVerificationCodeDto checkVerificationCodeDto) {
        String alipayAccount = checkVerificationCodeDto.getAlipayAccount();
        String code = checkVerificationCodeDto.getCode();
        String codeKey = String.format(RedisConstant.YY_ALIPAY_COUNT_CODE, alipayAccount);
        Object codeCache = redisUtil.get(codeKey);
        if (null == codeCache) throw new YouyaException("验证码已过期");
        String cacheCode = String.valueOf(codeCache);
        if (!cacheCode.equals(code)) {
            throw new YouyaException("验证码错误");
        }
    }

    /**
     * 绑定支付宝账号
     *
     * @param bindAlipayAccountDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindAlipayAccount(BindAlipayAccountDto bindAlipayAccountDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        String alipay = loginUser.getAlipayAccount();
        if (StringUtils.isBlank(alipay)) {
            String lastName = bindAlipayAccountDto.getLastName();
            String firstName = bindAlipayAccountDto.getFirstName();
            String alipayAccount = bindAlipayAccountDto.getAlipayAccount();
            String idcard = bindAlipayAccountDto.getIdcard();
            String name = lastName + firstName;
            if (!HuaWeiUtil.realNameAuth(idcard, alipayAccount, name)) throw new YouyaException("实名认证失败");
            Long id = loginUser.getId();
            User user = userMapper.selectById(id);
            user.setAlipayAccount(alipayAccount);
            user.setAlipayAccountName(name);
            user.setAlipayAccountIdentityCard(idcard);
            userMapper.updateById(user);
            String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
            redisUtil.del(cacheKey);
        } else {
            throw new YouyaException("请勿重复认证");
        }
    }

    /**
     * 查询用户钱包账户可用余额
     *
     * @return
     */
    @Override
    public BigDecimal queryAccountAvailableBalance() {
        Long userId = SpringSecurityUtil.getUserId();
        UserWalletAccount userWalletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
        if (null == userWalletAccount) throw new YouyaException("钱包账户不存在");
        Integer status = userWalletAccount.getStatus();
        if (WalletAccountStatusEnum.FROZEN.getStatus() == status) throw new YouyaException("钱包账户已被冻结，请联系客服");
        //可用余额
        BigDecimal availableBalance = userWalletAccount.getAvailableBalance();
        return availableBalance.multiply(new BigDecimal("0.01"));
    }

    /**
     * 解绑支付宝账号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindAlipayAccount() {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        String alipayAccount = loginUser.getAlipayAccount();
        if (StringUtils.isBlank(alipayAccount)) throw new YouyaException("当前账户未绑定支付宝账号");
        Long id = loginUser.getId();
        userMapper.unbindAlipayAccount(id);
        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
        redisUtil.del(cacheKey);
    }

    /**
     * 用户钱包提现
     *
     * @param withdrawalDto
     */
    @Override
    public R<?> withdrawal(WithdrawalDto withdrawalDto) {
        log.info("收到用户钱包提现请求");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        String phone = loginUser.getPhone();
        Long walletAccountId = userWalletAccountMapper.queryWalletIdByUserId(userId);
        if (null == walletAccountId) throw new YouyaException("钱包账户不存在");
        String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, walletAccountId);
        RLock walletLock = redissonClient.getLock(walletLockKey);
        try {
            boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
            if (tryWalletLock) {
                UserWalletAccount walletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
                if (null == walletAccount) throw new YouyaException("钱包账户不存在");
                Integer accountStatus = walletAccount.getStatus();
                if (WalletAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                    throw new YouyaException("钱包账户已被冻结，请联系客服");
                BigDecimal availableBalance = walletAccount.getAvailableBalance();
                if (availableBalance.intValue() <= 0) throw new YouyaException("当前账户可用余额为0，无法提现");
                String amount = withdrawalDto.getAmount();
                BigDecimal decimal = new BigDecimal(amount);
                //先去除尾部多余的零
                BigDecimal withdrawalAmount = decimal.stripTrailingZeros();
                //设置小数位数为2，使用HALF_UP或其他合适的舍入模式
                int scale = withdrawalAmount.scale();
                //判断经过处理后的小数位是否为2
                if (scale > 2) throw new YouyaException("提现最小金额单位为分");
                if (withdrawalAmount.compareTo(availableBalance) > 0) throw new YouyaException("提现金额不能大于账户可用余额");
                if (withdrawalAmount.compareTo(new BigDecimal("0.1")) < 0) throw new YouyaException("最小提现金额为0.1元");
                //查询支付宝商户账号可用余额
                AlipayFundAccountQueryResponse alipayFundAccountQueryResponse = AlipayUtil.fundAccountQuery();
                if (null == alipayFundAccountQueryResponse) throw new YouyaException("网络异常，请稍后重试");
                if (alipayFundAccountQueryResponse.isSuccess()) {
                    String availableAmount = alipayFundAccountQueryResponse.getAvailableAmount();
                    BigDecimal alipayAccountAvailableAmount = new BigDecimal(availableAmount);
                    if (withdrawalAmount.compareTo(alipayAccountAvailableAmount) > 0) {
                        log.error("用户:【{}】，提现：【{}】元，大于支付宝商户账户可用余额：【{}】，提现失败", phone, withdrawalAmount, alipayAccountAvailableAmount);
                        //发送短信通知支付宝账号充值负责人告知余额不足
                        String key = String.format(RedisConstant.YY_ALIPAY_INSUFFICIENT_BALANCE_NOTICE, ACCOUNTANT_PHONE);
                        Object o = redisUtil.get(key);
                        if (null == o) {
                            String code = verificationCodeGenerator();
                            if (HuaWeiUtil.sendVerificationCode(HuaWeiUtil.TEMPLATE_C, code, ACCOUNTANT_PHONE)) {
                                redisUtil.set(key, code, 600);
                                log.info("支付宝商户余额不足告知短信发送成功");
                            } else {
                                log.error("支付宝商户余额不足告知短信发送失败");
                            }
                        } else {
                            log.info("支付宝商户余额不足告知短信10分钟内已发送，不再重复发送");
                        }
                        throw new YouyaException("网络异常，请稍后重试");
                    }
                    String alipayAccount = loginUser.getAlipayAccount();
                    if (StringUtils.isBlank(alipayAccount)) throw new YouyaException("支付宝账号未绑定，提现失败");
                    String alipayAccountName = loginUser.getAlipayAccountName();
                    if (StringUtils.isBlank(alipayAccountName)) throw new YouyaException("支付宝账号未实名认证，提现失败");
                    BigDecimal multiplyAmount = withdrawalAmount.multiply(BigDecimal.valueOf(100));
                    //创建内部转账订单
                    Long withdrawalRecordId = IdWorker.getId();
                    WalletWithdrawalRecord walletWithdrawalRecord = new WalletWithdrawalRecord();
                    walletWithdrawalRecord.setId(withdrawalRecordId).setAccountId(walletAccountId).setAmount(multiplyAmount).setCurrency(CurrencyTypeEnum.CNY.getType()).setWithdrawalMethod(WithdrawalMethodEnum.ALIPAY_ACCOUNT.getMethod())
                            .setPaymentAccount(alipayAccount).setPaymentName(alipayAccountName).setStatus(WalletWithdrawalStatusEnum.PROCESSING.getStatus()).setRequestTime(LocalDateTime.now()).setProcessingTime(LocalDateTime.now());
                    //创建账户交易流水
                    WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                    walletTransactionFlow.setAccountId(walletAccountId).setOrderId(withdrawalRecordId).setTransactionType(TransactionTypeEnum
                            .WITHDRAWAL.getType()).setTransactionDirection(TransactionDirectionTypeEnum.DEBIT.getType()).setAmount(multiplyAmount).setCurrency(CurrencyTypeEnum.CNY.getType()).setDescription(WALLET_ACCOUNT_WITHDRAWAL)
                            .setInitiationDate(LocalDateTime.now()).setStatus(TransactionFlowStatusEnum.PENDING.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.PENDING.getStatusDesc());
                    try {
                        userService.createAccountWithdrawalRelatedInfo(walletWithdrawalRecord, walletTransactionFlow);
                        log.info("用户:【{}】，提现:【{}】元，创建转账订单和交易流水成功", phone, withdrawalAmount);
                    } catch (Exception e) {
                        log.error("用户:【{}】，提现:【{}】元，创建转账订单和交易流水失败，原因:【】", phone, withdrawalAmount, e);
                        throw new YouyaException("网络异常，请稍后重试");
                    }
                    //发起转账
                    AlipayFundTransUniTransferResponse transferResponse = AlipayUtil.transfer(WALLET_ACCOUNT_WITHDRAWAL, withdrawalRecordId, withdrawalAmount, alipayAccount, alipayAccountName);
                    if (null == transferResponse) throw new YouyaException("网络异常，请稍后重试");
                    log.info("友涯用户:【{}】，提现订单号：【{}】，友涯商户转账到支付宝账号响应明文：【{}】", phone, withdrawalRecordId, JSONObject.toJSONString(transferResponse));
                    if (transferResponse.isSuccess()) {
                        String code = transferResponse.getCode();
                        if (StringUtils.isBlank(code)) {
                            log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，友涯商户转账到支付宝账号响应报文缺失响应状态码", phone, withdrawalRecordId, withdrawalAmount);
                            return R.error("网络异常，请稍后重试");
                        }
                        String status = transferResponse.getStatus();
                        if (StringUtils.isBlank(status)) {
                            log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，友涯商户转账到支付宝账号响应报文缺失响应状态值", phone, withdrawalRecordId, withdrawalAmount);
                            return R.error("网络异常，请稍后重试");
                        }
                        String outBizNo = transferResponse.getOutBizNo();
                        if (StringUtils.isBlank(outBizNo)) {
                            log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，友涯商户转账到支付宝账号响应报文缺失商户订单号", phone, withdrawalRecordId, withdrawalAmount);
                            return R.error("网络异常，请稍后重试");
                        }
                        String outOrderId = transferResponse.getOrderId();
                        if (StringUtils.isBlank(outOrderId)) {
                            log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，友涯商户转账到支付宝账号响应报文缺失支付宝订单号", phone, withdrawalRecordId, withdrawalAmount);
                            return R.error("网络异常，请稍后重试");
                        }
                        String payFundOrderId = transferResponse.getPayFundOrderId();
                        if (StringUtils.isBlank(payFundOrderId)) {
                            log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，友涯商户转账到支付宝账号响应报文缺失支付宝支付资金流水号", phone, withdrawalRecordId, withdrawalAmount);
                            return R.error("网络异常，请稍后重试");
                        }
                        String transDate = transferResponse.getTransDate();
                        LocalDateTime completionDate = null;
                        if (StringUtils.isNotBlank(transDate)) {
                            completionDate = LocalDateTime.parse(transDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        }
                        //转账支付宝账号成功
                        if ("10000".equals(code) && "SUCCESS".equals(status)) {
                            if (withdrawalRecordId.toString().equals(outBizNo)) {
                                //更新转账订单状态
                                walletWithdrawalRecord.setStatus(WalletWithdrawalStatusEnum.SUCCESSFUL.getStatus());
                                walletWithdrawalRecord.setAlipayOrderId(outOrderId);
                                walletWithdrawalRecord.setAlipayFundOrderId(payFundOrderId);
                                walletWithdrawalRecord.setCompletionTime(completionDate);
                                //更新账户金额
                                BigDecimal hundred = BigDecimal.valueOf(100);
                                BigDecimal multiply = withdrawalAmount.multiply(hundred);
                                BigDecimal subtract = availableBalance.subtract(multiply);
                                walletAccount.setAvailableBalance(subtract);
                                //更新账户交易流水状态
                                walletTransactionFlow.setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus());
                                walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc());
                                walletTransactionFlow.setBalanceBefore(availableBalance);
                                walletTransactionFlow.setBalanceAfter(subtract);
                                walletTransactionFlow.setCompletionDate(completionDate);
                                walletTransactionFlow.setOutTransactionId(payFundOrderId);
                                try {
                                    userService.updateAccountWithdrawalRelatedInfo(walletWithdrawalRecord, walletAccount, walletTransactionFlow);
                                    log.info("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元成功，转账订单和交易流水更新成功", phone, withdrawalRecordId, withdrawalAmount);
                                    return R.ok();
                                } catch (Exception e) {
                                    log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元成功，转账订单和交易流水更新失败，原因：", phone, withdrawalRecordId, withdrawalAmount, e);
                                    return R.error("网络异常，请稍后重试");
                                }
                            } else {
                                log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，支付宝转账api接口响应报文中的商户订单号与平台提现订单号不一致", phone, withdrawalRecordId, withdrawalAmount);
                                return R.error("网络异常，请稍后重试");
                            }
                        } else {
                            //转账支付宝账号失败
                            String msg = transferResponse.getMsg();
                            String subCode = transferResponse.getSubCode();
                            String subMsg = transferResponse.getSubMsg();
                            if (StringUtils.isBlank(msg) || StringUtils.isBlank(subCode) || StringUtils.isBlank(subMsg)) {
                                log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，支付宝转账api接口响应报文中缺失状态码或状态描述信息", phone, withdrawalRecordId, withdrawalAmount);
                                return R.error("网络异常，请稍后重试");
                            }
                            //修改转账订单状态
                            walletWithdrawalRecord.setStatus(WalletWithdrawalStatusEnum.FAILED.getStatus());
                            walletWithdrawalRecord.setFailReason(subMsg);
                            walletWithdrawalRecord.setAlipayOrderId(outOrderId);
                            walletWithdrawalRecord.setAlipayFundOrderId(payFundOrderId);
                            walletWithdrawalRecord.setCompletionTime(completionDate);
                            //修改账户交易流水状态
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.FAILED.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.FAILED.getStatusDesc());
                            walletTransactionFlow.setTransactionFailReason(subMsg);
                            walletTransactionFlow.setBalanceBefore(availableBalance);
                            walletTransactionFlow.setBalanceAfter(availableBalance);
                            walletTransactionFlow.setCompletionDate(completionDate);
                            walletTransactionFlow.setOutTransactionId(payFundOrderId);
                            try {
                                userService.updateAccountWithdrawalRelatedInfo(walletWithdrawalRecord, null, walletTransactionFlow);
                                log.info("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，原因：【{}】，转账订单和交易流水更新成功", phone, withdrawalRecordId, withdrawalAmount, subMsg);
                            } catch (Exception e) {
                                log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，原因：【{}】，转账订单和交易流水更新失败，原因：", phone, withdrawalRecordId, withdrawalAmount, subMsg, e);
                            }
                            return R.error(subMsg);
                        }
                    } else {
                        String msg = transferResponse.getMsg();
                        String subCode = transferResponse.getSubCode();
                        String subMsg = transferResponse.getSubMsg();
                        if (StringUtils.isBlank(msg) || StringUtils.isBlank(subCode) || StringUtils.isBlank(subMsg)) {
                            log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，支付宝转账api接口响应报文中缺失状态码或状态描述信息", phone, withdrawalRecordId, withdrawalAmount);
                            return R.error("网络异常，请稍后重试");
                        }
                        //更新转账订单状态
                        walletWithdrawalRecord.setStatus(WalletWithdrawalStatusEnum.FAILED.getStatus());
                        walletWithdrawalRecord.setFailReason(subMsg);
                        //更新账户交易流水状态
                        walletTransactionFlow.setStatus(TransactionFlowStatusEnum.FAILED.getStatus());
                        walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.FAILED.getStatusDesc());
                        walletTransactionFlow.setTransactionFailReason(subMsg);
                        walletTransactionFlow.setBalanceBefore(availableBalance);
                        walletTransactionFlow.setBalanceAfter(availableBalance);
                        try {
                            userService.updateAccountWithdrawalRelatedInfo(walletWithdrawalRecord, null, walletTransactionFlow);
                            log.info("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，原因：【{}】，转账订单和交易流水更新成功", phone, withdrawalRecordId, withdrawalAmount, subMsg);
                        } catch (Exception e) {
                            log.error("友涯用户:【{}】，提现订单号：【{}】，提现：【{}】元失败，原因：【{}】，转账订单和交易流水更新失败，原因：", phone, withdrawalRecordId, withdrawalAmount, subMsg, e);
                        }
                        return R.error(subMsg);
                    }
                } else {
                    String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(alipayFundAccountQueryResponse);
                    log.error("查询支付宝商户可用余额失败，诊断url:【{}】", diagnosisUrl);
                    throw new YouyaException("网络异常，请稍后重试");
                }
            } else {
                log.error("用户：【{}】，钱包账户提现，获取钱包账户锁超时", phone);
                throw new YouyaException("网络异常，请稍后重试");
            }
        } catch (InterruptedException e) {
            log.error("用户：【{}】，钱包账户提现，获取订单锁失败，原因：", phone, e);
            throw new YouyaException("网络异常，请稍后重试");
        } catch (YouyaException e) {
            log.error("用户：【{}】，钱包账户提现失败", phone);
            throw e;
        } catch (Exception e) {
            log.error("用户：【{}】，钱包账户提现失败，原因", phone, e);
            throw new YouyaException("网络异常，请稍后重试");
        } finally {
            if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                walletLock.unlock();
            }
        }
    }

    /**
     * 创建账户提现相关信息
     *
     * @param walletWithdrawalRecord
     * @param walletTransactionFlow
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createAccountWithdrawalRelatedInfo(WalletWithdrawalRecord walletWithdrawalRecord, WalletTransactionFlow walletTransactionFlow) {
        if (null != walletWithdrawalRecord) {
            walletWithdrawalRecordMapper.insert(walletWithdrawalRecord);
        }
        if (null != walletTransactionFlow) {
            walletTransactionFlowMapper.insert(walletTransactionFlow);
        }
    }

    /**
     * 更新账户提现相关信息
     *
     * @param walletWithdrawalRecord
     * @param userWalletAccount
     * @param walletTransactionFlow
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateAccountWithdrawalRelatedInfo(WalletWithdrawalRecord walletWithdrawalRecord, UserWalletAccount userWalletAccount, WalletTransactionFlow walletTransactionFlow) {
        if (null != walletWithdrawalRecord) {
            walletWithdrawalRecordMapper.updateById(walletWithdrawalRecord);
        }
        if (null != userWalletAccount) {
            userWalletAccountMapper.updateById(userWalletAccount);
        }
        if (null != walletTransactionFlow) {
            walletTransactionFlowMapper.updateById(walletTransactionFlow);
        }
    }

    /**
     * 查询用户订单列表
     *
     * @param queryOrderListDto
     * @return
     */
    @Override
    public Page<SysOrderVo> queryOrderList(QueryOrderListDto queryOrderListDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        UserWalletAccount walletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
        if (null == walletAccount) throw new YouyaException("钱包账户不存在");
        Long walletAccountId = walletAccount.getId();
        Integer status = queryOrderListDto.getStatus();
        int pageNumber = queryOrderListDto.getPageNumber();
        int pageSize = queryOrderListDto.getPageSize();
        Long count = sysOrderMapper.selectCount(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getBuyerId, walletAccountId).eq((ObjectUtil.isNotNull(status)), SysOrder::getStatus, status).eq(SysOrder::getIsDelete, 0));
        List<SysOrderVo> list = sysOrderMapper.queryOrderList(walletAccountId, queryOrderListDto);
        Page<SysOrderVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 生成订单支付参数
     *
     * @param generatePaymentParametersDto
     * @return
     */
    @Override
    public JSONObject generatePaymentParameters(GeneratePaymentParametersDto generatePaymentParametersDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        String phone = loginUser.getPhone();
        UserWalletAccount walletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
        if (null == walletAccount) throw new YouyaException("钱包账户不存在");
        Integer accountStatus = walletAccount.getStatus();
        if (WalletAccountStatusEnum.FROZEN.getStatus() == accountStatus) throw new YouyaException("钱包账户已被冻结，请联系客服");
        Long orderId = generatePaymentParametersDto.getOrderId();
        SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
        if (null == sysOrder) throw new YouyaException("订单不存在");
        Integer status = sysOrder.getStatus();
        if (OrderStatusEnum.PENDING_PAYMENT.getStatus() != status) throw new YouyaException("只有待支付订单可以获取支付参数");
        Long buyerId = sysOrder.getBuyerId();
        Long walletAccountId = walletAccount.getId();
        if (!buyerId.equals(walletAccountId)) throw new YouyaException("非法操作");
        String code = generatePaymentParametersDto.getCode();
        String openid = WeChatUtil.code2Session(code);
        if (StringUtils.isBlank(openid)) throw new YouyaException("用户微信openid获取失败");
        if (StringUtils.isBlank(userRechargeNotifyUrl)) throw new YouyaException("支付通知地址获取失败");
        Integer actualAmount = sysOrder.getActualAmount().intValue();
        PrepayWithRequestPaymentResponse response = WechatPayUtil.prepayWithRequestPayment(RECHARGE_DESCRIPTION, orderId.toString(), userRechargeNotifyUrl, actualAmount, openid);
        if (null == response) throw new YouyaException("充值下单失败，请稍后重试");
        log.info("用户：【{}】，订单id：【{}】，生成调起支付参数成功，微信响应报文：【{}】", phone, orderId, JSONObject.toJSONString(response));
        JSONObject result = new JSONObject();
        result.put("timeStamp", response.getTimeStamp());
        result.put("nonceStr", response.getNonceStr());
        result.put("package", response.getPackageVal());
        result.put("signType", response.getSignType());
        result.put("paySign", response.getPaySign());
        result.put("orderId", orderId);
        return result;
    }

    /**
     * 取消订单
     *
     * @param cancelOrderDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(CancelOrderDto cancelOrderDto) {
        log.info("收到用户取消订单请求");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        String phone = loginUser.getPhone();
        Long orderId = cancelOrderDto.getOrderId();
        String orderLockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, orderId);
        RLock orderLock = redissonClient.getLock(orderLockKey);
        try {
            boolean tryOrderLock = orderLock.tryLock(3, TimeUnit.SECONDS);
            if (tryOrderLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
                if (null == sysOrder) throw new YouyaException("订单不存在");
                Integer status = sysOrder.getStatus();
                if (OrderStatusEnum.PENDING_PAYMENT.getStatus() != status)
                    throw new YouyaException("只有待支付的订单才能取消");
                Long buyerId = sysOrder.getBuyerId();
                String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, buyerId);
                RLock walletLock = redissonClient.getLock(walletLockKey);
                try {
                    boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
                    if (tryWalletLock) {
                        UserWalletAccount walletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
                        if (null == walletAccount) throw new YouyaException("钱包账户不存在");
                        Long walletAccountId = walletAccount.getId();
                        if (!buyerId.equals(walletAccountId)) throw new YouyaException("非法操作");
                        BigDecimal availableBalance = walletAccount.getAvailableBalance();
                        WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, orderId).eq(WalletTransactionFlow::getIsDelete, 0));
                        if (null == walletTransactionFlow) throw new YouyaException("系统不存在此笔订单交易流水");
                        sysOrder.setStatus(OrderStatusEnum.PAYMENT_CANCELED.getStatus());
                        sysOrderMapper.updateById(sysOrder);
                        walletTransactionFlow.setBalanceBefore(availableBalance);
                        walletTransactionFlow.setBalanceAfter(availableBalance);
                        walletTransactionFlow.setStatus(TransactionFlowStatusEnum.CANCELLED.getStatus());
                        walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.CANCELLED.getStatusDesc());
                        walletTransactionFlowMapper.updateById(walletTransactionFlow);
                        try {
                            WechatPayUtil.closeOrder(orderId.toString());
                            log.info("用户：【{}】，订单id：【{}】，调用微信关闭订单接口成功", phone, orderId);
                        } catch (Exception e) {
                            log.error("用户：【{}】，订单id：【{}】，调用微信关闭订单接口失败，原因：【{}】", phone, orderId, e);
                            log.error("用户：【{}】，订单id：【{}】，取消失败", phone, orderId);
                            throw new YouyaException("网络异常，请稍后重试");
                        }
                        log.info("用户：【{}】，订单id：【{}】，取消成功", phone, orderId);
                    } else {
                        log.error("用户：【{}】，订单id：【{}】，获取钱包账户锁超时", phone, orderId);
                        throw new YouyaException("网络异常，请稍后重试");
                    }
                } catch (InterruptedException e) {
                    log.error("用户：【{}】，订单id：【{}】，获取钱包账户锁失败，原因：", phone, orderId, e);
                    throw new YouyaException("网络异常，请稍后重试");
                } finally {
                    if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                        walletLock.unlock();
                    }
                }
            } else {
                log.error("用户：【{}】，订单id：【{}】，获取订单锁超时", phone, orderId);
                throw new YouyaException("网络异常，请稍后重试");
            }
        } catch (InterruptedException e) {
            log.error("用户：【{}】，订单id：【{}】，获取订单锁失败，原因：", phone, orderId, e);
            throw new YouyaException("网络异常，请稍后重试");
        } catch (YouyaException e) {
            log.error("用户：【{}】，订单id：【{}】，取消订单失败", phone, orderId);
            throw e;
        } catch (Exception e) {
            log.error("用户：【{}】，订单id：【{}】，取消订单失败，原因：", phone, orderId, e);
            throw new YouyaException("网络异常，请稍后重试");
        } finally {
            if (orderLock != null && orderLock.isHeldByCurrentThread()) {
                orderLock.unlock();
            }
        }
    }

    /**
     * 关闭订单
     *
     * @param orderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeUserOrder(Long orderId) {
        log.info("用户订单id:【{}】开始进行关闭处理", orderId);
        String lockKey = String.format(RedisConstant.YY_SYS_ORDER_LOCK, orderId);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean tryLock = lock.tryLock(3, TimeUnit.SECONDS);
            if (tryLock) {
                SysOrder sysOrder = sysOrderMapper.selectOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getId, orderId).eq(SysOrder::getIsDelete, 0));
                if (null != sysOrder) {
                    Integer status = sysOrder.getStatus();
                    if (OrderStatusEnum.PAYMENT_TIMEOUT.getStatus() == status) {
                        WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, orderId).eq(WalletTransactionFlow::getIsDelete, 0));
                        if (null != walletTransactionFlow) {
                            sysOrder.setStatus(OrderStatusEnum.CLOSED.getStatus());
                            sysOrderMapper.updateById(sysOrder);
                            walletTransactionFlow.setStatus(TransactionFlowStatusEnum.CLOSED.getStatus());
                            walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.CLOSED.getStatusDesc());
                            walletTransactionFlowMapper.updateById(walletTransactionFlow);
                            try {
                                WechatPayUtil.closeOrder(orderId.toString());
                                log.info("友涯订单id:【{}】，调用微信关闭订单接口成功", orderId);
                            } catch (Exception e) {
                                log.error("友涯订单id:【{}】，调用微信关闭订单接口失败，原因：", orderId, e);
                                log.error("友涯订单id:【{}】，关闭处理失败", orderId);
                                throw new YouyaException("调用微信关闭订单接口失败");
                            }
                            log.info("用户订单id:【{}】关闭处理成功", orderId);
                        } else {
                            log.info("用户订单id:【{}】未找到对应交易流水，停止处理", orderId);
                        }
                    } else {
                        log.info("用户订单id:【{}】不是待支付状态，停止处理", orderId);
                    }
                }
            } else {
                log.error("用户订单id：【{}】,获取订单锁超时", orderId);
                throw new YouyaException("获取订单锁超时，关闭订单处理失败");
            }
        } catch (InterruptedException e) {
            log.error("用户订单id：【{}】,获取订单锁失败，原因：", orderId, e);
            throw new YouyaException("获取订单锁失败，关闭订单处理失败");
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 查询用户已关闭订单列表
     *
     * @param queryClosedOrderListDto
     * @return
     */
    @Override
    public Page<SysOrderVo> queryClosedOrderList(QueryClosedOrderListDto queryClosedOrderListDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        UserWalletAccount walletAccount = userWalletAccountMapper.selectOne(new LambdaQueryWrapper<UserWalletAccount>().eq(UserWalletAccount::getUid, userId).eq(UserWalletAccount::getIsDelete, 0));
        if (null == walletAccount) throw new YouyaException("钱包账户不存在");
        Long walletAccountId = walletAccount.getId();
        int pageNumber = queryClosedOrderListDto.getPageNumber();
        int pageSize = queryClosedOrderListDto.getPageSize();
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.PAYMENT_FAILED.getStatus());
        statusList.add(OrderStatusEnum.PAYMENT_TIMEOUT.getStatus());
        statusList.add(OrderStatusEnum.PAYMENT_CANCELED.getStatus());
        statusList.add(OrderStatusEnum.REFUNDING.getStatus());
        statusList.add(OrderStatusEnum.REFUNDED.getStatus());
        statusList.add(OrderStatusEnum.CLOSED.getStatus());
        Long count = sysOrderMapper.selectCount(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getBuyerId, walletAccountId).in(SysOrder::getStatus, statusList).eq(SysOrder::getIsDelete, 0));
        List<SysOrderVo> list = sysOrderMapper.queryClosedOrderList(walletAccountId, queryClosedOrderListDto);
        Page<SysOrderVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
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

    /**
     * 将字符串转换为时间
     *
     * @param var
     * @return
     */
    private static LocalDateTime parseStringToLocalDateTime(String var) {
        // 创建一个可以解析ISO-8601格式时间字符串的formatter
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        // 解析为OffsetDateTime
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(var, formatter);
        // 将OffsetDateTime转换为LocalDateTime，忽略时区信息
        return offsetDateTime.toLocalDateTime();
    }

    public static void main(String[] args) throws InterruptedException {
//        String input = "2024-03-01T16:37:42+08:00";
//        // 创建一个可以解析ISO-8601格式时间字符串的formatter
//        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//        // 解析为OffsetDateTime
//        OffsetDateTime offsetDateTime = OffsetDateTime.parse(input, formatter);
//        // 将OffsetDateTime转换为LocalDateTime，忽略时区信息
//        LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
//        System.out.println(localDateTime);
//        BigDecimal bd = new BigDecimal("103");
//        try {
//            // 尝试将BigDecimal设置为两位小数，但不允许舍入
//            MathContext mathContext = new MathContext(3, RoundingMode.UNNECESSARY);
//            BigDecimal divide = bd.divide(new BigDecimal("100"), mathContext);
//            System.out.println(divide);
//        } catch (ArithmeticException e) {
//            System.out.println("ArithmeticException: " + e.getMessage());
//        }

//        BigDecimal value = new BigDecimal("123.451000");
//        BigDecimal decimal = value.stripTrailingZeros();
//        // 获取BigDecimal对象的小数位数（标度）
//        int scale = decimal.scale();
//        System.out.println(scale);

        //假设我们有两个时间戳（以毫秒为单位）
        long timestamp1 = Instant.now().toEpochMilli(); // 获取当前时间戳
        long timestamp2 = System.currentTimeMillis(); // 这里替换为另一个时间戳值
        //将时间戳转换为Instant对象
        Instant instant1 = Instant.ofEpochMilli(timestamp1);
        Instant instant2 = Instant.ofEpochMilli(timestamp2);
        //计算两个时间戳之间的 Duration
        Duration duration = Duration.between(instant1, instant2);
        //获取相差的分钟数
        long minutesApart = duration.toMinutes();
        System.out.println("两个时间戳相隔 " + minutesApart + " 分钟");
    }
}
