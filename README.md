# CSCI 3170 Database Course Project

* Group Number: 25
* Group Members:
  * Ekansh Gahlot (SID: 1155139809)	
  * Resul Mamiyev (SID: 1155135439)	
  * Zhenghao Peng (SID: 1155136634)


## Compile Instruction

We use `Oracle OpenJDK version 17.0.2` and `language level 17` to compile our program.

To compile files, you can use IDE like IntelliJ IDEA to compile the `src/main.java` file like us or using the following 
build script:

```bash
# Please make sure you are in ./CSCI3170project

# To compile the files
javac src/*.java -d out 

# To run main program
java -cp out:my-sql-connector-java-5.1.47.jar main
```

## Setup local mysql server

In `Admin|Manager|User.java`, you will find following arguments:

```java
// Used for local test
private String dbAddress = "jdbc:mysql://localhost:3306/db25";
private String userName = "root";
private String password = "12345678";
```

These parameters specify the location of local mysql server. Before you launching our code,
please setup local mysql server via:

```bash
mysql -u root -p  # User name is root
12345678  # The password

SHOW GLOBAL VARIABLES LIKE 'PORT';  # Make sure the port is 3306

# Create the database named db25
CREATE database db25;
use db25;
```

After setting up the mysql server, you can compile and run `main.java`. Hope you enjoy our project!

April 2022