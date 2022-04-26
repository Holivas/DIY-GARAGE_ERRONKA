/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintWriter;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import mvc.View.*;
import myClasses.*;
import forGraphics.*;

//import myClasses.*;
/**
 *
 * @author arceredillo.adrian
 */
public class Model {

    private final String DB = "db/db_pruebagarage1.db";
    //private static final String DB = "src/db/Hiztegia.db";

    private Connection connect() throws SQLException {

        // SQLite connection string
        //String url = "jdbc:sqlite:" + DB;
        //String url = "jdbc:mariadb://localhost:3307/dbpruebagarage";
        //String url = "jdbc:mariadb://localhost:3307/dbPrueba";
        String url = "jdbc:mysql://localhost:3306/db_pruebagarage1";
        //String url = "jdbc:sqlite:" + DB;

        Connection conn = null;
        /*Connection conn = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/dbpruebagarage", 
        "root", 
        "");
         */
        try {
            conn = DriverManager.getConnection(url, "root", "");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static Connection connect2() {
        // SQLite connection string
        String url = "jdbc:mysql://localhost:3306/db_pruebagarage1";
        //String url = "jdbc:sqlite:" + DB;

        Connection conn = null;
        try {
            //conn = DriverManager.getConnection(url);
            conn = DriverManager.getConnection(url, "root", "");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public String underAgeCustomersString() {
        String sql = "SELECT * FROM customer ORDER BY customer.Birthday desc";  //añadir: limit X
        ArrayList<Customer> underageCustomers = new ArrayList<>();

        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            LocalDate fechaActual = LocalDate.now();
            System.out.println("Fecha actual: " + fechaActual);

            LocalDate fechaLimite = fechaActual.minusYears(18);
            System.out.println("Para ser mayor de edad: " + fechaLimite);

            System.out.println("\nUnderage customers: ");
            System.out.println("==========================================================================================================================");
            System.out.printf("%-10s %10s %15s %15s %15s %-20s %20s \n", "Username", "Name", "Surname", "Password", "Birthday", "Mail", "Phone Number");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {

                LocalDate fechaCustomer = LocalDate.parse(rs.getString("Birthday"));

                if (fechaCustomer.isAfter(fechaLimite)) {
                    Customer underCustomer = new Customer(
                            rs.getString("Username"), rs.getString("Name"), rs.getString("Surname"), rs.getString("Password"),
                            LocalDate.parse(rs.getString("Birthday")), rs.getString("Mail"), rs.getInt("Phone_Number"));

                    underageCustomers.add(underCustomer);

                    System.out.printf("%-10s %10s %15s %15s %15s %-20s %20d \n",
                            rs.getString("Username"),
                            rs.getString("Name"),
                            rs.getString("Surname"),
                            rs.getString("Password"),
                            rs.getString("Birthday"),
                            rs.getString("Mail"),
                            rs.getInt("Phone_Number"));
                }

            }
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            System.out.println(underageCustomers.toString());
            System.out.println("");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return underageCustomers.toString();
    }

    public static ArrayList<Customer> underAgeCustomers() {
        String sql = "SELECT * FROM customer ORDER BY customer.Birthday desc";  //añadir: limit X
        ArrayList<Customer> underageCustomers = new ArrayList<>();

        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            LocalDate fechaActual = LocalDate.now();
            System.out.println("Fecha actual: " + fechaActual);

            LocalDate fechaLimite = fechaActual.minusYears(18);
            System.out.println("Para ser mayor de edad: " + fechaLimite);

            System.out.println("\nUnderage customers: ");
            System.out.println("==========================================================================================================================");
            System.out.printf("%-10s %10s %15s %15s %15s %-20s %20s \n", "Username", "Name", "Surname", "Password", "Birthday", "Mail", "Phone Number");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {

                LocalDate fechaCustomer = LocalDate.parse(rs.getString("Birthday"));

                if (fechaCustomer.isAfter(fechaLimite)) {
                    Customer underCustomer = new Customer(
                            rs.getString("Username"), rs.getString("Name"), rs.getString("Surname"), rs.getString("Password"),
                            LocalDate.parse(rs.getString("Birthday")), rs.getString("Mail"), rs.getInt("Phone_Number"));

                    underageCustomers.add(underCustomer);

                    System.out.printf("%-10s %10s %15s %15s %15s %-20s %20d \n",
                            rs.getString("Username"),
                            rs.getString("Name"),
                            rs.getString("Surname"),
                            rs.getString("Password"),
                            rs.getString("Birthday"),
                            rs.getString("Mail"),
                            rs.getInt("Phone_Number"));
                }
            }
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            System.out.println(underageCustomers.toString());
            System.out.println("");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return underageCustomers;
    }

    public static ArrayList<Purchase> purchasesOfDesiredCustomer(String desiredCustomer) {

        String sql = "SELECT * FROM purchase WHERE cust_Username = ?";  //añadir: limit X
        ArrayList<Purchase> comprasClienteDeseado = new ArrayList<>();

        ResultSet rs = null;

        try (Connection conn = connect2();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, desiredCustomer);
            rs = pstmt.executeQuery();

            System.out.println("\nPurchases of the desired Customer: ");
            System.out.println("====================================================================================================================");
            System.out.printf("%-10s %10s %20s %25s %20s %25s\n", "Purchase ID", "Username", "Product", "Date", "Amount", "Total");
            System.out.println("--------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                //Purchase cadaCompra = new Purchase(rs.getString("cust_Username"), rs.getString("prod_ID"), rs.getDate("Date").toLocalDate(), rs.getInt("Amount"), rs.getDouble("Final_Cost"));
                Purchase cadaCompra = new Purchase(rs.getString("cust_Username"), rs.getString("prod_ID"),
                        rs.getDate("Date").toLocalDate(), rs.getInt("Amount"), rs.getDouble("Final_Cost"));

                comprasClienteDeseado.add(cadaCompra);

                System.out.printf("%-10d %10s %20s %25s %20d %25.2f \n",
                        rs.getInt("id_Purchase"),
                        rs.getString("cust_Username"),
                        rs.getString("prod_ID"),
                        rs.getDate("Date").toLocalDate(),
                        rs.getInt("Amount"),
                        rs.getDouble("Final_Cost"));
            }
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            System.out.println(comprasClienteDeseado.toString());

            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return comprasClienteDeseado;
    }

    public static ArrayList<SaleTrack> /*void*/ mostSoldProducts() {
        String sql = "SELECT Distinct(prod_ID) as Catalogo, "
                + "count(prod_ID) as Recuento, "
                + "SUM(Amount) as Vendidos,  "
                + "SUM(purchase.amount * product.Price) as Total FROM purchase INNER JOIN product "
                + "ON (purchase.prod_ID = product.id_Product) group by prod_ID order by Total desc"; //añadir limit X

        ArrayList<SaleTrack> bestSelledProducts = new ArrayList<>();

        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {
            System.out.printf("%-10s %30s %30s %30s \n", "Product Name", "Appearance on purchases", "Sold units", "Earned with the product");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            while (rs.next()) {

                SaleTrack prodHistorial = new SaleTrack(rs.getString("Catalogo"), rs.getInt("Recuento"), rs.getInt("Vendidos"), rs.getDouble("Total"));

                bestSelledProducts.add(prodHistorial);

                //compradosProd.add(rs.getString("Catalogo"));    
                System.out.printf("%12s %30d %30d %30.2f \n",
                        rs.getString("Catalogo"),
                        rs.getInt("Recuento"),
                        rs.getInt("Vendidos"),
                        rs.getDouble("Total")
                );
            }
            System.out.println(bestSelledProducts.toString());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return bestSelledProducts;
    }
    
    //-------------------------//
    //seguir con los métodos para guardar contenido en ficheros
    
    public static String /*static ArrayList<String>*/ registrosArrayList() {
        ArrayList<String> regTerminoak = new ArrayList<>();
        String taula = "Terminoak";
        String sql = "SELECT * FROM " + taula;

        String guardarFile = "../StreamekinLanean/HiztegiaFromDB.txt";

        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                PrintWriter out = new PrintWriter(guardarFile)) {

            String cadaRegistro = "";
            while (rs.next()) {
                cadaRegistro = "id: " + rs.getInt("id") + ", Euskaraz: " + rs.getString("euskaraz") + ", Gazteleraz: " + rs.getString("gazteleraz");
                regTerminoak.add(cadaRegistro);
                out.println(cadaRegistro);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return regTerminoak.toString();

    }
    
    
    public static ArrayList<Customer> getAllCustomers() {
        
        ArrayList<Customer> allRegisteredCustomers = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        
        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                ){
            while (rs.next()) {
                Customer everyCustomer = new Customer(
                            rs.getString("Username"), rs.getString("Name"), rs.getString("Surname"), rs.getString("Password"),
                            LocalDate.parse(rs.getString("Birthday")), rs.getString("Mail"), rs.getInt("Phone_Number"));
                
                allRegisteredCustomers.add(everyCustomer);
            }
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return allRegisteredCustomers;

    }
    
    
    public static void saveCustomersToFile(String contentAllCustomers){
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        
        try {
            outputStream = new PrintWriter(new FileWriter("../pGarageDIY/CustomerHistory.txt"));
            outputStream.print(contentAllCustomers);
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }    
    
    
    
    public static ArrayList<Product> getAllProducts() {
        
        ArrayList<Product> allProductCatalog = new ArrayList<>();
        String sql = "SELECT * FROM product";
        
        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                ){
            while (rs.next()) {
                Product everyProduct = new Product(
                            rs.getString("id_Product"), rs.getString("Name"), rs.getDouble("Price"), rs.getString("Description"));
                
                allProductCatalog.add(everyProduct);
            }
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return allProductCatalog;
    }
    
    
    
    public static void saveCatalogToFile(String contentAllProducts){
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        
        try {
            outputStream = new PrintWriter(new FileWriter("../pGarageDIY/ProductCatalog.txt"));
            outputStream.print(contentAllProducts);
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    } 
    
    
    public static ArrayList<Worker> getAllWorkers() {
        
        ArrayList<Worker> allRegisteredWorkers = new ArrayList<>();
        String sql = "SELECT * FROM worker";
        
        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                ){

            while (rs.next()) {
                Worker everyWorker = new Worker(
                            rs.getInt("Worker_ID"), rs.getString("Name"), rs.getString("Surname"), rs.getString("Password"), rs.getString("Occupation"), rs.getString("Mail"),
                            rs.getInt("Phone_Number"), rs.getDouble("Salary"), rs.getTime("Start_time").toLocalTime(), rs.getTime("Finish_time").toLocalTime());
                
                allRegisteredWorkers.add(everyWorker);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return allRegisteredWorkers;

    }
    
    
    public static void saveStaffToFile(String contentAllWorkers){
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        
        try {
            outputStream = new PrintWriter(new FileWriter("../pGarageDIY/GarageStaff.txt"));
            outputStream.print(contentAllWorkers);
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
    
    
    public static ArrayList<Cabin> getAllCabins() {
        
        ArrayList<Cabin> allCabins = new ArrayList<>();
        String sql = "SELECT * FROM cabin";
        
        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                ){

            while (rs.next()) {
                
                Cabin everyCabin = new Cabin(WorkArea.valueOf(rs.getString("Cabin_ID")), Model.getRandomWorker(Model.getAllWorkers()), rs.getDouble("Size"), rs.getString("Color"), rs.getDouble("Price_Hour"), rs.getString("Description"));
                
                allCabins.add(everyCabin);
                
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return allCabins;
    }
    
    
    public static void saveCabinsToFile(String contentAllCabins){
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        
        try {
            outputStream = new PrintWriter(new FileWriter("../pGarageDIY/CabinStructure.txt"));
            outputStream.print(contentAllCabins);
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
    
    
    public static ArrayList<String> rushHourCabin() {
        /*String sql = "SELECT Distinct(id_cabin) as Cabin, "
                + "SCOUNT(*) as cantMorning FROM reservation where Ending_Hour <= '13:00:00' group by id_cabin"; //añadir limit X*/
        
        /*String sql = "SELECT Distinct(id_cabin) as Cabin, "
                + "COUNT(*) as cantMorning FROM reservation where Ending_Hour >= '15:00:00' group by id_cabin"; //añadir limit X*/
        /* DONE
        String sql = "select count(*) as Cabin,"
                + "count(case when Ending_Hour <= '13:00:00' then 1 else null end) as cantMorning "
                + "FROM reservation;";
        */
        String sql = "select DISTINCT(id_cabin) as Cabin,"
                + "count(case when Ending_Hour <= '13:00:00' then 1 else null end) as cantMorning,"
                + "count(case when Starting_Hour >= '15:00:00' then 1 else null end) as cantAfternoon "
                + "FROM reservation group by id_cabin;";

        ArrayList<String> morningReservations = new ArrayList<>();

        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {
            
            String rentMorning = "";
            while (rs.next()) {
                //"--------------------------------------------------" + rs.getString("Cabin") + "--------------------------------------------------"
                morningReservations.add("\n");
                rentMorning = "   |----------------------------------------------  " + rs.getString("Cabin") + "  -----------------------------------------------| " +  //3 spaces
                        "\n\t\t Morning: " + rs.getInt("cantMorning") + 
                        "\n\t\tAfternoon: " + rs.getInt("cantAfternoon") + "\n";
                
                morningReservations.add(rentMorning);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return morningReservations;
    }
    
    
    /**
     * Method created to asign a random customer to a cabin which is just created. (the 
     * cabin needs at least one worker; so this method allows us to asign random worker.)
     * @param list
     * @return 
     */
    public static Worker getRandomWorker(ArrayList<Worker> listOfWorkers)
    {
        Random rand = new Random();
        return listOfWorkers.get(rand.nextInt(listOfWorkers.size()));
    }
    
    
    /**
     * Option 1.1 - Select the best 2 customer (in terms of most money paid in reservations)
     * Option 1.2 - Select the 3 best reservations (depending on the generated money)
     * Option 2 - Select the 3 biggest Total Prices of the reservation's table
     */
    public static ArrayList<String> biggestTotalPricesReservations() {  
        
        ArrayList<String> bestTwoCustomers = new ArrayList<>();    //option 1.1
        String sql = "SELECT cust_Username, SUM(Amount_Hours) as ReservedHours, SUM(Total_Price) as eachPaid "
                + "FROM reservation group by cust_Username order by eachPaid desc limit 2;";
        
        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {
            
            String bestCust = "";
            String customerUsername = "";
            while (rs.next()) {
                //"--------------------------------------------------" + rs.getString("Cabin") + "--------------------------------------------------"
                bestTwoCustomers.add("\n");
                bestCust = 
                        //"\n\t\tUsername: " + rs.getString("cust_Username") + 
                        "\t\tUsername: " + rs.getString("cust_Username") + 
                        "\n\tBooking time: " + rs.getInt("ReservedHours") + " hours " +                          
                        "       Total paid: " + rs.getDouble("eachPaid") + " €" + 
                        "\n\n--------------------------------------------------xx---------------------------------------------------" + "\n\n";
                customerUsername = rs.getString("cust_Username");
                /*
                bestCust = 
                        "\n\t\t Username: " + rs.getString("cust_Username") + 
                        "\n\t\t Booking time: " + rs.getInt("ReservedHours") + " hours " +  
                        "\n\t\tTotal paid: " + rs.getDouble("eachPaid") + 
                        "\n--------------------------------------------------xx---------------------------------------------------" + "\n";
                */
                bestTwoCustomers.add(bestCust);         //add into the arraylist the basic data of those customers
                bestTwoCustomers.add(customerUsername); //add also, the 2 usernames of the best 2 customers
                
                System.out.println(bestCust);
                System.out.println(customerUsername);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bestTwoCustomers;
    }
    
    
    /**
     * Returning ArrayList<String> Method
     * 
     * This method returns an ArrayList made of Strings with the desired data. Returning a String ArrayList 
     * allows the user to change the GUI's textArea's content easier than using an ArrayList of other certain 
     * class. 
     * 
     * Later, we'll transform this method into one that returns an ArrayList of the class/object we are 
     * looking for (in this case, an object from the Reservation class).
     * 
     * @return 
     */
    public static ArrayList<String> oldestThreeCustomersBookingString() {
        
        //option 1 -> reservation_ID, username, date of last reservation of him/her
        //option 2 -> reservation.* (full data of the last reservation of the 3 customers)
        //option 3 -> custoemr.* (full data of the least 3 frequent customers)
        //option 4 -> username, name, surname, amount of days since the last reservation
        
        ArrayList<String> theNewests = new ArrayList<>();
        //String leastFrequentCustomer = "";
                
        String sql = "SELECT \n" +
"		reservation.id_Reservation as Reservation_Code, \n" +
"		MAX(reservation.Date) as Last_Reservation, \n" +
"		customer.Username, customer.Name, customer.Surname, \n" +
"               DATEDIFF(now(), MAX(reservation.Date)) as Days_Passed \n" +
"		FROM reservation, customer\n" +
"		WHERE reservation.cust_Username = customer.Username and \n" +
"		reservation.Date < cast((now()) as date) \n" +
"		GROUP BY cust_Username ORDER by Days_Passed desc limit 3;";
        
        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {
            
            String leastFrequentCustomer = "";
            String newestReservation = "";
            
            while (rs.next()) {
                /*
                bestCust = 
                        "\n\t\tUsername: " + rs.getString("cust_Username") + 
                         
                        "\n\tTotal paid: " + rs.getDouble("eachPaid") + 
                        "\tBooking time: " + rs.getInt("ReservedHours") + " hours " + 
                        "\n--------------------------------------------------xx---------------------------------------------------" + "\n";
                customerUsername = rs.getString("cust_Username");
                
                bestTwoCustomers.add(bestCust);         //add into the arraylist the basic data of those customers
                bestTwoCustomers.add(customerUsername); //add also, the 2 usernames of the best 2 customers
                */
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return theNewests;
    }
    
    /**
     * Returning ArrayList<LeastFrequentCustomer> Method
     * 
     * This method returns an ArrayList made of objects belonging to the Customer's 
     * subclass called LeastFrequentCustomer. 
     * 
     * This subclass will be quite similar to his father class. In adittion to the 
     * father class' main attributes, the LeastFrequentCustome class will have some 
     * other ones like:
     * 
     *  -> Other attributes: codeReservation, lastReservation, daysPassed
     * 
     * @return 
     */
    public static ArrayList<LeastFrequentCustomer> oldestThreeCustomersBooking() {
        
        //option 1 -> reservation_ID, username, date of last reservation of him/her
        //option 2 -> reservation.* (full data of the last reservation of the 3 customers)
        //option 3 -> custoemr.* (full data of the least 3 frequent customers)
        //option 4 -> username, name, surname, amount of days since the last reservation
        
        ArrayList<LeastFrequentCustomer> theNewests = new ArrayList<>();
                        
        String sql = "SELECT \n" +
"		reservation.id_Reservation as Reservation_Code, \n" +
"		MAX(reservation.Date) as Last_Reservation, \n" +
"		customer.Username as UsernameCust, customer.Name as NameCust, customer.Surname as SurnameCust, \n" +
"               DATEDIFF(now(), MAX(reservation.Date)) as Days_Passed \n" +
"		FROM reservation, customer\n" +
"		WHERE reservation.cust_Username = customer.Username and \n" +
"		reservation.Date < cast((now()) as date) \n" +
"		GROUP BY cust_Username ORDER by Days_Passed desc limit 3;";
        
        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {
            
            //String leastFrequentCustomer = "";
            //String newestReservation = "";
            
            
            
            while (rs.next()) {
                
                LeastFrequentCustomer infrequentlyCustomer = new LeastFrequentCustomer(
                        rs.getInt("Reservation_Code"), LocalDate.parse(rs.getString("Last_Reservation")),
                        rs.getString("UsernameCust"), rs.getString("NameCust"), rs.getString("SurnameCust"),
                        rs.getInt("Days_Passed")
                        );
                
                theNewests.add(infrequentlyCustomer);
                
            }
            System.out.println(theNewests.toString());
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return theNewests;
    }
    
    
    public static void clearGraphicFrame() {
        View.JFrameGraphicalReports.repaint();
        View.JLabelReportANumReservations.setVisible(false);
        View.JLabelReportATotalPaid.setVisible(false); 
        View.JLabelReportABookingTime.setVisible(false);

        
        View.ButtonGroupGraphReports.clearSelection();
    }
    
    public static void drawBest2original() {
             
            Graphics g0 = View.JFrameGraphicalReports.getGraphics();
            g0.drawLine(100, 825, 100, View.JFrameGraphicalReports.getHeight() / 2);
            g0.drawLine(100, 775, 825, 775);    //g0.drawLine(100, 825, 825, 825);
            
            g0.drawString("Num. Reservations", 150, 500);
            g0.drawString("Total Paid", 350, 500);
            g0.drawString("Booking time", 500, 500);    //g0.drawString("Booking time", 550, 500);
            g0.drawString("Top Reservation", 650, 500); //g0.drawString("Top Reservation", 700, 500);
            
            g0.drawString("First cust.: ", 125, 800);   //g0.drawString("First cust.: " + Model.biggestTotalPricesReservations().get(2), 125, 800);
            g0.setColor(Color.blue);
            g0.drawString(Model.biggestTotalPricesReservations().get(2), 190, 800);
            g0.setColor(Color.black);
            
            g0.drawString("Second cust.: ", 300, 800);
            g0.setColor(Color.red);
            g0.drawString(Model.biggestTotalPricesReservations().get(5), 380, 800);
            g0.setColor(Color.black);
            
            //plantear hacer el gráfico al revés
            //comparation 1 - number of reservations
            //comparation 2 - total paid
            //comparation 3 - booking time
            //comparation 4 - top/best reservation of each customer
            
            g0.setColor(Color.red);
            g0.fillRect(175, 625, 20, 90);  //(x, y, anchura, precio)
            
            g0.setColor(Color.blue);
            g0.fillRect(210, 625, 20, 142);
            
            System.out.println("First graphic report: Best two customers (reservations) ");

            for (int i = 0; i < Model.biggestTotalPricesReservations().size(); ++i) {
                System.out.println("");
                if (i == 0 || i == 1 || i == 4) {
                    View.JTextAreaGraphics.setText(View.JTextAreaGraphics.getText() + Model.biggestTotalPricesReservations().get(i));  
                }
                //View.JTextAreaGraphics.setText(View.JTextAreaGraphics.getText() + Model.biggestTotalPricesReservations().get(i));
            }
            View.JLabelReportANumReservations.setVisible(true);
            View.JLabelReportATotalPaid.setVisible(true); 
            View.JLabelReportABookingTime.setVisible(true);
            
    }
    
    
    public static ArrayList<Customer> adultCustomers() {
        String sql = "SELECT * FROM customer ORDER BY customer.Birthday desc";  //añadir: limit X
        ArrayList<Customer> adultCustomers = new ArrayList<>();

        try (Connection conn = connect2();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            LocalDate fechaActual = LocalDate.now();
            System.out.println("Fecha actual: " + fechaActual);

            LocalDate fechaLimite = fechaActual.minusYears(18);
            System.out.println("Para ser mayor de edad: " + fechaLimite);

            System.out.println("\nUnderage customers: ");
            System.out.println("==========================================================================================================================");
            System.out.printf("%-10s %10s %15s %15s %15s %-20s %20s \n", "Username", "Name", "Surname", "Password", "Birthday", "Mail", "Phone Number");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {

                LocalDate fechaCustomer = LocalDate.parse(rs.getString("Birthday"));

                if (!fechaCustomer.isAfter(fechaLimite)) {
                    Customer underCustomer = new Customer(
                            rs.getString("Username"), rs.getString("Name"), rs.getString("Surname"), rs.getString("Password"),
                            LocalDate.parse(rs.getString("Birthday")), rs.getString("Mail"), rs.getInt("Phone_Number"));

                    adultCustomers.add(underCustomer);

                    System.out.printf("%-10s %10s %15s %15s %15s %-20s %20d \n",
                            rs.getString("Username"),
                            rs.getString("Name"),
                            rs.getString("Surname"),
                            rs.getString("Password"),
                            rs.getString("Birthday"),
                            rs.getString("Mail"),
                            rs.getInt("Phone_Number"));
                }
            }
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
            System.out.println(adultCustomers.toString());
            System.out.println("");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return adultCustomers;
    }
    
    
    public static void drawSortByAge() {
        
        
        Graphics g1 = View.JFrameGraphicalReports.getGraphics();
        //g1.drawLine(300, 700, 100, 700);

        int amountUnderage = Model.underAgeCustomers().size();
        int amountAdults = Model.adultCustomers().size();

        int totalCustomers = amountUnderage + amountAdults;

        System.out.println(amountUnderage);
        System.out.println(amountAdults);
        System.out.println("Total customers: " + totalCustomers);
        //System.out.println("The biggest group is: " + theBiggestGroup);

        System.out.println("----");            


        int portionsUnderage = (360 * amountUnderage) / totalCustomers;  //int prueba1 = 360 * (amountUnderage / totalCustomers);
        System.out.println("afwf" + portionsUnderage);

        int portionsAdults = (360 * amountAdults) / totalCustomers;  //int prueba1 = 360 * (amountUnderage / totalCustomers);
        System.out.println("afwf" + portionsAdults);

        //g1.fillArc(350, 700, 100, 100, 0, 360 - (360 * (amountUnderage / totalCustomers)));

        g1.setColor(Color.orange);  g1.fillArc(350, 500, 100, 100, 90, portionsUnderage);   //g1.fillArc(350, 500, 150, 100, 90, portionsUnderage);

        //show amount of UNDERAGE customers
        g1.setColor(Color.black); g1.drawString("➜ Number of ", 500, 550);
        g1.setColor(Color.orange); g1.drawString("underage ", 580, 550);
        g1.setColor(Color.black); g1.drawString("customers: " + amountUnderage, 640, 550);

        //show amount of ADULTS customers
        g1.setColor(Color.black); g1.drawString("➜ Number of ", 500, 570);
        g1.setColor(Color.blue); g1.drawString("adult ", 580, 570);
        g1.setColor(Color.black); g1.drawString("customers: " + amountAdults, 615, 570);

        int startAngleAdults = 360 - (360 - (90 + portionsUnderage));
        int angleAdults = 90 + (360 - (90 + portionsUnderage));
        g1.setColor(Color.blue);
        g1.fillArc(350, 500, 100, 100, startAngleAdults, angleAdults);    //g1.fillArc(350, 500, 150, 100, 360 - (360 - 306), 90 + (360 - 306));
            
    }
    
    
}


/*
SELECT SUM(productos.precio * stock.cantidad) FROM productos
INNER JOIN stock
ON (productos.id_producto = stock.id_producto)


SELECT SUM(purchase.amount * product.Price) FROM purchase
INNER JOIN product
ON (purchase.prod_ID = product.id_Product)
*/

