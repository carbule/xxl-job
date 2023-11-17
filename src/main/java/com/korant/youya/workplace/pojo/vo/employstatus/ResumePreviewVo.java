package com.korant.youya.workplace.pojo.vo.employstatus;

import com.korant.youya.workplace.pojo.vo.attachment.AttachmentListVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceListVo;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaInfoVo;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateListDto;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePreviewVo;
import lombok.Data;

import java.util.List;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/17 14:59
 * @PackageName:com.korant.youya.workplace.pojo.vo.employstatus
 * @ClassName: ResumePreviewVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ResumePreviewVo {

    /**
     * 求职意向-意向职位
     */
    private List<ExpectedPositionInfoVo> expectedPositionInfoVoList;

    /**
     * 求职意向-期望工作区域
     */
    private List<ExpectedWorkAreaInfoVo> expectedWorkAreaInfoVoList;

    /**
     * 工作履历-项目经验
     */
    private List<WorkExperiencePreviewVo> workExperiencePreviewVoList;

    /**
     * 教育经历
     */
    private List<EducationExperienceListVo> educationExperienceListVoList;

    /**
     * 荣誉证书
     */
    private List<HonorCertificateListDto> honorCertificateListDtoList;

    /**
     * 其他附件
     */
    private List<AttachmentListVo> attachmentListVoList;

}
