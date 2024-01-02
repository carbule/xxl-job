package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.InternalRecommendMapper;
import com.korant.youya.workplace.pojo.dto.internalrecommend.InternalRecommendQueryListDto;
import com.korant.youya.workplace.pojo.po.InternalRecommend;
import com.korant.youya.workplace.pojo.vo.internalrecommend.InternalRecommendVo;
import com.korant.youya.workplace.service.InternalRecommendService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 内部推荐表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@Service
public class InternalRecommendServiceImpl extends ServiceImpl<InternalRecommendMapper, InternalRecommend> implements InternalRecommendService {

    @Resource
    private InternalRecommendMapper internalRecommendMapper;

    /**
     * 查询用户被推荐职位列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<InternalRecommendVo> queryList(InternalRecommendQueryListDto listDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        int count = internalRecommendMapper.queryListCount(userId);
        List<InternalRecommendVo> list = internalRecommendMapper.queryList(userId, listDto);
        Page<InternalRecommendVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }
}
