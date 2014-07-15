package im.aktive.aktive.util;

/**
 * Created by hoangtran on 14/7/14.
 */
import java.util.Set;
import android.content.Context;
import android.content.SharedPreferences;

public class ATUserPreferenceWrapper {
    private static final String DEFAULT_APP_PREFERENCE_NAME = "DEFAULT_SHARED_PREFERENCE";
    private static String prefFileName;
    private Context context = null;
    private static ATUserPreferenceWrapper instance = null;
    private SharedPreferences.Editor mEditor = null;
    private SharedPreferences mSharedPreferences = null;

    public ATUserPreferenceWrapper(Context context) {
        this(context, DEFAULT_APP_PREFERENCE_NAME);
    }

    public ATUserPreferenceWrapper(Context context, String fileName) {
        this.context = context;
        this.mSharedPreferences = context.getSharedPreferences(fileName, 0);
    }

    public void startEditPreference() {
        mEditor = mSharedPreferences.edit();
    }

    public void commitEditPreference() {
        mEditor.apply();
    }

    public void saveToPreference(String key, String value) {
        mEditor.putString(key, value);
    }

    public void saveToPreference(String key, Long value) {
        mEditor.putLong(key, value);
    }

    public void saveToPreference(String key, Integer value) {
        mEditor.putInt(key, value);
    }

    public void saveToPreference(String key, Float value) {
        mEditor.putFloat(key, value);
    }

    public void saveToPreference(String key, Boolean value) {
        mEditor.putBoolean(key, value);
    }

    public void saveAndCommitToPreference(String key, String value) {
        startEditPreference();
        saveToPreference(key, value);
        commitEditPreference();
    }

    public void saveAndCommitToPreference(String key, Long value) {
        startEditPreference();
        saveToPreference(key, value);
        commitEditPreference();
    }

    public void saveAndCommitToPreference(String key, Integer value) {
        startEditPreference();
        saveToPreference(key, value);
        commitEditPreference();
    }

    public void saveAndCommitToPreference(String key, Float value) {
        startEditPreference();
        saveToPreference(key, value);
        commitEditPreference();
    }

    public void saveAndCommitToPreference(String key, Boolean value) {
        startEditPreference();
        saveToPreference(key, value);
        commitEditPreference();
    }

    public boolean getBoolean(String key, boolean defValue)
    {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue)
    {
        return mSharedPreferences.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue)
    {
        return mSharedPreferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue)
    {
        return mSharedPreferences.getLong(key, defValue);
    }

    public String getString(String key, String defValue)
    {
        return mSharedPreferences.getString(key, defValue);
    }

    public void removeAndCommitPreference(String key) {
        startEditPreference();
        removePreference(key);
        commitEditPreference();
    }

    private void removePreference(String key) {
        mEditor.remove(key);
    }

    public void clear() {
        mEditor.clear();
    }
}
