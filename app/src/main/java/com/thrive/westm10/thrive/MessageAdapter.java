package com.thrive.westm10.thrive;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Matthew West on 11/04/2015.
 */
public class MessageAdapter {
    public static void message(Context context, String message) {
        Toast.makeText(context, "Unsuccessful", Toast.LENGTH_LONG).show();
    }
}
