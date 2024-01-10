package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.internalrecommend.InternalRecommendQueryListDto;
import com.korant.youya.workplace.pojo.po.InternalRecommend;
import com.korant.youya.workplace.pojo.vo.internalrecommend.InternalRecommendDetailVo;
import com.korant.youya.workplace.pojo.vo.internalrecommend.InternalRecommendVo;

/**
 * <p>
 * 内部推荐表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface InternalRecommendService extends IService<InternalRecommend> {

    /**
     * 查询用户被推荐职位列表
     *
     * @param listDto
     * @return
     */
    Page<InternalRecommendVo> queryList(InternalRecommendQueryListDto listDto);

    /**
     * 查询用户被推荐职位详情
     *
     * @param id
     * @return
     */
    InternalRecommendDetailVo detail(Long id);

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
