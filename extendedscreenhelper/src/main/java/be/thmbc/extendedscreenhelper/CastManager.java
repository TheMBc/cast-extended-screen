package be.thmbc.extendedscreenhelper;

/**
 * Created by maarten on 17/05/16.
 */
public class CastManager {

    private static CastManager instance;

    public static CastManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("CastManager is null, did you forget to initialize?");
        }
        return instance;
    }

    private CastManager(String castAppId, Class castActivity) {
        this.castAppId = castAppId;
    }

    private String castAppId;
    private Class castActivity;

    public String getCastAppId() {
        return castAppId;
    }

    public Class getCastActivity() {
        return castActivity;
    }

    public static class Builder {

        private String castAppId;
        private Class castActivity;

        public Builder setCastAppId(String castAppId) {
            this.castAppId = castAppId;
            return this;
        }

        public Builder setCastActivity(Class castActivity) {
            this.castActivity = castActivity;
            return this;
        }

        public void initialize() {
            if (castAppId == null ) {
                throw new RuntimeException("CastAppId is null, you need to supply your cast app id.");
            } else if (castActivity == null) {
                throw new RuntimeException("ActivityClass is null, you need to supply an Activity class.");
            }
            instance = new CastManager(castAppId, castActivity);
        }
    }
}
