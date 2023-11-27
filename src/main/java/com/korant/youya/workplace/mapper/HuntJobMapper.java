package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobModifyDto;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobPreviewVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 求职表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface HuntJobMapper extends BaseMapper<HuntJob> {

    /**
     * 求职预览
     *
     * @param userId
     * @return
     */
    HuntJobPreviewVo preview(@Param("userId") Long userId);

    /**
     * 修改求职信息
     *
     * @param modifyDto
     * @return
     */
    int modify(@Param("modifyDto") HuntJobModifyDto modifyDto);
}
