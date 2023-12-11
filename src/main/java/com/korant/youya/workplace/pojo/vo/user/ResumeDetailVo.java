package com.korant.youya.workplace.pojo.vo.user;

import com.korant.youya.workplace.annotations.Dict;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceVo;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaVo;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateVo;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceVo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ResumeDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/8 16:04
 * @Version 1.0
 */
@Data
public class ResumeDetailVo {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名字
     */
    private String firstName;

    /**
     * 实名认证状态
     */
    private Integer authenticationStatus;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户微信号
     */
    private String wechatId;

    /**
     * 用户QQ号
     */
    private String qq;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 国家编码
     */
    private String countryName;

    /**
     * 省份编码
     */
    private String provinceName;

    /**
     * 市级编码
     */
    private String cityName;

    /**
     * 行政区编码
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 状态
     */
    @Dict(categoryCode = "employ_status")
    private Integer employStatus;

    /**
     * 意向职位集
     */
    private List<ExpectedPositionVo> expectedPositionVoList;

    /**
     * 意向区域集
     */
    private List<ExpectedWorkAreaVo> expectedWorkAreaVoList;

    /**
     * 工作履历集
     */
    private List<WorkExperienceVo> workExperienceVoList;

    /**
     * 项目经验集
     */
    private List<ProjectExperienceVo> projectExperienceVoList;

    /**
     * 教育经历集
     */
    private List<EducationExperienceVo> educationExperienceVoList;

    /**
     * 荣誉证书集
     */
    private List<HonorCertificateVo> honorCertificateVoList;

    /**
     * 其他附件集
     */
    private List<AttachmentVo> attachmentVoList;
}
