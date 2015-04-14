package com.thrive.westm10.thrive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

/**
 * Created by Matthew West on 30/03/2015.
 */
public class TipsFragment extends Fragment {

    private ListView mListView;
    TipAdapter adapter;
    public TipsFragment() {
    }
    ImageButton runCommand;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tips_fragment, container, false);


        mListView = (ListView) rootView.findViewById(R.id.tip_list);

        TipItem[] tips = new TipItem[5];

        tips[0] = new TipItem(R.drawable.article_1,"7 Spring foods that have a muscular approach","Muscle and Fitness");
        tips[1] = new TipItem(R.drawable.article_2,"Push-pull combo to build upper body muscle","Muscle and Fitness");
        tips[2] = new TipItem(R.drawable.article_3,"Barry's Bootcamp: mint neapolitan protein shake","Womens Health");
        tips[3] = new TipItem(R.drawable.article_4,"How Jaime Lannister got his body battle-ready for Game of Thrones","Mens Health");
        tips[4] = new TipItem(R.drawable.article_5,"5 Grocery Store Myths That Need to Be Stopped","The Kitchn");


        adapter = new TipAdapter(getActivity(), R.layout.tips_item_row, tips);

        mListView.setAdapter(adapter);
        mListView.isClickable();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse("http://www.google.co.uk");
                switch(position) {
                    case 0:
                        uri = Uri.parse("http://www.muscleandfitness.com/nutrition/gain-mass/7-spring-foods-have-muscular-approach");
                        break;
                    case 1:
                        uri = Uri.parse("http://www.muscleandfitness.com/workouts/workout-routines/push-pull-combo-build-upper-body-muscle");
                        break;
                    case 2:
                        uri = Uri.parse("http://www.womenshealthmag.co.uk/nutrition/recipes/2784/barry-s-bootcamp-mint-neapolitan-protein-shake/");
                        break;
                    case 3:
                        uri = Uri.parse("http://www.menshealth.co.uk/building-muscle/how-jaime-lannister-got-his-body-battle-ready-for-game-of-thrones");
                        break;
                    case 4:
                        uri = Uri.parse("http://www.thekitchn.com/5-grocery-myths-dispelled-the-grocery-insider-217655");
                        break;
                }
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
