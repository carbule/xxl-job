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

    Page<HonorCertificateListDto> queryList(HonorCertificateQueryListDto listDto);

    void create(HonorCertificateCreateDto honorCertificateCreateDto);

    void modify(HonorCertificateModifyDto honorCertificateModifyDto);

    HonorCertificateDetailDto detail(Long id);

    void delete(Long id);

}
