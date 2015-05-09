package im.aktive.aktive;

import android.app.Application;

import im.aktive.aktive.manager.ATActivityManager;
import im.aktive.aktive.manager.ATTagManager;
import im.aktive.aktive.manager.ATTagValueManager;
import im.aktive.aktive.manager.ATUserActivityManager;
import im.aktive.aktive.manager.ATUserManager;
import im.aktive.aktive.model.ATTagValue;
import im.aktive.aktive.notification.ATGlobalEventDispatchManager;
import im.aktive.vendor.ICDispatch.ICDispatchApplication;

/**
 * Created by hoangtran on 13/7/14.
 */
public class ATApplication extends ICDispatchApplication{
    public static final String TAG = ATApplication.class
            .getSimpleName();

    private static ATApplication mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init() {
        ATGlobalEventDispatchManager.init();
        ATUserManager.getInstance();
        ATActivityManager.getInstance();
        ATUserActivityManager.getInstance();
        ATTagManager.getInstance();
        ATTagValueManager.getInstance();
    }

    public static synchronized ATApplication getInstance() {
        return mInstance;
    }
}
