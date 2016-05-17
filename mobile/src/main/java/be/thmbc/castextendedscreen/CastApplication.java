package be.thmbc.castextendedscreen;

import android.app.Application;

/**
 * Created by maarten on 12/05/16.
 */
public class CastApplication extends Application {

    private int value;
    private ValueChangeListener listener;

    public void addOneToValue() {
        value++;
        if (listener != null) {
            listener.notifyChanged(value);
        }
    }

    public int getValue() {
        return value;
    }

    public void setListener(ValueChangeListener listener) {
        this.listener = listener;
    }

    public void removeListener(ValueChangeListener listener) {
        if (this.listener.equals(listener)) {
            this.listener = null;
        }
    }

    public interface ValueChangeListener {
        void notifyChanged(int value);
    }
}
