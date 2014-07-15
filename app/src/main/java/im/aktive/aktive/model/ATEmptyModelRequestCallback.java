package im.aktive.aktive.model;

import org.json.JSONException;
import org.json.JSONObject;

import im.aktive.aktive.model.ATModelRequestCallback;

public class ATEmptyModelRequestCallback implements ATModelRequestCallback {
    @Override
    public void onSuccess(Object object) {
    }

    @Override
	public void onFailed(String errMsg) {
	}
}
