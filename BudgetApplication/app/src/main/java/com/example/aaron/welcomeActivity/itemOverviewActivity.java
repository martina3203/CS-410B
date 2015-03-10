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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_overview);

        nameBox = (TextView) findViewById(R.id.nameBox);
        priorityBox = (TextView) findViewById(R.id.priorityBox);
        currentCostBox = (TextView) findViewById(R.id.currentCostBox);
        maxCostBox = (TextView) findViewById(R.id.maxCostBox);

        Intent receivedIntent = this.getIntent();
        currentExpense = (expense) receivedIntent.getSerializableExtra("Expense");
        nameBox.setText(currentExpense.getName());
        priorityBox.setText(currentExpense.getName());
        String temp = Float.toString(currentExpense.getCurrentExpense());
        currentCostBox.setText(temp);
        String temp2 = Float.toString(currentExpense.getMaxExpense());
        maxCostBox.setText(temp2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent receivedIntent = this.getIntent();
        currentExpense = (expense) receivedIntent.getSerializableExtra("Expense");
        nameBox.setText(currentExpense.getName());
        priorityBox.setText(currentExpense.getPriority());
        String temp = Float.toString(currentExpense.getCurrentExpense());
        currentCostBox.setText(temp);
        String temp2 = Float.toString(currentExpense.getMaxExpense());
        maxCostBox.setText(temp2);
    }

    public void onDoneClick(View view)
    {
        this.finish();
    }
}
