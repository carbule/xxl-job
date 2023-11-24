package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobCreateDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobModifyDto;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobDetailVo;

/**
 * <p>
 * 求职表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface HuntJobService extends IService<HuntJob> {

    /**
     * 校验用户信息
     */
    void checkUserInfo();

    /**
     * 创建求职信息
     *
     * @param createDto
     */
    void create(HuntJobCreateDto createDto);

    /**
     * 修改求职信息
     *
     * @param modifyDto
     */
    void modify(HuntJobModifyDto modifyDto);

    /**
     * 查询求职信息详情
     *
     * @param id
     * @return
     */
    HuntJobDetailVo detail(Long id);

    /**
     * 删除求职信息
     *
     * @param id
     */
    void delete(Long id);
}
