package com.example.aaron.welcomeActivity;

import java.io.Serializable;

/**
 * Created by Jacob on 2/1/2015.
 */
public class expense implements Serializable {
    private String name; //name of expense
    private float currentExpense = 0; //how much is currently being spent on the item
    private float maxExpense = 100; //max amount of money that can be spent on item
    private long IDNumber = 0; //ID number that corresponds to the SQL database location
    private int priority = 0; //Priority that corresponds to the importance of the expense
    private int aisle = 0; //What aisle the product is in at the store you frequent. Zero = no aisle

    expense (String newName, float newCurrent, float newMax){
        name = newName;
        currentExpense = newCurrent;
        maxExpense = newMax;
    }

    void setCurrentExpense(float newCE){
        currentExpense = newCE;
    }

    void setMaxExpense(float newME){
        maxExpense = newME;
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
        return currentExpense;
    }

    float getMaxExpense(){
        return maxExpense;
    }

    String getName(){
        return name;
    }

    int getPriority() { return priority; }

    long getIDNumber() {return IDNumber; }

    int getAisle() {return aisle; }

    //This is needed to display the expense as a String on a listView
    public String toString() {
        return name;
    }
}

