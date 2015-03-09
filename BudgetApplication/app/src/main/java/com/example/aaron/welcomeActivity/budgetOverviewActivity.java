package com.example.aaron.welcomeActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import java.util.ArrayList;

//This activity is used with budget_overview

public class budgetOverviewActivity extends ActionBarActivity {

    private Button addNewExpenseButton;
    private TextView titleTextView;
    private TextView currentCostAmountTextView;
    private TextView moneyAvailableAmountTextView;
    private DatabaseAccess theDatabase;
    private ListView expenseListView;
    private ProgressBar progressBar;

    private ArrayList<expense> listItems=new ArrayList<expense>();
    private ArrayAdapter<expense> theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_overview);
        //Set up database
        theDatabase = new DatabaseAccess(getApplicationContext());

        //Creates buttons, textViews, listView, and progressBar
        addNewExpenseButton = (Button) findViewById(R.id.addNewExpenseButton);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        currentCostAmountTextView = (TextView) findViewById(R.id.currentCostAmountTextView);
        moneyAvailableAmountTextView = (TextView) findViewById(R.id.moneyAvailableAmountTextView);
        expenseListView = (ListView) findViewById(R.id.expenseListView);
        progressBar = (ProgressBar) findViewById(R.id.allocationProgressBar);

        //Acquire intent
        Intent receivedIntent = this.getIntent();
        budget currentBudget = (budget) receivedIntent.getSerializableExtra("Budget");
        Log.v("Budget Loaded: ", currentBudget.getName());

        theDatabase.closeDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
        return true;
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

    //Executes when add expense button is clicked
    public void onNewExpenseClick(View view){

    }
}
