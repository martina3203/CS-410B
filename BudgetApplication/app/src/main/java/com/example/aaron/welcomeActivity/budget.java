package com.example.aaron.welcomeActivity;

import java.io.Serializable;

/**
 * Created by Jacob on 2/1/2015.
 */
public class Budget implements Serializable {
    private String name = ""; //name of Budget
    private double maxValue = 100; //maximum amount of money that can be used
    private long IDNumber = 0;
    private String paymentInterval = null;

    Budget(String newName, double newMax)
    {
        name = newName;
        maxValue = newMax;
    }

    void setName(String newName){
        name = newName;
    }

    void setIDNumber(long newID) { IDNumber = newID; }

    double getMaxValue(){
        return maxValue;
    }

    String getName(){
        return name;
    }

    long getIDNumber() {return IDNumber; }

    String getPaymentInterval()
    {
        return paymentInterval;
    }

    void setPaymentInterval(String input)
    {
        paymentInterval = input;
    }

    //This is needed to display the Budget as a String on a listView
    public String toString() {
        return name;
    }
}

