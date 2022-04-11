import java.util.*;

public class main {
  public static void main(String[] args) {
    Boolean exit = false;
    System.out.println("Welcome to Car Renting System!");
    while (!exit) {
      System.out.println("-----Main menu-----");
      System.out.println("What kind of operations would you like to perform?");
      System.out.println("1. Operations for Administrator");
      System.out.println("2. Operations for User");
      System.out.println("3. Operations for Manager");
      System.out.println("4. Exit this program");
      System.out.printf("Enter Your Choice: ");
      Scanner input = new Scanner(System.in);
      String inputString = input.next();
      char nextAct = inputString.charAt(0);
      if (inputString.length() == 1) {
        if (nextAct == '4') {
          exit = true;
          continue;
        } else if (nextAct == '3') {
          manager mgr = new manager();
          mgr.run();
        } else if (nextAct == '2') {
          User obj = new User();
          obj.callUser();
        } else if (nextAct == '1') {
          admin adm = new admin();
          adm.run();
        } else {
          System.out.println("[Error]: Invalid selection. Please select between 1 and 4!");
        }
      } else {
        System.out.println("[Error]: Invalid selection. Please select between 1 and 4!");
      }
    }
  }
}