package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.job.JobModifyDto;
import com.korant.youya.workplace.pojo.po.Job;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface JobMapper extends BaseMapper<Job> {

    /**
     * 修改职位信息
     *
     * @param modifyDto
     * @return
     */
    int modify(@Param("modifyDto") JobModifyDto modifyDto);
}
