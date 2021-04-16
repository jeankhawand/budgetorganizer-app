package com.example.budgetorganizer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}