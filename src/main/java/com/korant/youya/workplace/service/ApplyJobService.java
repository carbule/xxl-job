package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.applyjob.ApplyJobQueryListDto;
import com.korant.youya.workplace.pojo.po.ApplyJob;
import com.korant.youya.workplace.pojo.vo.applyjob.ApplyJobDetailVo;
import com.korant.youya.workplace.pojo.vo.applyjob.ApplyJobVo;

/**
 * <p>
 * 职位申请表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface ApplyJobService extends IService<ApplyJob> {

    /**
     * 查询用户已申请职位列表
     *
     * @param listDto
     * @return
     */
    Page<ApplyJobVo> queryList(ApplyJobQueryListDto listDto);

    /**
     * 查询用户已申请职位详情
     *
     * @param id
     * @return
     */
    ApplyJobDetailVo detail(Long id);

    /**
     * 接受面试邀约
     *
     * @param id
     */
    void acceptInterview(Long id);

    /**
     * 接受入职邀约
     *
     * @param id
     */
    void acceptOnboarding(Long id);

    /**
     * 接受转正邀约
     *
     * @param id
     */
    void acceptConfirmation(Long id);
}
