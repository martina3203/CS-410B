package com.example.aaron.welcomeActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class editBudgetActivity extends ActionBarActivity {
    private String budgetName;
    private double budgetLimit;
    private Button editButton;
    private EditText budgetNameTextEdit;
    private EditText budgetTotalTextEdit;
    private DatabaseAccessObject theDatabase;
    private AlertDialog.Builder builder;
    private Budget currentBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_budget_layout);
        theDatabase = new DatabaseAccessObject(getApplicationContext());
        //Creates buttons
        editButton = (Button) findViewById(R.id.finishButton);
        budgetNameTextEdit = (EditText) findViewById(R.id.newBudgetNameTextEdit);
        budgetTotalTextEdit = (EditText) findViewById(R.id.totalBudgetTextEdit);

        //Need to get intents and stuff
        Intent receivedIntent = this.getIntent();
        currentBudget = (Budget) receivedIntent.getSerializableExtra("Budget");
        budgetNameTextEdit.setText(currentBudget.getName(), TextView.BufferType.EDITABLE);
        String maxCost = Double.toString(currentBudget.getMaxValue());
        budgetTotalTextEdit.setText(maxCost, TextView.BufferType.EDITABLE);
    }

    public void onFinishClick(View view)
    {
        //Get new Budget name
        budgetName = budgetNameTextEdit.getText().toString();
        if (budgetName.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a name into the Budget Name field.");
            builder.show();
            return;
        }

        //Get new Budget amount
        String budgetTotal = budgetTotalTextEdit.getText().toString();
        if (budgetTotal.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a value into the Total Budget Amount field.");
            builder.show();
            return;
        }

        //Now I have to check for duplicates in the database
        theDatabase.open();
        ArrayList<Budget> budgetList = theDatabase.findAllBudgets();
        for (int i = 0; i < budgetList.size(); i++)
        {
            Budget thisBudget = budgetList.get(i);
            if (budgetName.matches(thisBudget.getName()))
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

        //Update Budget object and add to database
        currentBudget.setName(budgetName);
        currentBudget.setMaxValue(budgetLimit);
        theDatabase.updateBudget(currentBudget);
        //Close database when done
        theDatabase.closeDatabase();
        //Finish Activity and return results
        Intent returnedIntent = this.getIntent();
        //Says it's ok and returns the information upon finish
        setResult(RESULT_OK,returnedIntent);
        this.finish();
    }
}
