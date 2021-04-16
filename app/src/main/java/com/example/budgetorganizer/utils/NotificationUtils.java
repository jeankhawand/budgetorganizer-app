package com.example.budgetorganizer.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.budgetorganizer.MainActivity;
import com.example.budgetorganizer.R;

public class NotificationUtils {
    private static final int GIFT_REMINDER_PENDING_INTENT_ID= 2304;
    private static final int GIFT_REMINDER_NOTIFICATION_ID= 2704;
    private static final String GIFT_REMINDER_NOTIFICATION_CHANNEL_ID= "reminder_gift_channel";
    private static PendingIntent contentIntent(Context context){
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        // FLAG_UPDATE_CURRENT used to keep this pendingintent updating itself without restarting it

        return PendingIntent.getActivity(context,GIFT_REMINDER_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }
    public static void remindUpcommingGift(Context context){
        // calling the system notificationservice
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // creating a channel
        // you will face issue cause notification manager was offered for android oreo +
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(GIFT_REMINDER_NOTIFICATION_CHANNEL_ID,context.getString(R.string.notification_channel_name),NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
//        other wise we will have to build notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, GIFT_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context,R.color.design_default_color_primary))
                .setSmallIcon(R.drawable.gift_black)
                .setLargeIcon(NotificationIcon(context))
                .setContentTitle(context.getString(R.string.gift_reminder_notification_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.gift_reminder_notification_title)))
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);



        notificationManager.notify(GIFT_REMINDER_NOTIFICATION_ID, notificationBuilder.build());

    }
    public static Bitmap NotificationIcon(Context context){
        Resources resources = context.getResources();
        Bitmap notificationIcon = BitmapFactory.decodeResource(resources, R.drawable.gift_black);
        return notificationIcon;
    }
}
