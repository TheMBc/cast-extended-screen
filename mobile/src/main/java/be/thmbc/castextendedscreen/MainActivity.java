package be.thmbc.castextendedscreen;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.google.android.gms.common.api.Status;

/**
 * Created by maarten on 12/05/16.
 */
public class MainActivity extends CastEnabledActivity {

    private MediaRouter mediaRouter;
    private MediaRouteSelector mediaRouteSelector;
    private MediaRouterChooserCallback mediaRouterCallback;
    private MenuItem castButton;
    private Button button;
    private int availableRoutes = 0;

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
        mediaRouter = MediaRouter.getInstance(getApplicationContext());
        mediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(BuildConfig.CAST_APP_ID))
                .build();
        mediaRouterCallback = new MediaRouterChooserCallback();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class MediaRouterChooserCallback extends MediaRouter.Callback {

        private CastDevice device;

        @Override
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo route) {
            super.onRouteAdded(router, route);
            if (++availableRoutes > 0) {
                castButton.setVisible(true);
            }
        }

        @Override
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo route) {
            super.onRouteRemoved(router, route);
            if (--availableRoutes < 1) {
                castButton.setVisible(false);
            }
        }

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            device = CastDevice.getFromBundle(info.getExtras());
            Intent intent = new Intent(MainActivity.this,
                    MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent notificationPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

            CastRemoteDisplayLocalService.NotificationSettings settings =
                    new CastRemoteDisplayLocalService.NotificationSettings.Builder()
                            .setNotificationPendingIntent(notificationPendingIntent).build();

            CastRemoteDisplayLocalService.startService(
                    getApplicationContext(),
                    PresentationService.class, BuildConfig.CAST_APP_ID,
                    device, settings,
                    new CastRemoteDisplayLocalService.Callbacks() {
                        @Override
                        public void onServiceCreated(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {

                        }

                        @Override
                        public void onRemoteDisplaySessionStarted(
                                CastRemoteDisplayLocalService service) {
                            // initialize sender UI
                        }

                        @Override
                        public void onRemoteDisplaySessionError(Status errorReason) {
                            initError();
                        }
                    });
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            teardown();
            CastRemoteDisplayLocalService.stopService();
            device = null;
        }
    }

    private void teardown() {

    }

    private void initError() {

    }
}
