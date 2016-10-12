package com.finderlo.easysql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Finderlo on 2016/10/12.
 */
public class ClassType {

    String className;

    Map<String,Field> classMap;

    Class classType;

    ClassType(){}
    ClassType(Class classType,Map<String,Field> classMap){
        this.classType = classType;
        this.className = classType.getSimpleName().toLowerCase();
        this.classMap = classMap;
    }

    boolean isFieldExist(String fieldName){
        for (String key:classMap.keySet()){
            if (key.toLowerCase().equals(fieldName.toLowerCase())){
                return true;
            }
        }
        return false;
    }
}
