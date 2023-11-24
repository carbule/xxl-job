package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.UserEnterprise;
import com.korant.youya.workplace.pojo.vo.userenterprise.UserEnterpriseColleagueInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户企业关联表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface UserEnterpriseMapper extends BaseMapper<UserEnterprise> {

    /**
     * 根据姓名查询公司同事总数
     *
     * @param
     * @return
     */
    Integer queryCountColleagueByName(@Param("id") Long id, @Param("name") String name);

    /**
     * 根据姓名查询公司同事
     *
     * @param
     * @return
     */
    List<UserEnterpriseColleagueInfoVo> queryColleagueByName(@Param("id") Long id, @Param("name") String name, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);
}
