package com.example.budgetorganizer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.database.DatabaseUtils.dumpCursorToString;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_budget.db";
    // we should increment this on each database push
    // so we can append a new column to the table by just running the onUpgrade()
    private static final int DATABASE_VERSION = 1;
    public DbHelper(Context context) {
        // null for cursor factory
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PERSON_TABLE = "CREATE TABLE " + PersonContract.PersonEntry.TABLE_NAME + " ("+
                PersonContract.PersonEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                PersonContract.PersonEntry.COLUMN_PERSON_NAME+ " TEXT NOT NULL, "+
                PersonContract.PersonEntry.COLUMN_BUDGET + " INTEGER NOT NULL "+
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_PERSON_TABLE);
        final String SQL_CREATE_GIFT_TABLE =  "CREATE TABLE " + GiftContract.GiftEntry.TABLE_NAME + " ("+
                GiftContract.GiftEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                GiftContract.GiftEntry.COLUMN_GIFT_NAME_NAME + " TEXT NOT NULL, "+
                GiftContract.GiftEntry.COLUMN_PRICE_NAME + " INTEGER NOT NULL, "+
                GiftContract.GiftEntry.COLUMN_DATE_NAME + " TEXT NOT NULL, "+
                GiftContract.GiftEntry.COLUMN_PHOTOPATH_NAME + " TEXT , "+
                GiftContract.GiftEntry.COLUMN_PERSON_ID_NAME+ " INTEGER  "+
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_GIFT_TABLE);


    }
    public static ArrayList<Person> getPersonsWithGiftBought(SQLiteDatabase db){
        String selectQuery = "SELECT Person.*, COUNT(Gift._id) AS TotalCount FROM Person  LEFT JOIN Gift ON Person._id = Gift.PersonID group by Person._id ;";

        ArrayList<Person> array_list = new ArrayList<>();
        Cursor res = db.rawQuery( selectQuery , null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            Person person = new Person();
            person.setId(res.getLong(res.getColumnIndex(PersonContract.PersonEntry._ID)));
            person.setName(res.getString(res.getColumnIndex(PersonContract.PersonEntry.COLUMN_PERSON_NAME)));
            person.setBudget(res.getInt(res.getColumnIndex(PersonContract.PersonEntry.COLUMN_BUDGET)));
            person.setTotalGifBought(res.getInt(res.getColumnIndex("TotalCount")));
            array_list.add(person);
            Log.v("Person",dumpCursorToString(res));
            res.moveToNext();
        }
        return array_list;
    }
    public static long insertPerson(SQLiteDatabase db, Person p){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonContract.PersonEntry.COLUMN_PERSON_NAME, p.getName());
        contentValues.put(PersonContract.PersonEntry.COLUMN_BUDGET, p.getBudget());
        return db.insert(PersonContract.PersonEntry.TABLE_NAME, null, contentValues);
    }
    public static boolean updatePerson(SQLiteDatabase db, Person p){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonContract.PersonEntry.COLUMN_PERSON_NAME, p.getName());
        contentValues.put(PersonContract.PersonEntry.COLUMN_BUDGET, p.getBudget());
        String id = String.valueOf(p.getId());
        return db.update(PersonContract.PersonEntry.TABLE_NAME, contentValues, PersonContract.PersonEntry._ID + "=?", new String[]{id}) > 0;
    }
    public static int removePerson(SQLiteDatabase db, Person p){
        return db.delete(PersonContract.PersonEntry.TABLE_NAME , PersonContract.PersonEntry._ID + "="+p.getId() , null);
    }



    public static ArrayList<Gift> getPersonsGift(SQLiteDatabase db, long person_id){
        String id = String.valueOf(person_id);
        String selectQuery = "SELECT *  FROM Gift  WHERE PersonID="+id+" ;";
        ArrayList<Gift> array_list = new ArrayList<>();
        Cursor res = db.rawQuery( selectQuery , null );
        Log.v("getPersonsGift",selectQuery);
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            Gift gift = new Gift();
            gift.setId(res.getLong(res.getColumnIndex(GiftContract.GiftEntry._ID)));
            gift.setName(res.getString(res.getColumnIndex(GiftContract.GiftEntry.COLUMN_GIFT_NAME_NAME)));
            gift.setPersonId(res.getLong(res.getColumnIndex(GiftContract.GiftEntry.COLUMN_PERSON_ID_NAME)));
            gift.setPhotoPath(res.getString(res.getColumnIndex(GiftContract.GiftEntry.COLUMN_PHOTOPATH_NAME)));
            gift.setDate(res.getString(res.getColumnIndex(GiftContract.GiftEntry.COLUMN_DATE_NAME)));
            gift.setPrice(res.getInt(res.getColumnIndex(GiftContract.GiftEntry.COLUMN_PRICE_NAME)));
            array_list.add(gift);
            Log.v("Gift",dumpCursorToString(res));
            res.moveToNext();
        }
        return array_list;
    }
    public static long insertGift(SQLiteDatabase db, Gift g){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GiftContract.GiftEntry.COLUMN_GIFT_NAME_NAME, g.getName());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PERSON_ID_NAME, g.getPersonId());
        contentValues.put(GiftContract.GiftEntry.COLUMN_DATE_NAME, g.getDate());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PHOTOPATH_NAME, g.getPhotoPath());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PRICE_NAME, g.getPrice());
        return db.insert(GiftContract.GiftEntry.TABLE_NAME, null, contentValues);
    }
    public static boolean updateGift(SQLiteDatabase db, Gift g){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GiftContract.GiftEntry.COLUMN_GIFT_NAME_NAME, g.getName());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PERSON_ID_NAME, g.getPersonId());
        contentValues.put(GiftContract.GiftEntry.COLUMN_DATE_NAME, g.getDate());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PHOTOPATH_NAME, g.getPhotoPath());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PRICE_NAME, g.getPrice());
        String id = String.valueOf(g.getId());
        return db.update(GiftContract.GiftEntry.TABLE_NAME, contentValues, GiftContract.GiftEntry._ID + "=?", new String[]{id}) > 0;
    }
    public static int removeGift(SQLiteDatabase db, Gift g){
        return db.delete(PersonContract.PersonEntry.TABLE_NAME , GiftContract.GiftEntry._ID + "="+g.getId() , null);
    }
    public static Person getPerson(SQLiteDatabase db, long person_id){
        String id = String.valueOf(person_id);
        Person p = new Person();
        String selectQuery = "SELECT *  FROM Person  WHERE _id="+id+" ;";
        Cursor res = db.rawQuery( selectQuery , null );
        res.moveToFirst();
        p.setId(res.getLong(res.getColumnIndex(PersonContract.PersonEntry._ID)));
        p.setName(res.getString(res.getColumnIndex(PersonContract.PersonEntry.COLUMN_PERSON_NAME)));
        p.setBudget(res.getInt(res.getColumnIndex(PersonContract.PersonEntry.COLUMN_BUDGET)));
        Log.v("SinglePerson",dumpCursorToString(res));
        return p;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ GiftContract.GiftEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ PersonContract.PersonEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
