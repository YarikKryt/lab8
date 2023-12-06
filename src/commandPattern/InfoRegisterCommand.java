package commandPattern;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class InfoRegisterCommand implements ICommand {
    private Statement statement;
    private RegisterCommand registerCommand;
    private ArrayList<String> snp = new ArrayList<>();
    private ArrayList<String> birthDate = new ArrayList<>();
    private ArrayList<String> city = new ArrayList<>();
    private ArrayList<String> experienceDriving = new ArrayList<>();
    private ArrayList<String> categoryB = new ArrayList<>();
    private ArrayList<String> experienceTaxiDriverB = new ArrayList<>();
    private ArrayList<String> experienceTaxiDriver = new ArrayList<>();
    private ArrayList<String> phoneNumber = new ArrayList<>();
    private ArrayList<String> selfDescription = new ArrayList<>();
    InfoRegisterCommand(Statement statement, RegisterCommand registerCommand){
        this.registerCommand = registerCommand;
        this.statement = statement;
    }

    @Override
    public void execute() {
        Scanner sc= new Scanner(System.in);
        System.out.println("Enter your Surname, Name and Patronymic");
        snp.add(sc.nextLine());
        System.out.println("Enter your birth date | Format YYYY-MM-DD");
        birthDate.add(sc.nextLine());
        System.out.println("Enter your city");
        city.add(sc.nextLine());
        System.out.println("Enter since what year you have driving license | Format YYYY-MM-DD");
        experienceDriving.add(sc.nextLine());
        while (true){
            System.out.println("Do you have category B license? | Type 'yes' or 'no'");
            String bool = sc.nextLine();
            if(bool.equals("yes") || bool.equals("no")){
                if(bool.equals("yes"))
                    categoryB.add("1");
                if(bool.equals("no"))
                    categoryB.add("0");
                break;
            }
            System.out.println("You didn't entered 'yes' or 'no'");
        }
        while (true){
            System.out.println("Do you have taxi driver experience? | Type 'yes' or 'no'");
            String bool = sc.nextLine();
            if(bool.equals("yes") || bool.equals("no")){
                if(bool.equals("yes"))
                    experienceTaxiDriverB.add("1");
                if(bool.equals("no"))
                    experienceTaxiDriverB.add("0");
                break;
            }
            System.out.println("You didn't entered 'yes' or 'no'");
        }
        if(experienceTaxiDriverB.get(experienceTaxiDriverB.size() - 1) == "1"){
            System.out.println("Enter since when you have been a taxi driver | Format YYYY-MM-DD");
            experienceTaxiDriver.add(sc.nextLine());
        } else {
            experienceTaxiDriver.add("NULL");
        }
        System.out.println("Enter your phone number");
        phoneNumber.add(sc.nextLine());
        System.out.println("Do you want to tell something about yourself? | Type 'yes' or 'no'");
        String bool = sc.nextLine();
        while (true){
            if(bool.equals("yes")){
                System.out.println("Please tell us about yourself:"); //Optional
                selfDescription.add(sc.nextLine());
                break;
            }
            if(bool.equals("no")){
                selfDescription.add("NULL");
                break;
            }
            System.out.println("You didn't entered 'yes' or 'no'");
        }
        String sqlCommand, sqlCommand1, sqlCommand2;
        sqlCommand1 = "SET IDENTITY_INSERT UserInformation ON insert into UserInformation(ID,SNP,BirthDate,City,ExperienceDriving,CategoryB,ExperienceTaxiDriverB,ExperienceTaxiDriver,PhoneNumber,SelfDescription) ";
        sqlCommand2 = "values("+
                registerCommand.getID()+",'"+
                snp.get(snp.size()-1)+"', '"+
                birthDate.get(birthDate.size()-1)+"', '"+
                city.get(city.size()-1)+"','"+
                experienceDriving.get(experienceDriving.size()-1)+"'," +
                categoryB.get(categoryB.size()-1)+","+
                experienceTaxiDriverB.get(experienceTaxiDriverB.size()-1)+",'" +
                experienceTaxiDriver.get(experienceTaxiDriver.size()-1)+"','"+
                phoneNumber.get(phoneNumber.size()-1)+"', '" +
                selfDescription.get(selfDescription.size()-1)+"');";
        sqlCommand = sqlCommand1 + sqlCommand2;
        System.out.println(sqlCommand);
        try{
            statement.execute(sqlCommand);
        }catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unexecute() {

    }
}