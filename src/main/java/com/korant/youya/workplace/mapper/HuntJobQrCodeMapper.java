package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.HuntJobQrCode;
import com.korant.youya.workplace.pojo.vo.huntjobqrcode.HuntJobQrcodeData;
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
     * 根据二维码id获取二维码数据
     *
     * @param id
     * @return
     */
    HuntJobQrcodeData getData(@Param("id") Long id);
}
