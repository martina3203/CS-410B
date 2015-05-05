package com.example.aaron.welcomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

//This activity is used with summary_layout

public class summaryActivity extends ActionBarActivity {

    private DatabaseAccessObject theDatabase;
    private Budget currentBudget;
    private ListView expenseListView;
    private TextView priceTextView;
    public static String frequency = "None";
    private Spinner frequencyDropdown;
    private customExpenseAdapter adapter;
    private ArrayList<Expense> expenseList = new ArrayList<Expense>();
    public static String[] frequencyValues = new String[]{"None", "Daily", "Five Days", "One Week", "Two Weeks",
            "One Month", "Quarterly", "Six Months", "Annually"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_layout);
        expenseListView = (ListView) findViewById(R.id.expenseListView);
        priceTextView = (TextView) findViewById(R.id.totalCostAmountTextView);
        frequencyDropdown = (Spinner) findViewById(R.id.frequencyDropDown);
        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, frequencyValues);
        frequencyDropdown.setAdapter(frequencyAdapter);

        theDatabase = new DatabaseAccessObject(getApplicationContext());
        //Acquire intent
        Intent receivedIntent = this.getIntent();
        currentBudget = (Budget) receivedIntent.getSerializableExtra("Budget");
        //Set beginning frequency value
        frequency = "None";

        //Get all expenses in the budget
        theDatabase.open();
        expenseList = theDatabase.findAllExpensesFrequency(currentBudget.getIDNumber(), frequency);
        theDatabase.closeDatabase();
        //Set the value of the textView
        setPriceTextView();
        //Update listView with expenses
        adapter = new customExpenseAdapter(this, expenseList);
        expenseListView.setAdapter(adapter);

        findFrequencyChoice();
    }

    @Override
    protected void onPause() {
        frequency = "None";
        super.onPause();
        theDatabase.closeDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        theDatabase.open();
        expenseList = theDatabase.findAllExpenses(currentBudget.getIDNumber());
        theDatabase.closeDatabase();
        customExpenseAdapter newAdapter = new customExpenseAdapter(this, expenseList);
        expenseListView.setAdapter(newAdapter);
        setPriceTextView();
    }

    private void findFrequencyChoice(){
        frequencyDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //sets variable when selected value is changed in dropdown
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                frequency = frequencyValues[position];
                customExpenseAdapter newAdapter = new customExpenseAdapter(getBaseContext(), expenseList);
                expenseListView.setAdapter(newAdapter);
                setPriceTextView();
            }

            @Override
            //sets variable if user doesn't select one
            public void onNothingSelected(AdapterView<?> parentView) {
                frequency = "None";
                customExpenseAdapter newAdapter = new customExpenseAdapter(getBaseContext(), expenseList);
                expenseListView.setAdapter(newAdapter);
                setPriceTextView();
            }
        });
    }

    //Updates list with expenses of specified frequency
    public void onOkayClick(View view){
        theDatabase.open();
        expenseList = theDatabase.findAllExpenses(currentBudget.getIDNumber());
        customExpenseAdapter newAdapter = new customExpenseAdapter(this, expenseList);
        expenseListView.setAdapter(newAdapter);
        theDatabase.closeDatabase();
        setPriceTextView();
    }

    //Sets value of total price text view;
    public void setPriceTextView(){
        Double totalCost = computeTotalEstimate();
        if (totalCost != 0){
            String currentAmount = String.format("%.2f", totalCost);
            currentAmount = "$" + currentAmount;
            priceTextView.setText(currentAmount, TextView.BufferType.NORMAL);
        }
        else{
            priceTextView.setText("$0.00", TextView.BufferType.NORMAL);
        }
    }

    //The following functions override the two back buttons that exist on Android
    @Override
    public void onBackPressed()
    {
        //Finish Activity and go to select Budget screen again
        Intent newIntent = new Intent(this, budgetOverviewActivity.class);
        newIntent.putExtra("Budget",currentBudget);
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

    public Double computeTotalEstimate()
    {
        Double total = 0.0;
        //Grab all the expenses
        //For each expense, find the use the frequency to determine the value that is currently has
        for (int i = 0; i < expenseList.size(); i++)
        {
            Expense currentExpense = expenseList.get(i);
            Double currentCost = Double.valueOf(currentExpense.getCurrentExpense());
            String paymentInterval = currentExpense.getPaymentInterval();
            switch (paymentInterval)
            {
                case "None":
                    //It is simply this value
                    break;
                case "Daily":
                    currentCost = computeDaily(currentCost);
                    break;
                case "Five Days":
                    currentCost = computeFiveDays(currentCost);
                    break;
                case "One Week":
                    currentCost = computeOneWeek(currentCost);
                    break;
                case "Two Weeks":
                    currentCost = computeTwoWeeks(currentCost);
                    break;
                case "One Month":
                    currentCost = computeOneMonth(currentCost);
                    break;
                case "Quarterly":
                    currentCost = computeQuarterly(currentCost);
                    break;
                case "Six Months":
                    currentCost = computeSixMonths(currentCost);
                    break;
                case "Annually":
                    currentCost = computeYearly(currentCost);
                    break;
                default:
                    Log.d("This is not a valid", "String");
                    break;
            }
            total = total + currentCost;
        }
        return total;
    }

    public static Double computeYearly(Double currentCost) {
        Double cost = 0.0;
        //Based on the frequency set by the user, compute these values
        //Daily
        if (frequency == frequencyValues[1])
        {
            cost = currentCost / 365.0;
        }
        //Five Days
        else if (frequency == frequencyValues[2])
        {
            cost = currentCost / 73.0;
        }
        //One Week
        else if (frequency == frequencyValues[3])
        {
            cost = currentCost / 52.1;
        }
        //Two Weeks
        else if (frequency == frequencyValues[4])
        {
            cost = currentCost / 26.1;
        }
        //One Month
        else if (frequency == frequencyValues[5])
        {
            cost = currentCost / 12.2;
        }
        //Quarterly
        else if (frequency == frequencyValues[6])
        {
            cost = currentCost / 4.0;
        }
        //6 Months
        else if (frequency == frequencyValues[7])
        {
            cost = currentCost / 2.0;
        }
        //Annually
        else if (frequency == frequencyValues[8])
        {
            cost = currentCost;
        }
        else
        {
            cost = currentCost;
        }
        return cost;
    }

    public static Double computeSixMonths(Double currentCost) {
        Double cost = 0.0;
        //Based on the frequency set by the user, compute these values
        //Daily
        if (frequency == frequencyValues[1])
        {
            cost = currentCost / 180.0;
        }
        //Five Days
        else if (frequency == frequencyValues[2])
        {
            cost = currentCost / 36.0;
        }
        //One Week
        else if (frequency == frequencyValues[3])
        {
            cost = currentCost / 25.7;
        }
        //Two Weeks
        else if (frequency == frequencyValues[4])
        {
            cost = currentCost / 38.6;
        }
        //One Month
        else if (frequency == frequencyValues[5])
        {
            cost = currentCost / 6.0;
        }
        //Quarterly
        else if (frequency == frequencyValues[6])
        {
            cost = currentCost / 3.0;
        }
        //6 Months
        else if (frequency == frequencyValues[7])
        {
            cost = currentCost;
        }
        //Annually
        else if (frequency == frequencyValues[8])
        {
            cost = currentCost * 2.0;
        }
        else
        {
            cost = currentCost;
        }
        return cost;
    }

    public static Double computeQuarterly(Double currentCost) {
        Double cost = 0.0;
        //Based on the frequency set by the user, compute these values
        //Daily
        if (frequency == frequencyValues[1])
        {
            cost = currentCost / 90.0;
        }
        //Five Days
        else if (frequency == frequencyValues[2])
        {
            cost = currentCost / 18.0;
        }
        //One Week
        else if (frequency == frequencyValues[3])
        {
            cost = currentCost / 12.8;
        }
        //Two Weeks
        else if (frequency == frequencyValues[4])
        {
            cost = currentCost / 6.4;
        }
        //One Month
        else if (frequency == frequencyValues[5])
        {
            cost = currentCost / 3.0;
        }
        //Quarterly
        else if (frequency == frequencyValues[6])
        {
            cost = currentCost;
        }
        //6 Months
        else if (frequency == frequencyValues[7])
        {
            cost = currentCost * 2;
        }
        //Annually
        else if (frequency == frequencyValues[8])
        {
            cost = currentCost * 4;
        }
        else
        {
            cost = currentCost;
        }
        return cost;
    }

    public static Double computeOneMonth(Double currentCost) {
        Double cost = 0.0;
        //Based on the frequency set by the user, compute these values
        //Daily
        if (frequency == frequencyValues[1])
        {
            cost = currentCost / 30.0;
        }
        //Five Days
        else if (frequency == frequencyValues[2])
        {
            cost = currentCost / 6.0;
        }
        //One Week
        else if (frequency == frequencyValues[3])
        {
            cost = currentCost / 4.3;
        }
        //Two Weeks
        else if (frequency == frequencyValues[4])
        {
            cost = currentCost / 2.1;
        }
        //One Month
        else if (frequency == frequencyValues[5])
        {
            cost = currentCost;
        }
        //Quarterly
        else if (frequency == frequencyValues[6])
        {
            cost = currentCost * 3.0;
        }
        //6 Months
        else if (frequency == frequencyValues[7])
        {
            cost = currentCost * 6.0;
        }
        //Annually
        else if (frequency == frequencyValues[8])
        {
            cost = currentCost * 12.0;
        }
        else
        {
            cost = currentCost;
        }
        return cost;
    }

    public static Double computeTwoWeeks(Double currentCost) {
        Double cost = 0.0;
        //Based on the frequency set by the user, compute these values
        //Daily
        if (frequency == frequencyValues[1])
        {
            cost = currentCost / 14.0;
        }
        //Five Days
        else if (frequency == frequencyValues[2])
        {
            cost = currentCost / 2.8;
        }
        //One Week
        else if (frequency == frequencyValues[3])
        {
            cost = currentCost / 2;
        }
        //Two Weeks
        else if (frequency == frequencyValues[4])
        {
            cost = currentCost;
        }
        //One Month
        else if (frequency == frequencyValues[5])
        {
            cost = currentCost * 2.14;
        }
        //Quarterly
        else if (frequency == frequencyValues[6])
        {
            cost = currentCost * 6.4;
        }
        //6 Months
        else if (frequency == frequencyValues[7])
        {
            cost = currentCost * 12.9;
        }
        //Annually
        else if (frequency == frequencyValues[8])
        {
            cost = currentCost * 26.1;
        }
        else
        {
            cost = currentCost;
        }
        return cost;
    }

    public static Double computeOneWeek(Double currentCost) {
        Double cost = 0.0;
        //Based on the frequency set by the user, compute these values
        //Daily
        if (frequency == frequencyValues[1])
        {
            cost = currentCost / 7;
        }
        //Five Days
        else if (frequency == frequencyValues[2])
        {
            cost = currentCost / 1.4;
        }
        //One Week
        else if (frequency == frequencyValues[3])
        {
            cost = currentCost;
        }
        //Two Weeks
        else if (frequency == frequencyValues[4])
        {
            cost = currentCost * 2;
        }
        //One Month
        else if (frequency == frequencyValues[5])
        {
            cost = currentCost * 4;
        }
        //Quarterly
        else if (frequency == frequencyValues[6])
        {
            cost = currentCost * 12.8;
        }
        //6 Months
        else if (frequency == frequencyValues[7])
        {
            cost = currentCost * 25.7;
        }
        //Annually
        else if (frequency == frequencyValues[8])
        {
            cost = currentCost * 52.1;
        }
        else
        {
            cost = currentCost;
        }
        return cost;
    }

    public static Double computeFiveDays(Double currentCost) {
        Double cost = 0.0;
        //Based on the frequency set by the user, compute these values
        //Daily
        if (frequency == frequencyValues[1])
        {
            cost = currentCost / 5.0;
        }
        //Five Days
        else if (frequency == frequencyValues[2])
        {
            cost = currentCost;
        }
        //One Week
        else if (frequency == frequencyValues[3])
        {
            cost = currentCost * 1.4;
        }
        //Two Weeks
        else if (frequency == frequencyValues[4])
        {
            cost = currentCost * 2.8;
        }
        //One Month
        else if (frequency == frequencyValues[5])
        {
            cost = currentCost * 6;
        }
        //Quarterly
        else if (frequency == frequencyValues[6])
        {
            cost = currentCost * 18;
        }
        //6 Months
        else if (frequency == frequencyValues[7])
        {
            cost = currentCost * 36;
        }
        //Annually
        else if (frequency == frequencyValues[8])
        {
            cost = currentCost * 73;
        }
        else
        {
            cost = currentCost;
        }
        return cost;
    }

    public static Double computeDaily(Double currentCost) {
        Double cost = 0.0;
        //Based on the frequency set by the user, compute these values
        //Daily
        if (frequency == frequencyValues[1])
        {
            cost = currentCost;
        }
        //Five Days
        else if (frequency == frequencyValues[2])
        {
            cost = currentCost * 5.0;
        }
        //One Week
        else if (frequency == frequencyValues[3])
        {
            cost = currentCost * 7.0;
        }
        //Two Weeks
        else if (frequency == frequencyValues[4])
        {
            cost = currentCost * 14.0;
        }
        //One Month
        else if (frequency == frequencyValues[5])
        {
            cost = currentCost * 30.0;
        }
        //Quarterly
        else if (frequency == frequencyValues[6])
        {
            cost = currentCost * 90;
        }
        //6 Months
        else if (frequency == frequencyValues[7])
        {
            cost = currentCost * 180;
        }
        //Annually
        else if (frequency == frequencyValues[8])
        {
            cost = currentCost * 365;
        }
        else
        {
            cost = currentCost;
        }
        return cost;
    }

}
