package com.example.budgetorganizer.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.TimeZone;

public class CalendarEventTask extends AsyncTask<Context, Void, Void> {
    long startMillis = 0;
    long endMillis = 0;
    // usually local calendar offered by the Android OS is 1
    long calId = 1;
    private long mDate;
    private String mgiftName;
    private String mPersonName;
    private Context mContext;
    public CalendarEventTask(Context context, String mPersonName, String mgiftName, long date){
        mContext = context;
        this.mgiftName = mgiftName;
        this.mPersonName = mPersonName;
        this.mDate = date;
    }
    @Override
    protected Void doInBackground(Context... contexts) {
        try{
            TimeZone aDefault = TimeZone.getDefault();
//            Calendar beginTime = Calendar.getInstance(aDefault);
//            Calendar endTime = Calendar.getInstance(aDefault);
//            beginTime.clear();
//            beginTime.set(2021, 4, 14);
//            startMillis = beginTime.getTimeInMillis();
//            endTime.clear();
//            endTime.set(2021, 4, 14);
//            endMillis = endTime.getTimeInMillis();
//            startMillis = beginTime.getTimeInMillis();
            endMillis = mDate;
            startMillis = mDate;
            ContentResolver cr = mContext.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, mgiftName);
            values.put(CalendarContract.Events.ALL_DAY, 1);
            values.put(CalendarContract.Events.DESCRIPTION, mgiftName+" - "+mPersonName);
            values.put(CalendarContract.Events.CALENDAR_ID, calId);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, aDefault.getID());
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            Log.v("CalendarEventTask.java",String.valueOf(uri));
            Log.v("CalendarEventTask.java",aDefault.getID());
        }catch (Exception e){
            Log.v("CalendarEventTask.java", String.valueOf(e));
        }

        return  null;
    }
}
