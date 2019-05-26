package lmao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static Connection conn;
    private static String username = "s263068";
    private static String password = "zet761";
    private static String url = "jdbc:postgresql://localhost:5432/studs";

    public static Connection connect() throws SQLException {
        try{
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException cnfe){
            System.err.println("Error: "+cnfe.getMessage());
        }
        conn = DriverManager.getConnection(url,username,password);
        return conn;
    }
    public static Connection getConnection() throws SQLException{
        if(conn !=null && !conn.isClosed())
            return conn;
        connect();
        return conn;

    }
}