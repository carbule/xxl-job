package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.intendedposition.IntendedPositionCreateDto;
import com.korant.youya.workplace.pojo.dto.intendedposition.IntendedPositionModifyDto;
import com.korant.youya.workplace.pojo.po.IntendedPosition;
import com.korant.youya.workplace.pojo.vo.intendedposition.IntendedPositionInfoVo;

import java.util.List;

/**
 * <p>
 * 用户意向职位表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface IntendedPositionService extends IService<IntendedPosition> {

    List<IntendedPositionInfoVo> findIntendedPositionInfo();

    void create(IntendedPositionCreateDto intendedPositionCreateDto);

    void modify(IntendedPositionModifyDto intendedPositionIncreaseDto);

    void delete(Long id);
}
