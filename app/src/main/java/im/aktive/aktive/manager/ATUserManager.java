package im.aktive.aktive.manager;

import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import im.aktive.aktive.ATApplication;
import im.aktive.aktive.api_requester.ATAPICallWrapper;
import im.aktive.aktive.api_requester.ATLoginManager;
import im.aktive.aktive.model.ATModelRequestCallback;
import im.aktive.aktive.model.ATUser;
import im.aktive.aktive.model.ATWrappedModelRequestCallback;
import im.aktive.aktive.network.ATNetworkCallback;
import im.aktive.aktive.notification.ATGlobalEventDispatchManager;
import im.aktive.aktive.serializer.ATSerializer;
import im.aktive.aktive.serializer.ATUserProfileSerializer;
import im.aktive.aktive.util.ATUserPreferenceWrapper;

/**
 * Created by hoangtran on 14/7/14.
 */
public class ATUserManager extends ATBaseManager<ATUser>{
    private static final String USER_ID_PREF_KEY = "USER_ID_PREF";

    protected static final String TAG = "UserManager";

    private ATUserManager() {
        mapUser = new HashMap<Integer, ATUser>();
        setFriend = new HashSet<Pair<Integer, Integer>>();
        mapFriendList = new HashMap<Integer, List<ATUser>>();
    }

    private static Date lastUpdateFriendList = null;

    private static ATUserManager instance = null;
    private int currentUserId = -1;
    private ATUser currentUser = null;
    private HashMap<Integer, ATUser> mapUser;
    private HashMap<Integer, List<ATUser>> mapFriendList = null;
    private HashSet<Pair<Integer, Integer>> setFriend = null;

    public static ATUserManager getInstance() {
        if (instance == null) {
            instance = new ATUserManager();
        }
        return instance;
    }

    public boolean isCurrentUser(ATUser user) {
        if (currentUser == null) {
            if (user == null) {
                return true;
            } else {
                return false;
            }
        }
        if (user == null) {
            return false;
        }
        return currentUser.getId() == user.getId();
    }

    @Override
    public void onPostLogout()
    {
        //super.onPostLogout();
        currentUser = null;
        currentUserId = -1;
        mapUser = new HashMap<Integer, ATUser>();
        setFriend = new HashSet<Pair<Integer, Integer>>();
        mapFriendList = new HashMap<Integer, List<ATUser>>();
        lastUpdateFriendList = null;
    }

    /**
     * LOGIN METHODS
     */

    private ATUser processLoginInfo(JSONObject object) {
        boolean result;
        try {
            result = object.getBoolean("success");
            JSONObject userJSON;
            if (!result) {
                // LoginCallback.onFailed(result);
            }
            userJSON = object.getJSONObject("user");
            ATUserProfileSerializer userSerializer = new ATUserProfileSerializer()
                    .deserialize(userJSON);
            ATUser user = updateFromSerializer(userSerializer);
            setCurrentUser(user);
			/*if (user != null)
			{
				BugSenseHandler.setUserIdentifier(String.valueOf(user.id));
			}*/
            //ATGoogleAnalyticsManager.setUserId(user.id);
            ATGlobalEventDispatchManager.getInstance().onPostLogin(user);
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean login(String email, String password,
                         ATWrappedModelRequestCallback cb) {
        final int requestId = addRequestCallback(cb);
        ATNetworkCallback callback = new ATNetworkCallback() {

            @Override
            public void onFinished(JSONObject resultJSON) {
                ATUser user = processLoginInfo(resultJSON);
                if (user != null) {
                    deliverResult(requestId, true, user, null);
                }
            }

            @Override
            public void onFailed(String errMsg) {
                deliverResult(requestId, false, null, errMsg);
            }
        };

        return ATLoginManager.getInstance().login(email, password, callback);
    }

    public boolean loginExternal(String provider, String accessToken,
                                 ATWrappedModelRequestCallback cb) {
        final int requestId = addRequestCallback(cb);
        ATNetworkCallback callback = new ATNetworkCallback() {

            @Override
            public void onFinished(JSONObject resultJSON) {
                ATUser user = processLoginInfo(resultJSON);
                if (user != null) {
                    deliverResult(requestId, true, user, null);
                }
            }

            @Override
            public void onFailed(String errMsg) {
                deliverResult(requestId, true, null, errMsg);
            }
        };
        return ATLoginManager.getInstance().loginExternal(provider,
                accessToken, callback);
    }

    public boolean loginExternalOauth1(String provider, String accessToken,
                                       String tokenSecret, final ATModelRequestCallback cb) {
        ATNetworkCallback callback = new ATNetworkCallback() {

            @Override
            public void onFinished(JSONObject resultJSON) {
                ATUser user = processLoginInfo(resultJSON);
                if (user != null) {
                    //cb.onSuccess(user);
                    callSuccessCallbackOnUIThread(cb, user);
                }
            }

            @Override
            public void onFailed(String errMsg) {
                //cb.onFailed(errMsg);
                callFailedCallbackOnUIThread(cb, errMsg);

            }
        };
        return ATLoginManager.getInstance().loginExternalOauth1(provider,
                accessToken, tokenSecret, callback);
    }

    public boolean tryLoginUsingDeviceToken(final ATModelRequestCallback cb) {
        ATUserPreferenceWrapper prefManager = new ATUserPreferenceWrapper(
                ATApplication.getInstance());
        int userId = prefManager.getInt(USER_ID_PREF_KEY, -1);
        if (userId == -1) {
            return false;
        }

        ATNetworkCallback callback = new ATNetworkCallback() {

            @Override
            public void onFinished(JSONObject resultJSON) {
                ATUser user = processLoginInfo(resultJSON);
                if (user != null) {
                    //cb.onSuccess(user);
                    callSuccessCallbackOnUIThread(cb, user);
                }
            }

            @Override
            public void onFailed(String errMsg) {
                //cb.onFailed(errMsg);
                callFailedCallbackOnUIThread(cb, errMsg);
            }
        };
        return ATLoginManager.getInstance().tryLoginUsingDeviceToken(userId,
                callback);
    }

    public void logout(final ATModelRequestCallback cb) {
        ATGlobalEventDispatchManager.getInstance().onPreLogout(currentUser);
        ATLoginManager.getInstance().logout();
		/*BugSenseHandler.setUserIdentifier("");*/
        ATGlobalEventDispatchManager.getInstance().onPostLogout();
        //ATGoogleAnalyticsManager.dispatchStat();
        //ATGoogleAnalyticsManager.startNewSessionTracking(R.string.screen_new_session_post_logout);
        cb.onSuccess(null);
    }

    protected void setCurrentUser(ATUser user) {
        currentUser = user;
        if (user != null) {
            currentUserId = user.getId();
            new ATUserPreferenceWrapper(ATApplication.getInstance())
                    .saveAndCommitToPreference(USER_ID_PREF_KEY, user.getId());
        } else {
            new ATUserPreferenceWrapper(ATApplication.getInstance())
                    .removeAndCommitPreference(USER_ID_PREF_KEY);
        }
    }

    public ATUser getCurrentUser() {
        return currentUser;
    }

    public ATUser getOrCreateUser(int userId) {
        ATUser user = mapUser.get(Integer.valueOf(userId));
        if (user == null) {
            user = new ATUser(userId);
            mapUser.put(Integer.valueOf(userId), user);
        }
        return user;
    }

    public ATUser getUser(int userId) {
        return mapUser.get(userId);
    }

    public int getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getId();
        }
        return -1;
    }
}
