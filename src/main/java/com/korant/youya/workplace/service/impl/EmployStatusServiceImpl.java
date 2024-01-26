package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.EmployStatusMapper;
import com.korant.youya.workplace.mapper.ExpectedPositionMapper;
import com.korant.youya.workplace.mapper.ExpectedWorkAreaMapper;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionModifyDto;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaModifyDto;
import com.korant.youya.workplace.pojo.po.EmployStatus;
import com.korant.youya.workplace.pojo.vo.employstatus.PersonalHuntJobIntentionVo;
import com.korant.youya.workplace.service.EmployStatusService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private ExpectedPositionMapper expectedPositionMapper;

    @Resource
    private ExpectedWorkAreaMapper expectedWorkAreaMapper;

    private static final String CHINA_CODE = "100000";

    /**
     * 查询个人求职意向
     *
     * @return
     */
    @Override
    public PersonalHuntJobIntentionVo queryPersonalHuntJobIntention() {
        Long userId = SpringSecurityUtil.getUserId();
        return employStatusMapper.queryPersonalHuntJobIntention(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(EmployStatusModifyDto modifyDto) {
        Long userId = SpringSecurityUtil.getUserId();
        EmployStatus employStatus = employStatusMapper.selectOne(new LambdaQueryWrapper<EmployStatus>().eq(EmployStatus::getUid, userId).eq(EmployStatus::getIsDelete, 0));
        if (null == employStatus) {
            employStatus = new EmployStatus();
            Integer status = modifyDto.getStatus();
            if (null != status) employStatus.setStatus(status);
            employStatus.setUid(userId);
            employStatusMapper.insert(employStatus);
            Long statusId = employStatus.getId();
            List<ExpectedPositionModifyDto> positionAddDtoList = modifyDto.getPositionModifyDtoList();
            List<ExpectedWorkAreaModifyDto> workAreaAddDtoList = modifyDto.getWorkAreaModifyDtoList();
            if (positionAddDtoList.size() > 5)
                throw new YouyaException("意向职位最多5个");
            if (workAreaAddDtoList.size() > 3)
                throw new YouyaException("意向区域最多3个");
            if (positionAddDtoList.size() > 0) {
                positionAddDtoList.forEach(s -> {
                    s.setPositionId(IdWorker.getId());
                    s.setStatusId(statusId);
                });
                expectedPositionMapper.batchInsert(positionAddDtoList);
            }
            if (workAreaAddDtoList.size() > 0) {
                workAreaAddDtoList.forEach(s -> {
                    s.setAreaId(IdWorker.getId());
                    s.setStatusId(statusId);
                    s.setCountryCode(CHINA_CODE);
                });
                expectedWorkAreaMapper.batchInsert(workAreaAddDtoList);
            }
        } else {
            Long statusId = employStatus.getId();
            List<ExpectedPositionModifyDto> positionModifyDtoList = modifyDto.getPositionModifyDtoList();
            List<ExpectedWorkAreaModifyDto> workAreaModifyDtoList = modifyDto.getWorkAreaModifyDtoList();
            List<Long> oldPositionIdList = expectedPositionMapper.selectListByStatusId(statusId);
            List<Long> oldWorkAreaIdList = expectedWorkAreaMapper.selectListByStatusId(statusId);
            List<Long> newPositionIdList = positionModifyDtoList.stream().map(ExpectedPositionModifyDto::getPositionId).collect(Collectors.toList());
            List<Long> newWorkAreaIdList = workAreaModifyDtoList.stream().map(ExpectedWorkAreaModifyDto::getAreaId).collect(Collectors.toList());
            List<Long> removedPositionIdList = oldPositionIdList.stream()
                    .filter(e -> !newPositionIdList.contains(e))
                    .collect(Collectors.toList());
            List<Long> removedWorkAreaIdList = oldWorkAreaIdList.stream()
                    .filter(e -> !newWorkAreaIdList.contains(e))
                    .collect(Collectors.toList());
            List<Long> positionSameIdList = oldPositionIdList.stream()
                    .filter(newPositionIdList::contains)
                    .collect(Collectors.toList());
            List<Long> workAreaSameIdList = oldWorkAreaIdList.stream()
                    .filter(newWorkAreaIdList::contains)
                    .collect(Collectors.toList());
            List<ExpectedPositionModifyDto> modifyPositionList = positionModifyDtoList.stream().filter(s -> positionSameIdList.contains(s.getPositionId())).collect(Collectors.toList());
            List<ExpectedWorkAreaModifyDto> modifyWorkAreaList = workAreaModifyDtoList.stream().filter(s -> workAreaSameIdList.contains(s.getAreaId())).collect(Collectors.toList());
            List<ExpectedPositionModifyDto> addPositionList = positionModifyDtoList.stream().filter(s -> null == s.getPositionId()).collect(Collectors.toList());
            List<ExpectedWorkAreaModifyDto> addWorkAreaList = workAreaModifyDtoList.stream().filter(s -> null == s.getAreaId()).collect(Collectors.toList());
            if (oldPositionIdList.size() - removedPositionIdList.size() + addPositionList.size() > 5)
                throw new YouyaException("意向职位最多5个");
            if (oldWorkAreaIdList.size() - removedWorkAreaIdList.size() + addWorkAreaList.size() > 3)
                throw new YouyaException("意向区域最多3个");
            if (removedPositionIdList.size() > 0) {
                expectedPositionMapper.batchDelete(removedPositionIdList);
            }
            if (removedWorkAreaIdList.size() > 0) {
                expectedWorkAreaMapper.batchDelete(removedWorkAreaIdList);
            }
            if (addPositionList.size() > 0) {
                addPositionList.forEach(s -> {
                    s.setPositionId(IdWorker.getId());
                    s.setStatusId(statusId);
                });
                expectedPositionMapper.batchInsert(addPositionList);
            }
            if (addWorkAreaList.size() > 0) {
                addWorkAreaList.forEach(s -> {
                    s.setAreaId(IdWorker.getId());
                    s.setStatusId(statusId);
                    s.setCountryCode(CHINA_CODE);
                });
                expectedWorkAreaMapper.batchInsert(addWorkAreaList);
            }
            if (modifyPositionList.size() > 0) {
                expectedPositionMapper.batchModify(modifyPositionList);
            }
            if (modifyWorkAreaList.size() > 0) {
                expectedWorkAreaMapper.batchModify(modifyWorkAreaList);
            }
            Integer status = modifyDto.getStatus();
            if (null != status) {
                employStatus.setStatus(status);
                employStatusMapper.updateById(employStatus);
            }
        }
    }
}
