package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.JobSubTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-03-29
 */
public interface JobSubTaskMapper extends BaseMapper<JobSubTask> {

    /**
     * 批量插入子任务
     *
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<JobSubTask> list);
}
