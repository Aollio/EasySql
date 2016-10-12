package com.finderlo.easysql;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Finderlo on 2016/10/11.
 */
public class Table {

    String tableName;

    List<String> primaryKey = new ArrayList<>();

    List<String> columnsKey = new ArrayList<>();

    Table(){}

    Table(String tableName,List<String> primaryKey,List<String> columnsKey){
        this.tableName = tableName.toLowerCase();
        this.columnsKey = columnsKey;
        this.primaryKey = primaryKey;
    }
}
