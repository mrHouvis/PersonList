package com.example.personlist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {

        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Constants.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);

    }

    //insert information
    public long insertInfo(String name, String age, String phone, byte[] image, String addTimeStamp, String updateTimeStamp){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.C_NAME, name);
        values.put(Constants.C_AGE, age);
        values.put(Constants.C_PHONE, phone);
        values.put(Constants.C_IMAGE, image);
        values.put(Constants.C_ADD_TIMESTAMP, addTimeStamp);
        values.put(Constants.C_UPDATE_TIMESTAMP, updateTimeStamp);

        long id = db.insert(Constants.TABLE_NAME, null, values);
        db.close();
        return id;

    }

    //update information
    public void updateInfo(String id, String name, String age, String phone, byte[] image, String addTimeStamp, String updateTimeStamp){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.C_NAME, name);
        values.put(Constants.C_AGE, age);
        values.put(Constants.C_PHONE, phone);
        values.put(Constants.C_IMAGE, image);
        values.put(Constants.C_ADD_TIMESTAMP, addTimeStamp);
        values.put(Constants.C_UPDATE_TIMESTAMP, updateTimeStamp);

        db.update(Constants.TABLE_NAME, values, Constants.C_ID + " = ? ", new String[]{id});
        db.close();

    }

    //delete information
    public void deleteInfo(String id){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.C_ID + " = ? ", new String[]{id});
        db.close();

    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.TABLE_NAME);
    }

    public ArrayList<Model> searchInfo(String title){

        ArrayList<Model> arrayList = new ArrayList<>();

        //selected all
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_NAME + " = '" + title +"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if(cursor.moveToNext()){

            do{

                @SuppressLint("Range") Model model = new Model(
                        "" + cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        cursor.getBlob(cursor.getColumnIndex(Constants.C_IMAGE)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_NAME)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_AGE)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_PHONE)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_ADD_TIMESTAMP)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_UPDATE_TIMESTAMP))
                );

                arrayList.add(model);

            } while (cursor.moveToNext());

        }

        db.close();
        return arrayList;

    }

    public ArrayList<Model> getAllData(String orderBy){

        ArrayList<Model> arrayList = new ArrayList<>();

        //selected all
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if(cursor.moveToNext()){

            do{

                @SuppressLint("Range") Model model = new Model(
                        "" + cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        cursor.getBlob(cursor.getColumnIndex(Constants.C_IMAGE)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_NAME)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_AGE)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_PHONE)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_ADD_TIMESTAMP)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_UPDATE_TIMESTAMP))
                );

                arrayList.add(model);

            } while (cursor.moveToNext());

        }

        db.close();
        return arrayList;

    }

}
