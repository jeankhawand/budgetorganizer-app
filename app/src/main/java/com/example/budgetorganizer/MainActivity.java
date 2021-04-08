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

public class MainActivity extends AppCompatActivity implements ListItemClickListener {
    private PersonAdapter mAdapter;
    private RecyclerView mPersonRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPersonRecyclerView = findViewById(R.id.rv_person_list);
        mPersonRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PersonAdapter(this, this);
        mPersonRecyclerView.setAdapter(mAdapter);
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
        Intent myIntent = new Intent(this, AddPersonActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    public void onListItemClick(View view, int clickedItemIndex) {
        long id = (long) view.getTag();
        Log.v("ClickedPerson", String.valueOf(id));
        Intent myIntent = new Intent(this, GiftActivity.class);
        myIntent.putExtra("person_id",id);
        MainActivity.this.startActivity(myIntent);
    }
}