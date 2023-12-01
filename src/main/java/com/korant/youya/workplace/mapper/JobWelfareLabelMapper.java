package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.JobWelfareLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 职位福利标签表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface JobWelfareLabelMapper extends BaseMapper<JobWelfareLabel> {

    /**
     * 批量添加职位福利标签
     *
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<JobWelfareLabel> list);

    /**
     * 根据职位id查询福利标签id
     *
     * @param jobId
     * @return
     */
    List<Long> selectWelfareLabelIdListByJobId(@Param("jobId") Long jobId);

    /**
     * 根据职位id批量修改
     *
     * @param jobId
     * @param list
     * @return
     */
    int batchModify(@Param("jobId") Long jobId, @Param("list") List<Long> list);
}
