import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class manager {
    private String someVariable = "com.mysql.jdbc.Driver";

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

    public void rentCar() {
    }

    public void returnCar() {
    }

    public void listUnreturnedCarCopies() {
    }
}






























