package com.korant.youya.workplace.service.impl;

import com.korant.youya.workplace.pojo.dto.graph.SharedDto;
import com.korant.youya.workplace.pojo.vo.user.UserGraphVo;
import com.korant.youya.workplace.repository.UserGraphRepo;
import com.korant.youya.workplace.service.GraphSharedService;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.neo4j.driver.Driver;
import org.neo4j.driver.summary.SummaryCounters;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GraphSharedServiceImpl implements GraphSharedService {

    @Resource
    private Driver neoDriver;

    @Resource
    private UserGraphRepo userGraphRepo;

    /**
     * 根据候选人id和职位id 查出职位分享链
     *
     * @param userId 候选人id
     * @param jobId  职位id
     * @return
     */
    @Override
    public List<UserGraphVo> findJobShared(Long userId, Long jobId) {
        var users = new ArrayList<UserGraphVo>();
        var cql = "match g=(fu:User)-[:Shared {type: 'job'}]->*(tu:User {id: $user_id}) " +
                "where fu.id<>$user_id and all(r in relationships(g) where r.target_id=$job_id and r.ishr=false) " +
                "return distinct fu.id, fu.name";
        var result = neoDriver.executableQuery(cql).withParameters(Map.of("user_id", userId, "job_id", jobId)).execute().records();
        if (!result.isEmpty()) {
            for (var i : result) {
                var user = new UserGraphVo();
                user.setId(Long.parseLong(i.values().get(0).toString()));
                user.setName(i.values().get(1).asString());
                users.add(user);
            }
            return users;
        } else {
            return null;
        }
    }

    /**
     * 根据内推人id和求职id 查出求职分享链
     *
     * @param userId    内推人id
     * @param huntJobId 求职id
     * @return
     */
    @Override
    public List<UserGraphVo> findHuntJobShared(Long userId, Long huntJobId) {
        var users = new ArrayList<UserGraphVo>();
        var cql = "match g=(fu:User)-[:Shared {type: 'huntjob'}]->*(tu:User {id: $user_id}) " +
                "where fu.id<>$user_id and all(r in relationships(g) where r.target_id=$huntjob_id and r.ishr=false) " +
                "return distinct fu.id, fu.name";
        var result = neoDriver.executableQuery(cql).withParameters(Map.of("user_id", userId, "huntjob_id", huntJobId)).execute().records();
        if (!result.isEmpty()) {
            for (var i : result) {
                var user = new UserGraphVo();
                user.setId(Long.parseLong(i.values().get(0).toString()));
                user.setName(i.values().get(1).asString());
                users.add(user);
            }
            return users;
        } else {
            return null;
        }
    }

    /**
     * 判断分享链是否已经存在
     *
     * @param fUserId
     * @param tUserId
     * @param targetId
     * @return
     */
    @Override
    public boolean existShared(Long fUserId, Long tUserId, Long targetId) {
        var cql = "match (fu:User {id: $fu_id})-[r:Shared]->(tu:User {id: $tu_id}) where r.target_id=$target_id return r";
        var result = neoDriver.executableQuery(cql).withParameters(Map.of("fu_id", fUserId, "tu_id", tUserId, "target_id", targetId)).execute();
        return !result.records().isEmpty();
    }

    /**
     * 根据候选人id和职位id 查出职位分享链中最后分享的人
     *
     * @param userId 候选人id
     * @param jobId  职位id
     * @return
     */
    @Override
    public UserGraphVo findJobSharer(Long userId, Long jobId) {
        var user = new UserGraphVo();
        var cql = "match (fu:User)-[r:Shared {type: 'job'}]->(tu:User {id: $user_id}) " +
                "where fu.id<>$user_id and r.target_id=$job_id and r.ishr=false " +
                "with fu, r order by r.timestamp limit 1 return fu.id, fu.name";
        var result = neoDriver.executableQuery(cql).withParameters(Map.of("user_id", userId, "job_id", jobId)).execute().records();
        if (!result.isEmpty()) {
            for (var i : result) {
                user.setId(Long.parseLong(i.values().get(0).toString()));
                user.setName(i.values().get(1).asString());
            }
            return user;
        } else {
            return null;
        }
    }

    @Override
    public SummaryCounters insertJobShared(@NotNull SharedDto sharedDto) {
        var fromUserRecord = userGraphRepo.findById(sharedDto.getToUserId());
        var toUserRecord = userGraphRepo.findById(sharedDto.getToUserId());
        var cql = "match (fu:User {id: $from_user_id}), (tu:User {id: $to_user_id}) create (fu)-[r:Shared {type: 'job'}]->(tu) " +
                "set r.target_id = $target_id, r.ishr = $ishr, r.timestamp = $ts";
        if (fromUserRecord.isPresent() && toUserRecord.isPresent()) {
            var result = neoDriver.executableQuery(cql).
                    withParameters(Map.of("from_user_id", sharedDto.getFromUserId(), "to_user_id", sharedDto.getToUserId(),
                            "target_id", sharedDto.getTargetId(), "ishr", sharedDto.getIsHr(), "ts", sharedDto.getTimestamp())).
                    execute();
            return result.summary().counters();
        } else {
            return null;
        }
    }

    @Override
    public SummaryCounters insertHuntJobShared(@NotNull SharedDto sharedDto) {
        var fromUserRecord = userGraphRepo.findById(sharedDto.getToUserId());
        var toUserRecord = userGraphRepo.findById(sharedDto.getToUserId());
        var cql = "match (fu:User {id: $from_user_id}), (tu:User {id: $to_user_id}) create (fu)-[r:Shared {type: 'huntjob'}]->(tu) " +
                "set r.target_id = $target_id, r.ishr = $ishr, r.timestamp = $ts";
        if (fromUserRecord.isPresent() && toUserRecord.isPresent()) {
            var result = neoDriver.executableQuery(cql).
                    withParameters(Map.of("from_user_id", sharedDto.getFromUserId(), "to_user_id", sharedDto.getToUserId(),
                            "target_id", sharedDto.getTargetId(), "ishr", sharedDto.getIsHr(), "ts", sharedDto.getTimestamp())).
                    execute();
            return result.summary().counters();
        } else {
            return null;
        }
    }
}
