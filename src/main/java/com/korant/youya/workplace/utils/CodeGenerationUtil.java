package com.korant.youya.workplace.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.builder.Service;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.function.ConverterFileName;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CodeGenerationUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2023/7/21 10:42
 * @Version 1.0
 */
public class CodeGenerationUtil {

    public static void main(String[] args) {

        //数据源
        DataSourceConfig.Builder datasourceBuilder = new DataSourceConfig.Builder("jdbc:postgresql://139.9.240.37:5432/workplace_dev?charset=utf-8&sslmode=disable", "postgres", "korant@123");
        DataSourceConfig dataSourceConfig = datasourceBuilder.build();

        //创建对象
        AutoGenerator autoGenerator = new AutoGenerator(dataSourceConfig);

        //全局配置
        GlobalConfig.Builder globalConfigBuilder = new GlobalConfig.Builder();
        globalConfigBuilder.outputDir(System.getProperty("user.dir") + "/src/main/java");
        globalConfigBuilder.author("chenyiqiang");
        globalConfigBuilder.disableOpenDir();
        GlobalConfig globalConfig = globalConfigBuilder.build();
        autoGenerator.global(globalConfig);

        //包配置
        PackageConfig.Builder builder = new PackageConfig.Builder();
        builder.parent("com.korant.youya.workplace");
        builder.entity("pojo.po");
        builder.mapper("mapper");
        builder.service("service");
        builder.serviceImpl("service.impl");
        builder.controller("controller");
        Map<OutputFile, String> map = new HashMap<>();
        map.put(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper");
        builder.pathInfo(map);
        PackageConfig packageConfig = builder.build();
        autoGenerator.packageInfo(packageConfig);

        //策略配置
        StrategyConfig.Builder strategyConfigBuilder = new StrategyConfig.Builder();
        //配置生成的表名 不配置生成所有的表
        strategyConfigBuilder.addInclude("yy_dictionary","yy_dictionary_category");
        //过滤表前缀
        strategyConfigBuilder.addTablePrefix("yy_");

        //controller设置
        Controller.Builder controllerBuilder = strategyConfigBuilder.controllerBuilder();
        //启用restful风格
        controllerBuilder.enableRestStyle();
        //启用文件覆盖功能
//        controllerBuilder.enableFileOverride();

        //entity设置
        Entity.Builder entityBuilder = strategyConfigBuilder.entityBuilder();
        //开启驼峰命名
        entityBuilder.columnNaming(NamingStrategy.underline_to_camel);
        entityBuilder.naming(NamingStrategy.underline_to_camel);
        //启用lombok注解
        entityBuilder.enableLombok();
        //启用字段注解
        entityBuilder.enableTableFieldAnnotation();
        //设置主键生成规则
        entityBuilder.idType(IdType.ASSIGN_ID);
        //启用链式编程
        entityBuilder.enableChainModel();
        //启用文件覆盖
        entityBuilder.enableFileOverride();
        //设置字段自动填充类型
        Column createTime = new Column("create_time", FieldFill.INSERT);
        Column updateTime = new Column("update_time", FieldFill.UPDATE);
        Column isDelete = new Column("is_delete", FieldFill.INSERT);
        entityBuilder.addTableFills(createTime, updateTime, isDelete);

        //service设置
        Service.Builder serviceBuilder = strategyConfigBuilder.serviceBuilder();
        //启用文件覆盖
//        serviceBuilder.enableFileOverride();
        //自定义Service层名称
        serviceBuilder.convertServiceFileName(new ConverterFileName() {
            @Override
            public @NotNull String convert(String entityName) {
                return entityName + "Service";
            }
        });

        //mapper设置
        Mapper.Builder mapperBuilder = strategyConfigBuilder.mapperBuilder();
        //启用文件覆盖
//        mapperBuilder.enableFileOverride();

        StrategyConfig strategyConfig = strategyConfigBuilder.build();
        autoGenerator.strategy(strategyConfig);

        //启动
        autoGenerator.execute();
    }
}
