package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.pojo.po.EmployStatus;
import com.korant.youya.workplace.pojo.vo.employstatus.EmployStatusVo;
import com.korant.youya.workplace.pojo.vo.employstatus.ResumePreviewVo;

/**
 * <p>
 * 求职状态表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
public interface EmployStatusService extends IService<EmployStatus> {

    EmployStatusVo status();

    void create(EmployStatusModifyDto employStatusModifyDto);

    ResumePreviewVo preview();
}
