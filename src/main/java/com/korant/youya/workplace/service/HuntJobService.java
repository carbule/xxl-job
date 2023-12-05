package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobCreateDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobModifyDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryHomePageListDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.pojo.vo.huntjob.*;

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
     * @return
     */
    Page<HuntJobHomePageListVo> queryHomePageList(HuntJobQueryHomePageListDto listDto);

    /**
     * 根据求职id查询首页求职信息详情
     *
     * @param id
     * @return
     */
    HuntJobHomePageDetailVo queryHomePageDetailById(Long id);

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
    Page<HuntJobPersonalListVo> queryPersonalList(HuntJobQueryPersonalListDto personalListDto);

    /**
     * 校验用户信息
     */
    void checkUserInfo();

    /**
     * 查询个人意向职位列表
     *
     * @return
     */
    List<PersonalExpectedPositionListVo> queryPersonalExpectedPositionList();

    /**
     * 查询个人意向区域列表
     *
     * @return
     */
    List<PersonalExpectedWorkAreaListVo> queryPersonalExpectedWorkAreaList();

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
