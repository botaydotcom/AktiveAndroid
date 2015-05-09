package im.aktive.aktive.api_requester;

import java.util.HashMap;
import java.util.Map;

import im.aktive.aktive.network.ATNetworkCallback;

/**
 * Created by hoangtran on 19/7/14.
 */
public class ATTagAPIManager {
    private static final String TAG = "ATTagAPIRequester";
    public static final String GET_TAGS = "/api/tags.json";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private ATAPIManager apiManager;

    public ATTagAPIManager()
    {
        apiManager = ATAPIManager.getInstance();
    }

    public boolean fetchTagsForFirstTime(final ATNetworkCallback callback) {
        Map<String, String> queryParams = new HashMap<String,String>();
        queryParams.put("is_first_time", TRUE);
        boolean result = apiManager.requestAsync(GET_TAGS, "GET", queryParams, null, callback);
        return result;
    }

    public boolean fetchAllTags(final ATNetworkCallback callback) {
        Map<String, String> queryParams = new HashMap<String,String>();
        queryParams.put("is_first_time", FALSE);
        boolean result = apiManager.requestAsync(GET_TAGS, "GET", queryParams, null, callback);
        return result;
    }
}
