import java.sql.*;
import java.util.Scanner;

public  class user {
    //Connecting to MySql using JAVA
//    public static Connection connect() {
//        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db25";
//        String dbUsername = "Group25";
//        String dbPassword = "CSCI3170";
//        Connection con = null;
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
//        } catch (ClassNotFoundException e) {
//            System.out.println("[Error]: Java  MySql DB  Driver not found!!");
//            System.exit(0);
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        return con;
//    }

    public static String jdbcDriver = "com.mysql.jdbc.Driver";
    public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db25";
    public static String userName = "Group25";
    public static String password = "CSCI3170";

    public static void callUser() {
        Boolean var1 = false;
        while (!var1) {
            System.out.println("-----Operations for User Menu-----");
            System.out.println("What kind of Operations you would like to perform?");
            System.out.println("1. Search for cars");
            System.out.println("2. Show loan record of user");
            System.out.println("3. Return to the main menu");
            System.out.printf("Enter your choice:");
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
                }
            } else {
                System.out.println("[Error]: Invalid selection. Please select between 1 and 3!");
            }
        }
    }

    public static void searchCars() {
        System.out.println("Choose the Search Criterion:");
        System.out.println("1. call number");
        System.out.println("2. name");
        System.out.println("3. company");
        Boolean var1 = false;
        while (!false) {
            System.out.print("Choose the search criterion:");
            Scanner var2 = new Scanner(System.in);
            String var3 = var2.next();
            char var4 = var3.charAt(0);
            if (var3.length() == 1) {
                if (var4 == '1') {
                    System.out.println("Type in the search keyword");
                    //Queries to follow for call number search
                    Scanner obj = new Scanner(System.in);
                    String input = obj.nextLine();

                    // String sql = "Select * From car Where callnum = " + input + "";
                    String sql = "SELECT callnum, name, ccname, manufacture, copynum FROM car c, car_category cc, copy cpy WHERE c.ccid = cc.ccid AND c.callnum  = '" + input + "' AND cpy.callnum = '" + input + "'ORDER BY callnum ASC";

                    try {
                        Class.forName(jdbcDriver);
                        Connection conn = DriverManager.getConnection(dbAddress, userName, password);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);
                        System.out.println("Call Num|Name|Car Category|Company|Available No. of Copy");
                        while (rs.next()) {
                            String callnum = rs.getString("callnum");
                            String name = rs.getString("name");
                            String ccname = rs.getString("ccname");
                            String manufacture = rs.getString("manufacture");
                            double numcop = rs.getInt("copynum");
//                            int supplierID = rs.getInt("SUP_ID");
//                            float price = rs.getFloat("PRICE");
//                            int sales = rs.getInt("SALES");
//                            int total = rs.getInt("TOTAL");
                            System.out.println(callnum + "|" + name + "|" + ccname + "|" + manufacture + "|" + numcop + "|");
                        }
                        System.out.println("End of Query");
                    } catch (ClassNotFoundException e) {
                        // e.printStackTrace();
                        System.out.printf("[Error]: %s\n", e.getMessage());
                    } catch (SQLException e) {
                        // e.printStackTrace();
                        System.out.println("[Error]: server cannot connect to database");
                    }
                    //For sql involved create, insert, update, drop, delete:
                    //stmt.executeUpdate(sql);
                    //For select query:
                    //ResultSet rs = stmt.executeQuery(sql);

                }
                if (var4 == '2') {
                    System.out.println("Type in the search keyword");
                    //Queries to follow for name search
                    Scanner obj = new Scanner(System.in);
                    String input = obj.nextLine();

                    // String sql = "Select * From car Where callnum = " + input + "";
                    String sql = "SELECT callnum, name, ccname, manufacture, copynum FROM car c, car_category cc, copy cpy WHERE c.ccid = cc.ccid AND c.callnum = cpy.callnum AND name LIKE '%" + input + "%' ORDER BY callnum ASC";

                    //Connection con = connect();

                    try{
                        Class.forName(jdbcDriver);
                        Connection conn = DriverManager.getConnection(dbAddress, userName, password);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);
                        System.out.println("Call Num|Name|Car Category|Company|Available No. of Copy");
                        while (rs.next()) {
                            String callnum = rs.getString("callnum");
                            String name = rs.getString("name");
                            String ccname = rs.getString("ccname");
                            String manufacture = rs.getString("manufacture");
                            double numcop = rs.getInt("copynum");
//                            int supplierID = rs.getInt("SUP_ID");
//                            float price = rs.getFloat("PRICE");
//                            int sales = rs.getInt("SALES");
//                            int total = rs.getInt("TOTAL");
                            System.out.println(callnum + "|" + name + "|" + ccname + "|" + manufacture + "|" + numcop + "|");
                        }
                        System.out.println("End of Query");
                    } catch (ClassNotFoundException e) {
                        // e.printStackTrace();
                        System.out.printf("[Error]: %s\n", e.getMessage());
                    } catch (SQLException e) {
                        // e.printStackTrace();
                        System.out.println("[Error]: server cannot connect to database");
                    }
                }
                if (var4 == '3') {
                    System.out.println("Type in the search keyword");
                    Scanner obj = new Scanner(System.in);
                    String input = obj.nextLine();

                    // String sql = "Select * From car Where callnum = " + input + "";
                    String sql = "SELECT callnum, name, ccname, manufacture, copynum FROM car c, car_category cc, copy cpy WHERE c.ccid = cc.ccid AND c.callnum = cpy.callnum AND manufacture LIKE '%" + input + "%' ORDER BY callnum ASC";

                    //Connection con = connect();

                    try {
                        Class.forName(jdbcDriver);
                        Connection conn = DriverManager.getConnection(dbAddress, userName, password);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        System.out.println("Call Num|Name|Car Category|Company|Available No. of Copy");
                        while (rs.next()) {
                            String callnum = rs.getString("callnum");
                            String name = rs.getString("name");
                            String ccname = rs.getString("ccname");
                            String manufacture = rs.getString("manufacture");
                            double numcop = rs.getInt("copynum");
                            System.out.println(callnum + "|" + name + "|" + ccname + "|" + manufacture + "|" + numcop + "|");
                        }
                        System.out.println("End of Query");
                    } catch (ClassNotFoundException e) {
                        // e.printStackTrace();
                        System.out.printf("[Error]: %s\n", e.getMessage());
                    } catch (SQLException e) {
                        // e.printStackTrace();
                        System.out.println("[Error]: server cannot connect to database");
                    }
                }
                var1 = true;
            } else {
                System.out.println("[Error]: Invalid selection. Please select between 1 and 3!");
            }

        }
    }

    public static void loanRecord() {
        System.out.println("Enter the user ID:");
        //System.out.println("Type in the search keyword");
        Scanner obj = new Scanner(System.in);
        String input = obj.nextLine();

        // String sql = "Select * From car Where callnum = " + input + "";
        String sql = "SELECT callnum, copynum, name, manufacture, checkout, return_date FROM rent r, car c WHERE r.callnum = c.callnum  ORDER BY checkout DSC";



        try {
            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(dbAddress, userName, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
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
                System.out.println(callnum + "|" + copynum + "|" + name + "|" + manufacture + "|" + checkout + "|" + check + "|");
            }
            System.out.println("End of Query");
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            System.out.printf("[Error]: %s\n", e.getMessage());
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("[Error]: server cannot connect to database");
        }


        callUser();

    }


}
