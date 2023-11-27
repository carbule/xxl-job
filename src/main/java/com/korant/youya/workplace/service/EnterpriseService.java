package com.korant.youya.workplace.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.po.Enterprise;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseDetailVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByNameVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByUserVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 企业信息表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EnterpriseService extends IService<Enterprise> {

    /**
     * 获取营业执照信息
     *
     * @param file
     * @return
     */
    JSONObject getBusinessLicenseInfo(MultipartFile file);

    /**
     * 创建企业
     *
     * @param createDto
     * @return
     */
    void create(EnterpriseCreateDto createDto);

    /**
     * 修改企业logo
     *
     * @param
     * @return
     */
    void modifyLogo(EnterpriseModifyLogoDto modifyLogoDto);

    /**
     * 修改企业
     *
     * @param
     * @return
     */
    void modify(EnterpriseModifyDto modifyDto);

    /**
     * 根据当前登陆用户查询企业信息
     *
     * @param
     * @return
     */
    EnterpriseInfoByUserVo queryEnterpriseInfoByUser();

    /**
     * 查询企业详细信息
     *
     * @return
     */
    EnterpriseDetailVo detail(Long id);

    /**
     * 变更企业
     *
     * @param
     * @return
     */
    void changeEnterpriseInfo(EnterpriseChangeDto changeDto);

    /**
     * 关联企业-根据企业名称查询企业
     *
     * @return
     */
    List<EnterpriseInfoByNameVo> getEnterpriseByName(EnterpriseQueryListDto enterpriseQueryListDto);

}
