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
        Cursor cursor = db.rawQuery( selectQuery , null );
        while(cursor.moveToNext()) {
            Person person = new Person();
            person.setId(cursor.getLong(cursor.getColumnIndex(PersonContract.PersonEntry._ID)));
            person.setName(cursor.getString(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_PERSON_NAME)));
            person.setBudget(cursor.getInt(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_BUDGET)));
            person.setTotalGifBought(cursor.getInt(cursor.getColumnIndex("TotalCount")));
            array_list.add(person);
            Log.v("DbHelper.java",dumpCursorToString(cursor));
        }
        cursor.close();
        return array_list;
    }
    public static long insertPerson(SQLiteDatabase db, Person person){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonContract.PersonEntry.COLUMN_PERSON_NAME, person.getName());
        contentValues.put(PersonContract.PersonEntry.COLUMN_BUDGET, person.getBudget());
        return db.insert(PersonContract.PersonEntry.TABLE_NAME, null, contentValues);
    }
    public static boolean updatePerson(SQLiteDatabase db, Person person){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonContract.PersonEntry.COLUMN_PERSON_NAME, person.getName());
        contentValues.put(PersonContract.PersonEntry.COLUMN_BUDGET, person.getBudget());
        String id = String.valueOf(person.getId());
        return db.update(PersonContract.PersonEntry.TABLE_NAME, contentValues, PersonContract.PersonEntry._ID + "=?", new String[]{id}) > 0;
    }
    public static int removePerson(SQLiteDatabase db, Person p){
        return db.delete(PersonContract.PersonEntry.TABLE_NAME , PersonContract.PersonEntry._ID + "="+p.getId() , null);
    }



    public static ArrayList<Gift> getPersonsGift(SQLiteDatabase db, long person_id){
        String id = String.valueOf(person_id);
        String selectQuery = "SELECT *  FROM Gift  WHERE PersonID="+id+" ;";
        ArrayList<Gift> array_list = new ArrayList<>();
        Cursor cursor = db.rawQuery( selectQuery , null );
        Log.v("DbHelper.java",selectQuery);
        while(cursor.moveToNext()) {
            Gift gift = new Gift();
            gift.setId(cursor.getLong(cursor.getColumnIndex(GiftContract.GiftEntry._ID)));
            gift.setName(cursor.getString(cursor.getColumnIndex(GiftContract.GiftEntry.COLUMN_GIFT_NAME_NAME)));
            gift.setPersonId(cursor.getLong(cursor.getColumnIndex(GiftContract.GiftEntry.COLUMN_PERSON_ID_NAME)));
            gift.setPhotoPath(cursor.getString(cursor.getColumnIndex(GiftContract.GiftEntry.COLUMN_PHOTOPATH_NAME)));
            gift.setDate(cursor.getString(cursor.getColumnIndex(GiftContract.GiftEntry.COLUMN_DATE_NAME)));
            gift.setPrice(cursor.getInt(cursor.getColumnIndex(GiftContract.GiftEntry.COLUMN_PRICE_NAME)));
            array_list.add(gift);
            Log.v("DbHelper.java",dumpCursorToString(cursor));
        }
        cursor.close();
        return array_list;
    }
    public static long insertGift(SQLiteDatabase db, Gift gift){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GiftContract.GiftEntry.COLUMN_GIFT_NAME_NAME, gift.getName());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PERSON_ID_NAME, gift.getPersonId());
        contentValues.put(GiftContract.GiftEntry.COLUMN_DATE_NAME, gift.getDate());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PHOTOPATH_NAME, gift.getPhotoPath());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PRICE_NAME, gift.getPrice());
        return db.insert(GiftContract.GiftEntry.TABLE_NAME, null, contentValues);
    }
    public static boolean updateGift(SQLiteDatabase db, Gift gift){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GiftContract.GiftEntry.COLUMN_GIFT_NAME_NAME, gift.getName());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PERSON_ID_NAME, gift.getPersonId());
        contentValues.put(GiftContract.GiftEntry.COLUMN_DATE_NAME, gift.getDate());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PHOTOPATH_NAME, gift.getPhotoPath());
        contentValues.put(GiftContract.GiftEntry.COLUMN_PRICE_NAME, gift.getPrice());
        String id = String.valueOf(gift.getId());
        return db.update(GiftContract.GiftEntry.TABLE_NAME, contentValues, GiftContract.GiftEntry._ID + "=?", new String[]{id}) > 0;
    }
    public static int removeGift(SQLiteDatabase db, Gift gift){
        return db.delete(PersonContract.PersonEntry.TABLE_NAME , GiftContract.GiftEntry._ID + "="+gift.getId() , null);
    }
    public static Person getPerson(SQLiteDatabase db, long person_id){
        String id = String.valueOf(person_id);
        Person person = new Person();
        String selectQuery = "SELECT *  FROM Person  WHERE _id="+id+" ;";
        Cursor cursor = db.rawQuery( selectQuery , null );
        cursor.moveToFirst();
        person.setId(cursor.getLong(cursor.getColumnIndex(PersonContract.PersonEntry._ID)));
        person.setName(cursor.getString(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_PERSON_NAME)));
        person.setBudget(cursor.getInt(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_BUDGET)));
        Log.v("DbHelper.java",dumpCursorToString(cursor));
        return person;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ GiftContract.GiftEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ PersonContract.PersonEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
