package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.AttentionHuntJobMapper;
import com.korant.youya.workplace.pojo.dto.attentionhuntjob.AttentionHuntJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.AttentionHuntJob;
import com.korant.youya.workplace.pojo.vo.attentionhuntjob.AttentionHuntJobPersonalVo;
import com.korant.youya.workplace.service.AttentionHuntJobService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 关注求职表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class AttentionHuntJobServiceImpl extends ServiceImpl<AttentionHuntJobMapper, AttentionHuntJob> implements AttentionHuntJobService {

    @Resource
    private AttentionHuntJobMapper attentionHuntJobMapper;

    /**
     * 查询用户求职关注列表
     *
     * @param personalListDto
     * @return
     */
    @Override
    public Page<AttentionHuntJobPersonalVo> queryPersonalList(AttentionHuntJobQueryPersonalListDto personalListDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = personalListDto.getPageNumber();
        int pageSize = personalListDto.getPageSize();
        Long count = attentionHuntJobMapper.selectCount(new LambdaQueryWrapper<AttentionHuntJob>().eq(AttentionHuntJob::getUid, userId).eq(AttentionHuntJob::getIsDelete, 0));
        List<AttentionHuntJobPersonalVo> list = attentionHuntJobMapper.queryPersonalList(userId, personalListDto);
        Page<AttentionHuntJobPersonalVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    @Override
    public void cancel(Long id) {
        attentionHuntJobMapper.deleteById(id);
    }
}
