package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.pojo.po.EmployStatus;
import com.korant.youya.workplace.mapper.EmployStatusMapper;
import com.korant.youya.workplace.pojo.vo.employstatus.EmployStatusVo;
import com.korant.youya.workplace.service.EmployStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 求职状态表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
@Service
public class EmployStatusServiceImpl extends ServiceImpl<EmployStatusMapper, EmployStatus> implements EmployStatusService {

    @Resource
    private EmployStatusMapper employStatusMapper;

    /**
     * 查询求职状态
     *
     * @param
     **/
    @Override
    public EmployStatusVo status() {

        Long userId = 1L;
        return employStatusMapper.status(userId);

    }

    /**
     * 修改求职状态
     *
     * @param employStatusModifyDto
     */
    @Override
    public void modify(EmployStatusModifyDto employStatusModifyDto) {

        Long userId = 1L;
        employStatusMapper.update(new EmployStatus(),
                new LambdaUpdateWrapper<EmployStatus>()
                        .eq(EmployStatus::getUid, userId)
                        .set(EmployStatus::getStatus, employStatusModifyDto.getStatus()));

    }
}
