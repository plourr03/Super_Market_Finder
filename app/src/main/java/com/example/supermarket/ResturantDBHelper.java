package com.example.supermarket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ResturantDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myresturant.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_RESTAURANT = "CREATE TABLE RESTAURANT (_ID Integer primary key autoincrement, resturantname text not null, streetaddress text,city text, state text, zipcode text, liquer float,produce float, cheese float);";
    public ResturantDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_TABLE_RESTAURANT); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
