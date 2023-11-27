package com.korant.youya.workplace.enums.userprivacy;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName OtherInfoVisibleTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/22 19:57
 * @Version 1.0
 */
public enum OtherInfoVisibleTypeEnum {

    VISIBLE_TO_EVERYONE("所有人可见", 1),
    VISIBLE_TO_RECRUITERS("只对招聘方可见", 2),
    VISIBLE_TO_ONESELF("只对自己可见", 3);

    private String name;

    private Integer value;

    OtherInfoVisibleTypeEnum(String name, Integer value) {
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
        return Arrays.stream(OtherInfoVisibleTypeEnum.values()).filter(s -> s.getValue().equals(value)).map(OtherInfoVisibleTypeEnum::getName).findFirst().orElse(null);
    }

    /**
     * 获取所有枚举值
     *
     * @return
     */
    public static List<?> getAllEnumsValue() {
        OtherInfoVisibleTypeEnum[] enums = OtherInfoVisibleTypeEnum.values();
        List<OtherInfoVisibleType> list = new ArrayList<>();
        for (OtherInfoVisibleTypeEnum typeEnum : enums) {
            OtherInfoVisibleType otherInfoVisibleType = new OtherInfoVisibleType();
            otherInfoVisibleType.setName(typeEnum.getName());
            otherInfoVisibleType.setValue(typeEnum.getValue());
            list.add(otherInfoVisibleType);
        }
        return list;
    }

    @Data
    static class OtherInfoVisibleType {
        private String name;

        private Integer value;
    }
}
