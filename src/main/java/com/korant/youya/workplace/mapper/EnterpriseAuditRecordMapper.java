package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.EnterpriseAuditRecord;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 企业审核记录表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EnterpriseAuditRecordMapper extends BaseMapper<EnterpriseAuditRecord> {

    /**
     * 查询企业审核未通过原因
     *
     * @return
     */
    String getRefuseReason(@Param("id") Long id);

}
