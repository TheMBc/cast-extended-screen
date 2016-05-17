package be.thmbc.extendedscreenhelper.cast;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.media.MediaRouter;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.google.android.gms.common.api.Status;

import be.thmbc.extendedscreenhelper.CastManager;
import be.thmbc.extendedscreenhelper.service.PresentationService;

/**
 * Created by maarten on 17/05/16.
 */
public class MediaRouterCallback extends MediaRouter.Callback {

    private Context context;
    private int availableRoutes = 0;
    private CastDevice device;

    public MediaRouterCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo route) {
        super.onRouteAdded(router, route);
        ++availableRoutes;
    }

    @Override
    public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo route) {
        super.onRouteRemoved(router, route);
        --availableRoutes;
    }

    @Override
    public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
        device = CastDevice.getFromBundle(info.getExtras());
        Intent intent = new Intent(context, CastManager.getInstance().getCastActivity());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        CastRemoteDisplayLocalService.NotificationSettings settings =
                new CastRemoteDisplayLocalService.NotificationSettings.Builder()
                        .setNotificationPendingIntent(notificationPendingIntent).build();

        CastRemoteDisplayLocalService.startService(
                context,
                PresentationService.class,
                CastManager.getInstance().getCastAppId(),
                device,
                settings,
                new CastRemoteDisplayLocalService.Callbacks() {
                    @Override
                    public void onServiceCreated(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {

                    }

                    @Override
                    public void onRemoteDisplaySessionStarted(CastRemoteDisplayLocalService service) {
                        // initialize sender UI
                    }

                    @Override
                    public void onRemoteDisplaySessionError(Status errorReason) {

                    }
                });
    }

    @Override
    public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
        CastRemoteDisplayLocalService.stopService();
        device = null;
    }
}
