package com.example.aaron.budgetapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aaron on 2/14/2015.
 *
 * This is the class for the fragment that handles the user making a new budget class.
 * This is called whenever the user presses the new budget button on the main activity
 */
public class newBudgetFragment extends Fragment {
    private String newBudgetName;
    private double budgetLimit;

    //Typical Constructor
    public newBudgetFragment(){
        newBudgetName = "";
        budgetLimit = 0.0;
    }

    //Required to create the interface for the fragment
    //The XML file used to the interface must be listed here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.new_budget_fragment_layout, container, false);
    }
}
