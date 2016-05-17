package be.thmbc.castextendedscreen.ui.cast;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import com.google.android.gms.cast.CastPresentation;

import be.thmbc.castextendedscreen.CastApplication;
import be.thmbc.castextendedscreen.R;

/**
 * Created by maarten on 12/05/16.
 */
public class FirstScreenPresentation extends CastPresentation implements CastApplication.ValueChangeListener {

    private TextView textView;

    public FirstScreenPresentation(Context context,
                                   Display display) {
        super(context, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_secondary);
        textView = (TextView) findViewById(R.id.secondary_text);
        notifyChanged(((CastApplication) getContext().getApplicationContext()).getValue());
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((CastApplication) getContext().getApplicationContext()).setListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((CastApplication) getContext().getApplicationContext()).removeListener(this);
    }

    @Override
    public void notifyChanged(int value) {
        textView.setText(Integer.toString(value));
    }
}