package com.example.aaron.welcomeActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

//corresponds with item_overview

public class itemOverviewActivity extends ActionBarActivity {

    private TextView nameBox;
    private TextView priorityBox;
    private TextView aisleBox;
    private TextView currentCostBox;
    private TextView maxCostBox;
    private DatabaseAccess theDatabase;
    private AlertDialog.Builder builder;

    expense currentExpense;
    budget currentBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_overview);

        nameBox = (TextView) findViewById(R.id.nameBox);
        priorityBox = (TextView) findViewById(R.id.priorityBox);
        aisleBox = (TextView) findViewById(R.id.aisleBox);
        currentCostBox = (TextView) findViewById(R.id.currentCostBox);
        maxCostBox = (TextView) findViewById(R.id.maxCostBox);

        theDatabase = new DatabaseAccess(getApplicationContext());
        builder = new AlertDialog.Builder(this);

        //Get budget and expense from intent
        Intent receivedIntent = this.getIntent();
        currentExpense = (expense) receivedIntent.getSerializableExtra("Expense");
        currentBudget = (budget) receivedIntent.getSerializableExtra("Budget");

        //Set values of text views
        nameBox.setText(currentExpense.getName());
        String temp =Integer.toString(currentExpense.getPriority());
        priorityBox.setText(temp);
        String temp2 =Integer.toString(currentExpense.getAisle());
        if (currentExpense.getAisle() == 0){
            aisleBox.setText("None");
        }
        else{
            aisleBox.setText(temp2);
        }
        String temp3 = Float.toString(currentExpense.getCurrentExpense());
        float f = Float.parseFloat(temp3);
        temp3 = String.format("%.2f", f);
        temp3 = "$" + temp3;
        currentCostBox.setText(temp3);
        String temp4 = Float.toString(currentExpense.getMaxExpense());
        f = Float.parseFloat(temp4);
        temp4 = String.format("%.2f", f);
        temp4 = "$" + temp4;
        maxCostBox.setText(temp4);

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
        expense transferExpense = currentExpense;
        budget transferBudget = currentBudget;
        //add budget and expense to intent
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
        //if they click yes, call deleteExpense to delete the expense (gasp)
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                deleteExpense();
            }
        });
        builder.create();
        builder.show();

        return;
    }

    //used to delete an expense.  called in onDeleteClick
    public void deleteExpense(){
        theDatabase.open();
        theDatabase.removeExpense(currentExpense.getIDNumber(), currentBudget.getName());
        theDatabase.closeDatabase();
        this.finish();
    };

}