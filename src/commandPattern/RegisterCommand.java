package commandPattern;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class RegisterCommand implements ICommand
{
    private ArrayList<String> Username = new ArrayList<>();
    private ArrayList<String> Password = new ArrayList<>();
    private Statement statement;

    RegisterCommand(Statement statement){
        this.statement = statement;
    }
    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        String username = " ";
        String password = " ";
        while (true)
        {
            System.out.print("Enter your username: ");
            String usernameLoop = sc.nextLine();
            String sqlCommand = "select Username from UserAccount where Username = '"+usernameLoop+"'";
            String usernameCheck = " ";
            try {
                ResultSet resultSet = statement.executeQuery(sqlCommand);
                while (resultSet.next()) {
                    usernameCheck = resultSet.getString("Username");
                }
            }catch (SQLException e) {
                System.out.println("An error occurred:");
                throw new RuntimeException(e);
            }
            if(usernameLoop.equals(usernameCheck))
                System.out.println("This username already exists, try again");
            if(!usernameLoop.equals(usernameCheck)){
                username = usernameLoop;
                break;
            }
        }
        while(true)
        {
            System.out.print("Enter your password: ");
            String password1 = sc.nextLine();
            System.out.print("Enter your password again: ");
            String password2 = sc.nextLine();
            if(password1.equals(password2)){
                password = password1;
                break;
            }
        }
        this.Password.add(password);
        this.Username.add(username);
        try {
            String sqlCommand = "insert into UserAccount(Username,[Password]) values('"
                    +Username.get(Username.size() - 1)
                    +"','"+Password.get(Password.size() - 1)+"');";
            statement.execute(sqlCommand);
        }catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void unexecute() {
        try {
            String sqlCommand = "delete from UserAccount where Username = '"+
                    Username.get(Username.size() - 1)+"';";
            statement.execute(sqlCommand);
        }catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }
        Username.remove(Username.size() - 1);
        Password.remove(Password.size() - 1);
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