package be.thmbc.castextendedscreen.cast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.cast.CastMediaControlIntent;

import be.thmbc.castextendedscreen.BuildConfig;
import be.thmbc.castextendedscreen.R;

/**
 * Created by maarten on 17/05/16.
 */
public class CastEnabledActivity extends AppCompatActivity {

    private MediaRouter mediaRouter;
    private MediaRouteSelector mediaRouteSelector;
    private MediaRouterCallback mediaRouterCallback;
    private MenuItem castButton;
    private int availableRoutes = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaRouter = MediaRouter.getInstance(getApplicationContext());
        mediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(BuildConfig.CAST_APP_ID))
                .build();
        mediaRouterCallback = new MediaRouterCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_cast, menu);
        castButton = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(castButton);
        mediaRouteActionProvider.setRouteSelector(mediaRouteSelector);
        if (availableRoutes > 0) {
            castButton.setVisible(true);
        } else {
            castButton.setVisible(false);
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaRouter.addCallback(mediaRouteSelector, mediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }

    @Override
    protected void onStop() {
        mediaRouter.removeCallback(mediaRouterCallback);
        super.onStop();
    }
}
