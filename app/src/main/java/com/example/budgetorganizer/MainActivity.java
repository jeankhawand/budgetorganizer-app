package com.example.budgetorganizer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetorganizer.data.DbHelper;
import com.example.budgetorganizer.utils.NotificationUtils;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {
    private PersonAdapter mAdapter;
    private RecyclerView mPersonRecyclerView;
    final int callbackId = 42;
    private IntentFilter mChargingIntentFilter;
    private IntentFilter mWifiIntentFilter;
    private  WifiStateReceiver mWifiStateReceiver;
    private ChargingBroadcastReceiver mChargingBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChargingIntentFilter = new IntentFilter();
        mChargingBroadcastReceiver = new ChargingBroadcastReceiver();
        mWifiStateReceiver = new WifiStateReceiver();
        mWifiIntentFilter = new IntentFilter();
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        mWifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mWifiIntentFilter.addAction(CONNECTIVITY_SERVICE);
        mPersonRecyclerView = findViewById(R.id.rv_person_list);
        mPersonRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PersonAdapter(this, this);
        mPersonRecyclerView.setAdapter(mAdapter);
        mAdapter.swapData(DbHelper.getPersonsWithGiftBought(mAdapter.db));
        checkPermission(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
    }
    private void checkPermission(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.swapData(DbHelper.getPersonsWithGiftBought(mAdapter.db));
        registerReceiver(mChargingBroadcastReceiver, mChargingIntentFilter);
        registerReceiver(mWifiStateReceiver, mWifiIntentFilter);


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mChargingBroadcastReceiver);
        unregisterReceiver(mWifiStateReceiver);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.swapData(DbHelper.getPersonsWithGiftBought(mAdapter.db));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    public void add(MenuItem item) {
        Intent myIntent = new Intent(this, AddPersonActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    public void onListItemClick(View view, int clickedItemIndex) {
        long id = (long) view.getTag();
        Log.v("MainActivity.java", String.valueOf(id));
        Intent myIntent = new Intent(this, GiftActivity.class);
        myIntent.putExtra("person_id",id);
        MainActivity.this.startActivity(myIntent);
    }

    public void testNotification(MenuItem item) {
        NotificationUtils.remindUpcommingGift(this);
    }
    private void detectChargingStatus(boolean isCharging){
        if (isCharging){
           Toast toast = Toast.makeText(this, "Phone is Charging", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            Toast toast = Toast.makeText(this, "Phone isn't Charging", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void detectWifiStatus(int WifiState){
        Toast toast;
        switch(WifiState) {
            case WifiManager.WIFI_STATE_DISABLED:
                //do something
                toast = Toast.makeText(this, "Wifi Disabled", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                toast = Toast.makeText(this, "Wifi Enabled", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                toast = Toast.makeText(this, "Wifi Enabling", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                toast = Toast.makeText(this, "Wifi Disabling", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                toast = Toast.makeText(this, "Check Hardware ", Toast.LENGTH_SHORT);
                toast.show();
                break;

        }

    }
    public class ChargingBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isCharging = (action.equals(Intent.ACTION_POWER_CONNECTED));
            detectChargingStatus(isCharging);
        }

    }
    public class WifiStateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

            detectWifiStatus(extraWifiState);

        }
    }

}