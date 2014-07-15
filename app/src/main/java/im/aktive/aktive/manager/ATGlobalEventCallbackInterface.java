package im.aktive.aktive.manager;

import im.aktive.aktive.model.ATUser;

/**
 * Created by hoangtran on 14/7/14.
 */
public interface ATGlobalEventCallbackInterface {
    void onPreLogin();
    void onPostLogin(ATUser user);
    void onPreLogout(ATUser user);
    void onPostLogout();
}
