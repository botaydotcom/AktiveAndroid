package im.aktive.aktive.notification;

import java.util.ArrayList;
import java.util.List;

import im.aktive.aktive.manager.ATGlobalEventCallbackInterface;
import im.aktive.aktive.model.ATUser;

/**
 * Created by hoangtran on 14/7/14.
 */
public class ATGlobalEventDispatchManager implements ATGlobalEventCallbackInterface{
    private List<ATGlobalEventCallbackInterface> mListCallback = null;

    public static final String GLOBAL_EVENT_PRE_LOGIN = "GLOBAL_EVENT_PRE_LOGIN";
    public static final String GLOBAL_EVENT_POST_LOGIN = "GLOBAL_EVENT_POST_LOGIN";
    public static final String GLOBAL_EVENT_PRE_LOGOUT = "GLOBAL_EVENT_PRE_LOGOUT";
    public static final String GLOBAL_EVENT_POST_LOGOUT = "GLOBAL_EVENT_POST_LOGOUT";

    private static ATGlobalEventDispatchManager instance = null;

    public static ATGlobalEventDispatchManager init()
    {
        if (instance == null)
        {
            instance = new ATGlobalEventDispatchManager();
        }
        return instance;
    }

    public static ATGlobalEventDispatchManager getInstance()
    {
        return instance;
    }

    private ATGlobalEventDispatchManager()
    {
        mListCallback = new ArrayList<ATGlobalEventCallbackInterface>();
    }

    @Override
    public synchronized void onPreLogin() {
        for (ATGlobalEventCallbackInterface callback : mListCallback)
        {
            callback.onPreLogin();
        }
    }

    @Override
    public synchronized void onPostLogin(ATUser user) {
        for (ATGlobalEventCallbackInterface callback : mListCallback)
        {
            callback.onPostLogin(user);
        }
    }

    @Override
    public synchronized void onPostLogout() {
        for (ATGlobalEventCallbackInterface callback : mListCallback)
        {
            callback.onPostLogout();
        }
    }

    @Override
    public synchronized void onPreLogout(ATUser user) {
        for (ATGlobalEventCallbackInterface callback : mListCallback)
        {
            callback.onPreLogout(user);
        }
    }

    public synchronized void registerCallbackForDispatch(ATGlobalEventCallbackInterface callback)
    {
        mListCallback.add(callback);
    }
}
