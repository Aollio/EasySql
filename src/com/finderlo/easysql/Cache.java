package com.finderlo.easysql;

import com.finderlo.easysql.model.ClassModel;
import com.finderlo.easysql.model.TableModel;
import com.finderlo.easysql.utility.EasyException;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static com.finderlo.easysql.Util.*;

/**
 * Created by Finderlo on 2016/10/12.
 */
public interface Cache {


    ClassModel getClassModel(Class classT) throws EasyException;

    TableModel getTableModel(String table) throws EasyException;

    void cacheTable(Connection connection, String tableName);

    void cacheClass(Connection connection, Class classT) throws EasyException;
}
