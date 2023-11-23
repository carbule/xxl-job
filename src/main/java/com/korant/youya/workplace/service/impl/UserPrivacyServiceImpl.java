package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.userprivacy.NameVisibleTypeEnum;
import com.korant.youya.workplace.enums.userprivacy.OtherInfoVisibleTypeEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.dto.userprivacy.UserPrivacyModifyDto;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.userprivacy.UserPersonalInfoPrivacyVo;
import com.korant.youya.workplace.service.UserPrivacyService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户个人信息隐私设置 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-22
 */
@Service
public class UserPrivacyServiceImpl extends ServiceImpl<UserPrivacyMapper, UserPrivacy> implements UserPrivacyService {

    @Resource
    private UserPrivacyMapper userPrivacyMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserNameVisibleInfoMapper userNameVisibleInfoMapper;

    @Resource
    private EveryoneVisibleInfoMapper everyoneVisibleInfoMapper;

    @Resource
    private RecruiterVisibleInfoMapper recruiterVisibleInfoMapper;

    /**
     * 查询个人信息隐私设置
     *
     * @return
     */
    @Override
    public UserPersonalInfoPrivacyVo queryPersonalInfoPrivacy() {
        Long userId = SpringSecurityUtil.getUserId();
        UserPersonalInfoPrivacyVo privacyVo = userPrivacyMapper.queryPersonalInfoPrivacy(userId);
        privacyVo.setNamePublicStatusName(NameVisibleTypeEnum.getNameByValue(privacyVo.getNamePublicStatusValue()));
        privacyVo.setPhonePublicStatusName(OtherInfoVisibleTypeEnum.getNameByValue(privacyVo.getPhonePublicStatusValue()));
        privacyVo.setWechatPublicStatusName(OtherInfoVisibleTypeEnum.getNameByValue(privacyVo.getWechatPublicStatusValue()));
        privacyVo.setQqPublicStatusName(OtherInfoVisibleTypeEnum.getNameByValue(privacyVo.getQqPublicStatusValue()));
        privacyVo.setEmailPublicStatusName(OtherInfoVisibleTypeEnum.getNameByValue(privacyVo.getEmailPublicStatusValue()));
        privacyVo.setAddressPublicStatusName(OtherInfoVisibleTypeEnum.getNameByValue(privacyVo.getAddressPublicStatusValue()));
        return privacyVo;
    }

