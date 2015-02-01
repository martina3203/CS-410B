package com.example.aaron.budgetapplication;

/**
 * Created by Jacob on 2/1/2015.
 */
public class expense {
    private String name;
    private float currentExpense = 0;
    private float maxExpense = 100;

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

