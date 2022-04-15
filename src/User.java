import java.sql.*;
import java.util.*;

class User {
  private String jdbcDriver = "com.mysql.jdbc.Driver";
  private String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db25";
  private String userName = "Group25";
  private String password = "CSCI3170";

  public User() {
  }

  public void callUser() {
    Boolean var1 = false;
    while (!var1) {
      System.out.println("-----Operations for User Menu-----");
      System.out.println("What kind of Operations you would like to perform?");
      System.out.println("1. Search for cars");
      System.out.println("2. Show loan record of user");
      System.out.println("3. Return to the main menu");
      System.out.printf("Enter your choice: ");
      Scanner var2 = new Scanner(System.in);
      String var3 = var2.next();
      char var4 = var3.charAt(0);
      if (var3.length() == 1) {
        if (var4 == '1') {
          searchCars();
        } else if (var4 == '2') {
          loanRecord();
        } else if (var4 == '3') {
          var1 = true;
        } else {
          System.out.println("[Error]: Invalid selection. Please select between 1 and 3!");
        }
      } else {
        System.out.println("[Error]: Invalid selection. Please select between 1 and 3!");
      }
    }
  }

  public void searchCars() {
    try {
      Class.forName(jdbcDriver);
      Connection conn = DriverManager.getConnection(dbAddress, userName, password);

      Statement stmt = conn.createStatement();
      ResultSet rs;

      while (true) {
        System.out.println("Choose the Search criterion: ");
        System.out.println("1. call number");
        System.out.println("2. name");
        System.out.println("3. company");
        System.out.printf("Choose the search criterion: ");
        Scanner var2 = new Scanner(System.in);
        String var3 = var2.next();
        char var4 = var3.charAt(0);
        if (var3.length() == 1) {
          if (var4 == '1') {
            System.out.printf("Type in the search Keyword: ");
            // Queries to follow for call number search
            Scanner obj = new Scanner(System.in);
            String input = obj.nextLine();

            String sql = "SELECT c.callnum, c.name, cc.ccname, p.cname, MAX(cpy.copynum) AS copynum FROM car c, car_category cc, copy cpy, produce p WHERE c.ccid = cc.ccid AND c.callnum  = '"
                + input + "' AND cpy.callnum = '" + input + "' AND c.callnum = p.callnum ORDER BY callnum ASC";

            rs = stmt.executeQuery(sql);
            boolean already_print = Boolean.FALSE;
            while (rs.next()) {
              String callnum = rs.getString("callnum");
              String name = rs.getString("name");
              String ccname = rs.getString("ccname");
              String cname = rs.getString("cname");
              int numcop = rs.getInt("copynum");
              if (already_print == Boolean.FALSE) {
                System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
              }
              System.out.println("|" + callnum + "|" + name + "|" + ccname + "|" + cname + "|" + numcop + "|");
              already_print = Boolean.TRUE;
            }
            if (already_print) {
              System.out.println("End of Query\n");
            } else {
              System.out.println("Query is empty");
            }
            break;
          } else if (var4 == '2') {
            System.out.printf("Type in the search keyword: ");
            // Queries to follow for name search
            Scanner obj = new Scanner(System.in);
            String input = obj.nextLine();

            String sql = "SELECT c.callnum, c.name, cc.ccname, p.cname, MAX(cpy.copynum) AS copynum  FROM car c, car_category cc, copy cpy, produce p WHERE c.ccid = cc.ccid AND c.callnum = cpy.callnum AND c.callnum = p.callnum AND name LIKE '%"
                + input + "%' GROUP BY callnum ORDER BY callnum ASC";

            rs = stmt.executeQuery(sql);
            boolean already_print = Boolean.FALSE;
            while (rs.next()) {
              String callnum = rs.getString("callnum");
              String name = rs.getString("name");
              String ccname = rs.getString("ccname");
              String cname = rs.getString("cname");
              int numcop = rs.getInt("copynum");
              if (already_print == Boolean.FALSE) {
                System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
              }
              System.out.println(callnum + "|" + name + "|" + ccname + "|" + cname + "|" + numcop + "|");
              already_print = Boolean.TRUE;
            }
            if (already_print) {
              System.out.println("End of Query\n");
            } else {
              System.out.println("Query is empty");
            }
            break;
          } else if (var4 == '3') {
            System.out.printf("Type in the search keyword: ");
            Scanner obj = new Scanner(System.in);
            String input = obj.nextLine();

            String sql = "SELECT c.callnum, c.name, cc.ccname, p.cname, MAX(cpy.copynum) AS copynum  FROM car c, car_category cc, copy cpy WHERE c.ccid = cc.ccid AND c.callnum = cpy.callnum AND c.callnum = p.callnum AND manufacture LIKE '%"
                + input + "%' GROUP BY callnum ORDER BY callnum ASC";

            rs = stmt.executeQuery(sql);
            boolean already_print = Boolean.FALSE;
            while (rs.next()) {
              String callnum = rs.getString("callnum");
              String name = rs.getString("name");
              String ccname = rs.getString("ccname");
              String cname = rs.getString("cname");
              int numcop = rs.getInt("copynum");
              if (already_print == Boolean.FALSE) {
                System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
              }
              System.out.println(callnum + "|" + name + "|" + ccname + "|" + cname + "|" + numcop + "|");
              already_print = Boolean.TRUE;
            }
            if (already_print) {
              System.out.println("End of Query\n");
            } else {
              System.out.println("Query is empty");
            }
            break;
          } else {
            System.out.println("[Error]: Invalid selection. Please select between 1 and 3!");
          }
        } else {
          System.out.println("[Error]: Invalid selection. Please select between 1 and 3!");
        }
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      // System.out.printf("[Error]: %s\n", e.getMessage());
    } catch (SQLException e) {
      e.printStackTrace();
      // System.out.println("[Error]: server cannot connect to database");
    }
  }

  public void loanRecord() {
    try {
      Class.forName(jdbcDriver);
      Connection conn = DriverManager.getConnection(dbAddress, userName, password);
      Statement stmt = conn.createStatement();
      ResultSet rs;

      System.out.printf("Enter the user ID:");
      Scanner obj = new Scanner(System.in);
      String input = obj.nextLine();
      String sql = "SELECT c.callnum, cpy.copynum, c.name, c.manufacture, r.checkout, r.return_date FROM rent r, car c, copy cpy WHERE r.callnum = c.callnum AND c.callnum = cpy.callnum ORDER BY checkout DESC";

      rs = stmt.executeQuery(sql);
      System.out.println("Call Num|CopyNum|Name|Company|Check-out|Returned?");
      String check = "";
      while (rs.next()) {
        String callnum = rs.getString("callnum");
        String copynum = rs.getString("copynum");
        String name = rs.getString("name");
        String manufacture = rs.getString("manufacture");
        String checkout = rs.getString("checkout");
        String returndate = rs.getString("return_date");
        if (rs.wasNull()) {
          check = "NO";
        } else {
          check = "YES";
        }
        System.out
            .println(callnum + "|" + copynum + "|" + name + "|" + manufacture + "|" + checkout + "|" + check + "|");
      }
      System.out.println("End of Query");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      // System.out.printf("[Error]: %s\n", e.getMessage());
    } catch (SQLException e) {
      e.printStackTrace();
      // System.out.println("[Error]: server cannot connect to database");
    }
  }
}
