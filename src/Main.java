import commandPattern.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException{
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TaxiDepotApp;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";

        try{
            Connection connection;
            connection = DriverManager.getConnection(url);
            if (connection != null) {
                System.out.println("Connected");
            }

            while (true) {
                Statement statement = connection.createStatement();
                AccountManager acc = new AccountManager(statement);
                Scanner sc = new Scanner(System.in);

                boolean programShut = false;
                System.out.println("If you want to log in   - press 0");
                System.out.println("If you want to register - press 1");
                System.out.println("If you want to close program - press 2");
                while (true) {
                    String choice = sc.nextLine();
                    if (choice.equals("0") || choice.equals("1") || choice.equals("2")) {
                        if (choice.equals("0"))
                            acc.logInAccount();
                        if (choice.equals("1"))
                            acc.createAccount();
                        if(choice.equals("2"))
                            programShut = true;
                        break;
                    }
                    System.out.println("You didn't choose between 0 to 2");
                }
                if(programShut == true)
                    break;

                System.out.println("Your options menu:");
                System.out.println("Show my data: press 1");
                System.out.println("Show renting car: press 2");
                System.out.println("Log out***: press 6");

                SmsService.writeLineToFile("Entering a menu");

                while (true) {
                    String choice = sc.nextLine();
                    if (choice.equals("1"))
                        acc.showUserData();
                    if (choice.equals("2"))
                        acc.showRentCars();
                    if (choice.equals("6")) {
                        SmsService.writeLineToFile("Log out");
                        break;
                    }
                    System.out.println("Your next options menu:");
                    System.out.println("Show my data: press 1");
                    System.out.println("Show renting car: press 2");
                    System.out.println("Log out***: press 6");
                }
            }

            System.out.println("Program succesfuly closed");
            SmsService.writeLineToFile("Program succesfuly closed");

            connection.close();
        } catch (SQLException e) {
            ByteArrayOutputStream stackTraceStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(stackTraceStream));
            String stackTrace = stackTraceStream.toString();

            SmsService.emailMsg("An error occurred:", stackTrace);
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }
    }
}