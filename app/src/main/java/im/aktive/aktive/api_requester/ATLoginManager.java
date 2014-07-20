package im.aktive.aktive.api_requester;

import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;

import im.aktive.aktive.ATApplication;
import im.aktive.aktive.manager.ATDeviceInfoManager;
//import im.aktive.aktive.manager.ATUserPreferenceWrapper;
import im.aktive.aktive.network.ATNetworkCallback;
import im.aktive.aktive.network.ATNetworkManager;
import im.aktive.aktive.util.ATUserPreferenceWrapper;

public class ATLoginManager {
	private boolean isLoggedIn = false;
	private static final String SERVER_URL = "http://192.168.1.103:3000";//http://10.0.2.2:3000";//"http://10.0.2.2:3000";//"http://127.0.0.1:3000";//"https://kroozor.com";
	private static final String AUTHENTICITY_KEY = "authenticity_token";
	private static final String AUTHENTICATION_KEY = "authentication_token";
	 // POST
	public static final String SIGNUP_PATH = "/api/users.json";
	public static final String LOGIN_PATH = "/api/users/sign_in.json";
	public static final String LOGOUT_PATH = "/api/users/sign_out.json";
	public static final String LOGIN_EXTERNAL_PATH = "/api/mobile_auth/%1$s.json";
	
	private static final String DEVICE_TOKEN_PREF_KEY = "DEVICE_TOKEN_PREF";
	private static final String LOGIN_DEVICE_TOKEN_PATH = "/api/mobile_auth/device_token";
	
	private ATNetworkManager networkManager;
	private static im.aktive.aktive.api_requester.ATLoginManager instance;
	private String authenticityToken = null;
	private String deviceToken = null;
	private HashSet<String> setUrlInRequest;
	
	private ATLoginManager() {
		
	}
	
	public static im.aktive.aktive.api_requester.ATLoginManager getInstance() {
		if (instance == null)
		{
			instance = new im.aktive.aktive.api_requester.ATLoginManager();
		}
		return instance;
	}
	
