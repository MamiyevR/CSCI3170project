import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;

public class Manager {
    private String jdbcDriver = "com.mysql.jdbc.Driver";
//    private String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db25";
//    private String userName = "Group25";
//    private String password = "CSCI3170";

    // Used for local test
    private String dbAddress = "jdbc:mysql://localhost:3306/db25";
    private String userName = "root";
    private String password = "12345678";

    public Manager() {
    }

    public int run() {
        Boolean exit = false;
        while (!exit) {
            System.out.println("-----Operations for manager menu-----");
            System.out.println("What kind of operations would you like to perform?");
            System.out.println("1. Car Renting");
            System.out.println("2. Car Returning");
            System.out.println("3. List all un-returned car copies which are checked-out within a period");
            System.out.println("4. Return to the main menu");
            System.out.printf("Enter Your Choice: ");
            Scanner input = new Scanner(System.in);
            String inputString = input.next();
            char nextAct = inputString.charAt(0);
            if (inputString.length() == 1) {
                if (nextAct == '4') {
                    exit = true;
                    continue;
                } else if (nextAct == '1') {
                    this.rentCar();
                } else if (nextAct == '2') {
                    this.returnCar();
                } else if (nextAct == '3') {
                    this.listUnreturnedCarCopies();
                } else {
                    System.out.println("[Error]: Invalid selection. Please select between 1 and 5!");
                }
            } else {
                System.out.println("[Error]: Invalid selection. Please select between 1 and 5!");
            }
        }
        return 1;
    }

    private String readUserID(Statement stmt, Scanner inputScanner) {
        System.out.printf("Enter the User ID: ");
        String userID = inputScanner.next();

//         Check whether the User ID is in the database
        String sql;
        sql = "SELECT * FROM user u WHERE u.uid='" + userID + "'";
//        System.out.println("SQL: "+ sql);

        try {
            ResultSet rs = stmt.executeQuery(sql);
            rs.last();
            if (rs.getRow() != 1) {
                System.out.println("The input User ID: " + userID + " is not found in database!");
                userID = "";
            }
            ;
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            userID = "";
        }

        return userID;
    }


    private String readCallNumber(Statement stmt, Scanner inputScanner) {
        System.out.printf("Enter the Call Number: ");
        String callNum = inputScanner.next();

//         Check whether the User ID is in the database
        String sql;
        sql = "SELECT * FROM car c WHERE c.callnum='" + callNum + "'";
//        System.out.println("SQL: "+ sql);

        try {
            ResultSet rs = stmt.executeQuery(sql);
            rs.last();
            if (rs.getRow() != 1) {
                System.out.println("The input Call Number: " + callNum + " is not found in database!");
                callNum = "";
            }
            ;
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            callNum = "";
        }

        return callNum;
    }

    private String readCopyNumber(String callnum, Statement stmt, Scanner inputScanner) {
        System.out.printf("Enter the Copy Number: ");
        String copyNum = inputScanner.next();

//         Check whether the User ID is in the database
        String sql;
        sql = "SELECT * FROM copy c WHERE c.copynum='" + copyNum + "' AND c.callnum='" + callnum + "' ";
//        System.out.println("SQL: "+ sql);

        try {
            ResultSet rs = stmt.executeQuery(sql);
            rs.last();
            if (rs.getRow() != 1) {
                System.out.println("The input Copy Number: " + copyNum + " is not found in database!");
                copyNum = "";
            }
            ;
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            copyNum = "";
        }

        return copyNum;
    }

