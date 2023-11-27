package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.time.format.DateTimeFormatter;
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
    private UserMapper userMapper;

    /**
     * 查询求职状态
     *
     * @param
     **/
    @Override
    public EmployStatusVo status() {

        Long userId = SpringSecurityUtil.getUserId();
        EmployStatusVo employStatusVo = employStatusMapper.queryStatus(userId);

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
            expectedPositionMapper.updateByStatus(employStatus.getId());
            expectedWorkAreaMapper.updateByStatus(employStatus.getId());
        }

        EmployStatus status = new EmployStatus();
        status.setUid(userId);
        status.setStatus(employStatusModifyDto.getStatus());
        employStatusMapper.insert(status);

        if (employStatusModifyDto.getExpectedPositionCreateDtos().length != 0){
            List<ExpectedPositionCreateDto> positionCreateDtoList = Arrays.asList(employStatusModifyDto.getExpectedPositionCreateDtos());
            if (positionCreateDtoList.size() > 5) throw new YouyaException("职位最多5个");
            Integer sort = 1;
            for (ExpectedPositionCreateDto expectedPositionCreateDto: positionCreateDtoList) {
                ExpectedPosition expectedPosition = new ExpectedPosition();
                BeanUtils.copyProperties(expectedPositionCreateDto, expectedPosition);
                expectedPosition.setStatusId(status.getId());
                expectedPosition.setSort(sort);
                expectedPosition.setIsDelete(0);
                expectedPosition.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                expectedPositionMapper.insert(expectedPosition);
                sort++;
            }
        }
        if (employStatusModifyDto.getExpectedWorkAreaCreateDtos().length != 0){
            List<ExpectedWorkAreaCreateDto> workAreaCreateDtoList = Arrays.asList(employStatusModifyDto.getExpectedWorkAreaCreateDtos());
            if (workAreaCreateDtoList.size() > 3) throw new YouyaException("工作区域最多3个");
            Integer sort = 1;
            for (ExpectedWorkAreaCreateDto expectedWorkAreaCreateDto: workAreaCreateDtoList) {
                ExpectedWorkArea expectedWorkArea = new ExpectedWorkArea();
                BeanUtils.copyProperties(expectedWorkAreaCreateDto, expectedWorkArea);
                expectedWorkArea.setStatusId(status.getId());
                expectedWorkArea.setSort(sort);
                expectedWorkArea.setIsDelete(0);
                expectedWorkArea.setCountryCode("100000");
                expectedWorkArea.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                expectedWorkAreaMapper.insert(expectedWorkArea);
                sort++;
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
        EmployStatusVo employStatusVo = employStatusMapper.queryStatus(userId);
        //避免用户未填写求职意向
        if(employStatusVo != null){
            resumePreviewVo.setEmployStatus(employStatusVo.getStatus());
            //        求职意向-意向职位
            resumePreviewVo.setExpectedPositionInfoVoList(employStatusVo.getExpectedPositionInfoVoList());
            //        求职意向-期望工作区域
            resumePreviewVo.setExpectedWorkAreaInfoVoList(employStatusVo.getExpectedWorkAreaInfoVoList());
        }

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
            LocalDate startDate = LocalDate.parse(startTime + "-01" , DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate today = LocalDate.now();
            Period period = Period.between(startDate, today);
            int years = period.getYears();
            resumePreviewVo.setSeniority(years);
        }

        return resumePreviewVo;
    }

}
