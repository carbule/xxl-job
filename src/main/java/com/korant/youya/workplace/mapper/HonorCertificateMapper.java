package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateModifyDto;
import com.korant.youya.workplace.pojo.po.HonorCertificate;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateDetailDto;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateListDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 荣誉证书表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface HonorCertificateMapper extends BaseMapper<HonorCertificate> {

    /**
     * 查询荣誉证书信息列表
     *
     * @param
     * @return
     */
    List<HonorCertificateListDto> queryHonorCertificateListByUserId(@Param("userId") Long userId, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);

    /**
     * 查询荣誉证书信息详情
     *
     * @param
     * @return
     */
    HonorCertificateDetailDto detail(@Param("id") Long id);

    /**
     * 查询全部荣誉证书信息列表
     *
     * @param
     * @return
     */
    List<HonorCertificateListDto> queryList(@Param("userId") Long userId);

    /**
     * 修改荣誉证书信息
     *
     * @param
     * @return
     */
    int modify(@Param("honorCertificateModifyDto") HonorCertificateModifyDto honorCertificateModifyDto);
}
