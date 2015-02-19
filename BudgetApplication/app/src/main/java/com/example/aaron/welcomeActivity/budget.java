package com.example.aaron.welcomeActivity;

import java.io.Serializable;

/**
 * Created by Jacob on 2/1/2015.
 */
public class budget implements Serializable {
    private String name; //name of budget
    private float currentValue = 0; //how much of the budget is currently used
    private float maxValue = 100; //maximum amount of money that can be used

    budget(String newName, float newMax)
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

    float getCurrentValue(){
        return currentValue;
    }

    float getMaxValue(){
        return maxValue;
    }

    String getName(){
        return name;
    }
}

