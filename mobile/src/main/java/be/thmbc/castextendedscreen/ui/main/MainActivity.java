package be.thmbc.castextendedscreen.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import be.thmbc.castextendedscreen.CastApplication;
import be.thmbc.castextendedscreen.R;
import be.thmbc.castextendedscreen.cast.CastEnabledActivity;

/**
 * Created by maarten on 12/05/16.
 */
public class MainActivity extends CastEnabledActivity {

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.main_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CastApplication) MainActivity.this.getApplicationContext()).addOneToValue();
            }
        });
    }
}
