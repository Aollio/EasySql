package com.aollio.easysql.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Finderlo on 2016/10/11.
 */
public class TableModel {
    //表的名称
    public String tableName;
    //表的主键
    public List<String> primaryKey = new ArrayList<>();
    //表的字段名
    public List<String> columnsKey = new ArrayList<>();

    public TableModel(){}

    public TableModel(String tableName, List<String> primaryKey, List<String> columnsKey){
        this.tableName = tableName.toLowerCase();
        this.columnsKey = columnsKey;
        this.primaryKey = primaryKey;
    }
}
