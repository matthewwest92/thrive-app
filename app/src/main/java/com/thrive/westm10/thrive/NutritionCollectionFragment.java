package com.thrive.westm10.thrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NutritionCollectionFragment extends Fragment {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fitness_collection, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }



    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FitnessFragment fragment = new FitnessFragment();
            Bundle args = new Bundle();
            args.putInt("page_position", position + 1);
            fragment.setArguments(args);
            return fragment;
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
        }
        @Override
        public int getCount() {
            // Show 7 total pages.
            return 7;
        }

    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class FitnessFragment extends Fragment {

        private static final String TAG = FitnessFragment.class.getSimpleName();
        private ListView mListView;
        private String[] mItemTitles;
        DatabaseAdapter db;
        TextView date;
        DateConversion converter;
        float julianDate;
        FitnessAdapter adapter;

        private static final String ARG_SECTION_NUMBER = "section_number";
        static int pos;

        public static FitnessFragment newInstance(int sectionNumber) {
            FitnessFragment fragment = new FitnessFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            pos=sectionNumber;
            return fragment;
        }

        public FitnessFragment() {

        }

        @Override
        public void onResume() {
            super.onResume();
            adapter.clear();
            adapter.addAll(db.getFitnessDay(julianDate));
            adapter.notifyDataSetChanged();
        }


        ImageButton runCommand;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.d(TAG, "onCreateView");
            Log.e("The position is: " + pos, "MSG");
            View rootView = inflater.inflate(R.layout.fit_fragment, container, false);
            runCommand=(ImageButton) rootView.findViewById(R.id.runCommand);

            db = new DatabaseAdapter(getActivity());

            date = (TextView) rootView.findViewById(R.id.dateText);
            converter = new DateConversion();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            c.setTime(c.getTime());
            c.add(Calendar.DATE, -(getArguments().getInt("page_position")-1));
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
            adapter = new FitnessAdapter(getActivity(), R.layout.fitness_item_row);
            adapter.addAll(db.getFitnessDay(julianDate));

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
            registerForContextMenu(mListView);

            return rootView;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;
            View view = info.targetView;
            switch (item.getItemId()) {
                case R.id.action_delete:
                    TextView listExName = (TextView) view.findViewById(R.id.exerciseName);
                    TextView listExCals = (TextView) view.findViewById(R.id.exerciseCals);
                    db.deleteExercise(listExName.getText().toString(), Float.parseFloat(listExCals.getText().toString()), julianDate);
                    adapter.clear();
                    adapter.addAll(db.getFitnessDay(julianDate));
                    adapter.notifyDataSetChanged();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }


    }




}
