package com.example.aaron.welcomeActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;

import java.text.NumberFormat;
import java.util.ArrayList;

//This activity is used with budget_overview

public class budgetOverviewActivity extends ActionBarActivity {

    private Button addNewExpenseButton;
    private Button editBudgetButton;
    private Button summaryButton;
    private TextView titleTextView;
    private TextView currentCostAmountTextView;
    private TextView moneyAvailableAmountTextView;
    private DatabaseAccessObject theDatabase;
    private ListView expenseListView;
    private ProgressBar progressBar;
    int selectedItemInListPosition = -1;
    int previousListPosition = -1;

    //List components
    private ArrayList<Expense> expenseList = new ArrayList<Expense>();
    private ArrayAdapter<Expense> theAdapter;
    Budget currentBudget;

    //Color components
    private final String redColor = "#FF4444";
    private final String greenColor = "#99CC00";
    private final String yellowColor = "#FFEB3B";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_overview);
        //Set up database
        theDatabase = new DatabaseAccessObject(getApplicationContext());

        //Creates buttons, textViews, listView, and progressBar
        addNewExpenseButton = (Button) findViewById(R.id.addNewExpenseButton);
        editBudgetButton = (Button) findViewById(R.id.editBudgetButton);
        summaryButton = (Button) findViewById(R.id.summaryButton);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        currentCostAmountTextView = (TextView) findViewById(R.id.currentCostAmountTextView);
        moneyAvailableAmountTextView = (TextView) findViewById(R.id.moneyAvailableAmountTextView);
        expenseListView = (ListView) findViewById(R.id.expenseListView);
        progressBar = (ProgressBar) findViewById(R.id.allocationProgressBar);

        //Acquire intent
        Intent receivedIntent = this.getIntent();
        currentBudget = (Budget) receivedIntent.getSerializableExtra("Budget");
        //Set title on screen to be the same as the Budget name
        titleTextView.setText(currentBudget.getName(), TextView.BufferType.NORMAL);

        //Open database and find all expenses for Budget
        theDatabase.open();
        expenseList = theDatabase.findAllExpenses(currentBudget.getIDNumber());
        theDatabase.closeDatabase();

        //Update list with expenses
        theAdapter = new customExpenseAdapter(this, expenseList);
        expenseListView.setAdapter(theAdapter);
        //set up the progress bar
        setUpProgressBar();
        //registers clicks on list items
        registerClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        theDatabase.open();
        //filling listItems with budgets from database
        expenseList = theDatabase.findAllExpenses(currentBudget.getIDNumber());
        //close database when you're done
        theDatabase.closeDatabase();

        //Updates list with budgets
        theAdapter = new customExpenseAdapter(this, expenseList);
        expenseListView.setAdapter(theAdapter);

        //set up the progress bar
        setUpProgressBar();
        //registers clicks on list items
        registerClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Reset some position items
        selectedItemInListPosition = -1;
        previousListPosition = -1;
        theDatabase.closeDatabase();
    }

    //Executes when add Expense button is clicked
    public void onNewExpenseClick(View view) {
        Intent newIntent = new Intent(this, newItemActivity.class);
        Budget transferBudget = currentBudget;
        newIntent.putExtra("Budget", transferBudget);
        startActivity(newIntent);
    }

    //Called when the view button is pressed a long with an item on the list
    public void onViewExpenseClick(View view) {
        Intent intent = new Intent(this, itemOverviewActivity.class);
        //Get the appropriate Budget ot pass into the next activity, if available
        if (selectedItemInListPosition != -1) {
            Expense transferExpense = expenseList.get(selectedItemInListPosition);
            Budget transferBudget = currentBudget;
            intent.putExtra("Expense", transferExpense);
            intent.putExtra("Budget", transferBudget);
            startActivity(intent);
        }
    }

    public void onEditClick(View view) {
        Intent newIntent = new Intent(this, editBudgetActivity.class);
        Budget transferBudget = currentBudget;
        newIntent.putExtra("Budget", transferBudget);
        startActivity(newIntent);
    }

    public void onSummaryClick(View view) {
        Intent newIntent = new Intent(this, summaryActivity.class);
        Budget transferBudget = currentBudget;
        newIntent.putExtra("Budget", transferBudget);
        startActivity(newIntent);
    }

    //Used to register when user clicks on list item
    private void registerClick() {
        ListView expenseListView = (ListView) findViewById(R.id.expenseListView);
        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //This section pertains to highlighting
                //Set the new selected to the color
                parent.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                //Revert the previous color
                if (previousListPosition != -1 && previousListPosition != position) {
                    parent.getChildAt(previousListPosition).
                            setBackgroundColor(Color.parseColor(mainActivity.BACKGROUND_COLOR));
                }
                //Update previous saved position so that we can revert it later
                previousListPosition = position;
                selectedItemInListPosition = position;
            }
        });

    }

    //Reviews what is returned when an intent is updated
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        //Omitted for now
    }

    //sets up the progress bar and the textViews below it
    private void setUpProgressBar() {

        double maxProgress = 0; //the max value of the progress bar
        double currentProgress = 0; //the current value of the progress bar
        String moneyNotInUse = ""; //string used to set textView text
        String moneyInUse = ""; //string sued to set textView text

        //If there are no expenses
        if (expenseList.isEmpty()) {
            //update the progress bar and textView with current money usage (which is none)
            currentCostAmountTextView.setText("$0.00");
            progressBar.setProgress((int) currentProgress);

            //update status bar and textView with money available
            double moneyAvailable = currentBudget.getMaxValue();
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            moneyNotInUse = formatter.format(moneyAvailable);
            moneyAvailableAmountTextView.setText(moneyNotInUse);

            double maxProgressbar = currentBudget.getMaxValue();
            maxProgress = (int) maxProgressbar; //progress bars only accept integers
            progressBar.setMax((int) maxProgress);
        }
        //if there are some expenses
        else {
            theDatabase.open();

            //set max value of progress bar
            double maxProgressbar = currentBudget.getMaxValue();
            maxProgress = (int) maxProgressbar;
            progressBar.setMax((int) maxProgress);

            //prepare progress bar with current money usage
            Double currentTotalCost = theDatabase.findTotalCost(currentBudget.getIDNumber());
            currentProgress = currentTotalCost;

            //This is to make sure the progress bar doesn't mess up if the current progress
            //becomes higher than the max progress
            if (currentProgress > maxProgress) {
                progressBar.setProgress((int) maxProgress);
                progressBar.getProgressDrawable().setColorFilter(Color.parseColor(redColor),
                        PorterDuff.Mode.SRC_IN);
            } else {
                //progressBar.setProgress(currentProgress);
                //We will set colors based on how far we are.
                progressBar.setProgress((int) currentProgress);
                //If less than 60%
                if ((currentProgress / maxProgress) > .6) {
                    progressBar.getProgressDrawable().setColorFilter(Color.parseColor(yellowColor),
                            PorterDuff.Mode.MULTIPLY);
                } else {
                    progressBar.getProgressDrawable().setColorFilter(Color.parseColor(greenColor),
                            PorterDuff.Mode.MULTIPLY);
                }
            }
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            moneyInUse = formatter.format(currentTotalCost);
            currentCostAmountTextView.setText(moneyInUse);

            //update textView with money available to use
            double moneyAvailable = currentBudget.getMaxValue();
            moneyAvailable = moneyAvailable - currentTotalCost;
            //if there is no money left in the Budget, the text view will say "None"
            if (moneyAvailable <= 0) {
                moneyAvailableAmountTextView.setText("None");
            }
            //if there is money leftover, set the text view's value
            else {
                formatter = NumberFormat.getCurrencyInstance();
                moneyNotInUse = formatter.format(moneyAvailable);
                moneyAvailableAmountTextView.setText(moneyNotInUse);
            }

            theDatabase.closeDatabase();
        }
    }

    public void onBackPressed() {
        //Finish Activity and go to select Budget screen again
        Intent newIntent = new Intent(this, mainActivity.class);
        startActivity(newIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}
