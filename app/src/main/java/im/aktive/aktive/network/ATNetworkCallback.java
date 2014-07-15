package im.aktive.aktive.network;

import org.json.JSONObject;

public interface ATNetworkCallback {
    void onFinished(JSONObject result);
    void onFailed(String errMsg);

}
