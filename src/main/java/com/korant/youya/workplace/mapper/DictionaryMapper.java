package com.korant.youya.workplace.mapper;

import com.korant.youya.workplace.pojo.po.Dictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
}
