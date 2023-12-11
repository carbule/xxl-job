package com.korant.youya.workplace.pojo.vo.user;

import com.korant.youya.workplace.annotations.Dict;
import com.korant.youya.workplace.pojo.vo.attachment.AttachmentVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceVo;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaVo;
import com.korant.youya.workplace.pojo.vo.honorcertificate.HonorCertificateVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePreviewVo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ResumePreviewVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/8 16:05
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
     * 用户年龄
     */
    private Integer age;

    /**
     * 学历
     */
    @Dict(categoryCode = "education")
    private Integer eduLevel;

    /**
     * 工作时长
     */
    private Integer workExperience;

    /**
     * 政治面貌
     */
    @Dict(categoryCode = "political_outlook")
    private Integer politicalOutlook;

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
     * 自我评价
     */
    private String selfEvaluation;

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
    private List<WorkExperiencePreviewVo> workExperienceVoList;

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
