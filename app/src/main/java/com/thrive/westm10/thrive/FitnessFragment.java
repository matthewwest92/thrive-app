package com.thrive.westm10.thrive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Matthew West on 30/03/2015.
 */
public class FitnessFragment extends Fragment {

    private ListView mListView;
    private String[] mItemTitles;

    public FitnessFragment() {
    }
    ImageButton runCommand;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fit_fragment, container, false);
        runCommand=(ImageButton) rootView.findViewById(R.id.runCommand);

        runCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Selected Button", Toast.LENGTH_SHORT).show();
            }
        });

        mItemTitles= getResources().getStringArray(R.array.exercise_list);
        mListView = (ListView) rootView.findViewById(R.id.fitness_list);
        FitnessItem[] drawerItem = new FitnessItem[8];

        drawerItem[0] = new FitnessItem("Jogging 5km", "200Kcal");
        drawerItem[1] = new FitnessItem("Jogging 5km", "200Kcal");
        drawerItem[2] = new FitnessItem("Jogging 5km", "200Kcal");
        drawerItem[3] = new FitnessItem("Jogging 5km", "200Kcal");
        drawerItem[4] = new FitnessItem("Jogging 5km", "200Kcal");
        drawerItem[5] = new FitnessItem("Jogging 5km", "200Kcal");
        drawerItem[6] = new FitnessItem("Jogging 5km", "200Kcal");
        drawerItem[7] = new FitnessItem("Jogging 5km", "200Kcal");

        FitnessAdapter adapter = new FitnessAdapter(getActivity(), R.layout.fitness_item_row, drawerItem);

        mListView.setAdapter(adapter);

        return rootView;
    }
}
