package com.thrive.westm10.thrive;

import android.app.Activity;
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
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Matthew West on 11/04/2015.
 */
public class ProfileSetupActivity extends ActionBarActivity {

    private static final int SELECT_PHOTO = 100;
    Calendar myCalendar = Calendar.getInstance();
    EditText firstEdit;
    EditText surEdit;
    EditText heightEdit;
    EditText weightEdit;
    EditText datePicker;
    EditText genderPicker;
    String choice = "Male";
    DatabaseAdapter db;
    ProfileObject tempProfile;
    DateConversion converter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_setup_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = new DatabaseAdapter(this);
        tempProfile = db.getProfile();
        converter = new DateConversion();



        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        loadImageFromStorage(directory.getAbsolutePath());
        /*ImageView avatarIV = (ImageView) this.findViewById(R.id.userAvatar);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.unknown);
        RoundImage roundedImage = new RoundImage(bm);
        avatarIV.setImageDrawable(roundedImage);*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        datePicker = (EditText) this.findViewById(R.id.dobEdit);
        genderPicker = (EditText) this.findViewById(R.id.genderEdit);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        /*datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileSetupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/

        genderPicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean arg1) {
                if(arg1) {
                    alertSimpleListView();
                    genderPicker.setText(choice);
                }
            }

        });

        datePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean arg1) {
                if(arg1) {
                    new DatePickerDialog(ProfileSetupActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }

        });
        firstEdit = (EditText) this.findViewById(R.id.firstNameEdit);
        surEdit = (EditText) this.findViewById(R.id.surnameEdit);
        heightEdit = (EditText) this.findViewById(R.id.heightEdit);
        weightEdit = (EditText) this.findViewById(R.id.weightEdit);

        firstEdit.setText(tempProfile.firstName);
        surEdit.setText(tempProfile.surname);
        genderPicker.setText(tempProfile.gender);
        if(tempProfile.height != 0.0) {
            heightEdit.setText(String.valueOf(tempProfile.height));
        }
        if(tempProfile.weight != 0.0) {
            weightEdit.setText(String.valueOf(tempProfile.weight));
        }
        if(tempProfile.dob != 0.0) {
            String myFormat = "dd/MM/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            datePicker.setText(sdf.format(converter.julianToDate(tempProfile.dob)));
        }




    }
    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        datePicker.setText(sdf.format(myCalendar.getTime()));
    }

    public void setImage(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void alertSimpleListView() {
        final CharSequence[] items = { "Male", "Female"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSetupActivity.this);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                choice = items[item].toString();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    saveToInternalStorage(yourSelectedImage);
                    RoundImage roundedImage = new RoundImage(yourSelectedImage);
                    ImageView avatarIV = (ImageView) this.findViewById(R.id.userAvatar);
                    avatarIV.setImageDrawable(roundedImage);
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_done:
                ProfileObject profile = new ProfileObject(firstEdit.getText().toString(),surEdit.getText().toString(),Float.parseFloat(heightEdit.getText().toString()),Float.parseFloat(weightEdit.getText().toString()),genderPicker.getText().toString(), (float) converter.dateToJulian(myCalendar.getTime()));
                db.saveProfile(profile);
                super.onBackPressed();
                return true;
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            RoundImage roundedImage = new RoundImage(b);
            ImageView avatarIV = (ImageView) this.findViewById(R.id.userAvatar);
            avatarIV.setImageDrawable(roundedImage);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
