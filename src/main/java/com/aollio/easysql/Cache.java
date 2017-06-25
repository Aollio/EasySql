package com.aollio.easysql;

import com.aollio.easysql.model.ClassModel;
import com.aollio.easysql.model.TableModel;
import com.aollio.easysql.utility.EasyException;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Aollio on 2016/10/12.
 */
public interface Cache {


    ClassModel getClassModel(Class classT) ;

    TableModel getTableModel(String table) ;

    void cacheTable(Connection connection, String tableName);

    void cacheClass(Connection connection, Class classT) throws EasyException;
}
