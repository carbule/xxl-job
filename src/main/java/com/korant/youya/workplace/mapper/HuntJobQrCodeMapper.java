package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.huntjobqrcode.huntJobQrCodeQueryListDto;
import com.korant.youya.workplace.pojo.po.HuntJobQrCode;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobQrcodeData;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobRecommendVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 求职推荐二维码表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface HuntJobQrCodeMapper extends BaseMapper<HuntJobQrCode> {

    /**
     * 查询求职推荐列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    HuntJobRecommendVo queryList(@Param("userId") Long userId, @Param("listDto") huntJobQrCodeQueryListDto listDto);

    /**
     * 根据二维码id获取二维码数据
     *
     * @param id
     * @return
     */
    HuntJobQrcodeData getData(@Param("id") Long id);
}
