import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.*;
import java.sql.*;

public class manager {
    private String jdbcDriver = "com.mysql.jdbc.Driver";
    private String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db25";
    private String userName = "Group25";
    private String password = "CSCI3170";

    public manager() {
    }

    public int run() {
        Boolean exit = false;
        while (!exit) {
            System.out.println("-----Operations for administrator menu-----");
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

        // Check whether the User ID is in the database
        String sql;
        sql = "SELECT uid, callnum, copynum, checkout, return_date" +
                "FROM rent R" +
                "WHERE R.uid=" + userID;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            rs.last();
            if (rs.getRow() != 1){
                System.out.println("The input User ID " + userID + " is not found in database!");
                userID = "";
            };
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userID;
    }


    private String readCallNumber(Statement stmt, Scanner inputScanner) {
        return "";
    }
    private String readCopyNumber(Statement stmt, Scanner inputScanner) {
        return "";
    }

    public void rentCar() {
        try {

            // Setup connection
            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(dbAddress, userName, password);
            Statement stmt = conn.createStatement();

            Scanner inputScanner = new Scanner(System.in);

            String userID = this.readUserID(stmt, inputScanner);
            String callNumber = this.readCallNumber(stmt, inputScanner);
            String copyNumber = this.readCopyNumber(stmt, inputScanner);


            // Do job!
            // Step 1: Then the system should check whether that car is available
            // to be rented (i.e., There is no rent record of the specified car copy
            // with NULL return date).
            //
            // Step 2: If the car copy is available, it is then borrowed and a new
            // check-out record of the specified car copy and user with NULL
            // return date should be added to the database accordingly.
            //
            // Step 3: Finally, there should be an informative message whether
            // the car copy can be lent successfully in layman terms.
            //


            // Finish
            System.out.println("Car renting performed successfully.");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void returnCar() {
    }

    public void listUnreturnedCarCopies() {
    }
}






























