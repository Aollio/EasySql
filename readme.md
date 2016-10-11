# EasySql
## 简介
一个出于兴趣爱好，用于简便在代码中拼接sql语句与数据库进行通信的开源库。

正在开发中

0.5版本

## 使用条件

初始化:连接参数和表名参数
**插入的model类中所有字段必须和表中字段相同**

## 使用样例

```java
//初始化
EasySql easySql = new EasySql(Connection,TableName);
//插入一个对象
easySql.insert(Model);
//删除一个对象；
easySql.delete(Model);
//删除表中id=2的一行
easySql.delete("id","2");
//查询表中所有行数据，返回list类型
List<Model> models = easySql.query(Model.clas);
//查询表中符合参数所有行数据，返回list类型。条件id=2；
List<Model> models = easySql.query(Model.clas，"id","2");
//更新表中数据，输入要更新的column名称、值，和定位所在行的条件。
//更新表中name=Tom所在行的age值为18岁。
easySql.update("age","18","name","Tom");
//静态方法
EasySql.insert(Connection,TableName,Object)
```

## API

### 静态方法
* static boolean insert(Connection connection, String tablename, Object object)

* static boolean delete(Connection connection, String tablename, Object object)

* static boolean delete(Connection connection, String tablename, String columName, String args)

* static List query(Connection connection, String tablename, Class classT, String columName, String args)

* static List query(Connection connection, String tablename, Class classT)

* static boolean update(Connection connection, String tableName, String setKey, String setValue, String whereKey, String whereValue)

### 事例方法
* boolean insert(Object object)

* boolean delete(Object object)

* delete(String columName, String args)

* List query(Class classT)

* List query(Class classT, String columName, String args)

* boolean update( String setKey, String setValue, String whereKey, String whereValue)