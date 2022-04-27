/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myClasses;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author AdriAlex
 */
public class MonthOccupation {
    
    private String month;
    private int amountReservationMonth;
    private double earnedPerMonth;
    
    
    public MonthOccupation(String month, int amountReservationMonth, double earnedPerMonth) {
        
        this.month = month;
        this.amountReservationMonth = amountReservationMonth;
        this.earnedPerMonth = earnedPerMonth;    
    }
    
    
    public String getMonth() {
        return month;
    }
    
    public int getAmountOfReservationsMonth() {
        return amountReservationMonth;
    }
    
    public double getTotalEarnedPerMonth() {
        return earnedPerMonth;
    }
    
    
    public String toString() {
        
        return "\nMonth: " + this.getMonth() + 
                "\nOccupation: " + this.getAmountOfReservationsMonth() + 
                "\nTotal Earned: " + this.getTotalEarnedPerMonth() + 
                "\n -------------------------------------------------- xx ---------------------------------------------------";
    }
    
    
    public String toStringExtended() {
        return "";
    }
    
}