package com.korant.youya.workplace.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.DistrictDataMapper;
import com.korant.youya.workplace.mapper.UserHistoricalLocationMapper;
import com.korant.youya.workplace.pojo.dto.userhistoricallocation.UserHistoricalLocationCreateDto;
import com.korant.youya.workplace.pojo.po.DistrictData;
import com.korant.youya.workplace.pojo.po.UserHistoricalLocation;
import com.korant.youya.workplace.pojo.vo.userhistoricallocation.UserHistoricalLocationVo;
import com.korant.youya.workplace.service.UserHistoricalLocationService;
import com.korant.youya.workplace.utils.RedisUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户历史定位城市 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-26
 */
@Service
public class UserHistoricalLocationServiceImpl extends ServiceImpl<UserHistoricalLocationMapper, UserHistoricalLocation> implements UserHistoricalLocationService {

    @Resource
    private UserHistoricalLocationMapper userHistoricalLocationMapper;

    @Resource
    private DistrictDataMapper districtDataMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 创建用户历史定位信息
     *
     * @param createDto
     */
    @Override
    public void create(UserHistoricalLocationCreateDto createDto) {
        String cityCode = createDto.getCityCode();
        DistrictData districtData = districtDataMapper.selectOne(new LambdaQueryWrapper<DistrictData>().eq(DistrictData::getCode, cityCode).eq(DistrictData::getIsDelete, 0));
        if (null == districtData) throw new YouyaException("城市信息不存在");
        String cityName = districtData.getName();
        Long userId = SpringSecurityUtil.getUserId();
        UserHistoricalLocation historicalLocation = userHistoricalLocationMapper.selectOne(new LambdaQueryWrapper<UserHistoricalLocation>().eq(UserHistoricalLocation::getUid, userId).eq(UserHistoricalLocation::getCityCode, cityCode).eq(UserHistoricalLocation::getIsDelete, 0));
        if (null == historicalLocation) {
            historicalLocation = new UserHistoricalLocation();
            historicalLocation.setUid(userId).setCityCode(cityCode).setCityName(cityName).setUpdateTime(LocalDateTime.now());
            userHistoricalLocationMapper.insert(historicalLocation);
        } else {
            userHistoricalLocationMapper.updateById(historicalLocation);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cityCode", cityCode);
        jsonObject.put("cityName", cityName);
        redisUtil.incrementScore(RedisConstant.YY_USER_LOCATION, jsonObject.toJSONString(), 1);
    }

    /**
     * 查询用户最新定位
     *
     * @return
     */
    @Override
    public UserHistoricalLocationVo queryLatestLocation() {
        Long userId = SpringSecurityUtil.getUserId();
        return userHistoricalLocationMapper.queryLatestLocation(userId);
    }
}
