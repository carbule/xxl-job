package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionModifyDto;
import com.korant.youya.workplace.pojo.po.ExpectedPosition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户意向职位表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
public interface ExpectedPositionMapper extends BaseMapper<ExpectedPosition> {

    /**
     * 根据意向id查询意向职位id
     *
     * @param statusId
     * @return
     */
    List<Long> selectListByStatusId(@Param("statusId") Long statusId);

    /**
     * 批量删除意向职位
     *
     * @param list
     */
    int batchDelete(@Param("list") List<Long> list);

    /**
     * 批量新增意向职位
     *
     * @param list
     */
    int batchInsert(@Param("list") List<ExpectedPositionModifyDto> list);

    /**
     * 批量修改意向职位
     *
     * @param modifyPositionList
     * @return
     */
    int batchModify(@Param("list") List<ExpectedPositionModifyDto> modifyPositionList);

    /**
     * 根据用户id查询意向职位数量
     *
     * @param userId
     * @return
     */
    int selectCountByUserId(@Param("userId") Long userId);
}
