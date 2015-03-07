package com.example.aaron.welcomeActivity;

import java.io.Serializable;

/**
 * Created by Jacob on 2/1/2015.
 */
public class budget implements Serializable {
    private String name = ""; //name of budget
    private float currentValue = 0; //how much of the budget is currently used
    private double maxValue = 100; //maximum amount of money that can be used
    private long IDNumber = 0;

    budget(String newName, double newMax)
    {
        name = newName;
        maxValue = newMax;
    }

    budget (float newCurrent, float newMax){
        currentValue = newCurrent;
        maxValue = newMax;
    }

    void setCurrentValue(float newCV){
        currentValue = newCV;
    }

    void setMaxValue(float newMV){
        maxValue = newMV;
    }

    void setName(String newName){
        name = newName;
    }

    void setIDNumber(long newID) { IDNumber = newID; }

    float getCurrentValue(){
        return currentValue;
    }

    double getMaxValue(){
        return maxValue;
    }

    String getName(){
        return name;
    }

    long getIDNumber() {return IDNumber; }

    //This is needed to display the budget as a String on a listView
    public String toString() {
        return name;
    }
}

