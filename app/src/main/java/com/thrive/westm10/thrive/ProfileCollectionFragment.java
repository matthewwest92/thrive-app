package com.thrive.westm10.thrive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;


public class ProfileCollectionFragment extends Fragment {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tabbed, null);
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
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 1) {
                return GoalsFragment.newInstance(position + 1);
            }
            if(position == 2) {
                return AchievementFragment.newInstance(position + 1);
            }
            else {
                return OverviewFragment.newInstance(position + 1);
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.profile_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.profile_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.profile_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class OverviewFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";
        DatabaseAdapter db;
        ProfileObject profile;
        GoalObject goal;
        double exerciseCals;

        DateConversion converter;
        float julianDate;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        ImageView avatarIV;
        public static OverviewFragment newInstance(int sectionNumber) {
            OverviewFragment fragment = new OverviewFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public OverviewFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
            TextView nameTV = (TextView) rootView.findViewById(R.id.name);
            nameTV.setText(getResources().getString(R.string.user_name));
            converter = new DateConversion();

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


            db = new DatabaseAdapter(getActivity());
            profile = db.getProfile();
            goal = db.getGoal();
            exerciseCals = db.getDaysExerciseCalories(julianDate);

            if (profile.firstName.length() > 0) {
                nameTV.setText(profile.firstName + " " + profile.surname);
            }


            TextView netCalTextTV = (TextView) rootView.findViewById(R.id.netCalText);
            netCalTextTV.setText(getResources().getString(R.string.calories_left_text));
            TextView netCalValTV = (TextView) rootView.findViewById(R.id.netCalVal);
            netCalValTV.setText(getResources().getString(R.string.calories_left_value));

            TextView foodCaltxtTV = (TextView) rootView.findViewById(R.id.foodCaltxt);
            TextView exerciseCaltxtTV = (TextView) rootView.findViewById(R.id.exerciseCaltxt);
            TextView goalCaltxtTV = (TextView) rootView.findViewById(R.id.goalCaltxt);
            foodCaltxtTV.setText(R.string.food_cals_text);
            exerciseCaltxtTV.setText(R.string.exercise_cals_text);
            goalCaltxtTV.setText(R.string.goal_cals_text);



            TextView foodCalValTV = (TextView) rootView.findViewById(R.id.foodCalval);
            foodCalValTV.setText(getResources().getString(R.string.placeholder_cals));
            TextView exerciseCalValTV = (TextView) rootView.findViewById(R.id.exerciseCalval);
            exerciseCalValTV.setText(getResources().getString(R.string.placeholder_cals));
            TextView goalCalValTV = (TextView) rootView.findViewById(R.id.goalCalval);
            goalCalValTV.setText(getResources().getString(R.string.placeholder_cals));

            if(goal.targetCals != 0000) {
                goalCalValTV.setText(String.valueOf(Math.round(goal.targetCals)));
            }

            if(exerciseCals != 0000) {
                exerciseCalValTV.setText(String.valueOf(Math.round(exerciseCals)));
            }

            netCalValTV.setText(String.valueOf(Integer.parseInt(foodCalValTV.getText().toString())-Integer.parseInt(exerciseCalValTV.getText().toString())));

            avatarIV = (ImageView) rootView.findViewById(R.id.avatar);
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.unknown);
            RoundImage roundedImage = new RoundImage(bm);
            avatarIV.setImageDrawable(roundedImage);

            ContextWrapper cw = new ContextWrapper(getActivity());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            loadImageFromStorage(directory.getAbsolutePath());

            avatarIV.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), ProfileSetupActivity.class);
                    startActivity(intent);

                }

            });

            return rootView;

        }

        private void loadImageFromStorage(String path)
        {

            try {
                File f=new File(path, "profile.jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                RoundImage roundedImage = new RoundImage(b);
                avatarIV.setImageDrawable(roundedImage);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }

        public static String fmt(double d)
        {
            if(d == (long) d)
                return String.format("%d",(long)d);
            else
                return String.format("%s",d);
        }
    }

    public static class GoalsFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */


        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GoalsFragment newInstance(int sectionNumber) {
            GoalsFragment fragment = new GoalsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public GoalsFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.goal_fragment, container, false);


            ImageView goalImage = (ImageView) rootView.findViewById(R.id.exerciseImg);

            goalImage.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), GoalSetupActivity.class);
                    startActivity(intent);

                }

            });

            return rootView;

        }
    }

    public static class AchievementFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private ListView mListView;
        private String[] mItemTitles;

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static AchievementFragment newInstance(int sectionNumber) {
            AchievementFragment fragment = new AchievementFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public AchievementFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.achievement_fragment, container, false);

            mItemTitles= getResources().getStringArray(R.array.achievement_list_array);
            mListView = (ListView) rootView.findViewById(R.id.achievement_list);
            DatabaseAdapter db = new DatabaseAdapter(getActivity());
            db.unlockAchievement("You''re Set!");

            AchievementAdapter adapter = new AchievementAdapter(getActivity(), R.layout.achievement_list_row, db.getAchievementsData());

            mListView.setAdapter(adapter);

            return rootView;

        }
    }

}
