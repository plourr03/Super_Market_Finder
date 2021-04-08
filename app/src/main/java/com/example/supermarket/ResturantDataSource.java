package com.example.supermarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class ResturantDataSource {
    private SQLiteDatabase database;
    private ResturantDBHelper dbHelper;

    public ResturantDataSource(Context context){dbHelper = new ResturantDBHelper(context);}
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public ArrayList<Resturant> getRestaurants(){
        ArrayList<Resturant> Restaurants = new ArrayList<Resturant>();
        try{
            String query = "SELECT * FROM RESTAURANT ORDER BY resturantname COLLATE NOCASE ASC";
            Cursor cursor = database.rawQuery(query,null);

            Resturant newRestaurant;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                newRestaurant = new Resturant();
                newRestaurant.setResturantID(cursor.getInt(0));
                newRestaurant.setResturantName(cursor.getString(1));
                newRestaurant.setStreetAddress(cursor.getString(2));
                newRestaurant.setCity(cursor.getString(3));
                newRestaurant.setState(cursor.getString(4));
                newRestaurant.setZipCode(cursor.getString(5));
                newRestaurant.setLiquerRating(cursor.getFloat(6));
                newRestaurant.setProduceRating(cursor.getFloat(7));
                newRestaurant.setCheeseRating((cursor.getFloat(8)));

                Restaurants.add(newRestaurant);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e){
            Restaurants = new ArrayList<Resturant>();
        }
        return Restaurants;
    }
    public Resturant getSpecificRestaurant(int restaurantId){
        Resturant resturant = new Resturant();
        String query = "SELECT * FROM RESTAURANT WHERE _id ="+restaurantId;
        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            resturant.setResturantID(cursor.getInt(0));
            resturant.setResturantName(cursor.getString(1));
            resturant.setStreetAddress(cursor.getString(2));
            resturant.setCity(cursor.getString(3));
            resturant.setState(cursor.getString(4));
            resturant.setZipCode(cursor.getString(5));


            cursor.close();
        }
        return resturant;
    }

    public boolean deleteResturant(int resturantId){
        boolean didDelete = false;
        try{
            didDelete = database.delete("RESTAURANT","_id="+resturantId,null)>0;
        }
        catch (Exception e){
            //Do nothing -return value already set to false
        }
        return didDelete;
    }
    public boolean updateRestaurant (Resturant r){
        boolean didSucceed = false;
        try{
            Long rowId = (long) r.getResturantID();
            ContentValues UpdateValues = new ContentValues();

            UpdateValues.put("resturantname",r.getResturantName());
            UpdateValues.put("streetaddress",r.getStreetAddress());
            UpdateValues.put("city",r.getCity());
            UpdateValues.put("state",r.getState());
            UpdateValues.put("zipcode",r.getZipCode());
            UpdateValues.put("liquer",r.getLiquerRating());
            UpdateValues.put("produce",r.getProduceRating());
            UpdateValues.put("cheese",r.getCheeseRating());


            didSucceed = database.update("RESTAURANT",UpdateValues,"_id=" + rowId ,null)>0;
        }
        catch(Exception e){

        }

        return didSucceed;
    }
    public boolean insertResturant (Resturant r){
        boolean didSucceed = false;
        try{
            ContentValues initialValues = new ContentValues();

            initialValues.put("resturantname",r.getResturantName());
            initialValues.put("streetaddress",r.getStreetAddress());
            initialValues.put("city",r.getCity());
            initialValues.put("state",r.getState());
            initialValues.put("zipcode",r.getZipCode());



            didSucceed = database.insert("RESTAURANT",null,initialValues)>0;
        }
        catch(Exception e){

        }

        return didSucceed;
    }
    public int getLastRestaurantID(){
        int lastID;
        try{
            String query = "Select Max(_id) from RESTAURANT";
            Cursor cursor = database.rawQuery(query,null);

            cursor.moveToFirst();
            lastID = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e){
            lastID = -1;
        }
        return lastID;
    }
    public ArrayList<String> getRestaurantName(){
        ArrayList<String> estaurantNames = new ArrayList<>();
        try{
            String query = "SELECT estaurantname FROM RESTAURANT";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                estaurantNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e){
            estaurantNames = new ArrayList<String>();
        }

        return estaurantNames;
    }
}
