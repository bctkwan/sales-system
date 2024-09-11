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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;


public class Salesperson {
    public static void menu(Connection conn) {
        int choice = 0;
        Scanner scanner= new Scanner(System.in);
        
        System.out.println("\n-----Operations for salesperson menu-----");
        System.out.println("What kinds of operation would you like to perform?");
        System.out.println("1. Search for parts");
        System.out.println("2. Sell a part");
        System.out.println("3. Return to the main menu");
        
        while (choice <1 || choice >3) {
            System.out.print("Enter Your Choice: ");
            choice = scanner.nextInt();
        }
        
        switch (choice) {
            case 1:
                parts_search(conn);
                break;
            case 2:
                perform_transaction(conn);
                break;
            default:
                break;       
        }
    }
    
    public static void parts_search(Connection conn) {
        int choiceForSearch = 0;
        int choiceForOrder = 0;
        String keyword, criterion, order;
        Scanner scanner= new Scanner(System.in);
        Scanner scanner2= new Scanner(System.in);
        
        System.out.println("Choose the Search criterion:");
        System.out.println("1. Part Name");
        System.out.println("2. Manufacturer Name");
        while (choiceForSearch <1 || choiceForSearch >2) {
            System.out.print("Choose the Search criterion: ");
            choiceForSearch = scanner.nextInt();
        }
        System.out.print("Type in the Search Keyword: ");
        keyword = scanner2.nextLine();
        System.out.println("Choose ordering:");
        System.out.println("1. By price, ascending order");
        System.out.println("2. By price, descending order");
        while (choiceForOrder <1 || choiceForOrder >2) {
            System.out.print("Choose ordering: ");
            choiceForOrder = scanner.nextInt();
        }
        
        if (choiceForSearch==1) {
            criterion = "part.pName";
        }
        else {
            criterion = "manufacturer.mName";
        }
        if (choiceForOrder==1) {
            order = "ASC";
        }
        else {
            order = "DESC";
        }
        
        try {
            int pID, pAvailableQuantity, pWarrantyPeriod, pPrice;
            String pName, mName, cName;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM category,manufacturer,part WHERE category.cID=part.cID AND manufacturer.mID=part.mID AND " + criterion + " like '%" + keyword + "%' ORDER BY part.pPrice " + order);
            System.out.println("| ID | Name | Manufacturer | Category | Quantity | Warranty | Price |");
            while(rs.next()) {
                pID = rs.getInt("pID");
                pName = rs.getString("pName");
                mName = rs.getString("mName");
                cName = rs.getString("cName");
                pAvailableQuantity = rs.getInt("pAvailableQuantity");
                pWarrantyPeriod = rs.getInt("pWarrantyPeriod");
                pPrice = rs.getInt("pPrice");
                System.out.println("| "+ pID +" | "+ pName +" | "+ mName +" | "+ cName +" | "+ pAvailableQuantity +" | "+ pWarrantyPeriod +" | "+ pPrice +" |"); 
            }
            rs.close();
            stmt.close();
            System.out.println("End of Query\n");
        }
        catch (SQLException e) {
            System.err.println("Unable to search!\n");
        }
    }
    
    public static void perform_transaction(Connection conn) {
        int pID, sID;
        Scanner scanner= new Scanner(System.in);
		
        System.out.print("Enter The Part ID: ");
        pID = scanner.nextInt();
        System.out.print("Enter The Saleperson ID: ");
        sID = scanner.nextInt();
        
        try {
            int pAvailableQuantity = 0;
            String pName = null;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT pName,pAvailableQuantity FROM part WHERE part.pID=" + pID);
            while (rs.next()) {
                pName = rs.getString("pName");
                pAvailableQuantity = rs.getInt("pAvailableQuantity");
            }
            rs.close();
            if (pAvailableQuantity > 0) {
                pAvailableQuantity--;
                stmt.executeUpdate("UPDATE part SET pAvailableQuantity=" + pAvailableQuantity + " WHERE part.pID=" + pID);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dateInStr = sdf.format(cal.getTime());
                stmt.executeUpdate("INSERT INTO transaction(pID,sID,tDate) VALUES (" + pID + ","+ sID + ",str_to_date('" + dateInStr + "','%d/%m/%Y'))");
                System.out.println("Product: " + pName + "(id: "+ pID + ") Remaining Quality: " + pAvailableQuantity + "\n");
            }
            else {
                System.out.println("The part cannot be sold!\n");
            }
        }
        catch (SQLException e) {
            System.err.println("Unable to perform transaction!\n");
        }
    }
}












