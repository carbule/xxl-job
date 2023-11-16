package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaCreateDto;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaModifyDto;
import com.korant.youya.workplace.pojo.po.ExpectedWorkArea;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaInfoVo;

import java.util.List;

/**
 * <p>
 * 用户期望工作区域表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface ExpectedWorkAreaService extends IService<ExpectedWorkArea> {

    List<ExpectedWorkAreaInfoVo> queryList();

    void create(ExpectedWorkAreaCreateDto expectedWorkAreaCreateDto);

    void modify(ExpectedWorkAreaModifyDto expectedWorkAreaModifyDto);

    void delete(Long id);
}
