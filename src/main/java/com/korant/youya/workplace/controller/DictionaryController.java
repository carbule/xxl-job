package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.DictVo;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.service.DictionaryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-21
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Resource
    private DictionaryService dictionaryService;

    /**
     * 根据字典分类查询字典列表
     *
     * @return
     */
    @GetMapping("/queryListByCategory/{category}")
    public R<?> queryListByCategory(@PathVariable("category") String category) {
        List<DictVo> dictVoList = dictionaryService.queryListByCategory(category);
        return R.success(dictVoList);
    }
}
