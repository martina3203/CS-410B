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
        values.put(theHelper.COLUMN_PAYMENT_INTERVAL,theBudget.getPaymentInterval());

        long ID = theDatabase.insert(theHelper.BUDGET_TABLE_NAME,null,values);
        return ID;
    }

    //Updates a Budget entry in the database
    public void updateBudget(Budget updatedBudget)
    {
        String command = "UPDATE " +  theHelper.BUDGET_TABLE_NAME +
                " SET " + theHelper.COLUMN_BUDGET_NAME + " = '" + updatedBudget.getName() + "'," +
                theHelper.COLUMN_BUDGET_LIMIT + " = " + updatedBudget.getMaxValue() + ", " +
                theHelper.COLUMN_PAYMENT_INTERVAL + " = '" + updatedBudget.getPaymentInterval() + "' " +
                " WHERE " + theHelper.COLUMN_ID + " = " + updatedBudget.getIDNumber();
        theDatabase.execSQL(command);
    }

    //Removes a listed Budget, if it exists
    public void removeBudget(long ID)
    {
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
            String budgetInterval = theCursor.getString(3);

            newBudget = new Budget(budgetName, budgetLimit);
            newBudget.setIDNumber(ID);
            newBudget.setPaymentInterval(budgetInterval);
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
            String budgetInterval = theCursor.getString(3);
            //Create Budget
            Budget newBudget = new Budget(budgetName,budgetLimit);
            newBudget.setIDNumber(budgetID);
            newBudget.setPaymentInterval(budgetInterval);
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
    public long insertExpense(Expense theExpense)
    {
        //Convert to appropriate table name
        //Build a Content Values class that contains the values of this Budget
        ContentValues values = new ContentValues();
        //Add values in
        values.put(theHelper.COLUMN_EXPENSE_NAME, theExpense.getName());
        values.put(theHelper.COLUMN_EXPENSE_COST, theExpense.getCurrentExpense());
        values.put(theHelper.COLUMN_EXPENSE_MAX_COST, theExpense.getMaxExpense());
        values.put(theHelper.COLUMN_EXPENSE_PRIORITY, theExpense.getPriority());
        values.put(theHelper.COLUMN_EXPENSE_AISLE_NUMBER, theExpense.getAisle());
        values.put(theHelper.COLUMN_PAYMENT_INTERVAL, theExpense.getPaymentInterval());
        values.put(theHelper.COLUMN_EXPENSE_BUDGET_ID_NUMBER, theExpense.getBudgetID());
        //Commit insertion
        long ID = theDatabase.insert(theHelper.EXPENSE_TABLE_NAME,null,values);
        return ID;
    }

    public void updateExpense(Expense theExpense)
    {
        //Builds a string to update an entry in an Expense table to a corresponding Budget
        //It is also important to note that this methods updates ALL of the fields corresponding to Expense except of course the ID
        //First convert to actual table name
        String command = "UPDATE " +  theHelper.EXPENSE_TABLE_NAME +
                " SET " + theHelper.COLUMN_EXPENSE_NAME + " = '" + theExpense.getName() + "'," +
                theHelper.COLUMN_EXPENSE_COST + " = " + theExpense.getCurrentExpense() + ","
                + theHelper.COLUMN_EXPENSE_MAX_COST + " = " + theExpense.getMaxExpense() + ","
                + theHelper.COLUMN_EXPENSE_PRIORITY + " = " + theExpense.getPriority() + ","
                + theHelper.COLUMN_EXPENSE_AISLE_NUMBER + " = " + theExpense.getAisle() + ","
                + theHelper.COLUMN_PAYMENT_INTERVAL + " = '" + theExpense.getPaymentInterval() +
                "' WHERE " + theHelper.COLUMN_ID + " = " + theExpense.getIDNumber();
        theDatabase.execSQL(command);
    }

    //Removes a listed Expense, if it exists
    public void removeExpense(long ID)
    {
        //This builds a delete command to be executed
        String command = "DELETE FROM " + theHelper.EXPENSE_TABLE_NAME + " WHERE " + theHelper.COLUMN_ID + " = " + ID;
        theDatabase.execSQL(command);
    }

    //Finds and returns an Expense that corresponds to that Budget and ID
    public Expense findExpense(long ID)
    {
        //Builds a cursor from the query where we find it by the ID number and the appropriate table
        Cursor theCursor = theDatabase.query(theHelper.EXPENSE_TABLE_NAME,null,theHelper.COLUMN_ID + " = " + ID,null,null,null,null);
        theCursor.moveToFirst();
        String expenseName = theCursor.getString(1);
        int expensePriority = theCursor.getInt(2);
        float expenseTotal = theCursor.getFloat(3);
        float expenseMax = theCursor.getFloat(4);
        int expenseAisle = theCursor.getInt(5);
        String expenseInterval = theCursor.getString(6);
        int expenseBudgetID = theCursor.getInt(7);

        //Create an Expense and return it with this information
        Expense newExpense = new Expense(expenseName,expenseTotal,expenseMax);
        newExpense.setPriority(expensePriority);
        newExpense.setIDNumber(ID);
        newExpense.setAisle(expenseAisle);
        newExpense.setPaymentInterval(expenseInterval);
        newExpense.setBudgetID(expenseBudgetID);
        return newExpense;
    }

    //Returns a list of expenses for a corresponding Budget in the database
    public ArrayList<Expense> findAllExpenses(long budgetID)
    {
        ArrayList<Expense> expenseList = new ArrayList<Expense>();
        //Build a cursor
        Cursor theCursor = theDatabase.query(theHelper.EXPENSE_TABLE_NAME,null,
                theHelper.COLUMN_EXPENSE_BUDGET_ID_NUMBER + " = " + budgetID,null,null,null,null);
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
            String expenseInterval = theCursor.getString(6);
            int expenseBudgetID = theCursor.getInt(7);

            //Create Expense
            Expense newExpense = new Expense(expenseName,expenseCost,expenseMaxCost);
            newExpense.setPriority(expensePriority);
            newExpense.setIDNumber(expenseID);
            newExpense.setAisle(expenseAisle);
            newExpense.setPaymentInterval(expenseInterval);
            newExpense.setBudgetID(expenseBudgetID);
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

    //Returns a list of expenses for a corresponding Budget in the database
    public ArrayList<Expense> findAllExpensesFrequency(long budgetID, String frequency)
    {
        ArrayList<Expense> expenseList = new ArrayList<Expense>();
        //Build a cursor
        Cursor theCursor = theDatabase.query(theHelper.EXPENSE_TABLE_NAME,null,
                theHelper.COLUMN_EXPENSE_BUDGET_ID_NUMBER + " = " + budgetID,null,null,null,null);
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
            String expenseInterval = theCursor.getString(6);
            int expenseBudgetID = theCursor.getInt(7);

            if (expenseInterval.matches(frequency)){
                //Create Expense
                Expense newExpense = new Expense(expenseName,expenseCost,expenseMaxCost);
                newExpense.setPriority(expensePriority);
                newExpense.setIDNumber(expenseID);
                newExpense.setAisle(expenseAisle);
                newExpense.setPaymentInterval(expenseInterval);
                newExpense.setBudgetID(expenseBudgetID);
                //Add to list
                expenseList.add(newExpense);
            }

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
    public float findTotalCost(long budgetID)
    {
        //Build a cursor
        Cursor theCursor = theDatabase.query(theHelper.EXPENSE_TABLE_NAME,null,
                theHelper.COLUMN_EXPENSE_BUDGET_ID_NUMBER + " = " + budgetID,null,null,null,null);
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

    //Call this when done with working with the database
    public void closeDatabase()
    {
        if ((theDatabase != null) && (theDatabase.isOpen()))
        {
            theDatabase.close();
        }
    }
}
