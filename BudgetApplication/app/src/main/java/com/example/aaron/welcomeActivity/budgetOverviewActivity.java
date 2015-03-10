package com.example.aaron.welcomeActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

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

    //List components
    private ArrayList<expense> expenseList = new ArrayList<expense>();
    private ArrayAdapter<expense> theAdapter;
    private int selectedItemInListPosition = -1;
    budget currentBudget;

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
        currentBudget = (budget) receivedIntent.getSerializableExtra("Budget");
        Log.v("Budget Loaded: ", currentBudget.getName());
        //Set title on screen to be the same as the budget name
        titleTextView.setText(currentBudget.getName());
        //Open database and find all expenses for budget
        theDatabase.open();
        expenseList = theDatabase.findAllExpenses(currentBudget.getName());
        theDatabase.closeDatabase();

        //Update list with expenses
        theAdapter = new ArrayAdapter<expense>(this,
                android.R.layout.simple_list_item_1, expenseList);
        expenseListView.setAdapter(theAdapter);

        setUpProgressBar();

        registerClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        theDatabase.open();
        //filling listItems with budgets from database
        expenseList = theDatabase.findAllExpenses(currentBudget.getName());
        //close database when you're done
        theDatabase.closeDatabase();

        //Updates list with budgets
        theAdapter = new ArrayAdapter<expense>(this,
                android.R.layout.simple_list_item_1, expenseList);

        expenseListView.setAdapter(theAdapter);

        setUpProgressBar();

        registerClick();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
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
        Intent newIntent = new Intent(this,newItemActivity.class);
        budget transferBudget = currentBudget;
        newIntent.putExtra("Budget",transferBudget);
        startActivity(newIntent);
    }

    //Used to register when user clicks on list item
    private void registerClick() {
        ListView expenseListView = (ListView) findViewById(R.id.expenseListView);
        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String selectedExpense = textView.getText().toString();
                String message = "You clicked # " + position + " which is string: " + selectedExpense;
                System.out.println(selectedExpense + " is the expense clicked!");
                //Sets the position up for list transfer
                selectedItemInListPosition = position;
                //Displays message showing which item was clicked
                Toast.makeText(budgetOverviewActivity.this, message, Toast.LENGTH_SHORT).show();
                /*Intent newIntent = new Intent(budgetOverviewActivity.this,itemOverviewActivity.class);
                expense transferExpense = expenseList.get(position);
                newIntent.putExtra("Expense",transferExpense);
                startActivity(newIntent);*/
            }
        });

    }

    //Reviews what is returned when an intent is updated
    protected void onActivityResult(int requestCode,int resultCode, Intent returnedIntent)
    {
        //Omitted for now
    }

    private void setUpProgressBar(){

        int maxProgress = 0;
        int currentProgress = 0;

        //If there are no expenses
        if(expenseList.isEmpty()) {
            //update the progress bar and textView with current money usage
            currentCostAmountTextView.setText("$0.00");
            progressBar.setProgress(0);

            //update status bar and textView with money available
            double moneyAvailable = currentBudget.getMaxValue();
            String temp = "$" + String.valueOf(moneyAvailable);
            moneyAvailableAmountTextView.setText(temp);

            double maxProgressbar = currentBudget.getMaxValue();
            maxProgress = (int) maxProgressbar; //progress bars only accept integers
            progressBar.setMax(maxProgress);
        }
        //if there are some expenses
        else{
            theDatabase.open();

            //set max value of progress bar
            double maxProgressbar = currentBudget.getMaxValue();
            maxProgress = (int) maxProgressbar;
            progressBar.setMax(maxProgress);

            //prepare progress bar with current money usage
            float currentTotalCost = theDatabase.findTotalCost(currentBudget.getName());
            currentProgress = (int) currentTotalCost;
            progressBar.setProgress(currentProgress);
            String temp = "$" + String.valueOf(currentTotalCost);
            currentCostAmountTextView.setText(temp);

            //update textView with money available
            double moneyAvailable = currentBudget.getMaxValue();
            String temp2 = "$" + String.valueOf(moneyAvailable);
            moneyAvailableAmountTextView.setText(temp2);

            theDatabase.closeDatabase();
        }

    }
}
