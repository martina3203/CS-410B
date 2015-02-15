package newBudgetActivity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Aaron on 2/14/2015.
 *
 * This is the class for the fragment that handles the user making a new budget class.
 * This is called whenever the user presses the new budget button on the main activity
 */
public class newBudgetActivity extends Activity {
    private String newBudgetName;
    private double budgetLimit;

    //Typical Constructor
    public newBudgetActivity(){
        newBudgetName = "";
        budgetLimit = 0.0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }
}
