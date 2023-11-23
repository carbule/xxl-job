package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionCreateDto;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaCreateDto;
import com.korant.youya.workplace.pojo.po.EmployStatus;
import com.korant.youya.workplace.pojo.po.ExpectedPosition;
import com.korant.youya.workplace.pojo.po.ExpectedWorkArea;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceListVo;
import com.korant.youya.workplace.pojo.vo.employstatus.EmployStatusVo;
import com.korant.youya.workplace.pojo.vo.employstatus.ResumePreviewVo;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaInfoVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePreviewVo;
import com.korant.youya.workplace.service.EmployStatusService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    @Resource
    private WorkExperienceMapper workExperienceMapper;
    @Resource
    private EducationExperienceMapper educationExperienceMapper;
    @Resource
    private  HonorCertificateMapper honorCertificateMapper;
    @Resource
    private AttachmentMapper attachmentMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 查询求职状态
     *
     * @param
     **/
    @Override
    public EmployStatusVo status() {

        Long userId = SpringSecurityUtil.getUserId();
        EmployStatusVo employStatusVo = new EmployStatusVo();
        Integer status =  employStatusMapper.status(userId);
        employStatusVo.setStatus(status);

        List<ExpectedPositionInfoVo> expectedPositionInfoVoList = expectedPositionMapper.findExpectedPositionInfo(userId);
        employStatusVo.setExpectedPositionInfoVoList(expectedPositionInfoVoList);

        List<ExpectedWorkAreaInfoVo> expectedWorkAreaInfoVoList = expectedWorkAreaMapper.queryList(userId);
        employStatusVo.setExpectedWorkAreaInfoVoList(expectedWorkAreaInfoVoList);

        return employStatusVo;

    }

    /**
     * 修改求职状态
     *
     * @param employStatusModifyDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(EmployStatusModifyDto employStatusModifyDto) {

        Long userId = SpringSecurityUtil.getUserId();
        EmployStatus employStatus = employStatusMapper.selectOne(new LambdaQueryWrapper<EmployStatus>().eq(EmployStatus::getUid, userId).eq(EmployStatus::getIsDelete, 0));
        if (employStatus != null) {
            //删除历史缓存数据
            employStatus.setIsDelete(1);
            employStatusMapper.updateById(employStatus);
            expectedPositionMapper.update(new ExpectedPosition(), new LambdaUpdateWrapper<ExpectedPosition>().eq(ExpectedPosition::getStatusId, employStatus.getId()).set(ExpectedPosition::getIsDelete, 1));
            expectedWorkAreaMapper.update(new ExpectedWorkArea(), new LambdaUpdateWrapper<ExpectedWorkArea>().eq(ExpectedWorkArea::getStatusId, employStatus.getId()).set(ExpectedWorkArea::getIsDelete, 1));
        }

        EmployStatus status = new EmployStatus();
        status.setUid(userId);
        status.setStatus(employStatusModifyDto.getStatus());
        employStatusMapper.insert(status);

        if (employStatusModifyDto.getExpectedPositionCreateDtos().length != 0){
            List<ExpectedPositionCreateDto> positionCreateDtoList = Arrays.asList(employStatusModifyDto.getExpectedPositionCreateDtos());
            if (positionCreateDtoList.size() > 5) throw new YouyaException("职位最多5个");
            for (ExpectedPositionCreateDto expectedPositionCreateDto: positionCreateDtoList) {
                ExpectedPosition expectedPosition = new ExpectedPosition();
                BeanUtils.copyProperties(expectedPositionCreateDto, expectedPosition);
                expectedPosition.setStatusId(status.getId());
                expectedPosition.setIsDelete(0);
                expectedPosition.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                expectedPositionMapper.insert(expectedPosition);
            }
        }
        if (employStatusModifyDto.getExpectedWorkAreaCreateDtos().length != 0){
            List<ExpectedWorkAreaCreateDto> workAreaCreateDtoList = Arrays.asList(employStatusModifyDto.getExpectedWorkAreaCreateDtos());
            if (workAreaCreateDtoList.size() > 3) throw new YouyaException("工作区域最多3个");
            for (ExpectedWorkAreaCreateDto expectedWorkAreaCreateDto: workAreaCreateDtoList) {
                ExpectedWorkArea expectedWorkArea = new ExpectedWorkArea();
                BeanUtils.copyProperties(expectedWorkAreaCreateDto, expectedWorkArea);
                expectedWorkArea.setStatusId(status.getId());
                expectedWorkArea.setIsDelete(0);
                expectedWorkArea.setCountryCode("100000");
                expectedWorkArea.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                expectedWorkAreaMapper.insert(expectedWorkArea);
            }
        }

    }

    /**
     * @Description 简历预览
     * @Param
     * @return
     **/
    @Override
    public ResumePreviewVo preview() {

        Long userId = SpringSecurityUtil.getUserId();
        ResumePreviewVo resumePreviewVo = userMapper.resumePersonPreview(userId);

        //        求职意向-意向职位
        List<ExpectedPositionInfoVo> expectedPositionInfoVoList = expectedPositionMapper.findExpectedPositionInfo(userId);
        resumePreviewVo.setExpectedPositionInfoVoList(expectedPositionInfoVoList);

        //        求职意向-期望工作区域
        List<ExpectedWorkAreaInfoVo> expectedWorkAreaInfoVoList = expectedWorkAreaMapper.queryList(userId);
        resumePreviewVo.setExpectedWorkAreaInfoVoList(expectedWorkAreaInfoVoList);

        //        工作履历-项目经验
        List<WorkExperiencePreviewVo> workExperiencePreviewVoList = workExperienceMapper.selectWorkExperienceAndProjectExperienceByUserId(userId);
        resumePreviewVo.setWorkExperiencePreviewVoList(workExperiencePreviewVoList);

        //        学历
        if (!CollectionUtils.isEmpty(resumePreviewVo.getEducationExperienceList())){
            Optional<EducationExperienceListVo> max = resumePreviewVo.getEducationExperienceList().stream().max(Comparator.comparingInt(EducationExperienceListVo::getEduLevel));
            resumePreviewVo.setEduLevel(max.get().getEduLevel());
        }

        //        工龄
        if (!CollectionUtils.isEmpty(workExperiencePreviewVoList)){
            Optional<WorkExperiencePreviewVo> min = workExperiencePreviewVoList.stream().min(Comparator.comparing(WorkExperiencePreviewVo::getStartTime));
            String startTime = min.get().getStartTime();
            LocalDate startDate = LocalDate.parse(startTime.substring(0, 6));
            LocalDate today = LocalDate.now();
            Period period = Period.between(startDate, today);
            int years = period.getYears();
            resumePreviewVo.setSeniority(years);
        }

        return resumePreviewVo;
    }

}
