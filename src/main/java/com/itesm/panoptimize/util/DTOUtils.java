package com.itesm.panoptimize.util;

import com.itesm.panoptimize.dto.IncludeField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DTOUtils {
    public static<T> T createDTO(Object source, Class<T> targetClass) {
        try{
            T target = targetClass.getDeclaredConstructor().newInstance();
            Map<String, Boolean> includeFields = getIncludeFields(targetClass);

            for (Field field : source.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (includeFields.containsKey(field.getName()) && includeFields.get(field.getName())) {
                    field.setAccessible(true);
                    Field targetField = targetClass.getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, field.get(source));
                }
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Error creating DTO", e);
        }
    }

    private static Map<String, Boolean> getIncludeFields(Class<?> dtoClass) {
        Map<String, Boolean> includeFields = new HashMap<>();
        for (Field field : dtoClass.getDeclaredFields()) {
            includeFields.put(field.getName(), field.isAnnotationPresent(IncludeField.class));
        }
        return includeFields;
    }
}
