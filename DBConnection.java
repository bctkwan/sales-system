/*
 * CSCI3170
 * Project
 * Phase 2
 * Java application
 * 
 * Group 1
 * Kwan Chun Tat    1155033423
 * Liu Sik Chung    1155049669
 * Ng Tsz Tan       1155050243
 */


import java.sql.*;


public class DBConnection {
    private static String url = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2712/db12?autoReconnect=true&useSSL=false";
    private static String user = "db12";
    private static String password = "d33f1c87";
    
    private static Connection conn = null;
    
    public static Connection getConnection() {
        try {
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e) {
            System.err.println("Unable to connect to the database!");
            System.exit(1);
        }
        return conn;
    }
    
    public static void closeConnection(Connection conn) {
        try {
            conn.close();
        }
        catch (SQLException e) {
            System.err.println("Unable to close the database connection!");
        }
    }
}
