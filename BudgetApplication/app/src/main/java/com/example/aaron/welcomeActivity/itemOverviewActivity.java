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
    private TextView currentCostBox;
    private TextView maxCostBox;

    expense currentExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("HERE DUDE3");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_overview);
        System.out.println("HERE DUDE4");

        nameBox = (TextView) findViewById(R.id.nameBox);
        priorityBox = (TextView) findViewById(R.id.priorityBox);
        currentCostBox = (TextView) findViewById(R.id.currentCostBox);
        maxCostBox = (TextView) findViewById(R.id.maxCostBox);
        System.out.println("HERE DUDE5");

        Intent receivedIntent = this.getIntent();
        System.out.println("HERE DUDE6");
        currentExpense = (expense) receivedIntent.getSerializableExtra("Expense");
        System.out.println("HERE DUDE7");
        nameBox.setText(currentExpense.getName());
        System.out.println("HERE DUDE8");
        String tempS =Integer.toString(currentExpense.getPriority());
        priorityBox.setText(tempS);
        System.out.println("HERE DUDE9");
        String temp = Float.toString(currentExpense.getCurrentExpense());
        System.out.println("HERE DUDE10");
        currentCostBox.setText(temp);
        System.out.println("HERE DUDE11");
        String temp2 = Float.toString(currentExpense.getMaxExpense());
        System.out.println("HERE DUDE12");
        maxCostBox.setText(temp2);
        System.out.println("HERE DUDE13");

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
}