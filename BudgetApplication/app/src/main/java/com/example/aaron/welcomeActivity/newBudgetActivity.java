package com.example.aaron.welcomeActivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class newBudgetActivity extends ActionBarActivity {
    private String newBudgetName;
    private double budgetLimit;
    private Button cancelButton;
    private Button addButton;
    private EditText newBudgetNameTextEdit;
    private EditText newBudgetTotalTextEdit;

    //Typical Constructor
    public newBudgetActivity(){
        newBudgetName = "";
        budgetLimit = 0.0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_budget_layout);
        //Creates buttons
        cancelButton = (Button) findViewById(R.id.cancelButton);
        addButton = (Button) findViewById(R.id.addButton);
        newBudgetNameTextEdit = (EditText) findViewById(R.id.newBudgetNameTextEdit);
        newBudgetTotalTextEdit = (EditText) findViewById(R.id.totalBudgetTextEdit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Executes when addButton is clicked
    public void onAddBudgetClick(View view)
    {
        //Gather Field Information
        String newBudgetName = newBudgetNameTextEdit.toString();
        String temp = newBudgetTotalTextEdit.toString();
        //Float newBudgetTotal = Float.parseFloat(temp);

        //Create new Budget object


        //Finish Activity and return results
        Intent returnedIntent = this.getIntent();
        //Says it's ok and returns the information upon finish
        setResult(RESULT_OK,returnedIntent);
        this.finish();
    }

    //Executes when cancelButton is clicked
    public void onCancelClick(View view)
    {
        //Finish activity and do nothing
        this.finish();
    }
}
