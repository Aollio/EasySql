package com.finderlo.easysql;

import com.finderlo.easysql.model.ClassModel;
import com.finderlo.easysql.model.TableModel;
import com.finderlo.easysql.utility.EasyException;

import java.io.IOException;
import java.sql.Connection;
import static com.finderlo.easysql.SqlDao.*;

/**
 * Created by Finderlo on 2016/10/11.
 */
public class Update {

    public boolean update(Connection connection,Class classT,String setKey, String setValue, String whereKey, String whereValue) throws EasyException, IOException {
        ClassModel classModel = Cache.getInstance().getClassModel(classT);
        TableModel tableModel = Cache.getInstance().getTableModel(classModel.tableName);
        return executeUpdate(connection,jointSql(tableModel,setKey, setValue, whereKey, whereValue)) == 1;
    }

    private String jointSql(TableModel tableModel,String setKey, String setValue, String whereKey, String whereValue) {
        return " update " + tableModel.tableName + " set " + setKey + " = '" + setValue + "' where " + whereKey + " = '" + whereValue + "'";
    }
}
