package be.thmbc.castextendedscreen;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.cast.CastPresentation;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;

/**
 * Created by maarten on 12/05/16.
 */
public class PresentationService extends CastRemoteDisplayLocalService {

    private Presentation presentation;

    @Override
    public void onCreatePresentation(Display display) {
        createPresentation(display);
    }

    @Override
    public void onDismissPresentation() {
        dismissPresentation();
    }

    private void dismissPresentation() {
        if (presentation != null) {
            presentation.dismiss();
            presentation = null;
        }
    }

    private void createPresentation(Display display) {
        dismissPresentation();
        presentation = new FirstScreenPresentation(this, display);
        try {
            presentation.show();
        } catch (WindowManager.InvalidDisplayException ex) {
            Log.e("service blabla", "Unable to show presentation, display was " +
                    "removed.", ex);
            dismissPresentation();
        }
    }

    private final static class FirstScreenPresentation extends CastPresentation implements CastApplication.ValueChangeListener {

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
}
