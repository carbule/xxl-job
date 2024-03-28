package com.korant.youya.workplace.service;

import com.korant.youya.workplace.pojo.po.UserGraph;
import org.neo4j.driver.summary.SummaryCounters;

import java.util.List;


// 对User节点的增删改查操作
public interface GraphUserService {

    /**
     * 查询所有用户
     *
     * @return
     */
    List<UserGraph> findAll();

    /**
     * 根据id查找用户
     *
     * @param id
     * @return
     */
    UserGraph findById(Long id);

    /**
     * 新增用户
     *
     * @param user
     */
    void insert(UserGraph user);

    /**
     * 用户软删除
     *
     * @param id
     * @return
     */
    SummaryCounters deleteById(Long id);

    /**
     * 用户软恢复
     *
     * @param id
     * @return
     */
    SummaryCounters recoverById(Long id);
}
