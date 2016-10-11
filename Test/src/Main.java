import com.finderlo.easysql.EasyException;
import com.finderlo.easysql.EasySql;
import com.sun.org.apache.regexp.internal.RE;
import model.WebPage;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Finderlo on 2016/10/10.
 */
public class Main {

//    public static final String TABLE_NAME = "webpage_copy";
    public static final String TABLE_NAME = "human";

    public static void main(String[] args) throws SQLException, EasyException, InstantiationException, IllegalAccessException {
//        List webpages = init(5);
//        insert(webpages);
////        deleteWithArg();
////        delete(webpages);
//        query();
        Connection connection = JDBCUitl.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String databaseName = databaseMetaData.getDatabaseProductName();//MySQL

        //获取表中所有列名
        ResultSet columns = databaseMetaData.getColumns(null, null, TABLE_NAME, null);

        while (columns.next()) {
            String name = columns.getString("COLUMN_NAME");//INDEX:4 ,name
            int dateType = columns.getInt("DATA_TYPE"); //INDEX:5
            System.out.println("name :" + name + ",dateType :" + dateType);
        }

        ResultSet primarys = databaseMetaData.getPrimaryKeys(null,null,TABLE_NAME);
        while (primarys.next()) {
            String name = primarys.getString("COLUMN_NAME");//INDEX:4 ,name
            System.out.println("primary name :" + name );
        }
    }

    public static List<WebPage> init(int count) {
        List<WebPage> webPages = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            webPages.add(new WebPage(i, "url" + i, "title" + i, "type" + i, "status" + i, " "));
        }
        return webPages;
    }

    public static void insert(List wepages) {
        wepages.forEach(wepage -> {
            EasySql easySql = new EasySql(JDBCUitl.getConnection(), TABLE_NAME);
            try {
                System.out.println("insert :" + easySql.insert(wepage));
            } catch (EasyException e) {
                e.printStackTrace();
            }
        });
    }

    public static void delete(List wepages) {
        wepages.forEach(wepage -> {
            EasySql easySql = new EasySql(JDBCUitl.getConnection(), TABLE_NAME);
            try {
                System.out.println("delete :" + easySql.delete(wepage));
            } catch (EasyException e) {
                e.printStackTrace();
            }
        });
    }

    public static void deleteWithArg() {
        EasySql easySql = new EasySql(JDBCUitl.getConnection(), TABLE_NAME);
        System.out.println("delete :" + easySql.delete("id", "2"));
    }

    public static void query() throws EasyException {
        EasySql easySql = new EasySql(JDBCUitl.getConnection(), TABLE_NAME);
        System.out.println(easySql.query(WebPage.class));
    }


}
