package com.example.aaron.welcomeActivity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;

//This activity is used with select_budget

public class mainActivity extends ActionBarActivity {

    private Button budgetAddButton; //Button that saves input when pressed
    private ListView budgetListView;
    private ArrayList budgetList;
    private DatabaseAccess theDatabase;

    //List components
    private ArrayList<budget> listItems=new ArrayList<budget>();
    private ArrayAdapter<budget> theAdapter;
    private int selectedItemInListPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_budget);
        //Sets up database material
        theDatabase = new DatabaseAccess(getApplicationContext());
        //Load Budgets
        //test();
        budgetAddButton = (Button) findViewById(R.id.budgetAddButton);
        budgetListView = (ListView) findViewById(R.id.budgetListView);

        //opening database
        theDatabase.open();
        //filling listItems with budgets from database
        listItems = theDatabase.findAllBudgets();
        //close database when you're done
        theDatabase.closeDatabase();

        //Updates list with budgets
        theAdapter = new ArrayAdapter<budget>(this,
                android.R.layout.simple_list_item_1, listItems);

        budgetListView.setAdapter(theAdapter);
        registerClick();
    }

    @Override
    protected void onResume() {
        super.onResume();

        theDatabase.open();
        //filling listItems with budgets from database
        listItems = theDatabase.findAllBudgets();
        //close database when you're done
        theDatabase.closeDatabase();

        //Updates list with budgets
        theAdapter = new ArrayAdapter<budget>(this,
                android.R.layout.simple_list_item_1, listItems);

        budgetListView.setAdapter(theAdapter);
        registerClick();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        theDatabase.closeDatabase();
    }
    
    public void test()
    {
        //Makes new budgets to add to database
        budget newBudget = new budget("Poop",100);
        budget newTurd = new budget("NotPoop",100);
        budget newCrap = new budget("TotallyPoop", 100);
        newBudget.setIDNumber(theDatabase.insertBudget(newBudget));
        newTurd.setIDNumber(theDatabase.insertBudget(newTurd));
        newCrap.setIDNumber(theDatabase.insertBudget(newCrap));
        long yes = newBudget.getIDNumber();
        long no = newTurd.getIDNumber();
        Log.d("Yes, the ID is:", Long.toString(yes));
        Log.d("Yes, the ID is not: ", Long.toString(no));
        newBudget = theDatabase.findBudget(yes);
        Log.d(newBudget.getName(),"yes");

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Executes when the budgetAddButton is pressed
    public void onAddBudgetClick(View view) {
        Intent newIntent = new Intent(this, newBudgetActivity.class);
        //Send to other activity
        startActivity(newIntent);
    }

    //Called when the edit button is pressed a long with an item on the list
    public void onEditClick(View view)
    {
        Intent newIntent = new Intent(this,budgetOverviewActivity.class);
        //Get the appropriate budget ot pass into the next activity, if available
        if (selectedItemInListPosition != -1)
        {
            budget transferBudget = listItems.get(selectedItemInListPosition);
            newIntent.putExtra("Budget",transferBudget);
            startActivity(newIntent);
        }
    }

    //Reviews what is returned when an intent is updated
    protected void onActivityResult(int requestCode,int resultCode, Intent returnedIntent)
    {
        //Omitted for now
    }

    //Used to register when user clicks on list item
    private void registerClick() {
        ListView budgetListView = (ListView) findViewById(R.id.budgetListView);
        budgetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                //needs to be sent to budgetOverviewActivity
                String selectedBudget = textView.getText().toString();
                String message = "You clicked # " + position + " which is string: " + selectedBudget;
                System.out.println(selectedBudget + " is the budget clicked!");
                //Sets the position up for list transfer
                selectedItemInListPosition = position;
                //Displays message showing which item was clicked
                Toast.makeText(mainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
