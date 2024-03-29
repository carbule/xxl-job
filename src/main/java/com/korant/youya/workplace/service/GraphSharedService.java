package com.korant.youya.workplace.service;

import com.korant.youya.workplace.pojo.dto.graph.SharedDto;
import com.korant.youya.workplace.pojo.vo.user.UserGraphVo;
import org.jetbrains.annotations.NotNull;
import org.neo4j.driver.summary.SummaryCounters;

import java.util.List;

// 对Shared图的操作
public interface GraphSharedService {

    /**
     * 查找职位分享链
     *
     * @param userId
     * @param jobId
     * @return
     */
    List<UserGraphVo> findJobShared(Long userId, Long jobId);

    /**
     * 查找求职分享链
     *
     * @param userId
     * @param huntJobId
     * @return
     */
    List<UserGraphVo> findHuntJobShared(Long userId, Long huntJobId);

    /**
     * 判断两个用户节点之间是否存在分享关系
     *
     * @param fUserId
     * @param tUserId
     * @param targetId
     * @return
     */
    boolean existShared(Long fUserId, Long tUserId, Long targetId);

    /**
     * 判断职位分享链中的直接推荐人
     *
     * @param userId
     * @param jobId
     * @return
     */
    UserGraphVo findJobSharer(Long userId, Long jobId);

    /**
     * 数据同步，建立职位分享链
     *
     * @param sharedDto
     * @return
     */
    SummaryCounters insertJobShared(@NotNull SharedDto sharedDto);

    /**
     * 数据同步，建立求职分享链
     *
     * @param sharedDto
     * @return
     */
    SummaryCounters insertHuntJobShared(@NotNull SharedDto sharedDto);
}
