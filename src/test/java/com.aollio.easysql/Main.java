import com.aollio.easysql.model.WebPage;
import com.aollio.easysql.utility.EasyException;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Finderlo on 2016/10/10.
 */
public class Main {

        public static final String TABLE_NAME = "webpage_copy";
//    public static final String TABLE_NAME = "human";

    public static void main(String[] args) throws SQLException, EasyException, InstantiationException, IllegalAccessException {
        List webpages = init(5);
//        insert(webpages);
//        deleteWithArg();
//        delete(webpages);
//        query();
//        System.out.println((String) null);

//        EasySql easySql = new EasySql(JDBCUitl.getConnection(), TABLE_NAME);
//            System.out.println("insert :" + easySql.insert(new WebPage(0,null,null,null,null,null)));

//        Connection connection = model.JDBCUitl.getConnection();
//        DatabaseMetaData databaseMetaData = connection.getMetaData();
//        String databaseName = databaseMetaData.getDatabaseProductName();//MySQL
//
//        //获取表中所有列名
//        ResultSet columns = databaseMetaData.getColumns(null, null, TABLE_NAME, null);
//
//        while (columns.next()) {
//            String name = columns.getString("COLUMN_NAME");//INDEX:4 ,name
//            int dateType = columns.getInt("DATA_TYPE"); //INDEX:5
//            System.out.println("name :" + name + ",dateType :" + dateType);
//        }
//
//        ResultSet primarys = databaseMetaData.getPrimaryKeys(null,null,TABLE_NAME);
//        while (primarys.next()) {
//            String name = primarys.getString("COLUMN_NAME");//INDEX:4 ,name
//            System.out.println("primary name :" + name );
//        }

//        String string = "abcdefd i daei";
//        System.out.println(string.indexOf("i"));
//        System.out.println(replace(8, "o", string));
//        String head = string.substring(0, 8);
//        String tail = string.substring(9);
//        System.out.println(head);
//        System.out.println(tail);
//        String sql = "insert into tablename ($,$,$,$) values('?','?','?','?')";
//
//        String[] strings = new String[4];
//        strings[0] = "0";
//        strings[1] = "utr?";
//        strings[2] = "title?";
//        strings[3] = "html?";
//
//        int nextIndex = sql.indexOf("?");
//        for (int i = 0; i <4; i++) {
//            int value =strings[i].length();
//            sql = replace(nextIndex,strings[i],sql);
////            if ((nextIndex+2+value-1)<sql.indexOf("?")){
////                //// TODO: 2016/10/11 说明此处传入的值含有？
////            }
//            nextIndex = (nextIndex+4+value-1);
////            sql = sql.replaceFirst("\\?", mNameAndTyper.get(mFieldsName.get(i)).value.toString());
//        }
//        System.out.println(sql);
    }

    public static String replace(int index, String newString, String oriString) {
        return oriString.substring(0, index) + newString + oriString.substring(index + 1);
    }

    public static List<WebPage> init(int count) {
        List<WebPage> webPages = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            webPages.add(new WebPage(i, "url" + i , "??" + i, "type??" + i, "status??" + i, " "));
        }
        return webPages;
    }

//    public static void insert(List wepages) {
//        wepages.forEach(wepage -> {
////            EasySql easySql = new EasySql(JDBCUitl.getConnection(), TABLE_NAME);
//            try {
//                System.out.println("insert :" + easySql.insert(wepage));
//            } catch (EasyException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    public static void delete(List wepages) {
//        wepages.forEach(wepage -> {
//            EasySql easySql = new EasySql(JDBCUitl.getConnection(), TABLE_NAME);
//            try {
//                System.out.println("delete :" + easySql.delete(wepage));
//            } catch (EasyException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    public static void deleteWithArg() {
//        EasySql easySql = new EasySql(JDBCUitl.getConnection(), TABLE_NAME);
//        System.out.println("delete :" + easySql.delete("id", "2"));
//    }
//
//    public static void query() throws EasyException {
//        EasySql easySql = new EasySql(JDBCUitl.getConnection(), TABLE_NAME);
//        System.out.println(easySql.query(WebPage.class));
//    }
//

}
