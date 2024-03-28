package com.korant.youya.workplace.repository;

import com.korant.youya.workplace.pojo.po.UserGraph;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

//Neo4j的Dao层，类似于MyBatis中的Mapper
@Repository
public interface UserGraphRepo extends Neo4jRepository<UserGraph, Long> {
}
