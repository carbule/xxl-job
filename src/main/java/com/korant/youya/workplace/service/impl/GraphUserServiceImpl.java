package com.korant.youya.workplace.service.impl;

import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.pojo.po.UserGraph;
import com.korant.youya.workplace.repository.UserGraphRepo;
import com.korant.youya.workplace.service.GraphUserService;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.neo4j.driver.Driver;
import org.neo4j.driver.summary.SummaryCounters;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GraphUserServiceImpl implements GraphUserService {

    @Resource
    private UserGraphRepo userGraphRepo;

    @Resource
    private Driver neoDriver;

    @Override
    public List<UserGraph> findAll() {
        return userGraphRepo.findAll();
    }

    @Override
    public UserGraph findById(Long id) {
        var result = userGraphRepo.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new YouyaException("该用户不存在");
        }
    }

    @Override
    public void insert(@NotNull UserGraph user) {
        if (userGraphRepo.existsById(user.getId())) {
            throw new YouyaException("用户已存在");
        }
        try {
            userGraphRepo.save(user);
        } catch (Exception e) {
            throw new YouyaException("用户添加失败");
        }
    }

    @Override
    public SummaryCounters deleteById(Long id) {
        var userRecord = userGraphRepo.findById(id);
        var cql = "match (u:User {id: $user_id}) set u.is_deleted = true";
        if (userRecord.isPresent()) {
            if (!userRecord.get().getIsDeleted()) {
                var result = neoDriver.executableQuery(cql).withParameters(Map.of("user_id", id)).execute();
                return result.summary().counters();
            }
        }
        return null;
    }

    @Override
    public SummaryCounters recoverById(Long id) {
        var userRecord = userGraphRepo.findById(id);
        var cql = "match (u:User {id: $user_id}) set u.is_deleted = false";
        if (userRecord.isPresent()) {
            if (userRecord.get().getIsDeleted()) {
                var result = neoDriver.executableQuery(cql).withParameters(Map.of("user_id", id)).execute();
                return result.summary().counters();
            }
        }
        return null;
    }
}
