package com.example.aaron.welcomeActivity;

/**
 * Created by Jacob on 2/1/2015.
 */
public class expense {
    private String name; //name of expense
    private float currentExpense = 0; //how much is currently being spent on the item
    private float maxExpense = 100; //max amount of money that can be spent on item

    expense (float newCurrent, float newMax){
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

