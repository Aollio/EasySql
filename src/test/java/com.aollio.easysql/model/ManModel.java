package com.aollio.easysql.model;

/**
 * Created by Finderlo on 2016/10/12.
 */
public class ManModel {

    private int id;
    private String man_name;
    private String man_sex;
    private int man_age;

    public ManModel(int id, String name, String sex, int age) {
        this.id = id;
        this.man_name = name;
        this.man_sex = sex;
        this.man_age = age;
    }

//    public static void main(String[] args) throws EasyException, SQLException {
//        EasySql easySql = new EasySql(JDBCUitl.getConnection(),"man");
//        ManModel manModel = new ManModel(1,"小明","男",18);
//        easySql.insert(manModel);
//    }
}
