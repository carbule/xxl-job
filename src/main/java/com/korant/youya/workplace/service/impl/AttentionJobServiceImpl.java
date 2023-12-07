package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.AttentionJobMapper;
import com.korant.youya.workplace.pojo.dto.attentionjob.AttentionJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.AttentionJob;
import com.korant.youya.workplace.pojo.vo.attentionjob.AttentionJobPersonalVo;
import com.korant.youya.workplace.service.AttentionJobService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 关注职位表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class AttentionJobServiceImpl extends ServiceImpl<AttentionJobMapper, AttentionJob> implements AttentionJobService {

    @Resource
    private AttentionJobMapper attentionJobMapper;

    /**
     * 查询用户职位关注列表
     *
     * @param personalListDto
     * @return
     */
    @Override
    public Page<AttentionJobPersonalVo> queryPersonalList(AttentionJobQueryPersonalListDto personalListDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = personalListDto.getPageNumber();
        int pageSize = personalListDto.getPageSize();
        Long count = attentionJobMapper.selectCount(new LambdaQueryWrapper<AttentionJob>().eq(AttentionJob::getUid, userId).eq(AttentionJob::getIsDelete, 0));
        List<AttentionJobPersonalVo> list = attentionJobMapper.queryPersonalList(userId, personalListDto);
        Page<AttentionJobPersonalVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 取消职位关注
     *
     * @param id
     */
    @Override
    public void cancel(Long id) {
        attentionJobMapper.deleteById(id);
    }
}
