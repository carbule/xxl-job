package com.korant.youya.workplace.pojo.vo.employstatus;

import com.korant.youya.workplace.annotations.Dict;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentListVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceListVo;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaInfoVo;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateListDto;
import com.korant.youya.workplace.pojo.vo.user.ResumePersonPreviewVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePreviewVo;
import lombok.Data;

import java.util.List;

/**
 * @Date 2023/11/17 14:59
 * @PackageName:com.korant.youya.workplace.pojo.vo.employstatus
 * @ClassName: ResumePreviewVo
 * @Description:
 * @Version 1.0
 */
@Data
public class ResumePreviewVo {

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
     * 用户性别
     */
    private Integer gender;

    /**
     * 用户生日
     */
    private Integer age;

    /**
     * 学历
     */
    @Dict(categoryCode = "education")
    private Integer eduLevel;

    /**
     * 工龄
     */
    private Integer seniority;

    /**
     * 政治面貌
     */
    @Dict(categoryCode = "political_outlook")
    private Integer politicalOutlook;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 自我评价
     */
    private String selfEvaluation;

    /**
     * 实名认证状态
     */
    private Integer authenticationStatus;

    /**
     * 求职状态
     */
    @Dict(categoryCode = "employ_status")
    private Integer employStatus;

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
     * 国家名称
     */
    private String countryName;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 行政区名称
     */
    private String districtName;

    /**
     *  详细地址
     */
    private String address;

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
    private List<EducationExperienceListVo> educationExperienceList;

    /**
     * 荣誉证书
     */
    private List<HonorCertificateListDto> honorCertificateList;

    /**
     * 其他附件
     */
    private List<AttachmentListVo> attachmentList;

}