    private String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void rentCar() {
        try {

            // Setup connection
            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(dbAddress, userName, password);
            Statement stmt = conn.createStatement();

            Scanner inputScanner = new Scanner(System.in);

            String userID = this.readUserID(stmt, inputScanner);
            if (Objects.equals(userID, "")) return;

            String callNumber = this.readCallNumber(stmt, inputScanner);
            if (Objects.equals(callNumber, "")) return;

            String copyNumber = this.readCopyNumber(callNumber, stmt, inputScanner);
            if (Objects.equals(copyNumber, "")) return;

            // Do job!
            // Step 1: Then the system should check whether that car is available
            // to be rented (i.e., There is no rent record of the specified car copy
            // with NULL return date).
            ResultSet rs;

            String sql = "SELECT * FROM rent r " +
                    "WHERE r.return_date IS NULL " +
                    "AND r.callnum='" + callNumber + "' " +
                    "AND r.copynum=" + Integer.parseInt(copyNumber);
            Boolean should_exit = Boolean.FALSE;
            try {
                rs = stmt.executeQuery(sql);
                rs.last();
                if (rs.getRow() == 1) {
                    System.out.println("The car Call Number: " +
                            callNumber + ", Copy Number: " + copyNumber + " is not available at this time!");
                    should_exit = Boolean.TRUE;
                }
                ;
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                should_exit = Boolean.TRUE;
            }
            if (should_exit) {
                return;
            }

            // Additional step: Check the user already rent too much car
            try {
                sql = "SELECT * From rent r " +
                        "WHERE r.return_date IS NULL AND r.uid='" + userID + "' ";
                should_exit = Boolean.FALSE;
                rs = stmt.executeQuery(sql);
                rs.last();
                int num_rent_cars = rs.getRow();

                sql = "SELECT uc.max FROM user_category uc WHERE uc.ucid IN (" +
                        "SELECT user.ucid FROM user WHERE user.uid='" + userID + "')";
                rs = stmt.executeQuery(sql);
                rs.next();
                int max_num = Integer.parseInt(rs.getString("max"));
                rs.close();

                if (num_rent_cars >= max_num){
                    System.out.println("The user " + userID + " already rent " + num_rent_cars + " cars, " +
                            "exceeding the maximum number of cars " + max_num + " that can be rented!");
                    should_exit = Boolean.TRUE;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                should_exit = Boolean.TRUE;
            }
            if (should_exit) {
                return;
            }

            // Step 2: If the car copy is available, it is then borrowed and a new
            // check-out record of the specified car copy and user with NULL
            // return date should be added to the database accordingly.
            String today_date = this.getDate();
            sql = String.format("INSERT INTO rent(uid, callnum, copynum, checkout, return_date)" +
                            "VALUES (\"%s\", \"%s\", %d, \"%s\", NULL)",
                    userID, callNumber, Integer.parseInt(copyNumber), today_date);
            try {
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                // It is possible that the same car is borrowed and returned TODAY! We can't rent it again!
                System.out.println("The car with user ID: " + userID + ", Call Number: " +
                        callNumber + ", Copy Number: " + copyNumber + " is not available at this time!");
                return;
            }

            // Step 3: Finally, there should be an informative message whether
            // the car copy can be lent successfully in layman terms.
            System.out.println(String.format(
                    "Car renting performed successfully. The User ID %s, Call Number %s, Copy Number %s, Checkout Date %s.",
                    userID, callNumber, copyNumber, today_date
            ));

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnCar() {
        try {
            // First, he/she needs to input call number and copy number of the car copy being borrowed and
            // the user ID of the user.

            // Setup connection
            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(dbAddress, userName, password);
            Statement stmt = conn.createStatement();

            Scanner inputScanner = new Scanner(System.in);

            String userID = this.readUserID(stmt, inputScanner);
            if (Objects.equals(userID, "")) return;

            String callNumber = this.readCallNumber(stmt, inputScanner);
            if (Objects.equals(callNumber, "")) return;

            String copyNumber = this.readCopyNumber(callNumber, stmt, inputScanner);
            if (Objects.equals(copyNumber, "")) return;


            // Then the system should check if a renting record corresponding to
            // the specified user ID, call number and copy number exists
            // and the return date is null.
            ResultSet rs;

            // TODO: To be verified!
            String sql = "SELECT * FROM rent r " +
                    "WHERE r.return_date IS NULL AND r.uid='" + userID + "' " +
                    "AND r.callnum='" + callNumber + "' " +
                    "AND r.copynum=" + Integer.parseInt(copyNumber);
            Boolean should_exit = Boolean.FALSE;
            try {
                rs = stmt.executeQuery(sql);
                rs.last();
                if (rs.getRow() != 1) {
                    System.out.println("The car with user ID: " + userID + ", Call Number: " +
                            callNumber + ", Copy Number: " + copyNumber + " is not borrowed now!");
                    should_exit = Boolean.TRUE;
                }
                ;
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                should_exit = Boolean.TRUE;
            }
            if (should_exit) {
                return;
            }

            // If such record is found, the car copy can be returned,
            // and the return date of the renting record found is updated
            // to be the current date of the database server.
            String today_date = this.getDate();
            sql = "UPDATE rent r SET return_date = '" + today_date + "' " +
                    "WHERE r.uid = '" + userID + "' " +
                    "AND r.callnum = '" + callNumber + "' " +
                    "AND r.copynum = " + Integer.parseInt(copyNumber);
            stmt.executeUpdate(sql);

            System.out.println(String.format(
                    "Car returning performed successfully. The User ID %s, Call Number %s, Copy Number %s, Return Date %s.",
                    userID, callNumber, Integer.parseInt(copyNumber), today_date
            ));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String readDate(Scanner inputScanner) throws ParseException {
        System.out.println("Type in the starting date[dd/mm/yyyy]: ");
        String dateBeforeParsing = inputScanner.next();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date startingDate = sdf.parse(dateBeforeParsing);

        System.out.println("Type in the ending date[dd/mm/yyyy]: ");
        String endDateBeforeParsing = inputScanner.next();
        java.util.Date endingDate = sdf.parse(endDateBeforeParsing);

        if (startingDate.after(endingDate)) {
            System.out.println("Input starting date is after the ending date. Invalid input!");
            return "";
        }

        sdf.applyPattern("yyyy-MM-dd");
        return "r.checkout BETWEEN '" + sdf.format(startingDate) + " 00:00:00' " +
                "AND '" + sdf.format(endingDate) + " 23:59:59' ";
    }

    public void listUnreturnedCarCopies() {
        try {
            // Setup connection
            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(dbAddress, userName, password);
            Statement stmt = conn.createStatement();
            Scanner inputScanner = new Scanner(System.in);
            ResultSet rs;

            String datePeriod = this.readDate(inputScanner);
            if (Objects.equals(datePeriod, "")) return;
            Boolean already_print = Boolean.FALSE;
            String sql = "SELECT * FROM rent r WHERE " + datePeriod + "AND r.return_date is NULL";
            try {
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String uid = rs.getString("uid");
                    String callnum = rs.getString("callnum");
                    double copynum = rs.getInt("copynum");
                    String checkoutdate = rs.getString("checkout");
                    if (already_print == Boolean.FALSE) {
                        System.out.println("|UID|CallNum|CopyNum|Checkout|");
                    }
                    System.out.printf("|%s|%s|%s|%s|%n", uid, callnum, copynum, checkoutdate);
                    already_print = Boolean.TRUE;
                }
                if (already_print) System.out.println("End of Query");
                else System.out.println("Record not found!");
                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException | SQLException | ParseException e) {
            e.printStackTrace();
        }
    }
}






























