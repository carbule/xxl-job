package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.ExpectedWorkAreaMapper;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaCreateDto;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaModifyDto;
import com.korant.youya.workplace.pojo.po.ExpectedWorkArea;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaInfoVo;
import com.korant.youya.workplace.service.ExpectedWorkAreaService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户期望工作区域表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class ExpectedWorkAreaServiceImpl extends ServiceImpl<ExpectedWorkAreaMapper, ExpectedWorkArea> implements ExpectedWorkAreaService {

    @Resource
    private ExpectedWorkAreaMapper expectedWorkAreaMapper;

    /**
     * 查询用户期望工作区域
     *
     * @return
     */
    @Override
    public List<ExpectedWorkAreaInfoVo> queryList() {

        Long userId = SpringSecurityUtil.getUserId();
        return expectedWorkAreaMapper.queryList(userId);

    }

    /**
     * 添加期望工作区域
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ExpectedWorkAreaCreateDto expectedWorkAreaCreateDto) {

        Long userId = SpringSecurityUtil.getUserId();
        Long count = expectedWorkAreaMapper.selectCountByUserId(userId);
        if (count >= 3) throw new YouyaException("您最多只能添加三个期望工作区域");
        ExpectedWorkArea expectedWorkArea = new ExpectedWorkArea();
        BeanUtils.copyProperties(expectedWorkAreaCreateDto, expectedWorkArea);
        expectedWorkAreaMapper.insert(expectedWorkArea);

    }

    /**
     * 编辑期望工作区域
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ExpectedWorkAreaModifyDto expectedWorkAreaModifyDto) {

        ExpectedWorkArea expectedWorkArea = expectedWorkAreaMapper.selectById(expectedWorkAreaModifyDto.getId());
        if (expectedWorkArea == null) throw new YouyaException("用户期望工作区域不存在!");
        BeanUtils.copyProperties(expectedWorkAreaModifyDto, expectedWorkArea);
        expectedWorkAreaMapper.updateById(expectedWorkArea);

    }

    /**
     * 删除期望工作区域
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        ExpectedWorkArea expectedWorkArea = expectedWorkAreaMapper.selectById(id);
        if (expectedWorkArea == null) throw new YouyaException("用户期望工作区域不存在!");
        expectedWorkArea.setIsDelete(1);
        expectedWorkAreaMapper.updateById(expectedWorkArea);

    }

}
