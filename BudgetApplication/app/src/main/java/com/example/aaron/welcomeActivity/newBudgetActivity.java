package com.example.aaron.welcomeActivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

//This activity is used with new_budget_layout

public class newBudgetActivity extends ActionBarActivity {
    private String newBudgetName;
    private double budgetLimit;
    private Button addButton;
    private EditText newBudgetNameTextEdit;
    private EditText newBudgetTotalTextEdit;
    private DatabaseAccess theDatabase;

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
        System.out.println("Here1");
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
        System.out.println("Here2");
        //Gather Field Information
        newBudgetName = newBudgetNameTextEdit.getText().toString();
        String temp = newBudgetTotalTextEdit.getText().toString();
        budgetLimit = Double.parseDouble(temp);

        //Create new Budget object and add to database
        theDatabase.open();
        budget tempBudget = new budget(newBudgetName, budgetLimit);
        tempBudget.setIDNumber(theDatabase.insertBudget(tempBudget));
        theDatabase.closeDatabase();
        System.out.println("Here3");
        long stuff = tempBudget.getIDNumber();
        Log.d(tempBudget.getName(), Long.toString(stuff));

        //Finish Activity and return results
        Intent returnedIntent = this.getIntent();
        //Says it's ok and returns the information upon finish
        setResult(RESULT_OK,returnedIntent);
        this.finish();
    }
}
