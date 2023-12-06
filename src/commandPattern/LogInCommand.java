package commandPattern;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class LogInCommand implements ICommand {
    private Statement statement;
    private ArrayList<String> Username = new ArrayList<>();
    LogInCommand(Statement statement){
        this.statement = statement;
    }
    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        String username, password;
        while (true) {
            String usernameCheck = " ";
            System.out.print("Username: ");
            username = sc.nextLine();

            String sqlCommand = "select Username from UserAccount where Username = '" + username + "'";
            try {
                ResultSet resultSet = statement.executeQuery(sqlCommand);

                while (resultSet.next()) {
                    usernameCheck = resultSet.getString("Username");
                }
            } catch (SQLException e) {
                System.out.println("An error occurred:");
                throw new RuntimeException(e);
            }
            if(username.equals(usernameCheck))
                break;
            else
                System.out.println("Incorrect username, try again");
        }
        while (true) {
            String usernameCheck = " ";
            System.out.print("Password: ");
            password = sc.nextLine();

            String sqlCommand = "select Username from UserAccount where Username = '" +
                    username + "' and Password = '"+ password +"'";
            try {
                ResultSet resultSet = statement.executeQuery(sqlCommand);

                while (resultSet.next()) {
                    usernameCheck = resultSet.getString("Username");
                }
            } catch (SQLException e) {
                System.out.println("An error occurred:");
                throw new RuntimeException(e);
            }
            if(username.equals(usernameCheck))
                break;
            else
                System.out.println("Incorrect password, try again");
        }
        this.Username.add(username);
    }

    @Override
    public void unexecute() {

    }

    public int getID(){
        String sqlCommand = "select ID from UserAccount where Username = '"+Username.get(Username.size() - 1)+"'";
        int ID = 0;
        try {
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            while (resultSet.next()){
                ID = resultSet.getInt("ID");
            }
        }catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }

        return ID;
    }
}