    /**
     * 修改个人隐私
     *
     * @param modifyDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(UserPrivacyModifyDto modifyDto) {
        Long userId = SpringSecurityUtil.getUserId();
        UserPrivacy userPrivacy = userPrivacyMapper.selectOne(new LambdaQueryWrapper<UserPrivacy>().eq(UserPrivacy::getId, modifyDto.getId()).eq(UserPrivacy::getIsDelete, 0));
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
        }

        Integer newNamePublicStatus = modifyDto.getNamePublicStatus();
        Integer newPhonePublicStatus = modifyDto.getPhonePublicStatus();
        Integer newWechatPublicStatus = modifyDto.getWechatPublicStatus();
        Integer newQqPublicStatus = modifyDto.getQqPublicStatus();
        Integer newEmailPublicStatus = modifyDto.getEmailPublicStatus();
        Integer newAddressPublicStatus = modifyDto.getAddressPublicStatus();
        HashMap<String, Integer> newPrivacy = new HashMap<>();
        newPrivacy.put("name", newNamePublicStatus);
        newPrivacy.put("phone", newPhonePublicStatus);
        newPrivacy.put("wechat", newWechatPublicStatus);
        newPrivacy.put("qq", newQqPublicStatus);
        newPrivacy.put("email", newEmailPublicStatus);
        newPrivacy.put("address", newAddressPublicStatus);

        Integer oldNamePublicStatus = userPrivacy.getNamePublicStatus();
        Integer oldPhonePublicStatus = userPrivacy.getPhonePublicStatus();
        Integer oldWechatPublicStatus = userPrivacy.getWechatPublicStatus();
        Integer oldQqPublicStatus = userPrivacy.getQqPublicStatus();
        Integer oldEmailPublicStatus = userPrivacy.getEmailPublicStatus();
        Integer oldAddressPublicStatus = userPrivacy.getAddressPublicStatus();
        HashMap<String, Integer> oldPrivacy = new HashMap<>();
        oldPrivacy.put("name", oldNamePublicStatus);
        oldPrivacy.put("phone", oldPhonePublicStatus);
        oldPrivacy.put("wechat", oldWechatPublicStatus);
        oldPrivacy.put("qq", oldQqPublicStatus);
        oldPrivacy.put("email", oldEmailPublicStatus);
        oldPrivacy.put("address", oldAddressPublicStatus);

        HashMap<String, Integer> map = new HashMap<>();

        Set<Map.Entry<String, Integer>> entries = oldPrivacy.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            Integer oldValue = entry.getValue();
            Integer newValue = newPrivacy.get(key);
            if (!oldValue.equals(newValue)) {
                map.put(key, newValue);
            }
        }

        if (map.size() > 0) {

            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId).eq(User::getIsDelete, 0));
            if (null == user) throw new YouyaException("用户未注册或已注销");

            UserNameVisibleInfo userNameVisibleInfo = userNameVisibleInfoMapper.selectOne(new LambdaQueryWrapper<UserNameVisibleInfo>().eq(UserNameVisibleInfo::getUid, userId).eq(UserNameVisibleInfo::getIsDelete, 0));
            if (null == userNameVisibleInfo) {
                userNameVisibleInfo = new UserNameVisibleInfo();
                userNameVisibleInfo.setUid(userId);
                userNameVisibleInfoMapper.insert(userNameVisibleInfo);
            }

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

            Set<Map.Entry<String, Integer>> mapEntries = map.entrySet();
            for (Map.Entry<String, Integer> mapEntry : mapEntries) {
                String key = mapEntry.getKey();
                Integer value = mapEntry.getValue();
                switch (key) {
                    case "name":
                        switch (value) {
                            case 1:
                                userNameVisibleInfo.setLastName(user.getLastName());
                                userNameVisibleInfo.setFirstName(user.getFirstName());
                                break;
                            case 2:
                                userNameVisibleInfo.setLastName(user.getLastName());
                                userNameVisibleInfo.setFirstName(null);
                                break;
                            case 3:
                                userNameVisibleInfo.setLastName(null);
                                userNameVisibleInfo.setFirstName(user.getFirstName());
                                break;
                            default:
                                break;
                        }
                        break;
                    case "phone":
                        switch (value) {
                            case 1:
                                everyoneVisibleInfo.setPhone(user.getPhone());
                                recruiterVisibleInfo.setPhone(null);
                                break;
                            case 2:
                                everyoneVisibleInfo.setPhone(null);
                                recruiterVisibleInfo.setPhone(user.getPhone());
                                break;
                            case 3:
                                everyoneVisibleInfo.setPhone(null);
                                recruiterVisibleInfo.setPhone(null);
                                break;
                            default:
                                break;
                        }
                        break;
                    case "wechat":
                        switch (value) {
                            case 1:
                                everyoneVisibleInfo.setWechatId(user.getWechatId());
                                recruiterVisibleInfo.setWechatId(null);
                                break;
                            case 2:
                                everyoneVisibleInfo.setWechatId(null);
                                recruiterVisibleInfo.setWechatId(user.getWechatId());
                                break;
                            case 3:
                                everyoneVisibleInfo.setWechatId(null);
                                recruiterVisibleInfo.setWechatId(null);
                                break;
                            default:
                                break;
                        }
                        break;
                    case "qq":
                        switch (value) {
                            case 1:
                                everyoneVisibleInfo.setQq(user.getQq());
                                recruiterVisibleInfo.setQq(null);
                                break;
                            case 2:
                                everyoneVisibleInfo.setQq(null);
                                recruiterVisibleInfo.setQq(user.getQq());
                                break;
                            case 3:
                                everyoneVisibleInfo.setQq(null);
                                recruiterVisibleInfo.setQq(null);
                                break;
                            default:
                                break;
                        }
                        break;
                    case "email":
                        switch (value) {
                            case 1:
                                everyoneVisibleInfo.setEmail(user.getEmail());
                                recruiterVisibleInfo.setEmail(null);
                                break;
                            case 2:
                                everyoneVisibleInfo.setEmail(null);
                                recruiterVisibleInfo.setEmail(user.getEmail());
                                break;
                            case 3:
                                everyoneVisibleInfo.setEmail(null);
                                recruiterVisibleInfo.setEmail(null);
                                break;
                            default:
                                break;
                        }
                        break;
                    case "address":
                        switch (value) {
                            case 1:
                                everyoneVisibleInfo.setCountryCode(user.getCountryCode());
                                everyoneVisibleInfo.setProvinceCode(user.getProvinceCode());
                                everyoneVisibleInfo.setCityCode(user.getCityCode());
                                everyoneVisibleInfo.setDistrictCode(user.getDistrictCode());
                                everyoneVisibleInfo.setAddress(user.getAddress());

                                recruiterVisibleInfo.setCountryCode(null);
                                recruiterVisibleInfo.setProvinceCode(null);
                                recruiterVisibleInfo.setCityCode(null);
                                recruiterVisibleInfo.setDistrictCode(null);
                                recruiterVisibleInfo.setAddress(null);
                                recruiterVisibleInfo.setAddress(null);
                                break;
                            case 2:
                                everyoneVisibleInfo.setCountryCode(null);
                                everyoneVisibleInfo.setProvinceCode(null);
                                everyoneVisibleInfo.setCityCode(null);
                                everyoneVisibleInfo.setDistrictCode(null);
                                everyoneVisibleInfo.setAddress(null);

                                recruiterVisibleInfo.setCountryCode(user.getCountryCode());
                                recruiterVisibleInfo.setProvinceCode(user.getProvinceCode());
                                recruiterVisibleInfo.setCityCode(user.getCityCode());
                                recruiterVisibleInfo.setDistrictCode(user.getDistrictCode());
                                recruiterVisibleInfo.setAddress(user.getAddress());
                                break;
                            case 3:
                                everyoneVisibleInfo.setCountryCode(null);
                                everyoneVisibleInfo.setProvinceCode(null);
                                everyoneVisibleInfo.setCityCode(null);
                                everyoneVisibleInfo.setDistrictCode(null);
                                everyoneVisibleInfo.setAddress(null);

                                recruiterVisibleInfo.setCountryCode(null);
                                recruiterVisibleInfo.setProvinceCode(null);
                                recruiterVisibleInfo.setCityCode(null);
                                recruiterVisibleInfo.setDistrictCode(null);
                                recruiterVisibleInfo.setAddress(null);
                                recruiterVisibleInfo.setAddress(null);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }

            userNameVisibleInfoMapper.updateById(userNameVisibleInfo);
            everyoneVisibleInfoMapper.updateById(everyoneVisibleInfo);
            recruiterVisibleInfoMapper.updateById(recruiterVisibleInfo);
            userPrivacyMapper.modify(modifyDto);
        }
    }
}
