package model;

/**
 * Created by Finderlo on 2016/10/11.
 */
public class WebPage {

    /*

  CREATE TABLE webpage_copy (
  Id int unsigned zerofill NOT NULL AUTO_INCREMENT,
  Url varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  Title varchar(255) DEFAULT NULL,
  Type varchar(255) DEFAULT NULL,
  Status varchar(255) DEFAULT NULL,
  Html varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
)



     */

    private int id;
    private String url;
    private String title;
    private String type;
    private String status;
    private String html;

    public WebPage() {
    }

    public WebPage(int id, String url, String title, String type, String status, String html) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.type = type;
        this.status = status;
        this.html = html;
    }

    @Override
    public String toString() {
        return " id = " + id + " title = " + title + " type = " + type + " status = " + status + " url = " + url + " html = " + html;
    }
}
