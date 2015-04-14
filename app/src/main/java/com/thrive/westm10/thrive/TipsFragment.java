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

        tips[0] = new TipItem(R.drawable.article_1,"7 SPRING FOODS THAT HAVE A MUSCULAR APPROACH","Muscle and Fitness");
        tips[1] = new TipItem(R.drawable.article_2,"PUSH-PULL COMBO TO BUILD UPPER-BODY MUSCLE","Muscle and Fitness");
        tips[2] = new TipItem(R.drawable.article_3,"BARRY'S BOOTCAMP MINT NEAPOLITAN PROTEIN SHAKE","Womens Health");
        tips[3] = new TipItem(R.drawable.article_4,"HOW JAIME LANNISTER GOT HIS BODY BATTLE-READY FOR GAME OF THRONES","Mens Health");
        tips[4] = new TipItem(R.drawable.article_5,"5 Grocery Store Myths That Need to Be Stopped","The Kitchn");


        adapter = new TipAdapter(getActivity(), R.layout.tips_item_row, tips);

        mListView.setAdapter(adapter);
        mListView.isClickable();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse("https://www.google.com");
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
