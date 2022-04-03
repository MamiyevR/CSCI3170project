import java.util.*;
import java.sql.*;

public class Admin {
  private String jdbcDriver = "com.mysql.jdbc.Driver";
  private String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db25";
  private String userName = "Group25";
  private String password = "CSCI3170";

  public Admin() {
  }

  public int run() {
    Boolean exit = false;
    while (!exit) {
      System.out.println("-----Operations for adminstrator menu-----");
      System.out.println("What kind of operations would you like to perform?");
      System.out.println("1. Create all tables");
      System.out.println("2. Delete all table");
      System.out.println("3. Load from datafile");
      System.out.println("4. Show number of records in each table");
      System.out.println("5. Return to the main menu");
      System.out.printf("Enter Your Choice: ");
      Scanner input = new Scanner(System.in);
      String inputString = input.next();
      char nextAct = inputString.charAt(0);
      if (inputString.length() == 1) {
        if (nextAct == '5') {
          exit = true;
          continue;
        } else if (nextAct == '4') {
          System.out.println("You have selected 4");
          // need to change above
        } else if (nextAct == '3') {
          System.out.println("You have selected 3");
          // need to change above
        } else if (nextAct == '2') {
          this.dropTable();
        } else if (nextAct == '1') {
          this.createTable();
        } else {
          System.out.println("[Error]: Invalid selection. Please select between 1 and 5!");
        }
      } else {
        System.out.println("[Error]: Invalid selection. Please select between 1 and 5!");
      }
    }
    return 1;
  }

  public void createTable() {
    try {
      Class.forName(jdbcDriver);
      Connection conn = DriverManager.getConnection(dbAddress, userName, password);
      Statement stmt = conn.createStatement();
      String sql = "CREATE TABLE user_category (" +
          "ucid DECIMAL(1) UNSIGNED, " +
          "max DECIMAL(1) UNSIGNED NOT NULL, " +
          "period DECIMAL(2) UNSIGNED NOT NULL, " +
          "PRIMARY KEY (ucid))";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE user (" +
          "uid VARCHAR(12), " +
          "name VARCHAR(25) NOT NULL, " +
          "age DECIMAL(2) UNSIGNED NOT NULL, " +
          "occupation VARCHAR(20) NOT NULL, " +
          "ucid DECIMAL(1) UNSIGNED NOT NULL, " +
          "PRIMARY KEY (uid), " +
          "FOREIGN KEY (ucid) REFERENCES user_category(ucid))";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE car_category (" +
          "ccid DECIMAL(1) UNSIGNED, " +
          "ccname VARCHAR(20) NOT NULL, " +
          "PRIMARY KEY (ccid))";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE car (" +
          "callnum VARCHAR(8), " +
          "name VARCHAR(10) NOT NULL, " +
          "manufacture DATE, " +
          "time_rent DECIMAL(2) UNSIGNED NOT NULL, " +
          "ccid DECIMAL(1) UNSIGNED NOT NULL, " +
          "PRIMARY KEY (callnum), " +
          "FOREIGN KEY (ccid) REFERENCES car_category(ccid))";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE copy (" +
          "callnum VARCHAR(8), " +
          "copynum DECIMAL(1) UNSIGNED, " +
          "PRIMARY KEY (copynum, callnum), " +
          "FOREIGN KEY (callnum) REFERENCES car(callnum) ON UPDATE CASCADE ON DELETE CASCADE)";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE rent (" +
          "uid VARCHAR(12), " +
          "callnum VARCHAR(8), " +
          "copynum DECIMAL(1) UNSIGNED, " +
          "checkout DATE, " +
          "return_date DATE, " +
          "PRIMARY KEY (checkout, uid, callnum, copynum), " +
          "FOREIGN KEY (uid) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE, " +
          "FOREIGN KEY (callnum) REFERENCES car(callnum) ON UPDATE CASCADE ON DELETE CASCADE, " +
          "FOREIGN KEY (copynum) REFERENCES copy(copynum) ON UPDATE CASCADE ON DELETE CASCADE)";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE produce (" +
          "callnum VARCHAR(8), " +
          "cname VARCHAR(25), " +
          "PRIMARY KEY (callnum, cname), " +
          "FOREIGN KEY (callnum) REFERENCES car(callnum) ON UPDATE CASCADE ON DELETE CASCADE)";
      stmt.executeUpdate(sql);
      System.out.println("...Done. Database is initialized");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void dropTable() {
    try {
      Class.forName(jdbcDriver);
      Connection conn = DriverManager.getConnection(dbAddress, userName, password);
      Statement stmt = conn.createStatement();
      String sql = "DROP TABLE IF EXISTS produce";
      stmt.executeUpdate(sql);
      sql = "DROP TABLE IF EXISTS rent";
      stmt.executeUpdate(sql);
      sql = "DROP TABLE IF EXISTS copy";
      stmt.executeUpdate(sql);
      sql = "DROP TABLE IF EXISTS car";
      stmt.executeUpdate(sql);
      sql = "DROP TABLE IF EXISTS car_category";
      stmt.executeUpdate(sql);
      sql = "DROP TABLE IF EXISTS user";
      stmt.executeUpdate(sql);
      sql = "DROP TABLE IF EXISTS user_category";
      stmt.executeUpdate(sql);
      System.out.println("...Done. Database is removed");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
