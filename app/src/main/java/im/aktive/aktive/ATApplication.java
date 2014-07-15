package im.aktive.aktive;

import android.app.Application;

import im.aktive.aktive.manager.ATUserManager;
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
        //ATUserManager.getInstance();
        //AT
    }

    public static synchronized ATApplication getInstance() {
        return mInstance;
    }
}
