package com.example.aaron.welcomeActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Aaron on 3/1/2015.
 */
public class budgetDatabaseHelper extends SQLiteOpenHelper {
    //This is the name of the table
    public static final String BUDGET_TABLE_NAME = "BUDGET_TABLE";
    public static final String EXPENSE_TABLE_NAME = "EXPENSE_TABLE";

    //These are the names of the Columns.
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_BUDGET_NAME = "BUDGET_NAME";
    public static final String COLUMN_BUDGET_LIMIT = "BUDGET_LIMIT";
    public static final String COLUMN_PAYMENT_INTERVAL = "PAYMENT_INTERVAL";

    //Name of the main database
    private static final String DATABASE_NAME = "Budget.db";
    private static final int DATABASE_VERSION = 1;

    //These are the names of the columns for our subtables that include expenses
    public static final String COLUMN_EXPENSE_NAME = "EXPENSE_NAME";
    public static final String COLUMN_EXPENSE_PRIORITY = "EXPENSE_PRIORITY";
    public static final String COLUMN_EXPENSE_COST = "EXPENSE_COST";
    public static final String COLUMN_EXPENSE_MAX_COST = "EXPENSE_MAX_COST";
    public static final String COLUMN_EXPENSE_AISLE_NUMBER = "EXPENSE_AISLE_NUMBER";
    public static final String COLUMN_EXPENSE_BUDGET_ID_NUMBER = "EXPENSE_BUDGET_ID_NUMBER";

    //This is the command that is executed to create a database
    //The formatting is basically like this: create table TABLE_NAME ( COLUMN_NAME DATA_TYPE, ...);
    //Accepted Data types are Text, Integers, Reals.
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + BUDGET_TABLE_NAME + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_BUDGET_NAME + " text, " +
            COLUMN_BUDGET_LIMIT + " real, " +
            COLUMN_PAYMENT_INTERVAL + " text)";

    private static final String EXPENSE_TABLE_CREATE = "CREATE TABLE " + EXPENSE_TABLE_NAME +
            " (" + COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_EXPENSE_NAME + " text, " + COLUMN_EXPENSE_PRIORITY + " integer," +
            COLUMN_EXPENSE_COST + " real, " + COLUMN_EXPENSE_MAX_COST + " real," +
            COLUMN_EXPENSE_AISLE_NUMBER + " integer, " + COLUMN_PAYMENT_INTERVAL +
            " text, " + COLUMN_EXPENSE_BUDGET_ID_NUMBER + " integer)";

    public budgetDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Called on creation
    @Override
    public void onCreate(SQLiteDatabase database) {
        //Carries out the creation of such a table
        database.execSQL(DATABASE_CREATE);
        database.execSQL(EXPENSE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        //This will handle if we want to delete the database and upgrade accordingly

    }

}
