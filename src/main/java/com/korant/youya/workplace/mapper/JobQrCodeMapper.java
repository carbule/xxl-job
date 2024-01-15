package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.jobqrcode.JobQrCodeQueryListDto;
import com.korant.youya.workplace.pojo.po.JobQrCode;
import com.korant.youya.workplace.pojo.vo.jobqrcode.JobQrCodeDetailVo;
import com.korant.youya.workplace.pojo.vo.jobqrcode.JobQrcodeData;
import com.korant.youya.workplace.pojo.vo.jobqrcode.JobSharingVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 职位推荐二维码表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface JobQrCodeMapper extends BaseMapper<JobQrCode> {

    /**
     * 查询职位分享列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    JobSharingVo queryList(@Param("userId") Long userId, @Param("listDto") JobQrCodeQueryListDto listDto);

    /**
     * 查询分享职位详情
     *
     * @param id
     * @return
     */
    JobQrCodeDetailVo queryJobDetail(@Param("id") Long id);

    /**
     * 根据二维码id获取二维码数据
     *
     * @param id
     * @return
     */
    JobQrcodeData getData(@Param("id") Long id);
}
