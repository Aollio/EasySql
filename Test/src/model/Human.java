package model;


/**
 * Created by Finderlo on 2016/10/10.
 */
public class Human {

    private int id;
    private String test_name;
    private int test_age;
    private String test_sex;



    public Human() {
    }

    public Human(int id, String name, int age, String test_sex) {
        this.id = id;
        this.test_name = name;
        this.test_age = age;
        this.test_sex = test_sex;
    }


    @Override
    public String toString() {
        return "id=" + id + ";test_name=" + test_name + ";test_age=" + test_age + ";test_sex=" + test_sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public int getTest_age() {
        return test_age;
    }

    public void setTest_age(int test_age) {
        this.test_age = test_age;
    }

    public String getTest_sex() {
        return test_sex;
    }

    public void setTest_sex(String test_sex) {
        this.test_sex = test_sex;
    }
}
