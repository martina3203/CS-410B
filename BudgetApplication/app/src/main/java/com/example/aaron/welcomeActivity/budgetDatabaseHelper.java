package com.example.aaron.welcomeActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aaron on 3/1/2015.
 */
public class budgetDatabaseHelper extends SQLiteOpenHelper {
    //This is the name of the table
    public static final String BUDGET_TABLE_NAME = "BUDGET_TABLE";

    //These are the names of the Columns.
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_BUDGET_NAME = "BUDGET_NAME";
    public static final String COLUMN_BUDGET_LIMIT = "BUDGET_LIMIT";

    //Name of the main database
    private static final String DATABASE_NAME = "budget.db";
    private static final int DATABASE_VERSION = 1;

    //These are the names of the columns for our subtables that include expenses
    private static final String COLUMN_EXPENSE_NAME = "EXPENSE_NAME";
    private static final String COLUMN_EXPENSE_PRIORITY = "EXPENSE_PRIORITY";
    private static final String COLUMN_EXPENSE_COST = "EXPENSE_COST";
    private static final String COLUMN_EXPENSE_MAX_COST = "EXPENSE_MAX_COST";

    //This is the command that is executed to create a database
    //The formatting is basically like this: create table TABLE_NAME ( COLUMN_NAME DATA_TYPE, ...);
    //Accepted Data types are Text, Integers, Reals.
    private static final String DATABASE_CREATE = "create table "
            + BUDGET_TABLE_NAME + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_BUDGET_NAME + " text, " +
            COLUMN_BUDGET_LIMIT + " real);";

    public budgetDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Called on creation
    @Override
    public void onCreate(SQLiteDatabase database) {
        //Carries out the creation of such a table
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        //This will handle if we want to delete the database and upgrade accordingly
    }

    //Adds a subtable of expenses that corresponds to a budget
    public void addExpenseTable(String newTableName) {
        //This is the string for execution via SQL.execSQL
        SQLiteDatabase database = this.getWritableDatabase();
        String command = "create table " + newTableName +
                "(" + COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_EXPENSE_NAME + "text, " + COLUMN_EXPENSE_PRIORITY + "integer," +
                COLUMN_EXPENSE_COST + "real, " + COLUMN_EXPENSE_MAX_COST + "real)";
        database.execSQL(command);
    }

    //Removes an existing subtable of the expenses that corresponds to a budget
    public void removeSubTable(String tableName) {
        SQLiteDatabase database = this.getWritableDatabase();
        String command = "DROP TABLE " + tableName;
        database.execSQL(command);
    }

    //Adds a budget to the table, returns ID number
    public long insertBudget(budget theBudget)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        //Build a Content Values class that contains the values of this budget
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUDGET_NAME, theBudget.getName());
        values.put(COLUMN_BUDGET_LIMIT, theBudget.getMaxValue());

        //Insert it into the database, this will also return the ID number, in case we decide to use it
        long ID = database.insert(BUDGET_TABLE_NAME,null,values);
        return ID;
    }

    public void updateExpense(long ID, expense theExpense, String budgetName)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        //Builds a string to update an entry in an expense table to a corresponding budget
        //It is also important to note that this methods updates ALL of the fields corresonding to Expense except of course the ID
        String command = "UPDATE " +  budgetName +
                " SET " + COLUMN_EXPENSE_NAME + " = '" + theExpense.getName() + "'," +
                COLUMN_EXPENSE_COST + " = " + theExpense.getCurrentExpense() + ","
                + COLUMN_EXPENSE_MAX_COST + " = " + theExpense.getCurrentExpense() + ","
                + COLUMN_EXPENSE_PRIORITY + " = " + theExpense.getPriority() +
                " WHERE " + COLUMN_ID + " = " + ID;
        database.execSQL(command);
    }

    //Adds an expense to the corresponding expense
    public long insertExpense(expense theExpense, String tableName)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        //Build a Content Values class that contains the values of this budget
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_NAME, theExpense.getName());
        values.put(COLUMN_EXPENSE_COST, theExpense.getCurrentExpense());
        values.put(COLUMN_EXPENSE_MAX_COST, theExpense.getMaxExpense());
        values.put(COLUMN_EXPENSE_PRIORITY, theExpense.getPriority());
        /*
        Equivalent SQL statement
        String command = "INSERT INTO " + tableName +
                COLUMN_EXPENSE_NAME +"," + COLUMN_EXPENSE_COST + "," + COLUMN_EXPENSE_MAX_COST + "," + COLUMN_EXPENSE_PRIORITY + ")" +
                "VALUES (" + theExpense.getName() + "," + theExpense.getCurrentExpense() + "," + theExpense.getMaxExpense() + "," + theExpense.getPriority() + ")";
        */
        long ID = database.insert(tableName,null,values);
        return ID;
    }

    //Removes a listed budget, if it exists
    public void removeBudget(long ID)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        //Thi builds a delete command to be executed
        String command = "DELETE FROM " + BUDGET_TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ID;
        database.execSQL(command);
    }

    //Removes a listed expense, if it exists
    public void removeExpense(long ID, String tableName)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        //This builds a delete command to be executed
        String command = "DELETE FROM " + tableName + " WHERE " + COLUMN_ID + " = " + ID;
        database.execSQL(command);
    }

    public budget findBudget(long ID)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        //Builds a cursor from the query where we find it by the ID number
        Cursor theCursor = database.query(BUDGET_TABLE_NAME,null,COLUMN_ID + " = " + ID,null,null,null,null);
        String budgetName = theCursor.getString(1);
        Double budgetLimit = theCursor.getDouble(2);

        budget newBudget = new budget(budgetName,budgetLimit);
        newBudget.setIDNumber(ID);
        return newBudget;
    }

    public expense findExpense(long ID, String budgetName)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        //Builds a cursor from the query where we find it by the ID number and the appropriate table
        Cursor theCursor = database.query(budgetName,null,COLUMN_ID + " = " + ID,null,null,null,null);
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
        SQLiteDatabase database = this.getWritableDatabase();
        if ((database != null) && (database.isOpen()))
        {
            database.close();
        }
    }
}
