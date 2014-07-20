package im.aktive.aktive.api_requester;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import im.aktive.aktive.helper.ATDateTimeUtils;
import im.aktive.aktive.model.ATActivity;
import im.aktive.aktive.network.ATNetworkCallback;

/**
 * Created by hoangtran on 17/7/14.
 */
public class ATUserActivityAPIRequester {
    private static final String TAG = "ATUserActivityAPIRequester";
    public static final String GET_PROFILE_ACTIVITY_LIST = "/api/profile/user_activities.json";
    private static final String POST_TODO_ACTIVITY = "/api/profile/user_activities.json";

    private ATAPIManager apiManager;

    public ATUserActivityAPIRequester()
    {
        apiManager = ATAPIManager.getInstance();
    }

    public boolean fetchTodoUserActivity(int offset, int limit, final ATNetworkCallback callback) {
        Map<String, String> queryParams = new HashMap<String,String>();
        queryParams.put("offset", String.valueOf(offset));
        queryParams.put("limit", String.valueOf(limit));
        boolean result = apiManager.requestAsync(GET_PROFILE_ACTIVITY_LIST, "GET", queryParams, null, callback);
        return result;
    }

    public boolean postTodoUserActivity(ATActivity activity, Date deadline, ATNetworkCallback callback) {
        JSONObject requestObject = new JSONObject();
        JSONObject userActivityObject = new JSONObject();
        try {
            userActivityObject.put("activity_id", activity.getId());
            ATDateTimeUtils.updateObjectWithRailsJSONOBjectForDateTime(deadline, "deadline", userActivityObject);
            requestObject.put("user_activity", userActivityObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean result = apiManager.requestAsync(POST_TODO_ACTIVITY, "POST", null, requestObject, callback);
        return result;
    }
}
