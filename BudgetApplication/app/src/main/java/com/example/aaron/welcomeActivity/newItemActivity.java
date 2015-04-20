package com.example.aaron.welcomeActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

//this activity is used with new_item_layout

public class newItemActivity extends ActionBarActivity {

    private String newItemName;
    private float currentCost;
    private float itemLimit;
    private int priority;
    private int aisle;
    private EditText newItemNameTextEdit;
    private TextView newItemCategoryName;
    private EditText newAisleTextEdit;
    private EditText newItemCurrentCostTextEdit;
    private EditText newItemMaxCostTextEdit;
    private Button addItemButton;
    Budget currentBudget;
    private DatabaseAccess theDatabase;
    private Spinner dropdown;
    private String selectedSpinner;
    String[] values = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private AlertDialog.Builder builder;

    //Constructor
    public newItemActivity() {
        newItemName = "";
        currentCost = 0;
        itemLimit = 0;
        priority = 0;
        aisle = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item_layout);
        //Creates edit texts, button, and drop down menu
        newItemNameTextEdit = (EditText) findViewById(R.id.newNameEditText);
        newItemCategoryName = (TextView) findViewById(R.id.newCategoryName);
        newAisleTextEdit = (EditText) findViewById(R.id.newAisleEditText);
        newItemCurrentCostTextEdit = (EditText) findViewById(R.id.newCurrentCostEditText);
        newItemMaxCostTextEdit = (EditText) findViewById(R.id.newMaxCostEditText);
        addItemButton = (Button) findViewById(R.id.addItemButton);
        dropdown = (Spinner)findViewById(R.id.priorityDropDown);
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        dropdown.setAdapter(priorityAdapter);

        builder = new AlertDialog.Builder(this);

        theDatabase = new DatabaseAccess(getApplicationContext());

        Intent receivedIntent = this.getIntent();
        currentBudget = (Budget) receivedIntent.getSerializableExtra("Budget");
        Log.v("Budget Loaded: ", currentBudget.getName());
        newItemCategoryName.setText(currentBudget.getName());

        findPriorityChoice();
    }

    //called when add item button is clicked
    public void onAddItemClick(View view) {
        /*Displays message to tell user that they did not fill out a field
         *Otherwise, it will read the field and move to the next one
         *This repeats until all fields are filled (except priority)*/

        newItemName = newItemNameTextEdit.getText().toString();
        if (newItemName.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a name!");
            builder.show();
            return;
        }

        String itemAisle = newAisleTextEdit.getText().toString();
        if (itemAisle.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered an aisle!");
            builder.show();
            return;
        }
        else{
            aisle = Integer.parseInt(itemAisle);
        }

        String itemCurrentCost = newItemCurrentCostTextEdit.getText().toString();
        if (itemCurrentCost.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a current cost!");
            builder.show();
            return;
        }
        else {
            currentCost = Float.parseFloat(itemCurrentCost);
        }

        String itemMaxCost = newItemMaxCostTextEdit.getText().toString();
        if (itemMaxCost.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You have not entered a max cost!");
            builder.show();
            return;
        }
        else {
            itemLimit = Float.parseFloat(itemMaxCost);
        }

        //Gets Priority from dropdown
        priority = Integer.parseInt(selectedSpinner);

        //Find current total cost of Budget and add the new item's price to it
        theDatabase.open();
        float budgetCost = theDatabase.findTotalCost(currentBudget.getName());
        budgetCost = budgetCost + currentCost;
        theDatabase.closeDatabase();

        /*if adding the new item may cause the user to go overbudget, display a warning box.
         *Otherwise, just add the Expense*/
        if(budgetCost > currentBudget.getMaxValue()){
            overbudgetAlert();
        }
        else{
            addExpense();
        }
    }

    //opens web browser and searches Amazon for item
    public void onPriceClick(View view) {
        //this is the URL for Amazon searches
        String url = "http://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=";
        String item = newItemNameTextEdit.getText().toString();
        //can't search without an item name
        if (item.matches("")){
            builder.setTitle("Error");
            builder.setMessage("You must enter an item name.");
            builder.show();
            return;
        }
        //append item name to URL and start the search
        url = url + item;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //Gets priority choice from dropdown menu
    private void findPriorityChoice(){
        dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            //sets variable when selected value is changed in dropdown
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedSpinner = values[position];
            }

            @Override
            //sets variable if user doesn't select one
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedSpinner = "1";
            }
        });
    }

    //adds new Expense to database based on inputted values
    public void addExpense(){
        //Create new Expense object
        Expense newExpense = new Expense(newItemName, currentCost, itemLimit);
        newExpense.setPriority(priority);
        newExpense.setAisle(aisle);
        theDatabase.open();
        theDatabase.insertExpense(newExpense, currentBudget.getName());
        theDatabase.closeDatabase();

        //Finish Activity and return results
        /*Intent returnedIntent = this.getIntent();
        //Says it's ok and returns the information upon finish
        setResult(RESULT_OK, returnedIntent);*/
        this.finish();
    }

    //alerts user if an item they are adding will cause them to go overbudget
    public void overbudgetAlert(){
        builder.setTitle("Overbudget!");
        builder.setMessage("Adding this item will cause you to go overbudget.  Add it anyway?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                //Do nothing
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                addExpense();
            }
        });
        builder.create();
        builder.show();

        return;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

