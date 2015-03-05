package com.example.aaron.welcomeActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 3/3/2015.
 */
public class DatabaseAccess {
    private budgetDatabaseHelper theHelper;
    private SQLiteDatabase theDatabase;

    DatabaseAccess(Context context)
    {
        theHelper = new budgetDatabaseHelper(context);
    }

    public void open()
    {
        theDatabase = theHelper.getWritableDatabase();
    }

    //Adds a subtable of expenses that corresponds to a budget
    public void addExpenseTable(String newTableName) {
        //This is the string for execution via SQL.execSQL
        String command = "create table " + newTableName +
                "(" + theHelper.COLUMN_ID + " integer primary key autoincrement, " +
                theHelper.COLUMN_EXPENSE_NAME + " text, " + theHelper.COLUMN_EXPENSE_PRIORITY + " integer," +
                theHelper.COLUMN_EXPENSE_COST + " real, " + theHelper.COLUMN_EXPENSE_MAX_COST + " real)";
        Log.d("Expense Table Created: ", newTableName);
        theDatabase.execSQL(command);
    }

    //Removes an existing subtable of the expenses that corresponds to a budget
    public void removeExpenseTable(String tableName) {
        String command = "DROP TABLE " + tableName;
        theDatabase.execSQL(command);
    }

    //Adds a budget to the table, returns ID number
    public long insertBudget(budget theBudget)
    {
        //Build a Content Values class that contains the values of this budget
        ContentValues values = new ContentValues();
        values.put(theHelper.COLUMN_BUDGET_NAME,theBudget.getName());
        values.put(theHelper.COLUMN_BUDGET_LIMIT,theBudget.getMaxValue());

        long ID = theDatabase.insert(theHelper.BUDGET_TABLE_NAME,null,values);
        return ID;
    }

    //Adds an expense to the corresponding expense
    public long insertExpense(expense theExpense, String tableName)
    {
        //Build a Content Values class that contains the values of this budget
        ContentValues values = new ContentValues();
        values.put(theHelper.COLUMN_EXPENSE_NAME, theExpense.getName());
        values.put(theHelper.COLUMN_EXPENSE_COST, theExpense.getCurrentExpense());
        values.put(theHelper.COLUMN_EXPENSE_MAX_COST, theExpense.getMaxExpense());
        values.put(theHelper.COLUMN_EXPENSE_PRIORITY, theExpense.getPriority());
        /*
        Equivalent SQL statement
        String command = "INSERT INTO " + tableName +
                COLUMN_EXPENSE_NAME +"," + COLUMN_EXPENSE_COST + "," + COLUMN_EXPENSE_MAX_COST + "," + COLUMN_EXPENSE_PRIORITY + ")" +
                "VALUES (" + theExpense.getName() + "," + theExpense.getCurrentExpense() + "," + theExpense.getMaxExpense() + "," + theExpense.getPriority() + ")";
        */
        long ID = theDatabase.insert(tableName,null,values);
        return ID;
    }

    public void updateExpense(long ID, expense theExpense, String budgetName)
    {
        SQLiteDatabase database = theHelper.getWritableDatabase();
        //Builds a string to update an entry in an expense table to a corresponding budget
        //It is also important to note that this methods updates ALL of the fields corresponding to Expense except of course the ID
        String command = "UPDATE " +  budgetName +
                " SET " + theHelper.COLUMN_EXPENSE_NAME + " = '" + theExpense.getName() + "'," +
                theHelper.COLUMN_EXPENSE_COST + " = " + theExpense.getCurrentExpense() + ","
                + theHelper.COLUMN_EXPENSE_MAX_COST + " = " + theExpense.getCurrentExpense() + ","
                + theHelper.COLUMN_EXPENSE_PRIORITY + " = " + theExpense.getPriority() +
                " WHERE " + theHelper.COLUMN_ID + " = " + ID;
        database.execSQL(command);
    }

    //Removes a listed budget, if it exists
    public void removeBudget(long ID)
    {
        //Thi builds a delete command to be executed
        String command = "DELETE FROM " + theHelper.BUDGET_TABLE_NAME + " WHERE " + theHelper.COLUMN_ID + " = " + ID;
        theDatabase.execSQL(command);
    }

    //Removes a listed expense, if it exists
    public void removeExpense(long ID, String tableName)
    {
        //This builds a delete command to be executed
        String command = "DELETE FROM " + tableName + " WHERE " + theHelper.COLUMN_ID + " = " + ID;
        theDatabase.execSQL(command);
    }

    public budget findBudget(long ID)
    {
        //Builds a cursor from the query where we find it by the ID number
        Cursor theCursor = theDatabase.query(theHelper.BUDGET_TABLE_NAME,null,theHelper.COLUMN_ID + " = " + ID ,null,null,null,null);
        theCursor.moveToFirst();
        //Failure budget, just in case
        budget newBudget = new budget("FAILURE10101",0);
        if (theCursor != null) {
            String budgetName = theCursor.getString(1);
            Double budgetLimit = theCursor.getDouble(2);

            newBudget = new budget(budgetName, budgetLimit);
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

    public ArrayList<budget> findAllBudgets()
    {
        //Builds a cursor that is at the top of the budget database
        ArrayList<budget> budgetList = new ArrayList<budget>();
        Cursor theCursor = theDatabase.query(theHelper.BUDGET_TABLE_NAME,null,null,null,null,null,null);
        theCursor.moveToFirst();
        //Traverse through each row
        while (theCursor.moveToNext())
        {
            //Grab material
            long budgetID = theCursor.getInt(0);
            String budgetName = theCursor.getString(1);
            Double budgetLimit = theCursor.getDouble(2);

            //Create budget
            budget newBudget = new budget(budgetName,budgetLimit);
            newBudget.setIDNumber(budgetID);
            //Add to list
            budgetList.add(newBudget);
        }
        //Close the cursor
        if ((theCursor != null) && (theCursor.isClosed() == true))
        {
            theCursor.close();
        }
        return budgetList;
    }

    public expense findExpense(long ID, String budgetName)
    {
        //Builds a cursor from the query where we find it by the ID number and the appropriate table
        Cursor theCursor = theDatabase.query(budgetName,null,theHelper.COLUMN_ID + " = " + ID,null,null,null,null);
        theCursor.moveToFirst();
        String expenseName = theCursor.getString(1);
        int expensePriority = theCursor.getInt(2);
        float expenseTotal = theCursor.getFloat(3);
        float expenseMax = theCursor.getFloat(4);

        expense newExpense = new expense(expenseName,expenseTotal,expenseMax);
        newExpense.setPriority(expensePriority);
        newExpense.setIDNumber(ID);
        return newExpense;
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
