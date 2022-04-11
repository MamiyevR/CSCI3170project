import java.util.*;
import java.sql.*;
import java.io.*;

public class admin {
  private String jdbcDriver = "com.mysql.jdbc.Driver";
//  private String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db25";
//  private String userName = "Group25";
//  private String password = "CSCI3170";

  // Used for local test
    private String dbAddress = "jdbc:mysql://localhost:3306/db25";
    private String userName = "root";
    private String password = "12345678";

  public admin() {
  }

  public int run() {
    Boolean exit = false;
    while (!exit) {
      System.out.println("-----Operations for administrator menu-----");
      System.out.println("What kind of operations would you like to perform?");
      System.out.println("1. Create all tables");
      System.out.println("2. Delete all tables");
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
          this.numOfRecords();
          // need to change above
        } else if (nextAct == '3') {
          this.loadTable(input);
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
      System.out.print("Processing...");
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
          "PRIMARY KEY (uid, callnum, copynum, checkout), " +
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
      System.out.println("Done. Database is initialized.");
    } catch (ClassNotFoundException e) {
       e.printStackTrace();
      System.out.printf("[Error]: %s\n", e.getMessage());
    } catch (SQLException e) {
       e.printStackTrace();
      System.out.println("[Error]: server cannot connect to database");
    }
  }

  public void dropTable() {
    try {
      System.out.print("Processing...");
      Class.forName(jdbcDriver);
      Connection conn = DriverManager.getConnection(dbAddress, userName, password);
      Statement stmt = conn.createStatement();
      String sql;
      String[] tables = { "produce", "rent", "copy", "car", "car_category", "user", "user_category" };

      for (String s : tables) {
        sql = String.format("DROP TABLE IF EXISTS %s", s);
        stmt.executeUpdate(sql);
      }
      System.out.println("...Done. Database is removed");
    } catch (ClassNotFoundException e) {
      // e.printStackTrace();
      System.out.printf("[Error]: %s\n", e.getMessage());
    } catch (SQLException e) {
      // e.printStackTrace();
      System.out.println("[Error]: server cannot connect to database");
    }
  }

  public void loadTable(Scanner input) {
    try {
      Class.forName(jdbcDriver);
      Connection conn = DriverManager.getConnection(dbAddress, userName, password);
      Statement stmt = conn.createStatement();
      String sql;
      System.out.printf("Type in the Source Data Folder Path: ");
      String inputString = input.next();

      File tempFile = new File(inputString + "/car_category.txt");
      Scanner reader = new Scanner(tempFile);
      while (reader.hasNextLine()) {
        String data = reader.nextLine();
        String[] datas = data.split("\t");
        sql = String.format("INSERT IGNORE INTO car_category(ccid, ccname) VALUES (%d, \"%s\")",
            Integer.parseInt(datas[0]), datas[1]);
        stmt.executeUpdate(sql);
      }
      reader.close();

      tempFile = new File(inputString + "/user_category.txt");
      reader = new Scanner(tempFile);
      while (reader.hasNextLine()) {
        String data = reader.nextLine();
        String[] datas = data.split("\t");
        sql = String.format("INSERT IGNORE INTO user_category(ucid, max, period) VALUES (%d, %d, %d)",
            Integer.parseInt(datas[0]), Integer.parseInt(datas[1]), Integer.parseInt(datas[2]));
        stmt.executeUpdate(sql);
      }
      reader.close();

      tempFile = new File(inputString + "/user.txt");
      reader = new Scanner(tempFile);
      while (reader.hasNextLine()) {
        String data = reader.nextLine();
        String[] datas = data.split("\t");
        sql = String.format("INSERT IGNORE INTO user(uid, name, age, occupation, ucid)" +
            "VALUES (\"%s\", \"%s\", %d, \"%s\", %d)",
            datas[0], datas[1], Integer.parseInt(datas[2]), datas[3], Integer.parseInt(datas[4]));
        stmt.executeUpdate(sql);
      }
      reader.close();

      tempFile = new File(inputString + "/car.txt");
      reader = new Scanner(tempFile);
      while (reader.hasNextLine()) {
        String data = reader.nextLine();
        String[] datas = data.split("\t");
        sql = String.format("INSERT IGNORE INTO car(callnum, name, manufacture, time_rent, ccid)" +
            "VALUES (\"%s\", \"%s\", \"%s\", %d, %d)",
            datas[0], datas[2], datas[4], Integer.parseInt(datas[5]), Integer.parseInt(datas[6]));
        stmt.executeUpdate(sql);

        sql = String.format("INSERT IGNORE INTO produce(callnum, cname)" +
            "VALUES (\"%s\", \"%s\")", datas[0], datas[3]);
        stmt.executeUpdate(sql);

        sql = String.format("INSERT IGNORE INTO copy(callnum, copynum)" +
            "VALUES (\"%s\", %d)", datas[0], Integer.parseInt(datas[1]));
        stmt.executeUpdate(sql);
      }
      reader.close();

      tempFile = new File(inputString + "/rent.txt");
      reader = new Scanner(tempFile);
      while (reader.hasNextLine()) {
        String data = reader.nextLine();
        String[] datas = data.split("\t");
        if (datas[4].equals("NULL")) {
          sql = String.format("INSERT IGNORE INTO rent(uid, callnum, copynum, checkout, return_date)" +
              "VALUES (\"%s\", \"%s\", %d, \"%s\", %s)",
              datas[2], datas[0], Integer.parseInt(datas[1]), datas[3], datas[4]);
        } else {
          sql = String.format("INSERT IGNORE INTO rent(uid, callnum, copynum, checkout, return_date)" +
              "VALUES (\"%s\", \"%s\", %d, \"%s\", \"%s\")",
              datas[2], datas[0], Integer.parseInt(datas[1]), datas[3], datas[4]);
        }
        stmt.executeUpdate(sql);
      }
      reader.close();
      System.out.println("...Done. Data is inputted to the database");
    } catch (ClassNotFoundException e) {
      // e.printStackTrace();
      System.out.printf("[Error]: %s\n", e.getMessage());
    } catch (FileNotFoundException e) {
      System.out.println("[Error]: Files is not found or cannot be opened in given filepath!");
      // e.printStackTrace();
    } catch (SQLException e) {
      System.out.println("[Error]: server cannot connect to database!");
      // e.printStackTrace();
    }
  }

  public void numOfRecords() {
    try {
      Class.forName(jdbcDriver);
      Connection conn = DriverManager.getConnection(dbAddress, userName, password);
      Statement stmt = conn.createStatement();

      int n;
      ResultSet rs;
      String sql;
      String[] tables = { "user_category", "user", "car_category", "car", "copy", "produce", "rent" };
      System.out.println("Number of records in each table:");
      for (String s : tables) {
        sql = String.format("SELECT COUNT(*) FROM %s", s);
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
          n = rs.getInt("COUNT(*)");
          System.out.printf("%s: %d\n", s, n);
        }
      }
    } catch (ClassNotFoundException e) {
      // e.printStackTrace();
      System.out.printf("[Error]: %s\n", e.getMessage());
    } catch (SQLException e) {
      // e.printStackTrace();
      System.out.println("[Error]: server cannot connect to database");
    }
  }
}
