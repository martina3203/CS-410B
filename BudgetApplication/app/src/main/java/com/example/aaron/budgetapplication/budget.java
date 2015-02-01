package com.example.aaron.budgetapplication;

/**
 * Created by Jacob on 2/1/2015.
 */
public class budget {
    private String name;
    private float currentValue = 0;
    private float maxValue = 100;

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

