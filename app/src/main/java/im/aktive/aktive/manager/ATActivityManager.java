package im.aktive.aktive.manager;

import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import im.aktive.aktive.api_requester.ATActivityAPIRequester;
import im.aktive.aktive.model.ATActivity;
import im.aktive.aktive.model.ATUser;
import im.aktive.aktive.model.ATUserActivity;
import im.aktive.aktive.model.ATWrappedModelRequestCallback;
import im.aktive.aktive.network.ATNetworkCallback;
import im.aktive.aktive.serializer.ATActivitySerializer;

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

    public boolean postActivity(String name, String description, ATWrappedModelRequestCallback cb) {
        final int requestId = addRequestCallback(cb);
        ATNetworkCallback callback = new ATNetworkCallback() {

            @Override
            public void onFinished(JSONObject jsonObject) {
                try {
                    JSONObject activityObject = jsonObject.getJSONObject("activity");
                    ATActivitySerializer activitySerializer = new ATActivitySerializer().deserialize(activityObject);
                    ATActivity activity = updateFromSerializer(activitySerializer);
                    deliverResult(requestId, true, activity, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailed("Cannot parse response from server");
                }
            }

            @Override
            public void onFailed(String errMsg) {
                deliverResult(requestId, true, null, errMsg);
            }
        };
        ATActivityAPIRequester controller = new ATActivityAPIRequester();
        return controller.postActivity(name, description, callback);
    }
}
