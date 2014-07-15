package im.aktive.aktive.api_requester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONObject;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Pair;

import im.aktive.aktive.ATApplication;
import im.aktive.aktive.R;
import im.aktive.aktive.network.ATNetworkCallback;
import im.aktive.aktive.network.ATNetworkManager;

public class ATAPIManager {
	private boolean isLoggedIn = false;
	private static final String AUTHENTICITY_KEY = "authenticity_token";
	private static final String METHOD_KEY = "_method";
	
	private ATNetworkManager networkManager;
	private static ATAPIManager instance;
	private String authenticityToken = null;
	private HashSet<String> setUrlInRequest;
	
	private static String apiHostUrl = "";
	
	private synchronized boolean addRequestUrl(String url)
	{
		return setUrlInRequest.add(url);
	}
	
	private synchronized void removeRequestUrl(String url)
	{
		setUrlInRequest.remove(url);
	}
	
	
	private ATAPIManager()
	{
		networkManager = ATNetworkManager.getInstance();
        Resources res = ATApplication.getInstance().getResources();
		apiHostUrl = res.getString(R.string.api_host_url);
	}
	
	/**
	 * Singleton get instance method
	 * @return
	 */
	public static ATAPIManager getInstance()
	{
		if (instance == null)
		{
			instance = new ATAPIManager();			
		}
		return instance;
	}
	
	public String getAuthToken()
	{
		return authenticityToken;
	}
	
	public void setAuthToken(String token)
	{
		authenticityToken = token;	
	}
	
//	public String requestSync(String path, String method, Map<String, String> queryParams, JSONObject requestParams)
//	{
//		return requestSync(SERVER_URL, path, method, queryParams, requestParams);
//	}
//	
//	public String requestSync(String serverUrl, String path, String method, Map<String, String> queryParams, JSONObject requestParams)
//	{
//		byte[] body = null;
//		if (requestParams != null)
//		{
//			body = requestParams.toString().getBytes();
//		}
//		if (method.toUpperCase() != "GET")
//		{
//			if (authToken != null)
//			{
//				try {
//					requestParams.put(AUTH_KEY, authToken);
//				} catch (JSONException e) {
//					return null;
//				}
//			}
//		}
//		return networkManager.requestSync(method, path, queryParams, body, serverUrl);
//	}
	
	public boolean requestAsync(String path, String method,
                                Map<String, String> queryParams, JSONObject requestParams,
                                ATNetworkCallback callback)
	{
		return requestAsync(apiHostUrl, path, method, queryParams, requestParams, callback);
	}
	
	public boolean requestAsync(String serverUrl, String path, String method,
                                Map<String, String> queryParams, JSONObject requestParams,
                                ATNetworkCallback callback)
	{
		byte[] body = null;
		if (requestParams == null)
		{
			requestParams = new JSONObject();
		}
		
		if (!method.toUpperCase().equals("GET"))
		{
			if (authenticityToken != null)
			{
				try {
					requestParams.put(AUTHENTICITY_KEY, authenticityToken);
					//requestParams.put(AUTHENTICATION_KEY, authenticityToken);
				} catch (JSONException e) {
					return false;
				}
			}
		}
		
		return networkManager.requestAsync(method, serverUrl, path, queryParams, requestParams, callback);
	}
	
	/*public boolean requestAsyncWithFile(String path, String method,
			Map<String, String> queryParams, JSONObject requestParams, Uri file, ATNetworkCallback callback)
	{
		return requestAsyncWithFile(apiHostUrl, path, method, queryParams, requestParams, file, callback);
	}
	
	public boolean requestAsyncWithFile(String serverUrl, String path, String method, 
			Map<String, String> queryParams, JSONObject requestParams, Uri file, ATNetworkCallback callback)
	{
		if (requestParams == null)
		{
			requestParams = new JSONObject();
		}
		
		requestParams = new JSONObject();
		if (!method.toUpperCase().equals("GET"))
		{
			if (authenticityToken != null)
			{
				try {
					requestParams.put(AUTHENTICITY_KEY, authenticityToken);
					//requestParams.put(AUTHENTICATION_KEY, authenticityToken);
				} catch (JSONException e) {
					return false;
				}
			}
		}
		
		Map<String, String> flattenedObject = flattenJsonObject(requestParams, "");
		
		return networkManager.requestAsyncUploadFile(method, path, queryParams, flattenedObject, file, serverUrl, callback);
	}*/
	
	public Map<String, String> flattenJsonObject(JSONObject object, String currentPrefix)
	{
		Map<String, String> result = new HashMap<String, String>();
		Iterator<String> iter = object.keys();
	    while (iter.hasNext()) {
	        String key = iter.next();
	        try {
	            Object value = object.get(key);
	            if (currentPrefix.length() > 0)
            	{
            		key = "[" + key + "]";
            	}

                if (value instanceof JSONObject)
	            {
	            	result.putAll(flattenJsonObject((JSONObject)value, currentPrefix + key));
	            } else if (value instanceof JSONArray)
	            {
	            	/*JSONArray jsonArray = (JSONArray)value;
	            	for (int i = 0; i < jsonArray.length(); i++)
	            	{
	            		result.add(object)
	            	}
	            	this[name][][][]*/
	            	// NOT FUKING NEEDED YET
	            }
	            else {
	            	result.put(currentPrefix  + key, value.toString());
	            }
	        } catch (JSONException e) {
	            // Something went wrong!
	        }
	    }
		return result;
	}

	public static String getServerUrlFromPath(String path) {
		return apiHostUrl + path;
	}

	public void onLogout() {
		authenticityToken = null;
		ATNetworkManager.getInstance().onLogout();
	}
}
