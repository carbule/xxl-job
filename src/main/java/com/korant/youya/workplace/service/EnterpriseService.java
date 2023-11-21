package com.korant.youya.workplace.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.po.Enterprise;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseDetailVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByNameVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByUserVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 企业信息表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EnterpriseService extends IService<Enterprise> {

    JSONObject getBusinessLicenseInfo(MultipartFile file);

    void create(EnterpriseCreateDto createDto);

    void modifyLogo(EnterpriseModifyLogoDto modifyLogoDto);

    void modify(EnterpriseModifyDto modifyDto);

    EnterpriseInfoByUserVo queryEnterpriseInfoByUser();

    EnterpriseDetailVo detail(Long id);

    void changeEnterpriseInfo(EnterpriseChangeDto changeDto);

    Page<EnterpriseInfoByNameVo> getEnterpriseByName(EnterpriseQueryListDto enterpriseQueryListDto);

    void Unbinding(Long id);
}
