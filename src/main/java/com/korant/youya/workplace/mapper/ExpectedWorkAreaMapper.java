package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaCreateDto;
import com.korant.youya.workplace.pojo.po.ExpectedWorkArea;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaInfoVo;
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
     * 查询用户期望工作区域
     *
     * @return
     */
    List<ExpectedWorkAreaInfoVo> queryList(@Param("userId") Long userId);

    /**
     * 查询用户期望工作区域总数
     *
     * @return
     */
    Long selectCountByUserId(@Param("userId") Long userId);

    void insertBatch(@Param("workAreaCreateDtoList") List<ExpectedWorkAreaCreateDto> workAreaCreateDtoList, @Param("id") Long id);

    /**
     * 删除期望工作区域
     *
     * @return
     */
    int updateByStatus(@Param("id") Long id);
}
