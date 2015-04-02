package com.example.aaron.welcomeActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import java.text.NumberFormat;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

//This activity is used with new_budget_layout

public class newBudgetActivity extends ActionBarActivity {
    private String newBudgetName;
    private double budgetLimit;
    private Button addButton;
    private EditText newBudgetNameTextEdit;
    private EditText newBudgetTotalTextEdit;
    private DatabaseAccess theDatabase;
    private static final NumberFormat currencyFormat;
    private AlertDialog.Builder builder;

    static {
        currencyFormat = NumberFormat.getCurrencyInstance();
    }

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

        //Add Text Listeners to convert entries to currency values
        //Credits go to http://stackoverflow.com/questions/27027070/android-edittext-addtextchangelistener-currency-format
        newBudgetTotalTextEdit.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    newBudgetTotalTextEdit.removeTextChangedListener(this);
                    String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
                    String cleanString = s.toString().replaceAll(replaceable, "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    formatter.setMaximumFractionDigits(0);
                    String formatted = formatter.format((parsed));

                    current = formatted;
                    newBudgetTotalTextEdit.setText(formatted);
                    newBudgetTotalTextEdit.setSelection(formatted.length());
                    newBudgetTotalTextEdit.addTextChangedListener(this);
                }
            }
        });

        //Handle Alert Dialogs by building the dialog
        builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
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
        //Get new budget name
        newBudgetName = newBudgetNameTextEdit.getText().toString();
        if (newBudgetName.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a name into the Budget field.");
            builder.show();
            return;
        }

        //Get new budget amount
        String budgetTotal = newBudgetTotalTextEdit.getText().toString();
        if (budgetTotal.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a value into the Total Budget field.");
            builder.show();
            return;
        }

        //Now I have to check for duplicates in the database
        theDatabase.open();
        ArrayList<budget> budgetList = theDatabase.findAllBudgets();
        for (int i = 0; i < budgetList.size(); i++)
        {
            budget currentBudget = budgetList.get(i);
            if (newBudgetName.matches(currentBudget.getName()))
            {
                //We have encountered a duplicate and will not proceed
                builder.setTitle("Error");
                builder.setMessage("A Budget of that name already exists.");
                builder.show();
                return;
            }
        }

        String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
        budgetTotal = budgetTotal.replaceAll(replaceable, "");
        budgetLimit = Double.parseDouble(budgetTotal);

        //Create new Budget object and add to database
        budget tempBudget = new budget(newBudgetName, budgetLimit);
        tempBudget.setIDNumber(theDatabase.insertBudget(tempBudget));
        //Create new expense table
        theDatabase.addExpenseTable(newBudgetName);
        //Close database when done
        theDatabase.closeDatabase();
        //Finish Activity and return results
        Intent returnedIntent = this.getIntent();
        //Says it's ok and returns the information upon finish
        setResult(RESULT_OK,returnedIntent);
        this.finish();
    }
}
