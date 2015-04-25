package com.example.aaron.welcomeActivity;

import java.io.Serializable;

/**
 * Created by Jacob on 2/1/2015.
 */
public class Expense implements Serializable {
    private String name; //name of Expense
    private float currentValue = 0; //how much is currently being spent on the item
    private float maxValue = 100; //max amount of money that can be spent on item
    private long IDNumber = 0; //ID number that corresponds to the SQL database location
    private int priority = 0; //Priority that corresponds to the importance of the Expense
    private int aisle = 0; //What aisle the product is in at the store you frequent. Zero = no aisle
    private String paymentInterval = null;

    Expense(String newName, float newCurrent, float newMax){
        name = newName;
        currentValue = newCurrent;
        maxValue = newMax;
    }

    void setCurrentExpense(float newCE){
        currentValue = newCE;
    }

    void setMaxExpense(float newME){
        maxValue = newME;
    }

    void setName(String newName){
        name = newName;
    }

    void setPriority(int newPriority) {priority = newPriority; }

    void setIDNumber(long newID) {IDNumber = newID; }

    void setAisle(int newAisle){
        aisle = newAisle;
    }

    float getCurrentExpense(){
        return currentValue;
    }

    float getMaxExpense(){
        return maxValue;
    }

    String getName(){
        return name;
    }

    int getPriority() { return priority; }

    long getIDNumber() {return IDNumber; }

    int getAisle() {return aisle; }

    String getPaymentInterval()
    {
        return paymentInterval;
    }

    void setPaymentInterval(String input)
    {
        paymentInterval = input;
    }
    //This is needed to display the Expense as a String on a listView
    public String toString() {
        return name;
    }
}

