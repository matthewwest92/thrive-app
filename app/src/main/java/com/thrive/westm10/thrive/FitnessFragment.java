package com.thrive.westm10.thrive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Matthew West on 30/03/2015.
 */
public class FitnessFragment extends Fragment {

    private ListView mListView;
    private String[] mItemTitles;
    DatabaseAdapter db;
    TextView date;
    DateConversion converter;
    float julianDate;
    FitnessAdapter adapter;

    public FitnessFragment() {
    }
    ImageButton runCommand;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fit_fragment, container, false);
        runCommand=(ImageButton) rootView.findViewById(R.id.runCommand);

        db = new DatabaseAdapter(getActivity());

        date = (TextView) rootView.findViewById(R.id.dateText);
        converter = new DateConversion();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        date.setText(String.valueOf(formattedDate));


        runCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Selected Button", Toast.LENGTH_SHORT).show();
            }
        });

        mItemTitles= getResources().getStringArray(R.array.exercise_list);
        mListView = (ListView) rootView.findViewById(R.id.fitness_list);
        Date convertDate = new Date();
        try {
            convertDate = df.parse(date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        julianDate = (float) converter.dateToJulian(convertDate);
        adapter = new FitnessAdapter(getActivity(), R.layout.fitness_item_row, db.getFitnessDay(julianDate));

        ImageButton plusButton = (ImageButton) rootView.findViewById(R.id.runCommand);

        // add button listener
        plusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), NewExercise.class);
                intent.putExtra("dateFromParent",julianDate);
                startActivity(intent);
            }
        });


        mListView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new FitnessAdapter(getActivity(), R.layout.fitness_item_row, db.getFitnessDay(julianDate));
        adapter.notifyDataSetChanged();
    }

}
