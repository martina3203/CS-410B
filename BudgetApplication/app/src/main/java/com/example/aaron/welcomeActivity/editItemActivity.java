package com.example.aaron.welcomeActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
    private Double currentCost;
    private Double itemLimit;
    private int priority;
    private String frequency;
    private int aisle;
    private EditText itemNameTextEdit;
    private TextView itemCategoryName;
    private EditText aisleTextEdit;
    private EditText itemCurrentCostTextEdit;
    private EditText itemMaxCostTextEdit;
    private Button editItemButton;
    Budget currentBudget;
    Expense currentExpense;
    private DatabaseAccessObject theDatabase;
    private Spinner dropdown;
    private Spinner frequencyDropdown;
    private String selectedSpinner;

    private AlertDialog.Builder builder;
    String[] values = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    String[] frequencyValues = new String[]{"None", "Daily", "Five Days", "One Week", "Two Weeks",
            "One Month", "Quarterly", "Six Months", "Annually"};

    //Constructor
    public editItemActivity(){
        itemName = "";
        currentCost = 0.0;
        itemLimit = 0.0;
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
        frequencyDropdown = (Spinner)findViewById(R.id.frequencyDropDown);
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, frequencyValues);
        dropdown.setAdapter(priorityAdapter);
        frequencyDropdown.setAdapter(frequencyAdapter);

        theDatabase = new DatabaseAccessObject(getApplicationContext());
        builder = new AlertDialog.Builder(this);

        //Get intent from item overview
        Intent receivedIntent = this.getIntent();
        currentExpense = (Expense) receivedIntent.getSerializableExtra("Expense");
        currentBudget = (Budget) receivedIntent.getSerializableExtra("Budget");

        //Set the values of the edit texts
        itemNameTextEdit.setText(currentExpense.getName(), TextView.BufferType.EDITABLE);
        itemCategoryName.setText(currentBudget.getName());
        String aisleNum = Integer.toString(currentExpense.getAisle());
        aisleTextEdit.setText(aisleNum, TextView.BufferType.EDITABLE);
        String curCost = Double.toString(currentExpense.getCurrentExpense());
        itemCurrentCostTextEdit.setText(curCost, TextView.BufferType.EDITABLE);
        String maxCost = Double.toString(currentExpense.getMaxExpense());
        itemMaxCostTextEdit.setText(maxCost, TextView.BufferType.EDITABLE);

        findPriorityChoice();
        findFrequencyChoice();
    }

    //Gets priority choice from dropdown menu
    private void findPriorityChoice(){
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //sets variable when selected value is changed in dropdown
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedSpinner = values[position];
            }

            @Override
            //sets variable if user doesn't select one
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedSpinner = Integer.toString(currentExpense.getAisle());
            }
        });
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

    public void onFinishClick(View view){
        itemName = itemNameTextEdit.getText().toString();
        if (itemName.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a name!");
            builder.show();
            return;
        }

        String itemAisle = aisleTextEdit.getText().toString();
        if (itemAisle.matches("")){
           aisle = 0;
        }
        else{
            aisle = Integer.parseInt(itemAisle);
        }

        String itemCurrentCost = itemCurrentCostTextEdit.getText().toString();
        if (itemCurrentCost.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a current cost!");
            builder.show();
            return;
        }
        else {
            currentCost = Double.parseDouble(itemCurrentCost);
        }

        String itemMaxCost = itemMaxCostTextEdit.getText().toString();
        if (itemMaxCost.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a max cost!");
            builder.show();
            return;
        }
        else {
            itemLimit = Double.parseDouble(itemMaxCost);
        }

        //Gets Priority from dropdown
        priority = Integer.parseInt(selectedSpinner);
        //Create new expense object
        currentExpense.setName(itemName);
        currentExpense.setCurrentExpense(currentCost);
        currentExpense.setMaxExpense(itemLimit);
        currentExpense.setAisle(aisle);
        currentExpense.setPriority(priority);
        currentExpense.setPaymentInterval(frequency);
        theDatabase.open();
        theDatabase.updateExpense(currentExpense);
        theDatabase.closeDatabase();

        //Finish Activity and go to select Budget screen
        Intent newIntent = new Intent(this, budgetOverviewActivity.class);
        newIntent.putExtra("Budget",currentBudget);
        newIntent.putExtra("Expense",currentExpense);

        startActivity(newIntent);
    }


    //The following functions override the two backbuttons that exist on Android
    @Override
    public void onBackPressed()
    {
        //Finish Activity and go to select Budget screen again
        Intent newIntent = new Intent(this, budgetOverviewActivity.class);
        newIntent.putExtra("Budget",currentBudget);
        newIntent.putExtra("Expense",currentExpense);
        startActivity(newIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
