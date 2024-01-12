package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.huntjob.*;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.pojo.vo.expectedposition.PersonalExpectedPositionVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.PersonalExpectedWorkAreaVo;
import com.korant.youya.workplace.pojo.vo.huntjob.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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
     * @param request
     * @return
     */
    Page<HuntJobHomePageVo> queryHomePageList(HuntJobQueryHomePageListDto listDto, HttpServletRequest request);

    /**
     * 根据求职id查询首页求职信息详情
     *
     * @param id
     * @return
     */
    HuntJobHomePageDetailVo queryHomePageDetailById(Long id);

    /**
     * 查询hr列表
     *
     * @return
     */
    List<EnterpriseHRVo> queryHRList();

    /**
     * 内推
     *
     * @param recommendDto
     */
    void recommend(HuntJobRecommendDto recommendDto);

    /**
     * 根据id查询分享信息
     *
     * @param id
     * @return
     */
    HuntJobShareInfo queryShareInfo(Long id);

    /**
     * 收藏或取消收藏求职信息
     *
     * @param id
     */
    void collect(Long id);

    /**
     * 查询用户个人求职列表
     *
     * @param personalListDto
     * @return
     */
    Page<HuntJobPersonalVo> queryPersonalList(HuntJobQueryPersonalListDto personalListDto);

    /**
     * 校验用户信息
     */
    void checkUserInfo();

    /**
     * 查询个人意向职位列表
     *
     * @return
     */
    List<PersonalExpectedPositionVo> queryPersonalExpectedPositionList();

    /**
     * 查询个人意向区域列表
     *
     * @return
     */
    List<PersonalExpectedWorkAreaVo> queryPersonalExpectedWorkAreaList();

    /**
     * 求职发布预览
     *
     * @return
     */
    HuntJobPublishPreviewVo publishPreview();

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
     * 根据id预览求职详细信息
     *
     * @param id
     * @return
     */
    HuntJobDetailsPreviewVo detailsPreview(Long id);

    /**
     * 根据id查询求职信息详情
     *
     * @param id
     * @return
     */
    HuntJobDetailVo detail(Long id);

    /**
     * 根据id关闭职位
     *
     * @param id
     */
    void close(Long id);

    /**
     * 根据id发布职位
     *
     * @param id
     */
    void release(Long id);

    /**
     * 删除求职信息
     *
     * @param id
     */
    void delete(Long id);
}
