package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobCreateDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobModifyDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryListDto;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobDetailVo;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobPersonInfoVo;
import com.korant.youya.workplace.service.HuntJobService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 求职表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/huntJob")
public class HuntJobController {

    @Resource
    private HuntJobService huntJobService;

    /**
     * 校验用户信息
     *
     * @return
     */
    @GetMapping("/checkUserInfo")
    public R<?> checkUserInfo() {
        huntJobService.checkUserInfo();
        return R.ok();
    }

    /**
     * 创建求职信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid HuntJobCreateDto createDto) {
        huntJobService.create(createDto);
        return R.ok();
    }

    /**
     * 修改求职信息
     *
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid HuntJobModifyDto modifyDto) {
        huntJobService.modify(modifyDto);
        return R.ok();
    }

    /**
     * 查询求职信息详情
     *
     * @param
     * @return
     */
    @GetMapping("/detail/{id}")
    public R<?> detail(@PathVariable("id") Long id) {
        HuntJobDetailVo detailVo = huntJobService.detail(id);
        return R.success(detailVo);
    }

    /**
     * 删除求职信息
     *
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        huntJobService.delete(id);
        return R.ok();
    }


    /**
     * 根据职位查看求职者
     *
     * @param
     * @return
     */
    @PostMapping("/queryListByPositionCode")
    public R<?> queryListByPositionCode(@RequestBody @Valid HuntJobQueryListDto huntJobQueryListDto) {
        Page<HuntJobPersonInfoVo> page = huntJobService.queryListByPositionCode(huntJobQueryListDto);
        return R.success(page);
    }

}
