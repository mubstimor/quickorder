package mubstimor.android.quickorder.util;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

//@Singleton
public class PreferencesManager {

    private static final String PREF_NAME = "quickorder";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

//    @Inject
    public PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setValue(String key, String value) {
        mPref.edit()
                .putString(key, value)
                .apply();
    }

    public String getValue(String key) {
        return mPref.getString(key, "");
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }

    public boolean clear() {
        System.out.println("clearing data ...");
        return mPref.edit()
                .clear()
                .commit();
    }
}