package test.easysql;

import com.finderlo.easysql.utility.EasyException;
import com.finderlo.easysql.EasySql;
import com.finderlo.easysql.Util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Finderlo on 2016/10/22.
 */
public class Two {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, EasyException {
        Model model = new Model();
        model.primary_one = "jack";
        model.primary_two = "140153816";
        model.foreign_one = "18217699893";

//        EasySql easySql = new EasySql(new File("C:/easysql.config"));
//        easySql.delete(model);

//        System.out.println(System.lineSeparator());


        Map<String, String> result = Util.parseProperties();
        String url = result.get("url");

        System.out.println(url);


    }
}
