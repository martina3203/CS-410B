package com.example.aaron.welcomeActivity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class newBudgetActivity extends ActionBarActivity {
    private String newBudgetName;
    private double budgetLimit;
    private Button cancelButton;
    private Button addButton;

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
        //Save results

        //Finish Activity
        this.finish();
    }

    //Executes when cancelButton is clicked
    public void onCancelClick(View view)
    {
        //Finish activity
        this.finish();
    }
}
