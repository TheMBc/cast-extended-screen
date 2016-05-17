package be.thmbc.extendedscreenhelper.service;

import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.cast.CastPresentation;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;


/**
 * Created by maarten on 17/05/16.
 */
public abstract class PresentationService<T extends CastPresentation> extends CastRemoteDisplayLocalService{

    private T presentation;

    @Override
    public void onCreatePresentation(Display display) {
        createSecondScreen(display);
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

    protected abstract T createPresentation(Display display);

    private void createSecondScreen(Display display) {
        dismissPresentation();
        presentation = createPresentation(display);
        try {
            presentation.show();
        } catch (WindowManager.InvalidDisplayException ex) {
            Log.e("service blabla", "Unable to show presentation, display was " +
                    "removed.", ex);
            dismissPresentation();
        }
    }
}
