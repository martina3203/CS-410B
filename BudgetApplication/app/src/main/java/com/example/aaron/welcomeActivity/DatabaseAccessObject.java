package com.example.aaron.welcomeActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Aaron on 3/3/2015.
 */
public class DatabaseAccessObject {
    private budgetDatabaseHelper theHelper;
    private SQLiteDatabase theDatabase;

    DatabaseAccessObject(Context context)
    {
        theHelper = new budgetDatabaseHelper(context);
    }

    public void open()
    {
        theDatabase = theHelper.getWritableDatabase();
    }

    //Adds a Budget to the table, returns ID number
    public long insertBudget(Budget theBudget)
    {
        //Build a Content Values class that contains the values of this Budget
        ContentValues values = new ContentValues();
        values.put(theHelper.COLUMN_BUDGET_NAME,theBudget.getName());
        values.put(theHelper.COLUMN_BUDGET_LIMIT,theBudget.getMaxValue());

        long ID = theDatabase.insert(theHelper.BUDGET_TABLE_NAME,null,values);
        return ID;
    }

    //Updates a Budget entry in the database
    public void updateBudget(Budget updatedBudget)
    {
        String command = "UPDATE " +  theHelper.BUDGET_TABLE_NAME +
                " SET " + theHelper.COLUMN_BUDGET_NAME + " = '" + updatedBudget.getName() + "'," +
                theHelper.COLUMN_BUDGET_LIMIT + " = " + updatedBudget.getMaxValue() +
                " WHERE " + theHelper.COLUMN_ID + " = " + updatedBudget.getIDNumber();
        theDatabase.execSQL(command);
    }

    //Removes a listed Budget, if it exists
    public void removeBudget(long ID)
    {
        //First we delete the subtable
        Budget targetBudget = findBudget(ID);
        removeExpenseTable(targetBudget.getName());
        //This builds a delete command to be executed
        String command = "DELETE FROM " + theHelper.BUDGET_TABLE_NAME + " WHERE " + theHelper.COLUMN_ID + " = " + ID;
        theDatabase.execSQL(command);
    }

    //Finds a specific Budget in the database
    public Budget findBudget(long ID)
    {
        //Builds a cursor from the query where we find it by the ID number
        Cursor theCursor = theDatabase.query(theHelper.BUDGET_TABLE_NAME,null,theHelper.COLUMN_ID + " = " + ID ,null,null,null,null);
        theCursor.moveToFirst();
        //Failure Budget, just in case
        Budget newBudget = new Budget("FAILURE10101",0);
        if (theCursor != null) {
            String budgetName = theCursor.getString(1);
            Double budgetLimit = theCursor.getDouble(2);

            newBudget = new Budget(budgetName, budgetLimit);
            newBudget.setIDNumber(ID);
            theCursor.close();
            return newBudget;
        }
        else
        {
            Log.d("Budget not found", null);
        }
        return newBudget;
    }

    //Returns a list of budgets in the database
    public ArrayList<Budget> findAllBudgets()
    {
        //Builds a cursor that is at the top of the Budget database
        ArrayList<Budget> budgetList = new ArrayList<Budget>();
        Cursor theCursor = theDatabase.query(theHelper.BUDGET_TABLE_NAME,null,null,null,theHelper.COLUMN_BUDGET_NAME,null,null);
        theCursor.moveToFirst();
        //Traverse through each row

        while (!theCursor.isAfterLast())
        {
            //Grab material
            long budgetID = theCursor.getInt(0);
            String budgetName = theCursor.getString(1);
            Double budgetLimit = theCursor.getDouble(2);
            //Create Budget
            Budget newBudget = new Budget(budgetName,budgetLimit);
            newBudget.setIDNumber(budgetID);
            //Add to list
            budgetList.add(newBudget);
            theCursor.moveToNext();
        }
        //Close the cursor
        if ((theCursor != null) && (theCursor.isClosed() == true))
        {
            theCursor.close();
        }
        return budgetList;
    }

    //Adds an Expense to the corresponding Expense
    public long insertExpense(Expense theExpense, String budgetName)
    {
        //Convert to appropriate table name
        String theTable = convertToSQLTableName(budgetName);
        //Build a Content Values class that contains the values of this Budget
        ContentValues values = new ContentValues();
        //Add values in
        values.put(theHelper.COLUMN_EXPENSE_NAME, theExpense.getName());
        values.put(theHelper.COLUMN_EXPENSE_COST, theExpense.getCurrentExpense());
        values.put(theHelper.COLUMN_EXPENSE_MAX_COST, theExpense.getMaxExpense());
        values.put(theHelper.COLUMN_EXPENSE_PRIORITY, theExpense.getPriority());
        values.put(theHelper.COLUMN_EXPENSE_AISLE_NUMBER, theExpense.getAisle());
        //Commit insertion
        long ID = theDatabase.insert(theTable,null,values);
        return ID;
    }

    public void updateExpense(Expense theExpense, String budgetName)
    {
        //Builds a string to update an entry in an Expense table to a corresponding Budget
        //It is also important to note that this methods updates ALL of the fields corresponding to Expense except of course the ID
        //First convert to actual table name
        String theTable = convertToSQLTableName(budgetName);
        String command = "UPDATE " +  theTable +
                " SET " + theHelper.COLUMN_EXPENSE_NAME + " = '" + theExpense.getName() + "'," +
                theHelper.COLUMN_EXPENSE_COST + " = " + theExpense.getCurrentExpense() + ","
                + theHelper.COLUMN_EXPENSE_MAX_COST + " = " + theExpense.getMaxExpense() + ","
                + theHelper.COLUMN_EXPENSE_PRIORITY + " = " + theExpense.getPriority() + ","
                + theHelper.COLUMN_EXPENSE_AISLE_NUMBER + " = " + theExpense.getAisle() +
                " WHERE " + theHelper.COLUMN_ID + " = " + theExpense.getIDNumber();
        theDatabase.execSQL(command);
    }

