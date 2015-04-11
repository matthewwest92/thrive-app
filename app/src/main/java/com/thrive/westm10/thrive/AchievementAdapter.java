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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Matthew West on 30/03/2015.
 */
public class AchievementAdapter extends ArrayAdapter<AchievementItem> {

    Context mContext;
    int layoutResourceId;
    AchievementItem data[] = null;

    public AchievementAdapter(Context mContext, int layoutResourceId, AchievementItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.achievementImage);
        TextView textViewName = (TextView) listItem.findViewById(R.id.achievementText);
        TextView textViewDescription = (TextView) listItem.findViewById(R.id.achievementDescription);
        AchievementItem folder = data[position];


        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);
        textViewDescription.setText(folder.description);

        return listItem;
    }

}
