package com.example.aaron.welcomeActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;

//corresponds with item_overview

public class itemOverviewActivity extends ActionBarActivity {

    private TextView nameBox;
    private TextView priorityBox;
    private TextView aisleBox;
    private TextView currentCostBox;
    private TextView maxCostBox;
    private TextView frequencyBox;
    private DatabaseAccessObject theDatabase;
    private AlertDialog.Builder builder;
    private Expense currentExpense;
    private Budget currentBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_overview);

        nameBox = (TextView) findViewById(R.id.nameBox);
        priorityBox = (TextView) findViewById(R.id.priorityBox);
        aisleBox = (TextView) findViewById(R.id.aisleBox);
        currentCostBox = (TextView) findViewById(R.id.currentCostBox);
        maxCostBox = (TextView) findViewById(R.id.maxCostBox);
        frequencyBox = (TextView) findViewById(R.id.frequencyBox);

        theDatabase = new DatabaseAccessObject(getApplicationContext());
        builder = new AlertDialog.Builder(this);

        //Get Budget and Expense from intent
        Intent receivedIntent = this.getIntent();
        currentExpense = (Expense) receivedIntent.getSerializableExtra("Expense");
        currentBudget = (Budget) receivedIntent.getSerializableExtra("Budget");

        //Set values of text views
        nameBox.setText(currentExpense.getName());
        String temp =Integer.toString(currentExpense.getPriority());
        priorityBox.setText(temp);
        //Handle the aisle
        String temp2 =Integer.toString(currentExpense.getAisle());
        if (currentExpense.getAisle() == 0){
            aisleBox.setText("None", TextView.BufferType.NORMAL);
        }
        else{
            aisleBox.setText(temp2, TextView.BufferType.NORMAL);
        }
        //This handles the current cost of the item.
        String temp3 = Float.toString(currentExpense.getCurrentExpense());
        float f = Float.parseFloat(temp3);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        temp3 = formatter.format(f);
        currentCostBox.setText(temp3, TextView.BufferType.NORMAL);
        //Now we handle maximum amount allowed by the expense
        String temp4 = Float.toString(currentExpense.getMaxExpense());
        f = Float.parseFloat(temp4);
        temp4 = formatter.format(f);
        maxCostBox.setText(temp4, TextView.BufferType.NORMAL);
        //And finally the payment interval
        frequencyBox.setText(currentExpense.getPaymentInterval(), TextView.BufferType.NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //called when done is pressed; returns to the budgetOverviewActivity
    public void onDoneClick(View view)
    {
        //Finishes activity and returns to last screen
        this.finish();
    }

    //called when edit button is pressed; sends user to edit item screen
    public void onEditItemClick(View view){
        //make new intent to send user to editItemActivity
        Intent intent = new Intent(this, editItemActivity.class);
        //set up info to be transferred
        Expense transferExpense = currentExpense;
        Budget transferBudget = currentBudget;
        //add Budget and Expense to intent
        intent.putExtra("Expense",transferExpense);
        intent.putExtra("Budget", transferBudget);
        //send intent to start new activity
        startActivity(intent);
    }

    //called when delete is touched
    public void onDeleteClick(View view){
        //make a dialog box asking if the user is sure they wish to delete the item
        builder.setMessage("Are you sure you want to delete this item?");
        //if they click no, do nothing
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                //Do nothing
            }
        });
        //if they click yes, call deleteExpense to delete the Expense (gasp)
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                deleteExpense();
            }
        });
        builder.create();
        builder.show();

        return;
    }

    //used to delete an Expense.  called in onDeleteClick
    public void deleteExpense(){
        theDatabase.open();
        theDatabase.removeExpense(currentExpense.getIDNumber());
        theDatabase.closeDatabase();
        this.finish();
    };

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