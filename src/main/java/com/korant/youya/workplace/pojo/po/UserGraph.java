package com.korant.youya.workplace.pojo.po;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * @ClassName UserGraph
 * @Description neo4j用户实体类 类似于关系数据库中的表名
 * @Author chenyiqiang
 * @Date 2024/3/28 15:56
 * @Version 1.0
 */
@Data
@Node("User")
public class UserGraph {

    @Id
    private Long id;

    @Property("is_deleted")
    private Boolean isDeleted = false;

    //Neo4j中每个节点必须有"name"属性，且不能重复，可以是关系数据库中的id值
    //由于手机号也是唯一的，类似于用户名，方便以后做数据分析
    @Property("name")
    private String phone;
}
