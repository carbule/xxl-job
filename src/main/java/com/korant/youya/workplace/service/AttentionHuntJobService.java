package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.attentionhuntjob.AttentionHuntJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.AttentionHuntJob;
import com.korant.youya.workplace.pojo.vo.attentionhuntjob.AttentionHuntJobPersonalVo;

/**
 * <p>
 * 关注求职表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface AttentionHuntJobService extends IService<AttentionHuntJob> {

    /**
     * 查询用户求职关注列表
     *
     * @param personalListDto
     * @return
     */
    Page<AttentionHuntJobPersonalVo> queryPersonalList(AttentionHuntJobQueryPersonalListDto personalListDto);

    /**
     * 取消求职关注
     *
     * @param id
     */
    void cancel(Long id);
}
