package com.example.aaron.welcomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    expense currentExpense;
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

        //Get intent from item overview
        Intent receivedIntent = this.getIntent();
        currentExpense = (expense) receivedIntent.getSerializableExtra("Expense");
        currentBudget = (budget) receivedIntent.getSerializableExtra("Budget");
        System.out.println("Budget name is " + currentBudget.getName());

        //Set the values of the edit texts
        itemNameTextEdit.setText(currentExpense.getName(), TextView.BufferType.EDITABLE);
        itemCategoryName.setText(currentBudget.getName());
        String aisleNum = Integer.toString(currentExpense.getAisle());
        aisleTextEdit.setText(aisleNum, TextView.BufferType.EDITABLE);
        String curCost = Float.toString(currentExpense.getCurrentExpense());
        itemCurrentCostTextEdit.setText(curCost, TextView.BufferType.EDITABLE);
        String maxCost = Double.toString(currentExpense.getMaxExpense());
        itemMaxCostTextEdit.setText(maxCost, TextView.BufferType.EDITABLE);

        findPriorityChoice();
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
                selectedSpinner = Integer.toString(currentExpense.getAisle());
                System.out.println("The dropdown is default!");
            }
        });
    }

    public void onFinishClick(View view){
        itemName = itemNameTextEdit.getText().toString();
        if (itemName.matches("")){
            Toast.makeText(this, "You did not enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        String itemAisle = aisleTextEdit.getText().toString();
        if (itemAisle.matches("")){
            Toast.makeText(this, "You did not enter an aisle", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            aisle = Integer.parseInt(itemAisle);
        }

        String itemCurrentCost = itemCurrentCostTextEdit.getText().toString();
        if (itemCurrentCost.matches("")){
            Toast.makeText(this, "You did not enter a current cost", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            currentCost = Float.parseFloat(itemCurrentCost);
        }

        String itemMaxCost = itemMaxCostTextEdit.getText().toString();
        if (itemMaxCost.matches("")){
            Toast.makeText(this, "You did not enter a max cost", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            itemLimit = Float.parseFloat(itemMaxCost);
        }

        //Gets Priority from dropdown
        priority = Integer.parseInt(selectedSpinner);

        System.out.println("Making new expense");

        //Create new expense object
        expense newExpense = new expense(itemName, currentCost, itemLimit);
        newExpense.setPriority(priority);
        newExpense.setAisle(aisle);
        theDatabase.open();
        System.out.println("Updating expense");
        theDatabase.updateExpense(newExpense, currentBudget.getName());
        theDatabase.closeDatabase();

        System.out.println("Starting intent");

        //Finish Activity and return results
        Intent returnedIntent = this.getIntent();
        //Says it's ok and returns the information upon finish
        setResult(RESULT_OK, returnedIntent);
        this.finish();
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
