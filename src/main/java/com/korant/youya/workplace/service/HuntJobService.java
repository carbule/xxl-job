package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobCreateDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobModifyDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryListDto;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobDetailOnHomePageVo;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobDetailVo;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobListOnHomePageVo;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobPreviewVo;

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
     * 查询首页求职信息列表
     *
     * @param listDto
     * @return
     */
    Page<HuntJobListOnHomePageVo> queryListOnHomePage(HuntJobQueryListDto listDto);

    /**
     * 根据求职id查询首页求职信息详情
     *
     * @param id
     * @return
     */
    HuntJobDetailOnHomePageVo queryDetailOnHomePageById(Long id);

    /**
     * 收藏或取消收藏求职信息
     *
     * @param id
     */
    void collect(Long id);

    /**
     * 校验用户信息
     */
    void checkUserInfo();

    /**
     * 求职预览
     *
     * @return
     */
    HuntJobPreviewVo preview();

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
