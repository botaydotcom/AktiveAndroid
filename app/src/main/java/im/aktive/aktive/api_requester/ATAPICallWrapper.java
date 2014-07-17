package im.aktive.aktive.api_requester;

import android.app.Activity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import im.aktive.aktive.model.ATModelRequestCallback;
import im.aktive.aktive.network.ATNetworkCallback;

/**
 * Created by hoangtran on 14/7/14.
 */
public class ATAPICallWrapper {

    private Map<Integer, ATModelRequestCallback> mapModelRequestCallback =
            new HashMap<Integer, ATModelRequestCallback>();
    private ReentrantLock mapLock = new ReentrantLock();

    public ATAPICallWrapper()
    {
    }

    public void onRequestSuccessful(int requestId, Object result)
    {
        mapLock.lock();
        ATModelRequestCallback cb = mapModelRequestCallback.get(Integer.valueOf(requestId));
        mapLock.unlock();
        if (cb != null)
        {
            cb.onSuccess(result);
        }
    }

    public void onRequestFailed(int requestId, String message)
    {
        mapLock.lock();
        ATModelRequestCallback cb = mapModelRequestCallback.get(Integer.valueOf(requestId));
        mapLock.unlock();
        if (cb != null)
        {
            cb.onFailed(message);
        }
    }

    public void addCallbackForRequestId(int requestId, ATModelRequestCallback cb) {
        mapLock.lock();
        mapModelRequestCallback.put(Integer.valueOf(requestId), cb);
        mapLock.unlock();
    }

    public void onStop()
    {
        // cancel any cancelable request
        mapLock.lock();
        mapModelRequestCallback.clear();
        mapLock.unlock();
    }
}
