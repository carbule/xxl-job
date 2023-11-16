package com.korant.youya.workplace.utils;

/**
 * @ClassName EnumUtils
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/14 11:48
 * @Version 1.0
 */
public class EnumUtil {

    /**
     * 获取枚举类型的所有值
     *
     * @param enumClass 枚举类型的 Class 对象
     * @return 枚举类型的所有值
     */
    public static <T extends Enum<T>> T[] getValues(Class<T> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException(enumClass.getName() + " is not an enum type");
        }
        return enumClass.getEnumConstants();
    }
}
