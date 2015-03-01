package com.example.aaron.welcomeActivity;

import android.content.Context;
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
            + BUDGET_TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_BUDGET_NAME
            + " text not null);";

    public budgetDatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //Called on creation
    public void onCreate(SQLiteDatabase database)
    {
        //Carries out the creation of such a table
        database.execSQL(DATABASE_CREATE);
    }

    //Adds a subtable of expenses that corresponds to a budget
    public void addExpenseTable(String newTableName,SQLiteDatabase database)
    {
        //This is the string for execution via SQL.execSQL
        String command = "create table " + newTableName +
                "(" + COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_EXPENSE_NAME  + "text, " + COLUMN_EXPENSE_PRIORITY + "integer," +
                COLUMN_EXPENSE_COST + "real, " + COLUMN_EXPENSE_MAX_COST + "real)";
        database.execSQL(command);

    }

    //Removes an existing subtable of the expenses that corresponds to a budget
    public void removeSubTable(String tableName, SQLiteDatabase database)
    {
        String command = "DROP TABLE " + DATABASE_NAME + "." + tableName;
        database.execSQL(command);
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        //This will handle if we want to delete the database and upgrade
    }
}
