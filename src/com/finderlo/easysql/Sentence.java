package com.finderlo.easysql;

import java.util.List;
import java.util.Map;

import static com.finderlo.easysql.Util.*;

/**
 * Created by Finderlo on 2016/10/11.
 */
class Sentence {

    static String jointDelete(String tableName, String columName, String args) {
        return "delete from " + tableName + " where " + columName + "='" + args + "'";
    }

    static String jointDelete(String tableName, List<String> fieldsName, Map<String, Typer> fieldsValuesMap) {

        String sql = "";
        if (fieldsName.size() >= 3) {
            sql = "delete from " + tableName + " where ? = '?' and ? = '?'";
        } else {
            sql = "delete from " + tableName + " where ? = '?'";
        }

        int nextIndex = sql.indexOf("?");
        //替换？为value
        for (int i = 1; i < fieldsName.size(); i++) {
            sql = replace(nextIndex, fieldsName.get(i), sql);
            nextIndex = nextIndex + 5 + fieldsName.get(i).length() - 1;
            sql = replace(nextIndex,fieldsValuesMap.get(fieldsName.get(i)).value.toString(),sql);
            nextIndex = nextIndex + 7 + fieldsValuesMap.get(fieldsName.get(i)).value.toString().length() -1;
//            sql = sql.replaceFirst("\\?", fieldsName.get(i));
//            sql = sql.replaceFirst("\\?", );
            if (nextIndex > sql.length()){
                break;
            }
        }
        System.out.println(sql);
        return sql;
    }

    /**
     * 拼接插入语句
     **/
    static String jointInsert(String tableName, List<String> fieldsName, Map<String, Typer> fieldsValuesMap) throws EasyException {
        if (fieldsName.isEmpty()) {
            throw new EasyException("The model object is empty or isn't initialize");
        }
        String sql = initInsert(tableName, fieldsName.size());

        //使用指针来确定替换位置，防止插入失败
        int nextIndex = sql.indexOf("$");
        //如果要替换字段中含有$的话，会造成插入失败
        //替换$为key
        for (int i = 0; i < fieldsName.size(); i++) {
//            sql = sql.replaceFirst("\\$", fieldsName.get(i));
            sql = replace(nextIndex, fieldsName.get(i), sql);
            nextIndex = nextIndex + 2 + fieldsName.get(i).length() - 1;
        }
        //使用指针下标来确定替换位置，防止插入失败
        nextIndex = sql.indexOf("?");
        //如果要替换值中含有？的话，会造成插入失败
        //替换？为value
        for (int i = 0; i < fieldsName.size(); i++) {
            int value = fieldsValuesMap.get(fieldsName.get(i)).value.toString().length();
            sql = replace(nextIndex, fieldsValuesMap.get(fieldsName.get(i)).value.toString(), sql);
            nextIndex = (nextIndex + 4 + value - 1);
//            sql = sql.replaceFirst("\\?", fieldsValuesMap.get(fieldsName.get(i)).value.toString());
        }
        return sql;
    }


    /**
     * 返回有2*length的占位符的插入语句，字段使用$占位，值使用？占位
     **/
    private static String initInsert(String tableName, int length) {
        String head = "insert into " + tableName + " (";
        String mid = ") values(";
        String tail = ")";
        StringBuilder stringBuilderValue = new StringBuilder();
        StringBuilder stringBuilderKey = new StringBuilder();

        if (length == 1) {
            stringBuilderKey.append("$");
        } else if (length == 2) {
            stringBuilderKey.append("$,$");
        } else {
            for (int i = 0; i < length - 1; i++) {
                stringBuilderKey.append("$,");
            }
            stringBuilderKey.append("$");
        }

        if (length == 1) {
            stringBuilderValue.append("'?'");
        } else if (length == 2) {
            stringBuilderValue.append("'?','?'");
        } else {
            for (int i = 0; i < length - 1; i++) {
                stringBuilderValue.append("'?',");
            }
            stringBuilderValue.append("'?'");
        }
        String value = stringBuilderValue.toString();
        String key = stringBuilderKey.toString();
        return head + key + mid + value + tail;
    }

}
