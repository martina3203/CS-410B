package com.example.aaron.welcomeActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

//This activity is used with summary_layout

public class summaryActivity extends ActionBarActivity {

    private DatabaseAccessObject theDatabase;
    private ListView expenseListView;
    private TextView priceTextView;
    private String frequency;
    private Spinner frequencyDropdown;
    private String[] frequencyValues = new String[]{"None", "Daily", "Five Days", "One Week", "Two Weeks",
            "One Month", "Quarterly", "Six Months", "Annually"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_layout);
        expenseListView = (ListView) findViewById(R.id.expenseListView);
        priceTextView = (TextView) findViewById(R.id.totalCostAmountTextView);
        frequencyDropdown = (Spinner) findViewById(R.id.frequencyDropDown);
        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, frequencyValues);
        frequencyDropdown.setAdapter(frequencyAdapter);

        theDatabase = new DatabaseAccessObject(getApplicationContext());
    }

    private void findFrequencyChoice(){
        frequencyDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //sets variable when selected value is changed in dropdown
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                frequency = frequencyValues[position];
            }

            @Override
            //sets variable if user doesn't select one
            public void onNothingSelected(AdapterView<?> parentView) {
                frequency = "None";
            }
        });
    }
}
