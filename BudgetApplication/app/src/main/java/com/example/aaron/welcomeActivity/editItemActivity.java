package com.example.aaron.welcomeActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

//this activity is used with edit_item_layout

public class editItemActivity extends ActionBarActivity{

    private String itemName;
    private float currentCost;
    private float itemLimit;
    private int priority;
    private int aisle;
    private EditText itemNameTextEdit;
    private TextView itemCategoryName;
    private EditText aisleTextEdit;
    private EditText itemCurrentCostTextEdit;
    private EditText itemMaxCostTextEdit;
    private Button editItemButton;
    budget currentBudget;
    private DatabaseAccess theDatabase;
    private Spinner dropdown;
    private String selectedSpinner;
    String[] values = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    //Constructor
    public editItemActivity(){
        itemName = "";
        currentCost = 0;
        itemLimit = 0;
        priority = 0;
        aisle = 0;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item_layout);
        itemNameTextEdit = (EditText) findViewById(R.id.nameEditText);
        itemCategoryName = (TextView) findViewById(R.id.categoryName);
        aisleTextEdit = (EditText) findViewById(R.id.aisleEditText);
        itemCurrentCostTextEdit = (EditText) findViewById(R.id.currentCostEditText);
        itemMaxCostTextEdit = (EditText) findViewById(R.id.maxCostEditText);
        editItemButton = (Button) findViewById(R.id.editItemButton);
        dropdown = (Spinner)findViewById(R.id.priorityDropDown);
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        dropdown.setAdapter(priorityAdapter);

        theDatabase = new DatabaseAccess(getApplicationContext());

        //Add intent stuff here
    }

    //Gets priority choice from dropdown menu
    private void findPriorityChoice(){
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //sets variable when selected value is changed in dropdown
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedSpinner = values[position];
                System.out.println("The dropdown is " + selectedSpinner + "!");
            }

            @Override
            //sets variable if user doesn't select one
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedSpinner = "1";
                System.out.println("The dropdown is one!");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
