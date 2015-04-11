package com.thrive.westm10.thrive;

import android.content.Context;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Matthew West on 05/04/2015.
 */
public class OpenSourcePreference extends DialogPreference {

    public OpenSourcePreference(Context oContext, AttributeSet attrs) {
        super(oContext, attrs);
        setDialogLayoutResource(R.layout.open_source_dialog);

    }

    @Override
    public void onBindDialogView(View view) {
        TextView t2 = (TextView) view.findViewById(R.id.settings_open_source_message);
        t2.setText(Html.fromHtml(view.getResources().getString(R.string.settings_message_8)));
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
