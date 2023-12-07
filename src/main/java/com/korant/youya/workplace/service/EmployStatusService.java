package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.pojo.po.EmployStatus;
import com.korant.youya.workplace.pojo.vo.employstatus.PersonalHuntJobIntentionVo;

/**
 * <p>
 * 求职状态表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
public interface EmployStatusService extends IService<EmployStatus> {

    /**
     * 查询个人求职意向
     *
     * @return
     */
    PersonalHuntJobIntentionVo queryPersonalHuntJobIntention();

    /**
     * 修改求职意向
     *
     * @param modifyDto
     */
    void modify(EmployStatusModifyDto modifyDto);
}
