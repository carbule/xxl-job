package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionCreateDto;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionModifyDto;
import com.korant.youya.workplace.pojo.po.ExpectedPosition;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoVo;

import java.util.List;

/**
 * <p>
 * 用户意向职位表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
public interface ExpectedPositionService extends IService<ExpectedPosition> {

    List<ExpectedPositionInfoVo> findExpectedPositionInfo();

    void create(ExpectedPositionCreateDto expectedPositionCreateDto);

    void modify(ExpectedPositionModifyDto expectedPositionModifyDto);

    void delete(Long id);
}
