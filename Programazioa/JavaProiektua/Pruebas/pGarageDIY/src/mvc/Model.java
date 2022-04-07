/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import myClasses.*;

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

    public String underAgeCustomers() {
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

    public static ArrayList<Customer> underAge() {
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

            System.out.println("Purchases of the desired Customer: ");
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
    
    
}


/*
SELECT SUM(productos.precio * stock.cantidad) FROM productos
INNER JOIN stock
ON (productos.id_producto = stock.id_producto)


SELECT SUM(purchase.amount * product.Price) FROM purchase
INNER JOIN product
ON (purchase.prod_ID = product.id_Product)
*/
