package com.finderlo.easysql.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 这个类是一个实体类，用于存放一个具体对象中的一个具体属性的类型和值
 */
public class ObjectFieldValue {


    public Map<Field, ValueInfo> fieldValueInfoMap;

    public ObjectFieldValue(Map<Field, ValueInfo> nameValueInfoMap) {
        this.fieldValueInfoMap = nameValueInfoMap;
    }

    public Object getValue(String fieldName) {
        Object result = null;
        for (Field field : fieldValueInfoMap.keySet()) {
            if (field.getName().toLowerCase().equals(fieldName.toLowerCase())) {
                result = fieldValueInfoMap.get(field).value;
                break;
            }
        }
        return result;
    }


    public boolean isFieldExist(String fieldName) {
        for (Field field : fieldValueInfoMap.keySet()) {
            if (field.getName().toLowerCase().equals(fieldName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static class ValueInfo {

        public Class type;
        public Object value;

        public ValueInfo(Class type, Object value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "class:" + type.getName() + "; value:" + value;
        }
    }

}
