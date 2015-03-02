package com.example.aaron.welcomeActivity;

/**
 * Created by Jacob on 2/1/2015.
 */
public class expense {
    private String name; //name of expense
    private float currentExpense = 0; //how much is currently being spent on the item
    private float maxExpense = 100; //max amount of money that can be spent on item
    private int IDNumber = 0; //ID number that corresponds to the SQL database location

    expense (String newName, float newCurrent, float newMax, int newIDNumber){
        name = newName;
        currentExpense = newCurrent;
        maxExpense = newMax;
        IDNumber = newIDNumber;
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

    float getCurrentExpense(){
        return currentExpense;
    }

    float getMaxExpense(){
        return maxExpense;
    }

    String getName(){
        return name;
    }
}

