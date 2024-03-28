package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.po.UserGraph;
import com.korant.youya.workplace.service.GraphUserService;
import jakarta.annotation.Resource;
import org.neo4j.driver.summary.SummaryCounters;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/graph/user")
public class GraphUserController {
    @Resource
    private GraphUserService userService;

    /**
     * 查询所有用户
     *
     * @return
     */
    @GetMapping("/findAll")
    public R<List<UserGraph>> findAll() {
        var data = userService.findAll();
        if (!data.isEmpty()) {
            return R.success(data);
        } else {
            return R.error("查询所有用户失败");
        }

    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @GetMapping("/findById/{id}")
    public R<UserGraph> findById(@PathVariable Long id) {
        var data = userService.findById(id);
        if (data != null) {
            return R.success(data);
        } else {
            return R.error("查询用户失败");
        }
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @PostMapping("/insert")
    public R<Void> insertUser(@RequestBody UserGraph user) {
        userService.insert(user);
        return R.ok(null);
    }

    /**
     * 软删除用户
     *
     * @param id
     * @return
     */
    @PatchMapping("/del/{id}")
    public R<SummaryCounters> deletedById(@PathVariable Long id) {
        var result = userService.deleteById(id);
        if (result.propertiesSet() == 1) {
            return R.ok();
        } else {
            return R.error("软删除用户失败");
        }
    }

    /**
     * 恢复用户
     *
     * @param id
     * @return
     */
    @PatchMapping("/rcv/{id}")
    public R<SummaryCounters> recoverById(@PathVariable Long id) {
        var result = userService.recoverById(id);
        if (result.propertiesSet() == 1) {
            return R.ok();
        } else {
            return R.error("恢复用户失败");
        }
    }
}
