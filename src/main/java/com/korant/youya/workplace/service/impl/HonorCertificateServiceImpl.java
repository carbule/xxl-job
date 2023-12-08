package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.HonorCertificateMapper;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateCreateDto;
import com.korant.youya.workplace.pojo.po.HonorCertificate;
import com.korant.youya.workplace.service.HonorCertificateService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 荣誉证书表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class HonorCertificateServiceImpl extends ServiceImpl<HonorCertificateMapper, HonorCertificate> implements HonorCertificateService {

    @Resource
    private HonorCertificateMapper honorCertificateMapper;

    /**
     * 创建荣誉证书信息
     *
     * @param createDto
     */
    @Override
    public void create(HonorCertificateCreateDto createDto) {
        Long userId = SpringSecurityUtil.getUserId();
        Long count = honorCertificateMapper.selectCount(new LambdaQueryWrapper<HonorCertificate>().eq(HonorCertificate::getUid, userId).eq(HonorCertificate::getIsDelete, 0));
        if (count >= 10L) throw new YouyaException("荣誉证书不得超过10个");
        HonorCertificate honorCertificate = new HonorCertificate();
        BeanUtils.copyProperties(createDto, honorCertificate, "obtainTime");
        honorCertificate.setObtainTime(createDto.getObtainTime().toString());
        honorCertificate.setUid(userId);
        honorCertificateMapper.insert(honorCertificate);
    }

    /**
     * 删除荣誉证书信息
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        HonorCertificate honorCertificate = honorCertificateMapper.selectOne(new LambdaQueryWrapper<HonorCertificate>().eq(HonorCertificate::getId, id).eq(HonorCertificate::getIsDelete, 0));
        if (null == honorCertificate) throw new YouyaException("荣誉证书不存在");
        honorCertificate.setIsDelete(1);
        honorCertificateMapper.updateById(honorCertificate);
    }
}
