package com.example.aaron.welcomeActivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Jacob on 3/4/2015.
 */
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
        newItemNameTextEdit = (EditText) findViewById(R.id.newNameEditText);
        newItemPriorityTextEdit = (EditText) findViewById(R.id.newPriorityEditText);
        newItemCategoryTextEdit = (EditText) findViewById(R.id.newCategoryEditText);
        newItemCurrentCostTextEdit = (EditText) findViewById(R.id.newCurrentCostEditText);
        newItemMaxCostTextEdit = (EditText) findViewById(R.id.newMaxCostEditText);
        addItemButton = (Button) findViewById(R.id.addItemButton);
    }
}
