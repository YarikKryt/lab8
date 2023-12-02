package commandPattern;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShowUserData implements ICommand{
    protected int ID;
    private Statement statement;
    ShowUserData(Statement statement){
        this.statement = statement;
    }
    @Override
    public void execute() {
        String sqlCommand = "SELECT SNP, BirthDate, City, ExperienceDriving, CategoryB, " +
                "ExperienceTaxiDriverB, ExperienceTaxiDriver, PhoneNumber, SelfDescription " +
                "FROM UserInformation where ID = " + ID;

        try {
            ResultSet resultSet = statement.executeQuery(sqlCommand);
            while (resultSet.next())
            {
                String snp = resultSet.getString("SNP");
                String birthDate = resultSet.getString("BirthDate");
                String city = resultSet.getString("City");
                String experienceDriving = resultSet.getString("ExperienceDriving");
                String categoryB = resultSet.getString("CategoryB");
                String experienceTaxiDriverB = resultSet.getString("ExperienceTaxiDriverB");
                String experienceTaxiDriver = resultSet.getString("ExperienceTaxiDriver");
                String phoneNumber = resultSet.getString("PhoneNumber");
                String selfDescription = resultSet.getString("SelfDescription");

                System.out.println("- My information -");
                System.out.printf("SNP: %s | City: %s | Birth date: %s\n", snp, city, birthDate);
                System.out.printf("Category B: %s | Driving license since: %s\n", categoryB.equals("1") ? "yes" : "no", experienceDriving);
                if(experienceTaxiDriverB.equals("1"))
                    System.out.printf("Taxi Driver since: %s\n", experienceTaxiDriver);
                System.out.printf("Your phone number: %s\n", phoneNumber);
                if(!selfDescription.equals("NULL"))
                    System.out.printf("Your self description:\n%s\n", selfDescription);
                System.out.println("- - - - - - - - - - - -");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unexecute() {

    }
}
