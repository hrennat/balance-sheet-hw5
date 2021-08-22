import java.sql.*;
import java.util.Scanner;


public class User {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/NewTest";
    static final String USER = "postgres";
    static final String PASS = "Eye0ftiger";
    static String name;
    static String address;


    public static void main(String[] args) throws SQLException {


        User user = new User( );
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("Insert name");
                name = sc.nextLine();
                System.out.println("Insert address");
                address = sc.nextLine();
                user.insert(connection, name,address);
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void insert (Connection connection, String name, String address) throws SQLException{
        String generatedColumns[] = { "userId" };
        String insertRqt = "INSERT INTO USERS (name, address) VALUES (?, ?)" +
                "returning userId" ;
        PreparedStatement stmt = connection.prepareStatement(insertRqt,generatedColumns);

        stmt.setString(1, name);
        stmt.setString(2, address);
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
