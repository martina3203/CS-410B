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
    private EditText newItemNameTextEdit;
    private EditText newItemPriorityTextEdit;
    private TextView newItemCategoryName;
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item_layout);
        //Creates edit texts and buttons
        newItemNameTextEdit = (EditText) findViewById(R.id.newNameEditText);
        newItemPriorityTextEdit = (EditText) findViewById(R.id.newPriorityEditText);
        newItemCategoryName = (TextView) findViewById(R.id.newCategoryName);
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
        Log.v("Here1 ", currentBudget.getName());
        newItemName = newItemNameTextEdit.getText().toString();
        String temp = newItemCurrentCostTextEdit.getText().toString();
        currentCost = Float.parseFloat(temp);
        String temp2 = newItemMaxCostTextEdit.getText().toString();
        itemLimit = Float.parseFloat(temp2);
        String temp3 = newItemPriorityTextEdit.getText().toString();
        priority = Integer.parseInt(temp3);
        Log.v("Here2 ", currentBudget.getName());

        /*if (newItemName != "" || currentCost != 0 || itemLimit != 0 || priority != 0) {
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
        else {
            Toast.makeText(newItemActivity.this, "You need to fill all fields!", Toast.LENGTH_SHORT).show();
        }*/
        //Create new expense object
        expense newExpense = new expense(newItemName, currentCost, itemLimit);
        newExpense.setPriority(priority);
        theDatabase.open();
        theDatabase.insertExpense(newExpense, currentBudget.getName());
        theDatabase.closeDatabase();
        Log.v("Here3 ", currentBudget.getName());


        //Finish Activity and return results
        Intent returnedIntent = this.getIntent();
        //Says it's ok and returns the information upon finish
        setResult(RESULT_OK, returnedIntent);
        this.finish();
    }
}

