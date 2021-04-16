package com.example.budgetorganizer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetorganizer.data.DbHelper;
import com.example.budgetorganizer.data.Gift;
import com.example.budgetorganizer.data.Person;
import com.example.budgetorganizer.tasks.CalendarEventTask;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddGiftActivity extends AppCompatActivity {
    private Button mPickDateButton;
    private EditText mGiftNameEditText, mGiftPriceEditText;
    private long person_id;
    private String date = "";
    private String picturePath = "";
    private long mdateSelection = 0;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);
        mPickDateButton = findViewById(R.id.pick_date_button);
        mGiftNameEditText = findViewById(R.id.ed_gift_name);
        mGiftPriceEditText = findViewById(R.id.ed_gift_price);
        DbHelper dbHelper = new DbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Intent intent = getIntent();
        person_id = intent.getLongExtra("person_id",0);
        DatePicker();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.common_menu, menu);
        return true;
    }
    public void add(MenuItem item) {
        if(mGiftNameEditText.getText().toString().matches("") || mGiftPriceEditText.getText().toString().matches("")

        ){
            Context context = getApplicationContext();
            CharSequence text = "Gift Name Or Price aren't valid";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            Gift gift = new Gift();
            gift.setName(mGiftNameEditText.getText().toString());
            gift.setPrice(Integer.parseInt(mGiftPriceEditText.getText().toString()));
            if(date.matches("")){
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d, yyyy");
                date = simpleDateFormat.format(calendar.getTime());
                mdateSelection = calendar.getTimeInMillis();
                Log.v("CurrentDate",date);
            }
            gift.setDate(date);
            gift.setPersonId(person_id);
            gift.setPhotoPath(picturePath);
            DbHelper.insertGift(mDb,gift);
            Person person = DbHelper.getPerson(mDb, person_id);
            person.setBudget(person.getBudget()-gift.getPrice());
            DbHelper.updatePerson(mDb, person);
            Context context = getApplicationContext();
            CalendarEventTask calendarEventTask = new CalendarEventTask(this,person.getName(),gift.getName(),mdateSelection);
            calendarEventTask.execute();
            CharSequence text = gift.getName()+" Added To The List !";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }


    }
    private void DatePicker(){
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        mPickDateButton.setOnClickListener(
                v -> materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));
        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener) selection -> {
                    mdateSelection = (long) selection;
                    date = materialDatePicker.getHeaderText();
                });
    }
    private void imageDialog(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);

            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        picturePath=getRealPathFromURI(getImageUri(getApplicationContext(), selectedImage));
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public void selectImage(View view) {
        imageDialog(this);
    }
}