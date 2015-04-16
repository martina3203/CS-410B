package com.example.aaron.welcomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

//This activity is used with select_budget

public class mainActivity extends Activity {

    private Button budgetAddButton; //Button that saves input when pressed
    private ListView budgetListView;
    private ArrayList budgetList;
    private DatabaseAccess theDatabase;
    public static String BACKGROUND_COLOR = "#ffddffd4";

    //List components
    private ArrayList<budget> listItems=new ArrayList<budget>();
    private ArrayAdapter<budget> theAdapter;
    private int selectedItemInListPosition = -1;
    private int previousListPosition = -1;

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
        //Reset some position flags
        selectedItemInListPosition = -1;
        previousListPosition = -1;
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
    public void onEditClick(View view) {
        Intent newIntent = new Intent(this,budgetOverviewActivity.class);
        //Get the appropriate budget ot pass into the next activity, if available
        if (selectedItemInListPosition != -1)
        {
            budget transferBudget = listItems.get(selectedItemInListPosition);
            newIntent.putExtra("Budget",transferBudget);
            startActivity(newIntent);
        }
    }

    //called when delete is pressed; deletes selected budget
    public void onDeleteClick(View view){
        if (selectedItemInListPosition != -1){
            //get budget to be deleted
            budget deleteBudget = listItems.get(selectedItemInListPosition);
            theDatabase.open();
            //removes budget from database
            theDatabase.removeBudget(deleteBudget.getIDNumber());
            //removes budget from listView and updates listView
            theAdapter.remove(deleteBudget);
            theAdapter.notifyDataSetChanged();
            theDatabase.closeDatabase();
            //Reset selected Flag
            selectedItemInListPosition = -1;
        }
    }

    //Reviews what is returned when an intent is updated
    protected void onActivityResult(int requestCode,int resultCode, Intent returnedIntent)
    {
        //Omitted for now
    }

    //Used to register when user clicks on list item
    private void registerClick() {
        budgetListView = (ListView) findViewById(R.id.budgetListView);
        budgetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                //Sets the position up for list transfer
                selectedItemInListPosition = position;
                budgetListView.setItemChecked(position,true);
                //This section pertains to highlighting
                //Set the new selected to the color
                parent.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                //Revert the previous color
                if (previousListPosition != -1 && previousListPosition != position){
                    parent.getChildAt(previousListPosition).setBackgroundColor(Color.parseColor(BACKGROUND_COLOR));
                }
                //Update previous saved position so that we can revert it later
                previousListPosition = position;
            }
        });

    }

}