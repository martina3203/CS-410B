package com.example.aaron.welcomeActivity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class newBudgetActivity extends ActionBarActivity {
    private String newBudgetName;
    private double budgetLimit;

    //Typical Constructor
    public newBudgetActivity(){
        newBudgetName = "";
        budgetLimit = 0.0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_budget_layout);
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

    //Executes when onAddBudgetClick is clicked
    public void onAddBudgetClick(View view)
    {
        //Save results

        //Finish Activity
        finish();
    }

    //Executes when cancel is clicked
    public void onCancelClick(View view)
    {
        //Finish activity
        finish();
    }
}
