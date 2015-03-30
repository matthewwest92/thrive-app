package com.thrive.westm10.thrive;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Matthew West on 30/03/2015.
 */
public class NutritionFragment extends Fragment {
    public NutritionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.nutrition_fragment, container, false);

        return rootView;
    }
}
