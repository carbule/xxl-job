package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.DictVo;
import com.korant.youya.workplace.pojo.po.Dictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-21
 */
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    /**
     * 根据dictCode和dictValue翻译字典
     *
     * @param categoryCode
     * @param fieldValue
     * @return
     */
    String translateDict(@Param("categoryCode") String categoryCode, @Param("fieldValue") Integer fieldValue);

    /**
     * 根据字典分类查询字典列表
     *
     * @param category
     * @return
     */
    List<DictVo> queryListByCategory(@Param("category") String category);
}
