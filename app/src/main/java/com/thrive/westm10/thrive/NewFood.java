package com.thrive.westm10.thrive;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by Matthew West on 11/04/2015.
 */
public class NewFood extends ActionBarActivity {

    Calendar myCalendar = Calendar.getInstance();
    DatabaseAdapter db;
    DateConversion converter;
    AutoCompleteTextView actv;
    EditText servingEdit;
    float dateFromParent;
    TextView fatText;
    TextView cholesterolText;
    TextView sodiumText;
    TextView potassiumText;
    TextView carbText;
    TextView sugarText;
    TextView proteinText;
    TextView calciumText;
    TextView ironText;
    EditText calEdit;
    FoodObject food;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dateFromParent = extras.getFloat("dateFromParent");
        }
        setContentView(R.layout.new_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = new DatabaseAdapter(this);
        converter = new DateConversion();

        actv = (AutoCompleteTextView) this.findViewById(R.id.foodNameEdit);

        ArrayAdapter ad = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1, db.getFoodNameList());
        actv.setAdapter(ad);

        servingEdit = (EditText) this.findViewById(R.id.servingSizeEdit);
        calEdit = (EditText) this.findViewById(R.id.calEdit);
        fatText = (TextView) this.findViewById(R.id.fatVal);
        cholesterolText = (TextView) this.findViewById(R.id.cholVal);
        sodiumText = (TextView) this.findViewById(R.id.sodiumVal);
        potassiumText = (TextView) this.findViewById(R.id.potassiumVal);
        carbText = (TextView) this.findViewById(R.id.carbVal);
        sugarText = (TextView) this.findViewById(R.id.sugarVal);
        proteinText = (TextView) this.findViewById(R.id.proteinVal);
        calciumText = (TextView) this.findViewById(R.id.calciumVal);
        ironText = (TextView) this.findViewById(R.id.ironVal);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        calEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean arg1) {

                if(arg1) {
                    if(actv.getText().toString().length() > 0 && servingEdit.getText().toString().length() > 0) {
                        food = db.getFoodData(actv.getText().toString());
                        float resultCalories = food.cals;
                        resultCalories = (resultCalories/100)*Float.parseFloat(servingEdit.getText().toString());
                        calEdit.setText(String.valueOf(Math.round(resultCalories)));
                        fatText.setText(String.valueOf(food.fat));
                        cholesterolText.setText(String.valueOf(food.cholesterol));
                        sodiumText.setText(String.valueOf(food.sodium));
                        potassiumText.setText(String.valueOf(food.potassium));
                        carbText.setText(String.valueOf(food.carbs));
                        sugarText.setText(String.valueOf(food.sugar));
                        proteinText.setText(String.valueOf(food.protein));
                        calciumText.setText(String.valueOf(food.calcium));
                        ironText.setText(String.valueOf(food.iron));

                    }
                }
            }

        });

        servingEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean arg1) {

                if(arg1) {
                    if(actv.getText().toString().length() > 0 && servingEdit.getText().toString().length() > 0) {
                        food = db.getFoodData(actv.getText().toString());
                        float resultCalories = food.cals;
                        resultCalories = (resultCalories/100)*Float.parseFloat(servingEdit.getText().toString());
                        calEdit.setText(String.valueOf(Math.round(resultCalories)));
                        fatText.setText(String.valueOf(food.fat));
                        cholesterolText.setText(String.valueOf(food.cholesterol));
                        sodiumText.setText(String.valueOf(food.sodium));
                        potassiumText.setText(String.valueOf(food.potassium));
                        carbText.setText(String.valueOf(food.carbs));
                        sugarText.setText(String.valueOf(food.sugar));
                        proteinText.setText(String.valueOf(food.protein));
                        calciumText.setText(String.valueOf(food.calcium));
                        ironText.setText(String.valueOf(food.iron));
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
                db.storeFood(actv.getText().toString(), Float.parseFloat(servingEdit.getText().toString()), dateFromParent);
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
