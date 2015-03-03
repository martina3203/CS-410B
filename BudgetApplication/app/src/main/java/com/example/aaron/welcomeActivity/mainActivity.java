package com.example.aaron.welcomeActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;


public class mainActivity extends ActionBarActivity {

    private Button budgetAddButton; //Button that saves input when pressed
    private ListView budgetListView;
    private ArrayList budgetList;
    private DatabaseAccess theDatabase;

    //List components
    private ArrayList<String> listItems=new ArrayList<String>();
    private ArrayAdapter<String> theAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_budget);
        //Sets up database material
        theDatabase = new DatabaseAccess(getApplicationContext());
        //Load Budgets
        test();
        budgetAddButton = (Button) findViewById(R.id.budgetAddButton);
        budgetListView = (ListView) findViewById(R.id.budgetListView);
        //Creates Array and will Load any saved information
        budgetList = new ArrayList();

        //Updates list with budgets
        //theAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        //budgetListView.setAdapter(theAdaptor);
    }

    public void test()
    {
        //This is used to my database testing, otherwise ignore and
        //from method OnCreate as you see fit
        budget newBudget = new budget("Poop",100);
        budget newTurd = new budget("NotPoop",100);
        newBudget.setIDNumber(theDatabase.insertBudget(newBudget));
        newTurd.setIDNumber(theDatabase.insertBudget(newTurd));
        long yes = newBudget.getIDNumber();
        long no = newTurd.getIDNumber();
        Log.d("Yes, the ID is:", Long.toString(yes));
        Log.d("Yes, the ID is not: ", Long.toString(no));
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
        //Send to other app
        startActivityForResult(newIntent,1);
    }

    //Reviews what is returned when an intent is updated
    protected void onActivityResult(int requestCode,int resultCode, Intent returnedIntent)
    {
        //If the request code corresponds
        if (requestCode == 1)
        {   //And the results are what we would like
            if ((resultCode == RESULT_OK) && (returnedIntent != null))
            {
                //Was replaced
            }
            return;
        }
    }

}
