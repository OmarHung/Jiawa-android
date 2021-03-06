package hung.jiawa;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by omar8 on 2017/5/15.
 */

public class PreferenceHelper {
    static int mode = MODE_PRIVATE;
    static String name = "Preference";
    static PreferenceHelper singleton = null;
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    PreferenceHelper(Context context) {
        preferences = context.getSharedPreferences(name, mode);
        editor = preferences.edit();
    }

    PreferenceHelper(Context context, String name, int mode) {
        preferences = context.getSharedPreferences(name, mode);
        editor = preferences.edit();
    }

    public static PreferenceHelper with(Context context) {
        if (singleton == null) {
            singleton = new Builder(context, name, mode).build();
        }
        return singleton;
    }

    public static PreferenceHelper with(Context context, String name, int mode) {
        if (singleton == null) {
            singleton = new Builder(context, name, mode).build();
        }
        return singleton;
    }

    public void save(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public void save(String key, String value) {
        editor.putString(key, value).apply();
    }

    public void save(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public void save(String key, float value) {
        editor.putFloat(key, value).apply();
    }

    public void save(String key, long value) {
        editor.putLong(key, value).apply();
    }

    public void save(String key, Set<String> value) {
        editor.putStringSet(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return preferences.getStringSet(key, defValue);
    }

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    public void remove(String key) {
        editor.remove(key).apply();
    }

    public void removeAll() {
        editor.clear().apply();
    }

    public void contains(String key) {
        preferences.contains(key);
    }

    private static class Builder {

        private final Context context;
        private final int mode;
        private final String name;

        public Builder(Context context, String name, int mode) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
            this.name = name;
            this.mode = mode;
        }

        /**
         * Method that creates an instance of Prefs
         *
         * @return an instance of Prefs
         */
        public PreferenceHelper build() {
            if (mode == -1 || name == null) {
                return new PreferenceHelper(context);
            }
            return new PreferenceHelper(context, name, mode);
        }
    }
}