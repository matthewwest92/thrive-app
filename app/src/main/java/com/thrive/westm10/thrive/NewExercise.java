package com.thrive.westm10.thrive;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Matthew West on 11/04/2015.
 */
public class NewExercise extends ActionBarActivity {

    Calendar myCalendar = Calendar.getInstance();
    DatabaseAdapter db;
    DateConversion converter;
    AutoCompleteTextView actv;
    EditText durationText;
    EditText calsText;
    float dateFromParent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dateFromParent = extras.getFloat("dateFromParent");
        }
        setContentView(R.layout.new_exercise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = new DatabaseAdapter(this);
        converter = new DateConversion();

        actv = (AutoCompleteTextView) this.findViewById(R.id.fitnessNameAutoTextview);

        ArrayAdapter ad = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1, db.getExerciseNameList());
        actv.setAdapter(ad);
        durationText = (EditText) this.findViewById(R.id.timeEdit);
        calsText = (EditText) this.findViewById(R.id.caloriesBurnedEdit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        calsText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean arg1) {

                if(arg1) {
                    if(actv.getText().toString().length() > 0 && durationText.getText().toString().length() > 0) {
                        float resultCalories = db.getExcerciseData(actv.getText().toString());
                        resultCalories = resultCalories * Float.parseFloat(durationText.getText().toString()) *db.getProfile().weight;
                        calsText.setText(String.valueOf(Math.round(resultCalories)));
                    }
                }
            }

        });

        durationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean arg1) {

                if(arg1) {
                    if(actv.getText().toString().length() > 0 && durationText.getText().toString().length() > 0) {
                        float resultCalories = db.getExcerciseData(actv.getText().toString());
                        resultCalories = resultCalories * Float.parseFloat(durationText.getText().toString()) *db.getProfile().weight;
                        calsText.setText(String.valueOf(Math.round(resultCalories)));
                    }
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_done:
                db.storeExercise(actv.getText().toString(), Float.parseFloat(calsText.getText().toString()), dateFromParent);
                finish();
                return true;
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
