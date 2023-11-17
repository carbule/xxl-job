package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.pojo.po.EmployStatus;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentListVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceListVo;
import com.korant.youya.workplace.pojo.vo.employstatus.EmployStatusVo;
import com.korant.youya.workplace.pojo.vo.employstatus.ResumePreviewVo;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaInfoVo;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateListDto;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePreviewVo;
import com.korant.youya.workplace.service.EmployStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * @Author Duan-zhixiao
     * @Description 简历预览
     * @Date 15:13 2023/11/17
     * @Param
     * @return
     **/
    @Override
    public ResumePreviewVo preview() {

        Long userId = 1L;
        ResumePreviewVo resumePreviewVo = new ResumePreviewVo();

        //        求职意向-意向职位
        List<ExpectedPositionInfoVo> expectedPositionInfoVoList = expectedPositionMapper.findExpectedPositionInfo(userId);
        resumePreviewVo.setExpectedPositionInfoVoList(expectedPositionInfoVoList);

        //        求职意向-期望工作区域
        List<ExpectedWorkAreaInfoVo> expectedWorkAreaInfoVoList = expectedWorkAreaMapper.queryList(userId);
        resumePreviewVo.setExpectedWorkAreaInfoVoList(expectedWorkAreaInfoVoList);

        //        工作履历-项目经验
        List<WorkExperiencePreviewVo> workExperiencePreviewVoList = workExperienceMapper.selectWorkExperienceAndProjectExperienceByUserId(userId);
        resumePreviewVo.setWorkExperiencePreviewVoList(workExperiencePreviewVoList);

        //        教育经历
        List<EducationExperienceListVo> educationExperienceListVoList = educationExperienceMapper.queryList(userId);
        resumePreviewVo.setEducationExperienceListVoList(educationExperienceListVoList);

        //        荣誉证书
        List<HonorCertificateListDto> honorCertificateListDtoList =  honorCertificateMapper.queryList(userId);
        resumePreviewVo.setHonorCertificateListDtoList(honorCertificateListDtoList);

        //        其他附件
        List<AttachmentListVo> attachmentListVoList = attachmentMapper.queryList(userId);
        resumePreviewVo.setAttachmentListVoList(attachmentListVoList);

        return resumePreviewVo;
    }

}
