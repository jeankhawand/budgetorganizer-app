package com.example.budgetorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GiftActivity extends AppCompatActivity implements ListItemClickListener {
    private long person_id;
    private RecyclerView mGiftRecycleView;
    private GiftAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);
        mGiftRecycleView = findViewById(R.id.rv_gift_list);
        Intent intent = getIntent();
        person_id = intent.getLongExtra("person_id",0);
        mGiftRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GiftAdapter(this, this,person_id);
        mGiftRecycleView.setAdapter(mAdapter);

    }
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    public void add(MenuItem item) {

        Intent myIntent = new Intent(this, AddGiftActivity.class);
        myIntent.putExtra("person_id",person_id);
        GiftActivity.this.startActivity(myIntent);
    }
    @Override
    public void onListItemClick(View view, int clickedItemIndex) {
        long id = (long) view.getTag();
        Log.v("ClickedGift", String.valueOf(id));
    }
}