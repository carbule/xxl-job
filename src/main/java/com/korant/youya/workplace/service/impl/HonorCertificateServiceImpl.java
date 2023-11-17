package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.HonorCertificateMapper;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateCreateDto;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateModifyDto;
import com.korant.youya.workplace.pojo.dto.honorcertificate.HonorCertificateQueryListDto;
import com.korant.youya.workplace.pojo.po.HonorCertificate;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateDetailDto;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateListDto;
import com.korant.youya.workplace.service.HonorCertificateService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private  HonorCertificateMapper honorCertificateMapper;

    /**
     * 查询荣誉证书信息列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<HonorCertificateListDto> queryList(HonorCertificateQueryListDto listDto) {

        Long userId = 1L;
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = honorCertificateMapper.selectCount(new LambdaQueryWrapper<HonorCertificate>().eq(HonorCertificate::getUid, userId).eq(HonorCertificate::getIsDelete, 0));
        List<HonorCertificateListDto> list = honorCertificateMapper.queryHonorCertificateListByUserId(userId, pageNumber, pageSize);
        Page<HonorCertificateListDto> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;

    }

    /**
     * 创建荣誉证书信息
     *
     * @return
     */
    @Override
    public void create(HonorCertificateCreateDto honorCertificateCreateDto) {

        Long userId = 1L;
        Long count = honorCertificateMapper.selectCount(new LambdaQueryWrapper<HonorCertificate>().eq(HonorCertificate::getUid, userId).eq(HonorCertificate::getIsDelete, 0));
        if (count >= 5L) throw new YouyaException("荣誉证书最多上传10个!");
        HonorCertificate honorCertificate = new HonorCertificate();
        BeanUtils.copyProperties(honorCertificateCreateDto, honorCertificate);
        honorCertificate.setUid(userId);
        honorCertificateMapper.insert(honorCertificate);

    }

    /**
     * 修改荣誉证书信息
     *
     * @param
     * @return
     */
    @Override
    public void modify(HonorCertificateModifyDto honorCertificateModifyDto) {

        HonorCertificate honorCertificate = honorCertificateMapper.selectById(honorCertificateModifyDto.getId());
        if (honorCertificate == null) throw new YouyaException("荣誉证书信息不存在!");
        BeanUtils.copyProperties(honorCertificateModifyDto, honorCertificate);
        honorCertificateMapper.updateById(honorCertificate);

    }

    /**
     * 查询荣誉证书信息详情
     *
     * @param id
     * @return
     */
    @Override
    public HonorCertificateDetailDto detail(Long id) {

        return honorCertificateMapper.detail(id);

    }

    /**
     * 删除荣誉证书信息
     *
     * @param
     * @return
     */
    @Override
    public void delete(Long id) {

        HonorCertificate honorCertificate = honorCertificateMapper.selectById(id);
        if (honorCertificate == null) throw new YouyaException("荣誉证书信息不存在!");
        honorCertificate.setIsDelete(1);
        honorCertificateMapper.updateById(honorCertificate);

    }
}
