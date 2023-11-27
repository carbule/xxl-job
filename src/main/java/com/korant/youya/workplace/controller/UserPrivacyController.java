package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.userprivacy.UserPrivacyModifyDto;
import com.korant.youya.workplace.pojo.vo.userprivacy.UserPersonalInfoPrivacyVo;
import com.korant.youya.workplace.service.UserPrivacyService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户个人信息隐私设置 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-22
 */
@RestController
@RequestMapping("/userPrivacy")
public class UserPrivacyController {

    @Resource
    private UserPrivacyService userPrivacyService;

    /**
     * 查询个人信息隐私设置
     *
     * @return
     */
    @GetMapping("/queryPersonalInfoPrivacy")
    public R<?> queryPersonalInfoPrivacy() {
        UserPersonalInfoPrivacyVo personalInfoPrivacy = userPrivacyService.queryPersonalInfoPrivacy();
        return R.success(personalInfoPrivacy);
    }

    /**
     * 修改个人信息隐私设置
     *
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid UserPrivacyModifyDto modifyDto) {
        userPrivacyService.modify(modifyDto);
        return R.ok();
    }

    /**
     * 查询名字隐私设置类型
     *
     * @return
     */
    @GetMapping("/queryNameVisibleType")
    public R<?> queryNameVisibleType() {
        List<?> value = userPrivacyService.queryNameVisibleType();
        return R.success(value);
    }

    /**
     * 查询其他隐私设置类型
     *
     * @return
     */
    @GetMapping("/queryOtherInfoVisibleType")
    public R<?> queryOtherInfoVisibleType() {
        List<?> value = userPrivacyService.queryOtherInfoVisibleType();
        return R.success(value);
    }
}
