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


public class Manager {
    public static void menu(Connection conn) {
        int choice = 0;
        Scanner scanner= new Scanner(System.in);
        
        System.out.println("\n-----Operations for manager menu-----");
        System.out.println("What kinds of operation would you like to perform?");
        System.out.println("1. Count the no. of sales record of each salesperson under a specific range on years of experience");
        System.out.println("2. Show the total sales value of each manufacturer");
        System.out.println("3. Show the N most popular part");
        System.out.println("4. Return to the main menu");
        
        while (choice <1 || choice >4) {
            System.out.print("Enter Your Choice: ");
            choice = scanner.nextInt();
        }
        
        switch (choice) {
            case 1:
                count_sales_record(conn);
                break;
            case 2:
                show_total_sales_value(conn);
                break;
            case 3:
                show_n_most_popular(conn);
                break;
            default:
                break;       
        }
    }
    
    public static void count_sales_record(Connection conn){
        int lowerBound, upperBound;
        Scanner scanner= new Scanner(System.in);
        
        System.out.print("Type in the lower bound for years of experience: ");
        lowerBound = scanner.nextInt();
        System.out.print("Type in the upper bound for years of experience: ");
        upperBound = scanner.nextInt();
        
        try {
            int sID, sExperience, noOfTransaction;
            String sName;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT salesperson.sID,salesperson.sName,salesperson.sExperience,COUNT(transaction.tID) FROM salesperson LEFT JOIN transaction ON salesperson.sID=transaction.sID WHERE salesperson.sExperience>=" + lowerBound + " AND salesperson.sExperience<=" + upperBound + " GROUP BY salesperson.sID ORDER BY salesperson.sID DESC");
            System.out.println("Transaction Record:");
            System.out.println("| ID | Name | Years of Experience | Number of Transaction |");
            while(rs.next()) {
                sID = rs.getInt(1);
                sName = rs.getString(2);
                sExperience = rs.getInt(3);
                noOfTransaction = rs.getInt(4);
                System.out.println("| " + sID + " | " + sName + " | " + sExperience + " | " + noOfTransaction + " |"); 
            }
            rs.close();
            stmt.close();
            System.out.println("End of Query\n");
        }
        catch (SQLException e) {
            System.err.println("Unable to count the no. of sales record!\n");
        }
    }
    
    public static void show_total_sales_value(Connection conn) {
        try {
            int mID, totalSalesValue;
            String mName;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT manufacturer.mID,manufacturer.mName,COALESCE(SUM(part.pPrice),0) FROM manufacturer LEFT JOIN (part JOIN transaction ON part.pID=transaction.pID) ON manufacturer.mID=part.mID GROUP BY manufacturer.mID ORDER BY SUM(part.pPrice) DESC");
            System.out.println("| Manufacturer ID | Manufacturer Name | Total Sales Value |");
            while(rs.next()) {
                mID = rs.getInt(1);
                mName = rs.getString(2);
                totalSalesValue = rs.getInt(3);
                System.out.println("| " + mID + " | " + mName + " | " + totalSalesValue + " |"); 
            }
            rs.close();
            stmt.close();
            System.out.println("End of Query\n");
        }
        catch (SQLException e) {
            System.err.println("Unable to show the total sales value!\n");
        }
    }
    
    public static void show_n_most_popular(Connection conn) {
        int n;
        Scanner scanner= new Scanner(System.in);
        
        System.out.print("Type in the number of parts: ");
        n = scanner.nextInt();
        
        try {
            int pID, noOfTransaction;
            String pName;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT part.pID,part.pName,COUNT(*) FROM part,transaction WHERE part.pID=transaction.pID GROUP BY part.pID ORDER BY COUNT(*) DESC LIMIT " + n);
            System.out.println("| Part ID | Part Name | No. of Transaction |");
            while(rs.next()) {
                pID = rs.getInt(1);
                pName = rs.getString(2);
                noOfTransaction = rs.getInt(3);
                System.out.println("| " + pID + " | " + pName + " | " + noOfTransaction + " |"); 
            }
            rs.close();
            stmt.close();
            System.out.println("End of Query\n");
        }
        catch (SQLException e) {
            System.err.println("Unable to show the N most popular parts!\n");
        }
    }
}