    //Adds a subtable of expenses that corresponds to a Budget
    //This should be called on the creation of a new Budget
    public void addExpenseTable(String newTableName) {
        //This is the string for execution via SQL.execSQL
        //Will have to add '_' to the string so that the SQL command doesn't have an error
        String newTable = convertToSQLTableName(newTableName);
        //Build create table command
        String command = "CREATE TABLE " + newTable +
                " (" + theHelper.COLUMN_ID + " integer primary key autoincrement, " +
                theHelper.COLUMN_EXPENSE_NAME + " text, " + theHelper.COLUMN_EXPENSE_PRIORITY + " integer," +
                theHelper.COLUMN_EXPENSE_COST + " real, " + theHelper.COLUMN_EXPENSE_MAX_COST + " real," +
                theHelper.COLUMN_EXPENSE_AISLE_NUMBER + " integer)";
        Log.d("Expense Table Created: ", newTable);
        theDatabase.execSQL(command);
    }

    //Removes an existing subtable of the expenses that corresponds to a Budget
    public void removeExpenseTable(String budgetName) {
        String properTableName = convertToSQLTableName(budgetName);
        String command = "DROP TABLE " + properTableName;
        theDatabase.execSQL(command);
    }

    //Removes a listed Expense, if it exists
    public void removeExpense(long ID, String tableName)
    {
        String properTableName = convertToSQLTableName(tableName);
        //This builds a delete command to be executed
        String command = "DELETE FROM " + properTableName + " WHERE " + theHelper.COLUMN_ID + " = " + ID;
        theDatabase.execSQL(command);
    }

    //Finds and returns an Expense that corresponds to that Budget and ID
    public Expense findExpense(long ID, String budgetName)
    {
        //Builds a cursor from the query where we find it by the ID number and the appropriate table
        Cursor theCursor = theDatabase.query(budgetName,null,theHelper.COLUMN_ID + " = " + ID,null,null,null,null);
        theCursor.moveToFirst();
        String expenseName = theCursor.getString(1);
        int expensePriority = theCursor.getInt(2);
        float expenseTotal = theCursor.getFloat(3);
        float expenseMax = theCursor.getFloat(4);
        int expenseAisle = theCursor.getInt(5);

        //Create an Expense and return it with this information
        Expense newExpense = new Expense(expenseName,expenseTotal,expenseMax);
        newExpense.setPriority(expensePriority);
        newExpense.setIDNumber(ID);
        newExpense.setAisle(expenseAisle);
        return newExpense;
    }

    //Returns a list of expenses for a corresponding Budget in the database
    public ArrayList<Expense> findAllExpenses(String theExpenseTable)
    {
        ArrayList<Expense> expenseList = new ArrayList<Expense>();
        //Get the actual table name
        String tableName = convertToSQLTableName(theExpenseTable);
        //Build a cursor
        Cursor theCursor = theDatabase.query(tableName,null,null,null,null,null,null);
        theCursor.moveToFirst();
        //Traverse through each row
        while (!theCursor.isAfterLast())
        {
            //Grab material
            long expenseID = theCursor.getInt(0);
            String expenseName = theCursor.getString(1);
            int expensePriority = theCursor.getInt(2);
            float expenseCost = theCursor.getFloat(3);
            float expenseMaxCost = theCursor.getFloat(4);
            int expenseAisle = theCursor.getInt(5);

            //Create Expense
            Expense newExpense = new Expense(expenseName,expenseCost,expenseMaxCost);
            newExpense.setPriority(expensePriority);
            newExpense.setIDNumber(expenseID);
            newExpense.setAisle(expenseAisle);
            //Add to list
            expenseList.add(newExpense);
            theCursor.moveToNext();
        }
        //Close the cursor
        if ((theCursor != null) && (theCursor.isClosed() == true))
        {
            theCursor.close();
        }
        return expenseList;
    }

    //Returns total current cost of all expenses in a table
    public float findTotalCost(String theExpenseTable)
    {
        //Get the actual table name
        String tableName = convertToSQLTableName(theExpenseTable);
        //Build a cursor
        Cursor theCursor = theDatabase.query(tableName,null,null,null,null,null,null);
        theCursor.moveToFirst();
        float totalCost = 0;
        //Traverse through each row
        while (!theCursor.isAfterLast())
        {
            //Grab material and add to total
            float expenseCost = theCursor.getFloat(3);
            totalCost += expenseCost;

            theCursor.moveToNext();
        }
        //Close the cursor
        if ((theCursor != null) && (theCursor.isClosed() == true))
        {
            theCursor.close();
        }
        return totalCost;
    }

    //Replaces ' ' with '_' to have a valid table name
    public String convertToSQLTableName(String input)
    {
        String returnString = "";
        for (int i = 0; i < input.length(); i++)
        {
            if ((input.charAt(i) == ' ') || (input.charAt(i) == '\''))
            {
                returnString = returnString + "_";
            }
            else
            {
                returnString = returnString + input.charAt(i);
            }
        }
        return returnString;
    }

    //Call this when done with working with the database
    public void closeDatabase()
    {
        if ((theDatabase != null) && (theDatabase.isOpen()))
        {
            theDatabase.close();
        }
    }
}
