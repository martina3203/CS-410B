package com.example.aaron.welcomeActivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//this activity is used with new_item_layout

public class newItemActivity extends ActionBarActivity {

    private String newItemName;
    private float currentCost;
    private float itemLimit;
    private int priority;
    private int aisle;
    private EditText newItemNameTextEdit;
    private EditText newItemPriorityTextEdit;
    private TextView newItemCategoryName;
    private EditText newAisleTextEdit;
    private EditText newItemCurrentCostTextEdit;
    private EditText newItemMaxCostTextEdit;
    private Button addItemButton;
    budget currentBudget;
    private DatabaseAccess theDatabase;

    //Constructor
    public newItemActivity() {
        newItemName = "";
        currentCost = 0;
        itemLimit = 0;
        priority = 0;
        aisle = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item_layout);
        //Creates edit texts and buttons
        newItemNameTextEdit = (EditText) findViewById(R.id.newNameEditText);
        newItemPriorityTextEdit = (EditText) findViewById(R.id.newPriorityEditText);
        newItemCategoryName = (TextView) findViewById(R.id.newCategoryName);
        newAisleTextEdit = (EditText) findViewById(R.id.newAisleEditText);
        newItemCurrentCostTextEdit = (EditText) findViewById(R.id.newCurrentCostEditText);
        newItemMaxCostTextEdit = (EditText) findViewById(R.id.newMaxCostEditText);
        addItemButton = (Button) findViewById(R.id.addItemButton);
        theDatabase = new DatabaseAccess(getApplicationContext());

        Intent receivedIntent = this.getIntent();
        currentBudget = (budget) receivedIntent.getSerializableExtra("Budget");
        Log.v("Budget Loaded: ", currentBudget.getName());
        newItemCategoryName.setText(currentBudget.getName());


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

    //called when add item button is clicked
    public void onAddItemClick(View view) {
        //Displays message to tell user that they did not fill out a field
        //Otherwise, it will read the field and move to the next one
        //This repeats until all fields are filled
        newItemName = newItemNameTextEdit.getText().toString();
        if (newItemName.matches("")){
            Toast.makeText(this, "You did not enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        String itemPriority = newItemPriorityTextEdit.getText().toString();
        if (itemPriority.matches("")){
            Toast.makeText(this, "You did not enter a priority", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            priority = Integer.parseInt(itemPriority);
        }

        String itemAisle = newAisleTextEdit.getText().toString();
        if (itemAisle.matches("")){
            Toast.makeText(this, "You did not enter an aisle", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            aisle = Integer.parseInt(itemAisle);
        }

        String itemCurrentCost = newItemCurrentCostTextEdit.getText().toString();
        if (itemCurrentCost.matches("")){
            Toast.makeText(this, "You did not enter a current cost", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            currentCost = Float.parseFloat(itemCurrentCost);
        }

        String itemMaxCost = newItemMaxCostTextEdit.getText().toString();
        if (itemMaxCost.matches("")){
            Toast.makeText(this, "You did not enter total amount", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            itemLimit = Float.parseFloat(itemMaxCost);
        }

        //Create new expense object
        expense newExpense = new expense(newItemName, currentCost, itemLimit);
        newExpense.setPriority(priority);
        theDatabase.open();
        theDatabase.insertExpense(newExpense, currentBudget.getName());
        theDatabase.closeDatabase();

        //Finish Activity and return results
        Intent returnedIntent = this.getIntent();
        //Says it's ok and returns the information upon finish
        setResult(RESULT_OK, returnedIntent);
        this.finish();
    }
}

