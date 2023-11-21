package com.korant.youya.workplace.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.korant.youya.workplace.annotations.Dict;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.constants.CommonConstant;
import com.korant.youya.workplace.mapper.DictionaryMapper;
import com.korant.youya.workplace.pojo.R;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenyiqiang
 * @date 2023-07-27
 */
@Aspect
@Component
@Slf4j
public class DictAspect {

    @Resource
    private DictionaryMapper dictionaryMapper;

    /**
     * 对响应对象进行字典翻译
     *
     * @param dict
     * @param value
     */
    @AfterReturning(pointcut = "@annotation(dict)", returning = "value")
    public void explanationDictionary(ExplanationDict dict, Object value) {
        if (value instanceof R) {
            Object data = ((R<?>) value).getData();
            ObjectMapper mapper = new ObjectMapper();
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
            mapper.registerModule(javaTimeModule);
            if (null != data) {
                String json = "{}";
                try {
                    json = mapper.writeValueAsString(data);
                } catch (JsonProcessingException e) {
                    log.error("json解析失败" + e.getMessage(), e);
                }
                JSONObject jsonObject = JSONObject.parseObject(json);
                for (Field field : getAllFields(data)) {
                    String fieldName = field.getName();
                    Class<?> type = field.getType();
                    if (type.equals(List.class)) {
                        try {
                            field.setAccessible(true);
                            Object obj = field.get(data);
                            JSONArray array = jsonObject.getJSONArray(fieldName);
                            JSONArray jsonArray = new JSONArray((List) obj);
                            for (int i = 0; i < jsonArray.size(); i++) {
                                Object o = jsonArray.get(i);
                                JSONObject arrayJSONObject = array.getJSONObject(i);
                                for (Field allField : getAllFields(o)) {
                                    String textValue = getTextValue(allField, o);
                                    if (StringUtils.isNotBlank(textValue)) {
                                        String allFieldName = allField.getName();
                                        arrayJSONObject.put(allFieldName + CommonConstant.DICT_TEXT_SUFFIX, textValue);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        String textValue = getTextValue(field, data);
                        if (StringUtils.isNotBlank(textValue)) {
                            jsonObject.put(fieldName + CommonConstant.DICT_TEXT_SUFFIX, textValue);
                        }
                    }
                }
                ((R<Object>) value).setData(jsonObject);
            }
        }
    }

    /**
     * 获取字典值
     *
     * @param field
     * @param data
     * @return
     */
    private String getTextValue(Field field, Object data) {
        String textValue = "";
        if (field.getAnnotation(Dict.class) != null) {
            String categoryCode = field.getAnnotation(Dict.class).categoryCode();
            String code = field.getAnnotation(Dict.class).dictCode();
            String fieldName = field.getName();
            Integer fieldValue = null;
            try {
                field.setAccessible(true);
                fieldValue = (Integer) field.get(data);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            textValue = translateDictValue(categoryCode, code, fieldValue);
            log.debug(" 字典Val : " + textValue);
            log.debug(" __翻译字典字段__ " + fieldName + CommonConstant.DICT_TEXT_SUFFIX + "： " + textValue);
            return textValue;
        }
        return textValue;
    }

    /**
     * 获取类的所有属性，包括父类
     *
     * @param object
     * @return
     */
    private static Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /**
     * 翻译字典文本
     *
     * @param categoryCode
     * @param dictCode
     * @param fieldValue
     * @return
     */
    private String translateDictValue(String categoryCode, String dictCode, Integer fieldValue) {
        String dictValue = "";
        if (ObjectUtil.isNull(fieldValue)) {
            log.error("fieldValue为空");
            return dictValue;
        }
        if (StringUtils.isBlank(dictCode)) {
            log.debug("--DictAspect------categoryCode=" + categoryCode + " ,fieldValue= " + fieldValue);
            dictValue = dictionaryMapper.translateDict(categoryCode, fieldValue);
        }
        return dictValue;
    }
}
