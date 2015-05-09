package im.aktive.aktive.api_requester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import im.aktive.aktive.util.ATDateTimeUtils;
import im.aktive.aktive.model.ATUser;
import im.aktive.aktive.network.ATNetworkCallback;

/**
 * Created by hoangtran on 19/7/14.
 */
public class ATUserAPIRequester {
    private ATAPIManager apiManager;

    /*
	 * USER RELATED
	 */
    public static final String GET_USER = "/api/users/%1$d.json";
    public static final String POST_USER = "/api/users.json";
    public static final String UPDATE_USER = "/api/profile.json";
    public static final String DELETE_USER = "/api/users/%1$d.json";

    private static final String POST_AVATAR_FOR_USER = "/api/profile/avatar.json";
    private static final String POST_COVER_FOR_USER = "/api/profile/cover.json";

    private static final String INPUT_TAG_FIRST_TIME = "/api/profile/input_first_time.json";
    private static final String INPUT_TAG = "/api/profile/input.json";
    public ATUserAPIRequester()
    {
        apiManager = ATAPIManager.getInstance();
    }

    public boolean requestUser(int userId, final ATNetworkCallback callback) {
        String url = String.format(GET_USER, userId);
        boolean result = apiManager.requestAsync(url, "GET", null, null, callback);
        return result;
    }

    public boolean updateUser(ATUser user, final ATNetworkCallback callback) {
        JSONObject requestParams = new JSONObject();
        JSONObject userParams = new JSONObject();
        try {
            userParams.put("full_name", user.getBeingFullName());
            if (user.getBeingGender() != null)
            {
                userParams.put("gender", user.getBeingGender().toServerString());
            }
            if (user.getBeingLocation() != null)
            {
                userParams.put("location", user.getBeingLocation());
            }

            if (user.getBeingBio() != null)
            {
                userParams.put("bio", user.getBeingBio());
            }

            if (user.getBeingBirthday() != null)
            {
                ATDateTimeUtils.updateObjectWithRailsJSONOBjectForDate(user.getBeingBirthday(),
                        "birth_day", userParams);
            }
            requestParams.put("user", userParams);
        } catch (JSONException e) {
            return false;
        }
        String url = String.format(UPDATE_USER, user.getId());
        boolean result = apiManager.requestAsync(url, "PUT", null, requestParams, callback);
        return result;
    }

    public boolean inputTagFirstTime(Map<Integer, Integer> mapTagAnswer, ATNetworkCallback callback) {
        JSONObject requestObject = new JSONObject();
        JSONArray tagObjectList = new JSONArray();
        try {
            for (Integer key : mapTagAnswer.keySet())
            {
                JSONObject answerObject = new JSONObject();
                answerObject.put("id", key);
                answerObject.put("tag_value_id", mapTagAnswer.get(key));
                tagObjectList.put(answerObject);
            }
            requestObject.put("tags", tagObjectList);
        } catch (JSONException e) {
            return false;
        }
        String url = INPUT_TAG_FIRST_TIME;
        boolean result = apiManager.requestAsync(url, "POST", null, requestObject, callback);
        return result;
    }

    public boolean inputTag(Map<Integer, Integer> mapTagAnswer, ATNetworkCallback callback) {
        JSONObject requestObject = new JSONObject();
        JSONArray tagObjectList = new JSONArray();
        try {
            for (Integer key : mapTagAnswer.keySet())
            {
                JSONObject answerObject = new JSONObject();
                answerObject.put("id", key);
                answerObject.put("tag_value_id", mapTagAnswer.get(key));
                tagObjectList.put(answerObject);
            }
            requestObject.put("tags", tagObjectList);
        } catch (JSONException e) {
            return false;
        }
        String url = INPUT_TAG;
        boolean result = apiManager.requestAsync(url, "POST", null, requestObject, callback);
        return result;
    }
}
