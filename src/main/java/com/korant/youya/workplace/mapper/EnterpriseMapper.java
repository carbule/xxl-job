package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.enterprise.EnterpriseModifyDto;
import com.korant.youya.workplace.pojo.dto.enterprise.EnterpriseModifyLogoDto;
import com.korant.youya.workplace.pojo.po.Enterprise;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseDetailVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByNameVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 企业信息表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EnterpriseMapper extends BaseMapper<Enterprise> {

    /**
     * @Author Duan-zhixiao
     * @Description 修改企业logo
     * @Date 14:33 2023/11/20
     * @Param
     * @return
     **/
    void modifyLogo(@Param("modifyLogoDto") EnterpriseModifyLogoDto modifyLogoDto);

    /**
     * @Author Duan-zhixiao
     * @Description 修改企业
     * @Date 14:34 2023/11/20
     * @Param
     * @return
     **/
    void modify(@Param("modifyDto") EnterpriseModifyDto modifyDto);

    /**
     * @Author Duan-zhixiao
     * @Description 根据当前登陆用户查询企业信息
     * @Date 14:38 2023/11/20
     * @Param
     * @return
     **/
    EnterpriseInfoByUserVo queryEnterpriseInfoByUser(@Param("userId") Long userId);

    /**
     * @Author Duan-zhixiao
     * @Description 查询企业详细信息
     * @Date 14:44 2023/11/20
     * @Param
     * @return
     **/
    EnterpriseDetailVo detail(@Param("id") Long id);

    /**
     * @Author Duan-zhixiao
     * @Description 根据企业名称查询企业
     * @Date 17:02 2023/11/20
     * @Param
     * @return
     **/
    List<EnterpriseInfoByNameVo> getEnterpriseByName(@Param("name") String name, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);

}
