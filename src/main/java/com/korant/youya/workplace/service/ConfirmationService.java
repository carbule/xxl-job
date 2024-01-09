package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.po.Confirmation;
import com.korant.youya.workplace.pojo.vo.confirmation.ConfirmationVo;

/**
 * <p>
 * 转正记录表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface ConfirmationService extends IService<Confirmation> {

    /**
     * 查询转正邀请列表
     *
     * @param listDto
     * @return
     */
    Page<ConfirmationVo> queryList(ConfirmationQueryListDto listDto);
}
