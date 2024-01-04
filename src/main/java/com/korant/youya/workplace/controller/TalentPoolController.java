package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.service.TalentPoolService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TalentPoolController
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/4 17:35
 * @Version 1.0
 */
@RestController
@RequestMapping("/talentPool")
public class TalentPoolController {

    @Resource
    private TalentPoolService talentPoolService;


}
