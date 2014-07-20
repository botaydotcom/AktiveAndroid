package im.aktive.aktive.api_requester;

import org.json.JSONException;
import org.json.JSONObject;

import im.aktive.aktive.helper.ATDateTimeUtils;
import im.aktive.aktive.network.ATNetworkCallback;

/**
 * Created by hoangtran on 21/7/14.
 */
public class ATActivityAPIRequester {
    private static final String TAG = "ATActivityAPIRequester";
    public static final String GET_ACTIVITY_LIST = "/api/profile/activities.json";
    private static final String POST_ACTIVITY = "/api/profile/activities.json";

    private ATAPIManager apiManager;

    public ATActivityAPIRequester()
    {
        apiManager = ATAPIManager.getInstance();
    }

    public boolean postActivity(String name, String description, ATNetworkCallback callback) {
        JSONObject requestObject = new JSONObject();
        JSONObject activityObject = new JSONObject();
        try {
            activityObject.put("name", name);
            activityObject.put("description", description);
            requestObject.put("activity", activityObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean result = apiManager.requestAsync(POST_ACTIVITY, "POST", null, requestObject, callback);
        return result;
    }
}
