package com.example.aaron.welcomeActivity;

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

        //Set Text Edits to have values of expense
        Intent receivedIntent = this.getIntent();
        currentExpense = (expense) receivedIntent.getSerializableExtra("Expense");
        currentBudget = (budget) receivedIntent.getSerializableExtra("Budget");
        System.out.println("Budget name is " + currentBudget.getName());
        nameBox.setText(currentExpense.getName());
        String temp =Integer.toString(currentExpense.getPriority());
        priorityBox.setText(temp);
        String temp2 =Integer.toString(currentExpense.getAisle());
        aisleBox.setText(temp2);
        String temp3 = Float.toString(currentExpense.getCurrentExpense());
        currentCostBox.setText(temp3);
        String temp4 = Float.toString(currentExpense.getMaxExpense());
        maxCostBox.setText(temp4);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onDoneClick(View view)
    {
        //Finish Activity and return results
        //Intent returnedIntent = this.getIntent();
        //Says it's ok and returns the information upon finish
        //setResult(RESULT_OK, returnedIntent);
        this.finish();
    }

    public void onEditItemClick(View view){
        Intent intent = new Intent(this, editItemActivity.class);
        expense transferExpense = currentExpense;
        budget transferBudget = currentBudget;
        intent.putExtra("Expense",transferExpense);
        intent.putExtra("Budget", transferBudget);
        startActivity(intent);
    }
}