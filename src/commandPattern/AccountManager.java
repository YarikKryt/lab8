package commandPattern;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AccountManager
{
    private Statement statement;
    private RegisterCommand registerCommand;
    private InfoRegisterCommand infoRegisterCommand;
    private LogInCommand logInCommand;
    private ShowUserData showUserData;
    private int ID;

    public AccountManager(Statement statement){
        this.statement = statement;
        this.registerCommand = new RegisterCommand(statement);
        this.infoRegisterCommand = new InfoRegisterCommand(statement, registerCommand);
        this.logInCommand = new LogInCommand(statement);
        this.showUserData = new ShowUserData(statement);
    }
    public void setCommand(ICommand command) {}
    public void createAccount() {
        registerCommand.execute();
        infoRegisterCommand.execute();
        this.ID = registerCommand.getID();

        SmsService.writeLineToFile("Account create");
    }
    public void logInAccount(){
        logInCommand.execute();
        this.ID = logInCommand.getID();
        System.out.println("You are logged in!");
        SmsService.writeLineToFile("Log In");
    }
    public void showUserData(){
        this.showUserData.ID = this.ID;
        showUserData.execute();
        showUserRentCars(ID);
        SmsService.writeLineToFile("Show user data");
    }
    public void showRentCars(){
        showRentCars(ID);
    }

    private void showRentCars(int userID){
        SmsService.writeLineToFile("Show rent cars");

        String sqlCommand = "select * from TaxiDepot";
        Scanner sc = new Scanner(System.in);
        try {
            ResultSet resultSet = statement.executeQuery(sqlCommand);
            System.out.println("- Cars for rent -");
            while (resultSet.next())
            {
                int id = resultSet.getInt("ID");
                String fuel = resultSet.getString("Fuel");
                String bodyType = resultSet.getString("BodyType");
                String brand = resultSet.getString("Brand");
                String model = resultSet.getString("Model");
                String year = resultSet.getString("Year");
                String price = resultSet.getString("Price");
                String priceToRent = resultSet.getString("PriceToRent");
                String transmission = resultSet.getString("Transmission");
                String userAccountID = resultSet.getString("UserAccountID");
                float litersPer100Km = resultSet.getFloat("LitersPer100Km");
                float accelerationTo100KmAnHour = resultSet.getFloat("AccelerationTo100KmAnHour");

                System.out.printf("ID: %d | %s %s %s - %s\n", id, brand, model, year, (userAccountID == null) ? "unoccupied" : "occupied");
                System.out.printf(" Fuel: %s | Body Type: %s | Transmission: %s\n", fuel, bodyType, transmission);
                System.out.printf(" Price to rent: %s | Car price: %s\n", priceToRent, price);
                if(fuel.equals("Gasoline"))
                    System.out.printf(" Liters per 100 km: %s L\n", litersPer100Km);
                System.out.printf(" Acceleration To 100 Km An Hour: %s s.\n\n", accelerationTo100KmAnHour);
            }
            System.out.println("- - - - - - - - - - - -");

            System.out.println("Do you want to rent a car?");
            System.out.println("yes - press '0'");
            System.out.println("no - press '1'");
            while (true){
                String choice = sc.nextLine();
                if(choice.equals("0")){
                    System.out.println("Please enter ID of a car");
                    while (true) {
                        try {
                            int carID = Integer.parseInt(sc.nextLine());
                            String updateSql = "UPDATE TaxiDepot SET UserAccountID = " + userID + " WHERE ID = " + carID;

                            int idExist = 0;
                            boolean unoccupied = false;
                            ResultSet carExist = statement.executeQuery("select * from TaxiDepot where ID = "+ carID);
                            while (carExist.next()){
                                idExist = carExist.getInt("ID");
                                int freeRent = carExist.getInt("UserAccountID");
                                if(freeRent == 0)
                                    unoccupied = true;
                            }
                            if(idExist == carID && unoccupied == true){
                                statement.execute(updateSql);
                                System.out.printf("You now renting a car under ID - %d\n", carID);
                                break;
                            }
                            else
                                System.out.println("Car with this ID does not exist or it's occupied");

                        } catch (NumberFormatException nfe) {
                            System.out.println("Input data is wrong");
                        }
                    }
                    break;
                }
                if(choice.equals("1"))
                    break;
                System.out.println("Input data is wrong");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }
    }

    private void showUserRentCars(int userID){
        SmsService.writeLineToFile("Show user rent cars");

        String sqlCommand = "select * from TaxiDepot where UserAccountID = "+ userID;
        boolean rentingCars = false;
        try {
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            while (resultSet.next())
            {
                int id = resultSet.getInt("ID");
                if(id != 0)
                    rentingCars = true;
                String fuel = resultSet.getString("Fuel");
                String bodyType = resultSet.getString("BodyType");
                String brand = resultSet.getString("Brand");
                String model = resultSet.getString("Model");
                String year = resultSet.getString("Year");
                String price = resultSet.getString("Price");
                String priceToRent = resultSet.getString("PriceToRent");
                String transmission = resultSet.getString("Transmission");
                String userAccountID = resultSet.getString("UserAccountID");
                float litersPer100Km = resultSet.getFloat("LitersPer100Km");
                float accelerationTo100KmAnHour = resultSet.getFloat("AccelerationTo100KmAnHour");

                System.out.printf("ID: %d | %s %s %s\n", id, brand, model, year);
                System.out.printf(" Fuel: %s | Body Type: %s | Transmission: %s\n", fuel, bodyType, transmission);
                System.out.printf(" Price to rent: %s | Car price: %s\n", priceToRent, price);
                if(fuel.equals("Gasoline"))
                    System.out.printf(" Liters per 100 km: %s L\n", litersPer100Km);
                System.out.printf(" Acceleration To 100 Km An Hour: %s s.\n\n", accelerationTo100KmAnHour);
            }
            if(rentingCars == true){
            Scanner sc = new Scanner(System.in);
            System.out.println("- You are renting these cars -");
            System.out.println("Do you want to unrent a car?");
            System.out.println("yes - press '0'");
            System.out.println("no - press '1'");
            while (true){
                String choice = sc.nextLine();
                if(choice.equals("0")){
                    System.out.println("Please enter ID of a car");
                    while (true) {
                        try {
                            int carID = Integer.parseInt(sc.nextLine());
                            String updateSql = "UPDATE TaxiDepot SET UserAccountID = null WHERE ID = " + carID;

                            int idExist = 0;
                            ResultSet carExist = statement.executeQuery("select * from TaxiDepot where ID = "+carID+" and UserAccountID = "+ userID);
                            while (carExist.next()){
                                idExist = carExist.getInt("ID");
                            }
                            if(idExist == carID){
                                statement.execute(updateSql);
                                System.out.printf("You now unrented a car under ID - %d\n", carID);
                                break;
                            }
                            else
                                System.out.println("Car with this ID does not exist");
                        } catch (NumberFormatException nfe) {
                            System.out.println("Input data is wrong");
                        }
                    }
                    break;
                }
                if(choice.equals("1"))
                    break;
                System.out.println("Input data is wrong");
            }
            }
            else
                System.out.println("- You don't rent any cars currently -");
        } catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }
    }
}