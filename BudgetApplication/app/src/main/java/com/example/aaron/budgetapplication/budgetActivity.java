package com.example.aaron.budgetapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class budgetActivity extends ActionBarActivity {

    private Button saveButton; //Button that saves input when pressed
    private EditText entryEditText1; //entry name input field
    private EditText entryCostEdit1; //entry cost input field

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        saveButton = (Button) findViewById(R.id.saveButton);
        entryEditText1 = (EditText) findViewById(R.id.entryTextEdit1);
        entryCostEdit1 = (EditText) findViewById(R.id.entryCostEdit1);
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

    public void onSaveClick(View view) {

        //gets text from entryEditText1
        String entryName = String.valueOf(entryEditText1.getText());

        //gets text from costEditText1
        String entryPriceStr = String.valueOf(entryCostEdit1.getText());

        //changes string into a float since I can only read in strings
        Float f = Float.parseFloat(entryPriceStr);

        //strings to be outputted
        String testString = "The entry name was " + entryName;
        String testString2 = "The entry price was " + f;

        //displays notifications on the screen just to shoe that this is working
        Toast.makeText(this, testString,Toast.LENGTH_SHORT).show();
        Toast.makeText(this, testString2,Toast.LENGTH_SHORT).show();
    }
}
