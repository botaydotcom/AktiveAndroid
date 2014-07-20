package im.aktive.aktive.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

import im.aktive.aktive.ATApplication;
//import im.aktive.aktive.utils.ATConnectionManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

public class ATNetworkManager {
	private static final String TAG = "ATNetworkManager";
	public HttpURLConnection connection;
	private static im.aktive.aktive.network.ATNetworkManager instance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private ATOkHttpStack httpStack = null;
	private HttpContext localContext = new BasicHttpContext();
	private static final int BUFFER_SIZE = 2048;
    private static final String GENERAL_REQUEST_TAG = "General Request";

    /**
     * Singleton get instance method
     * @return
     */
    public static ATNetworkManager getInstance()
    {
        if (instance == null)
        {
            instance = new ATNetworkManager();
        }
        return instance;
    }

	private ATNetworkManager()
	{

	}

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            httpStack = new ATOkHttpStack();
            mRequestQueue = Volley.newRequestQueue(ATApplication.getInstance(), httpStack);
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            //mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? GENERAL_REQUEST_TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(GENERAL_REQUEST_TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
	
	public void onLogout() {
		httpStack.clearCookies();
	}
	
	public String getGetUrl(String serverUrl, String path, Map<String, String> params)
	{
		String result = serverUrl + path;
		if (params == null || params.size() == 0)
			return result;
		if (!result.contains("?"))
		{
			result = result + "?";
		}
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet())
        {
			String encodedKey;
			String encodedValue;
			try {
				encodedKey = URLEncoder.encode(entry.getKey(), "UTF-8");
				encodedValue = URLEncoder.encode(entry.getValue(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// dont append this to the path then
				continue;
			}
			result = result + (i>0 ? "&" : "") + encodedKey + "=" + encodedValue;
            i++;
		}
		return result;
	}

    /*
    REQUEST ASYNC METHODS
     */
	
	public boolean requestAsync(String methodStr, String serverUrl, String path, Map<String, String> params, JSONObject jsonObject,
                                final ATNetworkCallback callback, String tag)
	{
        int method = Request.Method.GET;
        if (methodStr.equals("POST"))
        {
            method = Request.Method.POST;
        } else if (methodStr.equals("PUT"))
        {
            method = Request.Method.PUT;
        } else if (methodStr.equals("DELETE"))
        {
            method = Request.Method.DELETE;
        }
        String urlStr = null;
        if (methodStr.equals("GET"))
        {
            urlStr = getGetUrl(serverUrl, path, params);
        } else {
            urlStr = getGetUrl(serverUrl, path, null);
        }
        Log.d(TAG, "Request: " + methodStr + " to: " + urlStr);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method,
                urlStr, jsonObject,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Successful: " + response.toString());
                        callback.onFinished(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        callback.onFailed(error.getMessage());
                    }
                });
        addToRequestQueue(jsonObjReq, tag);
        return true;
	}
    /*
        CONVENIENT METHODS
     */
    public boolean requestAsync(String serverUrl, String path, final ATNetworkCallback callback)
    {
        return requestAsync("GET", serverUrl, path, null, null, callback, GENERAL_REQUEST_TAG);
    }

    public boolean requestAsync(String methodStr, String serverUrl, String path, final ATNetworkCallback callback)
    {
        return requestAsync(methodStr, serverUrl, path, null, null, callback, GENERAL_REQUEST_TAG);
    }

    public boolean requestAsync(String methodStr, String serverUrl, String path, JSONObject jsonObject,
                                final ATNetworkCallback callback)
    {
        return requestAsync(methodStr, serverUrl, path, null, jsonObject, callback, GENERAL_REQUEST_TAG);
    }

    public boolean requestAsync(String methodStr, String serverUrl, String path, Map<String, String> params, JSONObject jsonObject,
                                final ATNetworkCallback callback)
    {
        return requestAsync(methodStr, serverUrl, path, params, jsonObject, callback, GENERAL_REQUEST_TAG);
    }
	
	/*
	 * Helpers
	 */
	byte[] readFully(InputStream in) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] buffer = new byte[BUFFER_SIZE];
	    for (int count; (count = in.read(buffer)) != -1; ) {
	      out.write(buffer, 0, count);
	    }
	    return out.toByteArray();
	  }
}
