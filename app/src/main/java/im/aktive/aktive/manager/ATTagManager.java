package im.aktive.aktive.manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import im.aktive.aktive.api_requester.ATTagAPIManager;
import im.aktive.aktive.model.ATTag;
import im.aktive.aktive.model.ATTagValue;
import im.aktive.aktive.model.ATUser;
import im.aktive.aktive.model.ATWrappedModelRequestCallback;
import im.aktive.aktive.network.ATNetworkCallback;
import im.aktive.aktive.serializer.ATTagSerializer;

/**
 * Created by hoangtran on 18/7/14.
 */
public class ATTagManager extends ATBaseManager<ATTag> {
    private static final String TAG = "TagManager";
    private Map<Integer, ATTag> mapTag = new HashMap<Integer, ATTag>();
    private static ATTagManager instance = null;

    private ATTagManager() {
    }

    public static ATTagManager getInstance()
    {
        if (instance == null)
        {
            instance = new ATTagManager();
        }
        return instance;
    }

    public ATTag getTag(int id)
    {
        return mapTag.get(Integer.valueOf(id));
    }

    public ATTag getOrCreateTag(int id)
    {
        ATTag tag = getTag(id);
        if (tag == null)
        {
            tag = new ATTag(id);
            mapTag.put(Integer.valueOf(id), tag);
        }
        return tag;
    }

    public List<ATTag> getListTag()
    {
        return getListTag(false);
    }

    public List<ATTag> getListTag(boolean forFirstTime)
    {
        List<ATTag> listResult = new ArrayList<ATTag>();
        for (ATTag tag : mapTag.values())
        {
            if (tag.getCanBeUsedForFirstTime() == forFirstTime)
            {
                listResult.add(tag);
            }
        }
        return listResult;
    }

    public void fetchTagsForFirstTime(ATWrappedModelRequestCallback cb)
    {
        final int requestId = addRequestCallback(cb);
        ATNetworkCallback callback = new ATNetworkCallback() {
            @Override
            public void onFinished(JSONObject jsonObject) {
                JSONArray tagArray;
                try {
                    tagArray = jsonObject.getJSONArray("tags");
                    ATTagSerializer[] tagSerializerList = new ATTagSerializer[tagArray.length()];
                    for (int i = 0; i < tagArray.length(); i++)
                    {
                        JSONObject object = tagArray.getJSONObject(i);
                        ATTagSerializer tagSerializer = new ATTagSerializer().deserialize(object);
                        tagSerializerList[i] = tagSerializer;
                    }
                    List<ATTag> listTags = ATTagManager.getInstance().updateFromListSerializer(tagSerializerList);
                    deliverResult(requestId, true, listTags, null);
                } catch (JSONException e)
                {
                    onFailed("Cannot parse result from server");
                } catch (Exception e)
                {
                    onFailed("Cannot parse result from server");
                }
            }

            @Override
            public void onFailed(String errMsg) {
                deliverResult(requestId, false, null, errMsg);
            }
        };
        ATTagAPIManager controller = new ATTagAPIManager();
        controller.fetchTagsForFirstTime(callback);
    }

    public void fetchTags(ATWrappedModelRequestCallback cb)
    {
        final int requestId = addRequestCallback(cb);
        ATNetworkCallback callback = new ATNetworkCallback() {
            @Override
            public void onFinished(JSONObject jsonObject) {
                JSONArray tagArray;
                try {
                    tagArray = jsonObject.getJSONArray("tags");
                    ATTagSerializer[] tagSerializerList = new ATTagSerializer[tagArray.length()];
                    for (int i = 0; i < tagArray.length(); i++)
                    {
                        JSONObject object = tagArray.getJSONObject(i);
                        ATTagSerializer tagSerializer = new ATTagSerializer().deserialize(object);
                        tagSerializerList[i] = tagSerializer;
                    }
                    List<ATTag> listTags = ATTagManager.getInstance().updateFromListSerializer(tagSerializerList);
                    deliverResult(requestId, true, listTags, null);
                } catch (JSONException e)
                {
                    onFailed("Cannot parse result from server");
                } catch (Exception e)
                {
                    onFailed("Cannot parse result from server");
                }
            }

            @Override
            public void onFailed(String errMsg) {
                deliverResult(requestId, false, null, errMsg);
            }
        };
        ATTagAPIManager controller = new ATTagAPIManager();
        controller.fetchAllTags(callback);
    }

    @Override
    public void onPostLogin(ATUser user)
    {
        fetchTags(null);
    }

    private int min(int a, int b)
    {
        return a < b? a: b;
    }

    public List<ATTag> getListTagForSuggestion() {
        List<ATTag> listResult = getListTag(false);
        Random random = new Random();
        for (int i = 0; i <= listResult.size() - min(5, listResult.size()); i++)
        {
            int j = random.nextInt(listResult.size());
            listResult.remove(j);
        }
        return listResult;
    }
}
