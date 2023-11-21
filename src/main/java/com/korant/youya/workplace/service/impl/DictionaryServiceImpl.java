package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.DictionaryMapper;
import com.korant.youya.workplace.pojo.DictVo;
import com.korant.youya.workplace.pojo.po.Dictionary;
import com.korant.youya.workplace.service.DictionaryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-21
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Resource
    private DictionaryMapper dictionaryMapper;

    /**
     * 根据字典分类查询字典列表
     *
     * @param category
     * @return
     */
    @Override
    public List<DictVo> queryListByCategory(String category) {
        return dictionaryMapper.queryListByCategory(category);
    }
}