	/*
	 * LOGIN
	 */
	public boolean login(String email, String password, final ATNetworkCallback callback)
	{
		JSONObject loginInfo = new JSONObject();
		
		try {
			JSONObject userInfo = new JSONObject();
			userInfo.put("email", email);
			userInfo.put("password", password);
			loginInfo.put("user", userInfo);
			loginInfo.put("device_id", ATDeviceInfoManager.getDeviceId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		ATNetworkCallback networkCallback = new ATNetworkCallback() {
			
			@Override
			public void onFinished(JSONObject jsonResult) {
				try {
					if (!jsonResult.has("success"))
					{
						callback.onFailed("Cannot get response from server."); // incorrect format
					}
					boolean code = jsonResult.getBoolean("success");
					if (!code)
					{
						callback.onFailed("Cannot get response from server.");
					} else {
						processLoginInfo(jsonResult);
						callback.onFinished(jsonResult);
					}
				} catch (JSONException e)
				{
					callback.onFailed("Cannot get response from server.");
				}
			}

			@Override
			public void onFailed(String errMsg) {
				callback.onFailed(errMsg);
			}
		};
		
		// wrap the callback in a network callback and call
		return ATAPIManager.getInstance().requestAsync(LOGIN_PATH, "POST", null, loginInfo, networkCallback);
	}

	public boolean loginExternal(String provider, String accessToken,
			final ATNetworkCallback callback) {
		JSONObject loginInfo = new JSONObject();
		
		try {
			loginInfo.put("access_token", accessToken);
			loginInfo.put("device_id", ATDeviceInfoManager.getDeviceId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		ATNetworkCallback networkCallback = new ATNetworkCallback() {
			
			@Override
			public void onFinished(JSONObject jsonResult) {
				try {
					if (!jsonResult.has("status"))
					{
						callback.onFailed("Cannot get response from server.");
					}
					boolean code = jsonResult.getBoolean("status");
					if (!code)
					{
						callback.onFailed("Cannot get response from server.");
					} else {
						processLoginInfo(jsonResult);
						callback.onFinished(jsonResult);
					}
					
				} catch (JSONException e)
				{
					callback.onFailed("Cannot get response from server.");
				}
			}

			@Override
			public void onFailed(String errMsg) {
				callback.onFailed(errMsg);
			}
		};
		
		String url = String.format(LOGIN_EXTERNAL_PATH, provider);
		// wrap the callback in a network callback and call
		return ATAPIManager.getInstance().requestAsync(url, "POST", null, loginInfo, networkCallback);
	}
	
	public boolean loginExternalOauth1(String provider, String accessToken,
			String tokenSecret, final ATNetworkCallback callback) {
		JSONObject loginInfo = new JSONObject();
		
		try {
			loginInfo.put("access_token", accessToken);
			loginInfo.put("token_secret", tokenSecret);
			loginInfo.put("device_id", ATDeviceInfoManager.getDeviceId());
		} catch (JSONException e) {
			return false;
		}
		
		ATNetworkCallback networkCallback = new ATNetworkCallback() {
			
			@Override
			public void onFinished(JSONObject jsonResult) {
				try {
					if (!jsonResult.has("status"))
					{
						callback.onFailed("Cannot get response from server.");
					}
					boolean code = jsonResult.getBoolean("status");
					if (!code)
					{
						callback.onFailed("Cannot get response from server.");
					} else {
						processLoginInfo(jsonResult);
						callback.onFinished(jsonResult);
					}
					
				} catch (JSONException e)
				{
					callback.onFailed("Cannot get response from server.");
				}
			}

			@Override
			public void onFailed(String errMsg) {
				callback.onFailed(errMsg);
			}
		};
		
		String url = String.format(LOGIN_EXTERNAL_PATH, provider);
		// wrap the callback in a network callback and call
		return ATAPIManager.getInstance().requestAsync(url, "POST", null, loginInfo, networkCallback);
	}
	
	public boolean tryLoginUsingDeviceToken(int userId, final ATNetworkCallback callback) {
		ATUserPreferenceWrapper prefManager = new ATUserPreferenceWrapper(ATApplication.getInstance());
		String deviceToken = prefManager.getString(DEVICE_TOKEN_PREF_KEY, null);
		if (deviceToken == null)
		{
			return false;
		}
		JSONObject loginInfo = new JSONObject();
		try {
			loginInfo.put("user_id", userId);
			loginInfo.put("device_token", deviceToken);
			loginInfo.put("device_id", ATDeviceInfoManager.getDeviceId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		ATNetworkCallback networkCallback = new ATNetworkCallback() {
			
			@Override
			public void onFinished(JSONObject jsonResult) {
				try {
					if (!jsonResult.has("status"))
					{
						callback.onFailed("Cannot get response from server.");
					}
					boolean code = jsonResult.getBoolean("status");
					if (!code)
					{
						callback.onFailed("Cannot get response from server.");
					} else {
						processLoginInfo(jsonResult);
						callback.onFinished(jsonResult);
					}
					
				} catch (JSONException e)
				{
					callback.onFailed("Cannot get response from server.");
				}
			}

			@Override
			public void onFailed(String errMsg) {
				callback.onFailed(errMsg);
			}
		};
		
		String url = LOGIN_DEVICE_TOKEN_PATH;
		// wrap the callback in a network callback and call
		return ATAPIManager.getInstance().requestAsync(url, "POST", null, loginInfo, networkCallback);
	}
	
	private void processLoginInfo(JSONObject jsonResult) {
		try {
			authenticityToken = jsonResult.getString("auth_token");
			deviceToken = jsonResult.getString("device_access_token");
			// save device token
			new ATUserPreferenceWrapper(ATApplication.getInstance()).saveAndCommitToPreference(DEVICE_TOKEN_PREF_KEY, deviceToken);
			isLoggedIn = true;
			ATAPIManager.getInstance().setAuthToken(authenticityToken);
			// read user json
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean signup(String fullName, String email, String password,
			String passwordConfirmation, final ATNetworkCallback callback) {
		JSONObject loginInfo = new JSONObject();

		try {
			JSONObject userInfo = new JSONObject();
			userInfo.put("full_name", fullName);
			userInfo.put("email", email);
			userInfo.put("password", password);
			userInfo.put("password_confirmation", passwordConfirmation);
			loginInfo.put("user", userInfo);
			loginInfo.put("device_id", ATDeviceInfoManager.getDeviceId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}

		ATNetworkCallback networkCallback = new ATNetworkCallback() {

			@Override
			public void onFinished(JSONObject jsonResult) {
				try {
					if (jsonResult.has("status"))
					{
						boolean code = jsonResult.getBoolean("status");
						if (!code)
						{
							callback.onFailed("Cannot get response from server.");
						} else {
							processLoginInfo(jsonResult);
							callback.onFinished(jsonResult);
						}
					} else {
						if (jsonResult.has("errors"))
						{
							String errorCode = jsonResult.getString("errors");
							callback.onFailed(errorCode);
						}
						else {
							callback.onFailed("Cannot get response from server."); // incorrect format
						}
					}

				} catch (JSONException e)
				{
					callback.onFailed("Cannot get response from server.");
				}
			}

			@Override
			public void onFailed(String errMsg) {
				callback.onFailed(errMsg);
			}
		};

		// wrap the callback in a network callback and call
		return ATAPIManager.getInstance().requestAsync(SIGNUP_PATH, "POST", null, loginInfo, networkCallback);
	}

	public void logout() {
		// wrap the callback in a network callback and call
		ATAPIManager.getInstance().requestAsync(LOGOUT_PATH, "POST", null, null, null);
		ATAPIManager.getInstance().onLogout();
		authenticityToken = null;
		isLoggedIn = false;
		deviceToken = null;
		new ATUserPreferenceWrapper(ATApplication.getInstance()).saveAndCommitToPreference(DEVICE_TOKEN_PREF_KEY, deviceToken);
	}
}
