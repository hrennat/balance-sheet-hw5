import pojo.Accounts;
import pojo.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Account {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/NewTest";
    static final String USER = "postgres";
    static final String PASS = "Eye0ftiger";
    static List<Accounts> accounts = new ArrayList<>();
    static List<Users> users = new ArrayList<>();
    static int accountId;
    static int userId;
    static long balance;
    static String currency;


    public static void main(String[] args) {
        Account acc = new Account();

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            try {
                Scanner sc = new Scanner(System.in);
              try{
                System.out.println("Insert UserId");
                 userId = Integer.parseInt(sc.nextLine());
                 System.out.println("Insert balance");
                 balance = Long.parseLong(sc.nextLine());
                 System.out.println("Insert currency");
                 currency = sc.nextLine();
                acc.selectUser(connection);
                acc.select(connection,accountId,userId,balance,currency);
                acc.insert(connection, userId,balance, currency);
              } catch (NumberFormatException n){
                  System.out.println("Please enter valid value for UserId, Balance or Currency");
              }
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean doesUserCurrencyAlreadyExist(int userId, String currency){
for(Accounts a: accounts){
    if(a.getUserId()==userId && a.getCurrency().equals(currency)){
        return true;
    }
}
return false;
    }

    static boolean doesUserExist(int userId){
        for(Users u: users){
            if(u.getUserId()==userId){
                return true;
            }
        }
        return false;
    }

    private void insert (Connection connection, int userId, long balance, String currency) throws SQLException {
        String generatedColumns[] = { "accountId" };
        if(!doesUserExist(userId)){
            System.out.println("UserId " +userId+" does not exist");
            return;
        } else if (doesUserExist(userId)){
        if (doesUserCurrencyAlreadyExist(userId, currency)) {
            System.out.println("Account already exists for UserId " + userId + " and  currency " + currency);
            return;
        } else if (!doesUserCurrencyAlreadyExist(userId, currency)) {
            if ((balance<0) | (balance>2000000000)) {
                System.out.println("Please check balance. It should be greater than 0 and less than 2000000000");
                return;
            } else if ((balance>=0) && (balance <= 2000000000)){
            String insertRqt = "INSERT INTO ACCOUNTS (userId, balance, currency) VALUES (?, ?, ?) " +
                    "returning accountId ";
            PreparedStatement stmt = connection.prepareStatement(insertRqt, generatedColumns);
            stmt.setInt(1, userId);
            stmt.setLong(2, balance);
            stmt.setString(3, currency);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                System.out.println("Inserted accountId - " + id); // display inserted record
            }
            rs.close();
            stmt.close();
        }
        }
        }
    }
    private void select (Connection connection, int accountId, int userId, long balance, String currency) throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ACCOUNTS;");
        while (rs.next()) {
            accountId = rs.getInt("accountId");
            userId = rs.getInt("userId");
            balance = rs.getLong("balance");
            currency = rs.getString("currency");
            accounts.add(new Accounts(accountId,userId,balance, currency));
        }
        rs.close();
        stmt.close();

    }

    private void selectUser (Connection connection) throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM USERS;");
        while (rs.next()) {
            int userId = rs.getInt("userId");
            String name = rs.getString("name");
            String address = rs.getString("address");
            users.add(new Users(userId,name, address));
        }
        rs.close();
        stmt.close();

    }
}
