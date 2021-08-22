import pojo.Accounts;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Transaction {

    static final String DB_URL = "jdbc:postgresql://localhost:5432/NewTest";
    static final String USER = "postgres";
    static final String PASS = "Eye0ftiger";
    static List<Accounts> accounts = new ArrayList<>();
    static int accountId;
    static long amount;



    public static void main(String[] args) throws SQLException {

        Transaction tr = new Transaction();
        Scanner sc = new Scanner(System.in);


        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No postgres driver");
        }
        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            try {

                System.out.println("Select the operation: 1 - Deposit, 2 - Withdraw");
                int operation = Integer.parseInt(sc.next());
               switch (operation) {
                  case 1:
                          System.out.println("Deposit");
                          Scanner scanner = new Scanner(System.in);
                          System.out.println("Insert AccountId");
                          accountId = Integer.parseInt(scanner.nextLine());
                          System.out.println("Insert Amount");
                          amount = Long.parseLong(scanner.nextLine());
                          tr.selectAccount(connection);
                          tr.deposit(connection, accountId, amount);
                          break;

                    case 2:
                        System.out.println("Withdraw");
                        System.out.println("Insert AccountId");
                        Scanner scan = new Scanner(System.in);
                        accountId = Integer.parseInt(scan.nextLine());
                        System.out.println("Insert Amount");
                        amount = Long.parseLong(scan.nextLine());
                        tr.selectAccount(connection);
                        tr.withdraw(connection, accountId, amount);
                        break;
                    default:
                        System.out.println("No such operation");
                        break;

               }
            }finally {
                connection.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static boolean doesAccountExist(int accountId){
        for(Accounts a: accounts){
            if(a.getAccountId()==accountId){
                return true;
            }
        }
        return false;
    }

    static long accountBalanceDeposit(int accountId, long amount){
       long  totalAmount= amount;
        for(Accounts a: accounts){
            if(a.getAccountId()==accountId){
                totalAmount = a.getBalance()+amount;
            }
        }
        if (totalAmount>2000000000){
            System.out.println("Balance exceeds 2000000000.000");

        }
        return totalAmount;
    }

    static long accountBalanceWithdrawal(int accountId, long amount){
        long  totalAmount= amount;
        for(Accounts a: accounts){
            if(a.getAccountId()==accountId){
                totalAmount = a.getBalance()-amount;
            }
        }
        if (totalAmount<0){
            System.out.println("Balance cannot be less than 0");

        }
        return totalAmount;
    }

    private void selectAccount (Connection connection) throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ACCOUNTS;");
        while (rs.next()) {
            int accountId = rs.getInt("accountId");
            int userId = rs.getInt("userId");
            long balance = rs.getLong("balance");
            String currency = rs.getString("currency");
            accounts.add(new Accounts(accountId,userId,balance, currency));
        }
        rs.close();
        stmt.close();

    }
   private void deposit(Connection connection, int accountId, long amount ) throws SQLException  {
        if (!doesAccountExist(accountId)){
           System.out.println("AccountId "+ accountId + " does not exist");
            return;}
        else if(doesAccountExist(accountId)) {
            if(amount<0){
                System.out.println("Amount cannot be less than 0");
                return;
            }
        else     if (amount> 100000000){
            System.out.println("Exceeding the transaction amount");
            return;
        }
        else if ((amount <= 100000000)&&(amount>=0)) {
                if (accountBalanceDeposit(accountId, amount)>2000000000){
                    return;
                }
else if (accountBalanceDeposit(accountId, amount)<=2000000000){
                String generatedColumns[] = { "transactionId" };
                String insertRqt = "INSERT INTO TRANSACTIONS (accountId, amount) VALUES (?, ?) returning " +
                        "transactionId";
                PreparedStatement smt = connection.prepareStatement(insertRqt,generatedColumns);
                smt.setInt(1, accountId);
                smt.setLong(2, amount);
                smt.executeUpdate();

                    ResultSet rs = smt.getGeneratedKeys();

                    if (rs.next()) {
                        long id = rs.getLong(1);
                        System.out.println("Inserted transactionId - " + id); // display inserted record
                    }
                    rs.close();
                    smt.close();
                String updateRqt = "UPDATE ACCOUNTS a set balance = balance+amount " +
                        "from transactions t " +
                        "where t.accountId = a.accountId and t.accountId =? and " +
                        "t.transactionId in (select max(transactionId) from Transactions)";
                PreparedStatement stmt = connection.prepareStatement(updateRqt);
                stmt.setInt(1, accountId);
                stmt.executeUpdate();
                stmt.close();
}

        }
    }}

    private void withdraw(Connection connection, int accountId, long amount ) throws SQLException  {
        if (!doesAccountExist(accountId)){
            System.out.println("AccountId "+ accountId + " does not exist");
            return;}
        else if(doesAccountExist(accountId)) {
            if(amount<0){
                System.out.println("Amount cannot be less than 0");
                return;
            }
            else if (amount> 100000000){
                System.out.println("Exceeding the transaction amount");
                return;
            }
            else if (amount <= 100000000) {
                if (accountBalanceWithdrawal(accountId, amount)<0){
                    return;
                }
                else if (accountBalanceWithdrawal(accountId, amount)>=0){
                    String generatedColumns[] = { "transactionId" };
                    String insertRqt = "INSERT INTO TRANSACTIONS (accountId, amount) VALUES (?, ?) returning " +
                            "transactionId";
                    PreparedStatement smt = connection.prepareStatement(insertRqt,generatedColumns);
                    smt.setInt(1, accountId);
                    smt.setLong(2, amount);
                    smt.executeUpdate();

                    ResultSet rs = smt.getGeneratedKeys();

                    if (rs.next()) {
                        long id = rs.getLong(1);
                        System.out.println("Inserted transactionId -" + id); // display inserted record
                    }
                    rs.close();
                    smt.close();
                    String updateRqt = "UPDATE ACCOUNTS a set balance = balance-amount " +
                            "from transactions t " +
                            "where t.accountId = a.accountId and t.accountId =? and " +
                            "t.transactionId in (select max(transactionId) from Transactions)";
                    PreparedStatement stmt = connection.prepareStatement(updateRqt);
                    stmt.setInt(1, accountId);
                    stmt.executeUpdate();
                    stmt.close();
                }

            }
        }}

}
