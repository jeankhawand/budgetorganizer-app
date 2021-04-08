package com.example.budgetorganizer;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetorganizer.data.DbHelper;
import com.example.budgetorganizer.data.Person;

public class AddPersonActivity extends AppCompatActivity {
    private SQLiteDatabase mDb;
    EditText mPersonNameEditText, mBudgetEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        DbHelper dbHelper = new DbHelper(this);
        mPersonNameEditText = findViewById(R.id.ed_person_name);
        mBudgetEditText = findViewById(R.id.ed_budget);
        mDb = dbHelper.getWritableDatabase();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    public void add(MenuItem item) {
        if(mBudgetEditText.getText().toString().matches("") || mPersonNameEditText.getText().toString().matches("")

        ){
            Context context = getApplicationContext();
            CharSequence text = "Person Name Or Budget aren't valid";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            Person person = new Person();
            person.setName(mPersonNameEditText.getText().toString());
            person.setBudget(Integer.parseInt(mBudgetEditText.getText().toString()));
            long personid = DbHelper.insertPerson(mDb, person);
            Context context = getApplicationContext();
            CharSequence text = mPersonNameEditText.getText().toString()+" Added To The List !";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent myIntent = new Intent(this, AddGiftActivity.class);
            myIntent.putExtra("person_id",personid);
            AddPersonActivity.this.startActivity(myIntent);

        }


    }


}