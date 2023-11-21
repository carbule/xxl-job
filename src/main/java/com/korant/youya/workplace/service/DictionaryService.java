package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.DictVo;
import com.korant.youya.workplace.pojo.po.Dictionary;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-21
 */
public interface DictionaryService extends IService<Dictionary> {

    /**
     * 根据字典分类查询字典列表
     *
     * @param category
     * @return
     */
    List<DictVo> queryListByCategory(String category);
}
