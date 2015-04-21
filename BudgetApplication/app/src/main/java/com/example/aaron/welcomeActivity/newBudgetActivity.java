package com.example.aaron.welcomeActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import java.text.NumberFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

//This activity is used with new_budget_layout

public class newBudgetActivity extends ActionBarActivity {
    private String newBudgetName;
    private double budgetLimit;
    private Button addButton;
    private EditText newBudgetNameTextEdit;
    private EditText newBudgetTotalTextEdit;
    private DatabaseAccess theDatabase;
    private static final NumberFormat currencyFormat;
    private AlertDialog.Builder builder;

    static {
        currencyFormat = NumberFormat.getCurrencyInstance();
    }

    //Typical Constructor
    public newBudgetActivity(){
        newBudgetName = "";
        budgetLimit = 0.0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_budget_layout);
        theDatabase = new DatabaseAccess(getApplicationContext());
        //Creates buttons
        addButton = (Button) findViewById(R.id.addButton);
        newBudgetNameTextEdit = (EditText) findViewById(R.id.newBudgetNameTextEdit);
        newBudgetTotalTextEdit = (EditText) findViewById(R.id.totalBudgetTextEdit);

        //Handle Alert Dialogs by building the dialog
        builder = new AlertDialog.Builder(this);
    }

    //Executes when addButton is clicked
    public void onAddBudgetClick(View view)
    {
        //Get new Budget name
        newBudgetName = newBudgetNameTextEdit.getText().toString();
        if (newBudgetName.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a name into the Budget field.");
            builder.show();
            return;
        }

        //Get new Budget amount
        String budgetTotal = newBudgetTotalTextEdit.getText().toString();
        if (budgetTotal.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a value into the Total Budget field.");
            builder.show();
            return;
        }

        //Now I have to check for duplicates in the database
        theDatabase.open();
        ArrayList<Budget> budgetList = theDatabase.findAllBudgets();
        for (int i = 0; i < budgetList.size(); i++)
        {
            Budget currentBudget = budgetList.get(i);
            if (newBudgetName.matches(currentBudget.getName()))
            {
                //We have encountered a duplicate and will not proceed
                builder.setTitle("Error");
                builder.setMessage("A Budget of that name already exists.");
                builder.show();
                return;
            }
        }

        //Parse Double out of this statement
        budgetLimit = Double.parseDouble(budgetTotal);

        //Create new Budget object and add to database
        Budget tempBudget = new Budget(newBudgetName, budgetLimit);
        tempBudget.setIDNumber(theDatabase.insertBudget(tempBudget));
        //Create new Expense table
        theDatabase.addExpenseTable(newBudgetName);
        //Close database when done
        theDatabase.closeDatabase();
        //Finish Activity and return results
        Intent returnedIntent = this.getIntent();
        //Says it's ok and returns the information upon finish
        setResult(RESULT_OK,returnedIntent);
        this.finish();
    }
}
