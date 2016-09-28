package ssthouse.com.simplereader.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 操作sharedpreference工具类
 * Created by ssthouse on 2016/9/28.
 */

public class PreferUtil {

    //preference文件名
    private static final String PREFERENCE_FILE_NAME = "preference";

    //key 是否为第一次打开app
    private static final String KEY_IS_FIST_IN = "isFistIn";

    private SharedPreferences mPreference;

    /**
     * 唯一单例
     */
    private static PreferUtil mInstance;

    /**
     * 私有化构造方法
     */
    private PreferUtil() {

    }

    /**
     * 获取单例
     *
     * @return
     */
    public static PreferUtil getInstance() {
        if (mInstance == null) {
            mInstance = new PreferUtil();
        }
        return mInstance;
    }


    public boolean isFistIn(Context context) {
        return getBoolean(context, KEY_IS_FIST_IN, true);
    }

    public void setIsFistIn(Context context, boolean isFistIn) {
        setBoolean(context, KEY_IS_FIST_IN, isFistIn);
    }

    private boolean getBoolean(Context context, String key, boolean defaultValue) {
        if (mPreference == null) {
            mPreference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        }
        return mPreference.getBoolean(key, defaultValue);
    }

    public String getString(Context context, String key, String defaultValue) {
        if (mPreference == null) {
            mPreference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        }
        return mPreference.getString(key, defaultValue);
    }


    private void setBoolean(Context context, String key, boolean value) {
        if (mPreference == null) {
            mPreference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void setString(Context context, String key, String value) {
        if (mPreference == null) {
            mPreference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }
}

