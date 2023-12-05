package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.attentionjob.AttentionJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.AttentionJob;
import com.korant.youya.workplace.pojo.vo.attentionjob.AttentionJobPersonalListVo;

/**
 * <p>
 * 关注职位表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface AttentionJobService extends IService<AttentionJob> {

    /**
     * 查询用户职位关注列表
     *
     * @param personalListDto
     * @return
     */
    Page<AttentionJobPersonalListVo> queryPersonalList(AttentionJobQueryPersonalListDto personalListDto);

    /**
     * 取消职位关注
     *
     * @param id
     */
    void cancel(Long id);
}
