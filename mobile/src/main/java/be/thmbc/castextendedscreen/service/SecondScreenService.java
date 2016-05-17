package be.thmbc.castextendedscreen.service;

import android.view.Display;

import be.thmbc.castextendedscreen.ui.cast.FirstScreenPresentation;
import be.thmbc.extendedscreenhelper.service.PresentationService;

/**
 * Created by maarten on 17/05/16.
 */
public class SecondScreenService extends PresentationService<FirstScreenPresentation> {
    @Override
    protected FirstScreenPresentation createPresentation(Display display) {
        return new FirstScreenPresentation(this, display);
    }
}
