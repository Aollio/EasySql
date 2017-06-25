
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Finderlo on 2016/10/22.
 */
public class One {
    public static void main(String[] args) {
        String head = "delete from tablename where ";
        StringBuilder middle = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                middle.append("$ = '?' ");
            } else {
                middle.append("and $ = '?' ");
            }
        }
        String sql = head + middle.toString();
        System.out.println(head + middle.toString());

        List<String> mFieldsName = new ArrayList<>();
        mFieldsName.add("arg1");
        mFieldsName.add("arg2");
        mFieldsName.add("arg3");

        Map<String,String> mNameAndTyper = new HashMap<>();
        mNameAndTyper.put("arg1","??val??");
        mNameAndTyper.put("arg2","??val??");
        mNameAndTyper.put("arg3","??val??");

        //替换参数
        int k = 0;
        int v = 0;
        char[] sqlChar = sql.toCharArray();

        StringBuilder sqlBuiler = new StringBuilder(sql);

        for (int i = sql.length() - 1; i >= 0; i--) {
            if (sqlChar[i] == '$' && k < mFieldsName.size()) {
                String key = mFieldsName.get(mFieldsName.size() - 1 - k);
                sqlBuiler.delete(i, i + 1);
                sqlBuiler.insert(i, key);
                k++;
            } else if (sqlChar[i] == '?' && v < mFieldsName.size()) {
                String key = mFieldsName.get(mFieldsName.size() - 1 - v);
                String value = mNameAndTyper.get(key);
                sqlBuiler.delete(i, i + 1);
                sqlBuiler.insert(i, value);
                v++;
            }
        }
        System.out.println(sqlBuiler.toString());
    }

}
