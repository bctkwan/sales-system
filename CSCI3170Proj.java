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
import java.util.Scanner;


public class CSCI3170Proj {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception x) {
            System.err.println("Unable to load the driver class!");
            System.exit(1);
        }
        
        Connection conn = DBConnection.getConnection();
        
        int choice = 0;
        Scanner scanner= new Scanner(System.in);
        
        System.out.println("Welcome to sales system!\n");
        
        while (choice !=4) {
            System.out.println("-----Main menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Operations for administrator");
            System.out.println("2. Operations for salesperson");
            System.out.println("3. Operations for manager");
            System.out.println("4. Exit this program");
            
            choice = 0;
            while (choice <1 || choice >4) {
                System.out.print("Enter Your Choice: ");
                choice = scanner.nextInt();
            }
            
            switch (choice) {
                case 1:
                    Administrator.menu(conn);
                    break;
                case 2:
                    Salesperson.menu(conn);
                    break;
                case 3:
                    Manager.menu(conn);
                    break;
                default:
                    break;       
            }
        }
        
        DBConnection.closeConnection(conn);
    }
}
