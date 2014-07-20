package im.aktive.aktive.manager;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import im.aktive.aktive.api_requester.ATUserAPIRequester;
import im.aktive.aktive.api_requester.ATUserActivityAPIRequester;
import im.aktive.aktive.model.ATActivity;
import im.aktive.aktive.model.ATUser;
import im.aktive.aktive.model.ATUserActivity;
import im.aktive.aktive.model.ATWrappedModelRequestCallback;
import im.aktive.aktive.network.ATNetworkCallback;
import im.aktive.aktive.serializer.ATUserActivityInListSerializer;

/**
 * Created by hoangtran on 16/7/14.
 */
public class ATUserActivityManager extends ATBaseManager<ATUserActivity>{
    private static final String TAG = "UserManager";
    private Map<Integer, ATUserActivity> mapUserActivity = new HashMap<Integer, ATUserActivity>();
    private static ATUserActivityManager instance = null;
    private int currentOffsetDoneList = 0;

    private ATUserActivityManager() {
    }

    public static ATUserActivityManager getInstance()
    {
        if (instance == null)
        {
            instance = new ATUserActivityManager();
        }
        return instance;
    }

    public ATUserActivity getUserActivity(int id)
    {
        return mapUserActivity.get(Integer.valueOf(id));
    }

    public ATUserActivity getOrCreateUserActivity(int id, ATUser user, ATActivity activity)
    {
        ATUserActivity userActivity = getUserActivity(id);
        if (userActivity == null)
        {
            userActivity = new ATUserActivity(id, user, activity);
            mapUserActivity.put(Integer.valueOf(id), userActivity);
        }
        return userActivity;
    }

    public List<ATUserActivity> getListTodoUserActivity()
    {
        List<ATUserActivity> listResult = new ArrayList<ATUserActivity>();
        listResult.addAll(mapUserActivity.values());
        return listResult;
    }

    public void fetchTodoUserActivity()
    {
        ATNetworkCallback callback = new ATNetworkCallback() {
            @Override
            public void onFinished(JSONObject jsonObject) {
                JSONArray userActivitiesArray;
                try {
                    userActivitiesArray = jsonObject.getJSONArray("user_activities");
                    ATUserActivityInListSerializer[] userActivitySerializerList =
                            new ATUserActivityInListSerializer[userActivitiesArray.length()];
                    for (int i = 0; i < userActivitiesArray.length(); i++)
                    {
                        JSONObject object = userActivitiesArray.getJSONObject(i);
                        ATUserActivityInListSerializer userActivity =
                                new ATUserActivityInListSerializer().deserialize(object);
                        userActivitySerializerList[i] = userActivity;
                    }
                    int prevSize = mapUserActivity.values().size();
                    List<ATUserActivity> userActivityList = updateFromListSerializer(userActivitySerializerList);
                    int newSize = mapUserActivity.values().size();
                    currentOffsetDoneList = currentOffsetDoneList + (newSize - prevSize);
                    notifyObserver();
                } catch (JSONException e)
                {
                } catch (Exception e)
                {
                }
            }

            @Override
            public void onFailed(String errMsg) {
            }
        };
        ATUserActivityAPIRequester apiRequester = new ATUserActivityAPIRequester();
        apiRequester.fetchTodoUserActivity(currentOffsetDoneList, 20, callback);
    }

    @Override
    public void onPostLogin(ATUser user)
    {
        fetchTodoUserActivity();
    }

    public boolean postTodoUserActivity(ATActivity activity, Date deadline, ATWrappedModelRequestCallback cb) {
        final int requestId = addRequestCallback(cb);
        ATNetworkCallback callback = new ATNetworkCallback() {

            @Override
            public void onFinished(JSONObject jsonObject) {
                try {
                    JSONObject userActivityObject = jsonObject.getJSONObject("user_activity");
                    ATUserActivityInListSerializer userActivitySerializer =
                            new ATUserActivityInListSerializer().deserialize(userActivityObject);
                    ATUserActivity userActivity = updateFromSerializer(userActivitySerializer);
                    deliverResult(requestId, true, userActivity, null);
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
        ATUserActivityAPIRequester controller = new ATUserActivityAPIRequester();
        return controller.postTodoUserActivity(activity, deadline, callback);
    }
}
