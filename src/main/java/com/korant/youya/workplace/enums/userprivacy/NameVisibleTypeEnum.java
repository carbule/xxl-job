package com.korant.youya.workplace.enums.userprivacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName NameVisibleTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/22 19:31
 * @Version 1.0
 */
public enum NameVisibleTypeEnum {

    FULL_NAME("显示全名", 1),
    ONLY_LAST_NAME("只显示姓（如王先生，李女士）", 2),
    ONLY_FIRST_NAME("只显示名", 3);

    private String name;

    private Integer value;

    NameVisibleTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * 根据value换取name
     *
     * @param value
     * @return
     */
    public static String getNameByValue(Integer value) {
        return Arrays.stream(NameVisibleTypeEnum.values()).filter(s -> s.getValue().equals(value)).map(NameVisibleTypeEnum::getName).findFirst().orElse(null);
    }

    /**
     * 获取所有枚举值
     *
     * @return
     */
    public static List<?> getAllEnumsValue() {
        NameVisibleTypeEnum[] enums = NameVisibleTypeEnum.values();
        List<NameVisibleType> list = new ArrayList<>();
        for (NameVisibleTypeEnum typeEnum : enums) {
            NameVisibleType nameVisibleType = new NameVisibleType();
            nameVisibleType.setName(typeEnum.getName());
            nameVisibleType.setValue(typeEnum.getValue());
            list.add(nameVisibleType);
        }
        return list;
    }

    public static void main(String[] args) throws JsonProcessingException {
    }

    @Data
    static class NameVisibleType {
        private String name;

        private Integer value;
    }

}

