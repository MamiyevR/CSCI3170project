import java.util.*;
import java.sql.*;

public class jdbcConnect {
  private String jdbcDriver = "com.mysql.jdbc.Driver";
  private String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db25";
  private String userName = "Group25";
  private String password = "CSCI3170";

  public Statement connect() {
    try {
      Class.forName(jdbcDriver);
      Connection conn = DriverManager.getConnection(dbAddress, userName, password);
      Statement stmt = conn.createStatement();
      return stmt;
    } catch (ClassNotFoundException e) {
      System.out.println("[Error]: Java MySQL DB Driver not found!!");
    } catch()
    
  }
}
