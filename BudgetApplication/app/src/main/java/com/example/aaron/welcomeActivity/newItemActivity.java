package com.example.aaron.welcomeActivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//this activity is used with new_item_layout

public class newItemActivity extends ActionBarActivity {

    private EditText newItemNameTextEdit;
    private EditText newItemPriorityTextEdit;
    private EditText newItemCategoryTextEdit;
    private EditText newItemCurrentCostTextEdit;
    private EditText newItemMaxCostTextEdit;
    private Button addItemButton;

    //Constructor
    public newItemActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item_layout);
        //Creates edit texts and buttons
        newItemNameTextEdit = (EditText) findViewById(R.id.newNameEditText);
        newItemPriorityTextEdit = (EditText) findViewById(R.id.newPriorityEditText);
        newItemCategoryTextEdit = (EditText) findViewById(R.id.newCategoryEditText);
        newItemCurrentCostTextEdit = (EditText) findViewById(R.id.newCurrentCostEditText);
        newItemMaxCostTextEdit = (EditText) findViewById(R.id.newMaxCostEditText);
        addItemButton = (Button) findViewById(R.id.addItemButton);
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

    //called when add item button is clicked
    public void onAddItemClick(View view){

    }
}

