package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaModifyDto;
import com.korant.youya.workplace.pojo.po.ExpectedWorkArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户期望工作区域表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface ExpectedWorkAreaMapper extends BaseMapper<ExpectedWorkArea> {

    /**
     * 根据意向id查询意向区域id
     *
     * @param statusId
     * @return
     */
    List<Long> selectListByStatusId(@Param("statusId") Long statusId);

    /**
     * 批量删除意向区域
     *
     * @param list
     * @return
     */
    int batchDelete(@Param("list") List<Long> list);

    /**
     * 批量新增意向区域
     *
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<ExpectedWorkAreaModifyDto> list);

    /**
     * 批量修改意向区域
     *
     * @param modifyWorkAreaList
     * @return
     */
    int batchModify(@Param("list") List<ExpectedWorkAreaModifyDto> modifyWorkAreaList);

    /**
     * 根据用户id查询意向区域数量
     *
     * @param userId
     * @return
     */
    int selectCountByUserId(@Param("userId") Long userId);
}
