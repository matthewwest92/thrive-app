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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

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
public class GoalSetupActivity extends ActionBarActivity {

    private static final int SELECT_PHOTO = 100;
    Calendar myCalendar = Calendar.getInstance();
    DatabaseAdapter db;
    EditText goalType;
    EditText goalRate;
    EditText goalDuration;
    EditText goalStartWeight;
    EditText goalTargetWeight;
    EditText goalTargetCals;
    ImageView goalImage;
    ProfileObject tempProfile;
    DateConversion converter;
    float julianDate;
    float julianEndDate;

    String rateChoice = "";
    double rateValue= 0.0;
    String typeChoice = "";
    String durationChoice = "";
    int durationValue= 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        converter = new DateConversion();
        db = new DatabaseAdapter(this);
        tempProfile = db.getProfile();
        setContentView(R.layout.profile_goal_setup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        goalImage= (ImageView) this.findViewById(R.id.goalImage);
        goalType = (EditText) this.findViewById(R.id.goalTypeEdit);
        goalRate = (EditText) this.findViewById(R.id.goalRateEdit);
        goalDuration = (EditText) this.findViewById(R.id.goalDurationEdit);
        goalStartWeight = (EditText) this.findViewById(R.id.goalStartWeightEdit);
        goalTargetWeight = (EditText) this.findViewById(R.id.goalTargetWeightEdit);
        goalTargetCals = (EditText) this.findViewById(R.id.goalTargetCalsEdit);

        goalStartWeight.setText(Float.toString(tempProfile.weight));


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        c.setTime(c.getTime());
        String formattedDate = df.format(c.getTime());


        Date convertDate = new Date();
        try {
            convertDate = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        julianDate = (float) converter.dateToJulian(convertDate);

        goalType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean arg1) {
                if(arg1) {
                    alertTypeListView();
                    goalType.setText(typeChoice);
                }
            }

        });
        goalRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean arg1) {
                if(arg1) {
                    alertRateListView();
                    goalRate.setText(rateChoice);
                }
            }

        });
        goalDuration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean arg1) {
                if(arg1) {
                    alertDurationListView();
                    goalDuration.setText(durationChoice);
                }
            }

        });



        db = new DatabaseAdapter(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }

    public void alertTypeListView() {
        final CharSequence[] items = { "Maintain my weight", "Gain weight", "Lose weight"};

        AlertDialog.Builder builder = new AlertDialog.Builder(GoalSetupActivity.this);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                typeChoice = items[item].toString();
                goalType.setText(typeChoice);

                if(items[item].toString().equals("Maintain my weight")) {
                    goalImage.setImageResource(R.drawable.maintenance);
                    goalRate.setFocusable(false);
                } else if (items[item].toString().equals("Gain weight")) {
                    goalImage.setImageResource(R.drawable.dumbells);
                    goalRate.setFocusable(true);
                } else {
                    goalImage.setImageResource(R.drawable.runner);
                    goalRate.setFocusable(true);
                }
                dialog.dismiss();
                if(goalDuration.getText().toString().length() > 0 && goalType.getText().toString().length() > 0 && goalRate.getText().toString().length() > 0) {
                    goalTargetWeight.setText(calcTargetWeight());
                    goalTargetCals.setText(calcTargetCals());
                } else if (goalDuration.getText().toString().length() > 0 && goalType.getText().toString().equals("Maintain my weight")) {
                    goalTargetWeight.setText(calcTargetWeight());
                    goalTargetCals.setText(calcTargetCals());
                    rateValue=0;
                }
            }
        }).show();
    }

    public void alertRateListView() {
        final CharSequence[] items = { "0.5lb per week", "1lb per week", "1.5lb per week", "2lb per week"};

        AlertDialog.Builder builder = new AlertDialog.Builder(GoalSetupActivity.this);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].toString().equals("0.5lb per week")) {
                    rateValue = 0.5;
                } else if (items[item].toString().equals("1lb per week")) {
                    rateValue = 1;
                } else if (items[item].toString().equals("1.5lb per week")) {
                    rateValue = 1.5;
                } else {
                    rateValue = 2;
                }
                rateChoice = items[item].toString();
                goalRate.setText(rateChoice);
                if(goalDuration.getText().toString().length() > 0 && goalType.getText().toString().length() > 0 && goalRate.getText().toString().length() > 0) {
                    goalTargetWeight.setText(calcTargetWeight());
                    goalTargetCals.setText(calcTargetCals());
                } else if (goalDuration.getText().toString().length() > 0 && goalType.getText().toString().equals("Maintain my weight")) {
                    goalTargetWeight.setText(calcTargetWeight());
                    goalTargetCals.setText(calcTargetCals());
                    rateValue=0;
                }
                dialog.dismiss();
            }
        }).show();
    }

    public void alertDurationListView() {
        final CharSequence[] items = { "One Month", "Three Months", "Six Months", "Unlimited Duration"};

        AlertDialog.Builder builder = new AlertDialog.Builder(GoalSetupActivity.this);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                durationChoice = items[item].toString();

                if(items[item].toString().equals("One Month")) {
                    durationValue = 1;
                } else if (items[item].toString().equals("Three Months")) {
                    durationValue = 3;
                } else if (items[item].toString().equals("Six Months")) {
                    durationValue = 6;
                } else {
                    durationValue = 0;
                }
                goalDuration.setText(durationChoice);
                if(goalDuration.getText().toString().length() > 0 && goalType.getText().toString().length() > 0 && goalRate.getText().toString().length() > 0) {
                    goalTargetWeight.setText(calcTargetWeight());
                    goalTargetCals.setText(calcTargetCals());
                } else if (goalDuration.getText().toString().length() > 0 && goalType.getText().toString().equals("Maintain my weight")) {
                    goalTargetWeight.setText(calcTargetWeight());
                    goalTargetCals.setText(calcTargetCals());
                    rateValue=0;
                }

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                c.setTime(c.getTime());
                c.add(Calendar.DATE, +(durationValue*4*7));
                String formattedDate = df.format(c.getTime());


                Date convertDate = new Date();
                try {
                    convertDate = df.parse(formattedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                julianEndDate = (float) converter.dateToJulian(convertDate);


                dialog.dismiss();
            }
        }).show();
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
                GoalObject goal = new GoalObject(Float.parseFloat(goalStartWeight.getText().toString()), durationValue,goalType.getText().toString(), julianDate, julianEndDate, Float.parseFloat(goalTargetCals.getText().toString()), (float) rateValue);
                db.saveGoal(goal);
                super.onBackPressed();
                return true;
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public float startWeight;
    public int duration;
    public String type;
    public float startDate;
    public float endDate;
    public float targetCals;
    public float rate;*/


    @Override
    protected void onResume() {
        super.onResume();
        goalDuration.setText(durationChoice);
        goalType.setText(typeChoice);
        goalRate.setText(rateChoice);

        if(goalDuration.getText().toString().length() > 0 && goalType.getText().toString().length() > 0 && goalRate.getText().toString().length() > 0) {
            goalTargetWeight.setText(calcTargetWeight());
            goalTargetCals.setText(calcTargetCals());
        }

    }

    public String calcTargetWeight() {
        String weight = "";
        double targetVal = 0.0;
        double rate = rateValue;
        if(typeChoice.toString().equals("Gain weight")) {
            rate = rate*-1;
        }
        targetVal = tempProfile.weight - (rate * durationValue * 4);

        weight = String.valueOf(targetVal);
        return weight;
    }

    public String calcTargetCals() {
        String cals= "";
        int baseCals;
        double targetVal = 0.0;
        double rate = rateValue;

        if(typeChoice.toString().equals("Maintain my weight")) {
            rate=0;
        } else if (typeChoice.toString().equals("Lose weight")) {
            rate= rate*-1;
        }

        if(tempProfile.gender.equals("Male")) {
            baseCals = 2500;

            targetVal = baseCals + (rate * 500);
        } else {
            baseCals = 2000;
            targetVal = baseCals + (rate * 500);
        }
        cals = String.valueOf(targetVal);
        return cals;
    }
}
