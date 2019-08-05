package edu.wit.mobileapp.wentwealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Wealth.db";
    public static final String TABLE_NAME = "balances_table";
    public static final String COL_1 = "BUDGET";
    public static final String COL_2 = "SAVINGS";
    public static final String COL_3 = "TOTAL";
    public static final String COL_4 = "ROLLOVER";
    public static final String COL_5 = "WISHLIST";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (BUDGET TEXT,SAVINGS TEXT,TOTAL TEXT,ROLLOVER TEXT,WISHLIST TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String budget, String savings, String total, String rollover, String wishlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, budget);
        contentValues.put(COL_2, savings);
        contentValues.put(COL_3, total);
        contentValues.put(COL_4, rollover);
        contentValues.put(COL_5, wishlist);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }
}
