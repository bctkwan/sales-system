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


import java.io.*;
import java.sql.*;
import java.util.Scanner;


public class Administrator {
    public static void menu(Connection conn) {
        int choice = 0;
        Scanner scanner= new Scanner(System.in);
        
        System.out.println("\n-----Operations for administrator menu-----");
        System.out.println("What kinds of operation would you like to perform?");
        System.out.println("1. Create all tables");
        System.out.println("2. Delete all tables");
        System.out.println("3. Load from datafile");
        System.out.println("4. Show number of records in each table");
        System.out.println("5. Return to the main menu");
        
        while (choice <1 || choice >5) {
            System.out.print("Enter Your Choice: ");
            choice = scanner.nextInt();
        }
        
        switch (choice) {
            case 1:
                create_table(conn);
                break;
            case 2:
                delete_table(conn);
                break;
            case 3:
                load_datafile(conn);
                break;
            case 4:
                show_records_no(conn);
                break;
            default:
                break;       
        }
    }
    
    public static void create_table(Connection conn) {
        System.out.print("Processing...");
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE category (cID INT(1) PRIMARY KEY, cName VARCHAR(20) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE manufacturer (mID INT(2) PRIMARY KEY, mName VARCHAR(20) NOT NULL, mAddress VARCHAR(50) NOT NULL, mPhoneNumber INT(8) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE part (pID INT(3) PRIMARY KEY, pName VARCHAR(20) NOT NULL, pPrice INT(5) NOT NULL, mID INT(2) NOT NULL, cID INT(1) NOT NULL, pWarrantyPeriod INT(2) NOT NULL, pAvailableQuantity INT(2) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE salesperson (sID INT(2) PRIMARY KEY, sName VARCHAR(20) NOT NULL, sAddress VARCHAR(50) NOT NULL, sPhoneNumber INT(8) NOT NULL, sExperience INT(1) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE transaction (tID INT(4) PRIMARY KEY AUTO_INCREMENT, pID INT(3) NOT NULL, sID INT(2) NOT NULL, tDate DATE NOT NULL)");
            stmt.close();
            System.out.println("Done! Database is initialized!\n");
        }
        catch (SQLException e) {
            System.err.println("Unable to create tables!\n");
        }
    }
    
    public static void delete_table(Connection conn) {
        System.out.print("Processing...");
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE category");
            stmt.executeUpdate("DROP TABLE manufacturer");
            stmt.executeUpdate("DROP TABLE part");
            stmt.executeUpdate("DROP TABLE salesperson");
            stmt.executeUpdate("DROP TABLE transaction");
            stmt.close();
            System.out.println("Done! Database is removed!\n");
        }
        catch (SQLException e) {
            System.err.println("Unable to delete tables!\n");
        }
    }
    
    public static void load_datafile(Connection conn) {
        String path;
        Scanner scanner= new Scanner(System.in);
        
        System.out.print("\nType in the Source Data Folder Path: ");
        path = scanner.nextLine();
        System.out.print("Processing...");
        
        String thisLine;
        String TableName;
        Boolean error = false;
        
        //read category.txt
        TableName="category";
        try {
            int cID; String cName;
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(path+"/"+TableName+".txt"));
            while ((thisLine = br.readLine()) != null){
                String[] resultC = thisLine.split("\t");
                cID = Integer.valueOf(resultC[0]);
                cName = resultC[1];
                try{
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("INSERT INTO "+TableName+"(cID,cName)"+"VALUES ("+cID+",'"+cName+"')");
                    stmt.close();
                }
                catch (SQLException e) {
                    System.err.println("\nUnable to insert record to table " + TableName);
                    error = true;
                    break;
                }
            }
            br.close();
        } catch (IOException x) {
            System.err.println("\nUnable to load " + TableName + ".txt");
            error = true;
        }    
        //The End of reading

        //read manufacturer.txt
        TableName="manufacturer";
        try {
            int mID,mPhoneNumber; String mName, mAddress;
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(path+"/"+TableName+".txt"));
            while ((thisLine = br.readLine()) != null){
                String[] ResultM = thisLine.split("\t");
                mID = Integer.valueOf(ResultM[0]);
                mName = ResultM[1];
                mAddress = ResultM[2];
                mPhoneNumber = Integer.valueOf(ResultM[3]);
                try{
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("INSERT INTO "+TableName+" VALUES ("+mID+",'"+mName+"','"+mAddress+"',"+mPhoneNumber+")");
                    stmt.close();
                }
                catch (SQLException e) {
                    System.err.println("\nUnable to insert record to table " + TableName);
                    error = true;
                    break;
                }
            }
            br.close();
        }catch (IOException x){
            System.err.println("\nUnable to load " + TableName + ".txt");
            error = true;
        }   
        //The End of reading

        //read part.txt
        TableName="part";
        try {
            int mID, cID, pID, pPrice, pWarrantyPeriod, pAvailableQuantity; String pName;
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(path+"/"+TableName+".txt"));
            while ((thisLine = br.readLine()) != null){
                String[] ResultP = thisLine.split("\t");
                pID = Integer.valueOf(ResultP[0]);
                pName = ResultP[1];
                pPrice = Integer.valueOf(ResultP[2]);
                mID = Integer.valueOf(ResultP[3]);
                cID = Integer.valueOf(ResultP[4]);
                pWarrantyPeriod = Integer.valueOf(ResultP[5]);
                pAvailableQuantity = Integer.valueOf(ResultP[6]);
                try{
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("INSERT INTO "+TableName+" VALUES ("+pID+",'"+pName+"',"+pPrice+","+mID+","+cID+","+pWarrantyPeriod+","+pAvailableQuantity+")");
                    stmt.close();
                }
                catch (SQLException e) {
                    System.err.println("\nUnable to insert record to table " + TableName);
                    error = true;
                    break;
                }
            }
            br.close();
        }catch (IOException x){
            System.err.println("\nUnable to load " + TableName + ".txt");
            error = true;
        }   
        //The End of reading

