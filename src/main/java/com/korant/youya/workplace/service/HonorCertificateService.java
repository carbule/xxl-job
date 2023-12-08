package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateCreateDto;
import com.korant.youya.workplace.pojo.po.HonorCertificate;

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
     * 创建荣誉证书信息
     *
     * @param createDto
     */
    void create(HonorCertificateCreateDto createDto);

    /**
     * 删除荣誉证书信息
     *
     * @param id
     */
    void delete(Long id);
}
