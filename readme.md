
# EasySql

## 简介

一个出于兴趣爱好，用于简便在代码中拼接sql语句与数据库进行通信的库。

适用于个人的小型的小项目

正在开发中


## 使用条件

初始化:连接参数和表名参数**插入的model类中字段必须和表中字段相同，可以多出表中的字段（10/12）**

## 简单例子介绍
先建表或模型类


```sql
//建表语句
CREATE TABLE man (
Id int NOT NULL AUTO_INCREMENT,
man_name varchar(255)  ,
man_age int ,
man_sex varchar(255),
PRIMARY KEY (`Id`)
)
```
```java
//模型类
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
}
```
```java
//调用方法
public static void main(String[] args) 
               throws EasyException, SQLException {
  EasySql easySql = new EasySql(getConnection(),"man");
  ManModel manModel = new ManModel(1,"小明","男",18);
  easySql.insert(manModel);
}
```
插入前：
![](https://raw.githubusercontent.com/finderlo/EasySql/master/other/20161012154219.png)

插入后：
![](https://raw.githubusercontent.com/finderlo/EasySql/master/other/20161012155216.png)

## 使用样例

```java
//初始化
EasySql easySql = new EasySql(Connection,TableName);

//插入一个对象
easySql.insert(Model);
//删除一个对象
easySql.delete(Model);
//删除表中id=2的一行
easySql.delete("id","2");
//查询表中所有行数据，返回list类型
List<Model> models = easySql.query(Model.clas);
//查询表中符合参数所有行数据，返回list类型。条件id=2
List<Model> models = easySql.query(Model.clas，"id","2");
//更新表中数据，输入要更新的column名称、值，和定位所在行的条件
//更新表中name=Tom所在行的age值为18岁。
easySql.update("age","18","name","Tom");

//静态方法
EasySql.insert(Connection,TableName,Object)
```

## API

### 静态方法

*   static boolean insert(Connection connection, String tablename, Object object)

*   static boolean delete(Connection connection, String tablename, Object object)

*   static boolean delete(Connection connection, String tablename, String columName, String args)

*   static List query(Connection connection, String tablename, Class classT, String columName, String args)

*   static List query(Connection connection, String tablename, Class classT)

*   static boolean update(Connection connection, String tableName, String setKey, String setValue, String whereKey, String whereValue)

### 实例方法

*   boolean insert(Object object)

*   boolean delete(Object object)

*   delete(String columName, String args)

*   List query(Class classT)

*   List query(Class classT, String columName, String args)

*   boolean update( String setKey, String setValue, String whereKey, String whereValue)

### 说明

*   版本0.65 使用缓存，增加了对一个表多次操作的效率。问题：如果在一个项目中运行中使用了两个不同的连接，而且连接中都含有相同表名的表，会出现不可预知的错误。使用了缓存。

*   版本0.6 修复传入对象若含有与拼接sql中占位符相同的字符会导致异常；问题：插入对象中属性（对应表中主键时）为0，而表结构中设置了自增长主键，可以插入成功，但是再次删除这些对象会导致删除失败。

*   版本0.5 支持基本功能