        //read salesperson.txt
        TableName="salesperson";
        try {
            int sID, sPhoneNumber, sExperience, pWarrantyPeriod; String sName, sAddress;
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(path+"/"+TableName+".txt"));
            while ((thisLine = br.readLine()) != null){
                String[] ResultS = thisLine.split("\t");
                sID = Integer.valueOf(ResultS[0]);
                sName = ResultS[1];
                sAddress = ResultS[2];
                sPhoneNumber = Integer.valueOf(ResultS[3]);
                sExperience = Integer.valueOf(ResultS[4]);
                try{
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("INSERT INTO "+TableName+" VALUES ("+sID+",'"+sName+"','"+sAddress+"',"+sPhoneNumber+","+sExperience+")");
                    stmt.close();
                }
                catch (SQLException e) {
                    System.err.println("\nUnable to insert record to table " + TableName);
                    error = true;
                    break;
                }
            }
            br.close();
        }catch (IOException x){
            System.err.println("\nUnable to load " + TableName + ".txt");
            error = true;
        }  
        //The End of reading

        //read transaction.txt
        TableName="transaction";
        try {
            int tID, pID, sID; String tDate;
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(path+"/"+TableName+".txt"));
            while ((thisLine = br.readLine()) != null){
                String[] ResultT = thisLine.split("\t");
                tID = Integer.valueOf(ResultT[0]);
                pID = Integer.valueOf(ResultT[1]);
                sID = Integer.valueOf(ResultT[2]);
                tDate =  ResultT[3];	
                try{
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("INSERT INTO "+TableName+" VALUES ("+tID+","+pID+","+sID+",str_to_date('"+tDate+"','%d/%m/%Y'))");
                    stmt.close();
                }
                catch (SQLException e) {
                    System.err.println("\nUnable to insert record to table " + TableName);
                    error = true;
                    break;
                }
            }
            br.close();
        }catch (IOException x){
            System.err.println("\nUnable to load " + TableName + ".txt");
            error = true;
        }
        //The End of reading
        
        if (error == false) {
            System.out.println("Done! Data is inputted to the database!");
        }
        System.out.println();

    }
    
    public static void show_records_no(Connection conn){
        int count;
        String[] tableName ={"category","manufacturer","part","salesperson","transaction"};
        System.out.println("Number of records in each table:");
        for (int i=0; i<5; i++) {
            try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName[i]);
            while (rs.next()) {
                count = rs.getInt(1);
                System.out.println(tableName[i] + ": " + count);
            }
            rs.close();
            stmt.close();
            }
            catch (SQLException e) {
                System.err.println(tableName[i] + ": Unable to show the number of records!");
            }
        }
        System.out.println();
    }
}
