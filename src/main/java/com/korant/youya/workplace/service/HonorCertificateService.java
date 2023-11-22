package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateCreateDto;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateModifyDto;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateQueryListDto;
import com.korant.youya.workplace.pojo.po.HonorCertificate;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateDetailDto;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateListDto;

/**
 * <p>
 * 荣誉证书表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface HonorCertificateService extends IService<HonorCertificate> {

    /**
     * 查询荣誉证书信息列表
     *
     * @param listDto
     * @return
     */
    Page<HonorCertificateListDto> queryList(HonorCertificateQueryListDto listDto);

    /**
     * 创建荣誉证书信息
     *
     * @return
     */
    void create(HonorCertificateCreateDto honorCertificateCreateDto);

    /**
     * 修改荣誉证书信息
     *
     * @param
     * @return
     */
    void modify(HonorCertificateModifyDto honorCertificateModifyDto);

    /**
     * 查询荣誉证书信息详情
     *
     * @param id
     * @return
     */
    HonorCertificateDetailDto detail(Long id);

    /**
     * 删除荣誉证书信息
     *
     * @param
     * @return
     */
    void delete(Long id);

}
