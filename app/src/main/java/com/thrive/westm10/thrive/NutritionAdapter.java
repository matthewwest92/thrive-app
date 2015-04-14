package com.thrive.westm10.thrive;

/**
 * Created by Matthew West on 01/04/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Matthew West on 30/03/2015.
 */
public class NutritionAdapter extends ArrayAdapter<FoodItem> {

    Context mContext;
    int layoutResourceId;

    public NutritionAdapter(Context mContext, int layoutResourceId) {

        super(mContext, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        FoodItem folder = getItem(position);

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView exerciseName = (TextView) listItem.findViewById(R.id.exerciseName);
        TextView exerciseCals = (TextView) listItem.findViewById(R.id.exerciseCals);


        exerciseName.setText(folder.name);
        exerciseCals.setText(folder.cals);

        return listItem;
    }

}
