package com.thrive.westm10.thrive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Matthew West on 30/03/2015.
 */
public class SettingsActivity extends Fragment {
    public SettingsActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.settings_activity, container, false);

        return rootView;
    }
}
