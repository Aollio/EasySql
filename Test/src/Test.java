import Annotation.Table;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Administrator on 16-10-25.
 */
//@TableModel("Model")
public class Test {

    private String name;
    private int age;

    private int ino;

    public Test(){}
    public Test(String name,int age){
        this.name=name;
        this.age = age;
    }

    public static void main(String[] args) throws IOException, SQLException {

        //使用注解来获得对象所属于的表名;
        //获得的annotation为空，说明没有设置注解.
        Class classT = Test.class;
        Table annotation = (Table) classT.getAnnotation(Table.class);
        if (annotation==null){
            System.out.println();
        }
        System.out.println(annotation.value());

//        //通过配置文件来获取连接信息;通过PROPERTIE文件信息获取
//        Properties properties = new Properties();
//        File file = new File("pro.pro");
//        System.out.println(file.getAbsolutePath());
//        System.out.println(file.getCanonicalPath());
//        properties.load(new FileReader(file));
//
//        System.out.println(properties.getProperty("url"));
//        System.out.println(properties.getProperty("user"));
//        System.out.println(properties.getProperty("password"));
//
//        Connection connection = DriverManager.getConnection("url");

    }

}
