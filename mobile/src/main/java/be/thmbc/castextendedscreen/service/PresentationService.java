package be.thmbc.castextendedscreen.service;

import android.app.Presentation;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.cast.CastRemoteDisplayLocalService;

import be.thmbc.castextendedscreen.ui.cast.FirstScreenPresentation;

/**
 * Created by maarten on 17/05/16.
 */
public class PresentationService extends CastRemoteDisplayLocalService{

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
}
