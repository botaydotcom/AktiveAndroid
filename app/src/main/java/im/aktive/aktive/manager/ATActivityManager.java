package im.aktive.aktive.manager;

import android.util.Pair;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import im.aktive.aktive.model.ATActivity;
import im.aktive.aktive.model.ATUser;

/**
 * Created by hoangtran on 17/7/14.
 */
public class ATActivityManager extends ATBaseManager<ATActivity> {
    protected static final String TAG = "ActivityManager";
    private HashMap<Integer, ATActivity> mapActivity;

    private ATActivityManager() {
        mapActivity = new HashMap<Integer, ATActivity>();
    }

    private static ATActivityManager instance = null;

    public static ATActivityManager getInstance()
    {
        if (instance == null)
        {
            instance = new ATActivityManager();
        }
        return instance;
    }

    public ATActivity getOrCreateActivity(int activityId) {
        ATActivity activity = mapActivity.get(Integer.valueOf(activityId));
        if (activity == null) {
            activity = new ATActivity(activityId);
            mapActivity.put(Integer.valueOf(activityId), activity);
        }
        return activity;
    }

    public ATActivity getActivity(int activityId) {
        return mapActivity.get(activityId);
    }
}
