package xyz.ismailnurudeen.minidb;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;
import java.util.Set;

import xyz.ismailnurudeen.minidb.utils.BitmapToStringConverter;
import xyz.ismailnurudeen.minidb.utils.SecurePreferences;
import xyz.ismailnurudeen.minidb.utils.VUtils;

public class MiniDBSecured extends SecurePreferences {
    private Context mContext;
    private String mName;
    private Gson mGson;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mSharedPrefEditor;
    private boolean isEncrypted = false;
    private SecurePreferences mSecurePrefs;

    /**
     * This will initialize an instance of the SecurePreferences class
     *
     * @param context your current context.
     * @param name    name of preferences file (preferenceName.xml)
     */
    private MiniDBSecured(Context context, String name, String secureKey) {
        super(context, name, secureKey, true);
        mContext = context;
        isEncrypted = true;
        mName = name;
        mGson = new Gson();
        mSharedPref = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
    }

    private MiniDBSecured(Context context, String name) {
        super(context, name, "", false);
        mContext = context;
        mName = name;
        mGson = new Gson();
        mSharedPref = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
    }

    @NonNull
    public static MiniDBSecured open(Context context, String dbName) {
        return new MiniDBSecured(context, dbName);
    }

    @NonNull
    public static MiniDBSecured openWithEncryption(Context context, String dbName, @NonNull String secretKey) {
        return new MiniDBSecured(context, dbName, secretKey);
    }

    public boolean insertString(String key, String value) {
        if (isEncrypted) {
            return mSharedPref.edit().putString(key, encrypt(value, getWriter())).commit();
        } else {
            return mSharedPref.edit().putString(key, value).commit();
        }
    }

    public boolean insertBoolean(String key, Boolean value) {
        if (isEncrypted) {
            return mSharedPref.edit().putBoolean(encryptKey(key), value).commit();
        } else {
            return mSharedPref.edit().putBoolean(key, value).commit();
        }
    }

    public boolean insertFloat(String key, float value) {
        if (isEncrypted) {
            return mSharedPref.edit().putFloat(encryptKey(key), value).commit();
        } else {
            return mSharedPref.edit().putFloat(key, value).commit();
        }
    }

    public boolean insertInt(String key, int value) {
        if (isEncrypted) {
            return mSharedPref.edit().putInt(encryptKey(key), value).commit();
        } else {
            return mSharedPref.edit().putInt(key, value).commit();
        }
    }

    public boolean insertLong(String key, long value) {
        if (isEncrypted) {
            return mSharedPref.edit().putLong(encryptKey(key), value).commit();
        } else {
            return mSharedPref.edit().putLong(key, value).commit();
        }
    }

    public boolean insertStringSet(String key, Set<String> value) {
        if (isEncrypted) {
            return mSharedPref.edit().putStringSet(encryptKey(key), value).commit();
        } else {
            return mSharedPref.edit().putStringSet(key, value).commit();
        }
    }

    public boolean insertStringArray(String key, String[] value) {
        if (isEncrypted) {
            return mSharedPref.edit().putString(key, encrypt(mGson.toJson(value), getWriter())).commit();
        } else {
            return mSharedPref.edit().putString(key, mGson.toJson(value)).commit();
        }
    }

    public boolean insertIntArray(String key, Integer[] value) {
        if (isEncrypted) {
            return mSharedPref.edit().putString(key, encrypt(mGson.toJson(value), getWriter())).commit();
        } else {
            return mSharedPref.edit().putString(key, mGson.toJson(value)).commit();
        }
    }

    public boolean insertList(String key, List<Object> value) {
        if (isEncrypted) {
            return mSharedPref.edit().putString(key, encrypt(mGson.toJson(value), getWriter())).commit();
        } else {
            return mSharedPref.edit().putString(key, mGson.toJson(value)).commit();
        }
    }

    public boolean insertObject(String key, Object value) {
        if (isEncrypted) {
            return mSharedPref.edit().putString(key, encrypt(mGson.toJson(value), getWriter())).commit();
        } else {
            return mSharedPref.edit().putString(key, mGson.toJson(value)).commit();
        }
    }

    public boolean insertDrawable(String key, Drawable drawable) {
        Bitmap bitmap = new VUtils(mContext).getBitMapFromDrawable(drawable);
        String imgStr = BitmapToStringConverter.convertToString(bitmap);
        if (isEncrypted) {
            return mSharedPref.edit().putString(key, encrypt(imgStr, getWriter())).commit();
        } else {
            return mSharedPref.edit().putString(key, imgStr).commit();
        }
    }

    public boolean insertDrawable(String key, int drawableRes) {
        Bitmap bitmap = new VUtils(mContext).getBitMapFromDrawableRes(drawableRes);
        String imgStr = BitmapToStringConverter.convertToString(bitmap);
        if (isEncrypted) {
            return mSharedPref.edit().putString(key, encrypt(imgStr, getWriter())).commit();
        }
        return mSharedPref.edit().putString(key, imgStr).commit();
    }

    public boolean insertBitmap(String key, Bitmap value) {
        String imgStr = BitmapToStringConverter.convertToString(value);
        if (isEncrypted) {
            return mSharedPref.edit().putString(key, encrypt(imgStr, getWriter())).commit();
        }
        return mSharedPref.edit().putString(key, imgStr).commit();
    }

    public String readString(String key, String defaultValue) {
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            String securedEncodedValue = mSharedPref.getString(encryptKey(key), defaultValue);
            return decrypt(securedEncodedValue);
        }
        return mSharedPref.getString(key, defaultValue);
    }

    public int readInt(String key, int defaultValue) {
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            return mSharedPref.getInt(encryptKey(key), defaultValue);
        }
        return mSharedPref.getInt(key, defaultValue);
    }

    public boolean readBoolean(String key, Boolean defaultValue) {
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            return mSharedPref.getBoolean(encryptKey(key), defaultValue);
        }
        return mSharedPref.getBoolean(key, defaultValue);
    }

    public long readLong(String key, long defaultValue) {
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            return mSharedPref.getLong(encryptKey(key), defaultValue);
        }
        return mSharedPref.getLong(key, defaultValue);
    }

    public float readFloat(String key, float defaultValue) {
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            return mSharedPref.getFloat(encryptKey(key), defaultValue);
        }
        return mSharedPref.getFloat(key, defaultValue);
    }

    public Set<String> readStringSet(String key, Set<String> defaultValue) {
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            return mSharedPref.getStringSet(encryptKey(key), defaultValue);
        }
        return mSharedPref.getStringSet(key, defaultValue);
    }

    public Drawable readDrawable(String key, int defaultDrawable) {
        VUtils vUtils = new VUtils(mContext);
        String imgStr = mSharedPref.getString(key, "");
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            imgStr = mSharedPref.getString(encryptKey(key), "");
        }

        if (!imgStr.equalsIgnoreCase("")) {
            return vUtils.getDrawableFromBitmap(BitmapToStringConverter.convertToBitmap(imgStr));
        }
        return vUtils.getDrawableFromBitmap(vUtils.getBitMapFromDrawableRes(defaultDrawable));
    }

    public Drawable readDrawable(String key, Drawable defaultDrawable) {
        String imgStr = mSharedPref.getString(key, "");
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            imgStr = mSharedPref.getString(encryptKey(key), "");
        }
        if (!imgStr.equalsIgnoreCase("")) {
            return new VUtils(mContext).getDrawableFromBitmap(BitmapToStringConverter.convertToBitmap(imgStr));
        }
        return defaultDrawable;
    }

    public Bitmap readBitmap(String key, Bitmap defaultBitmap) {
        String imgStr = mSharedPref.getString(key, "");
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            imgStr = mSharedPref.getString(encryptKey(key), "");
        }
        if (!imgStr.equalsIgnoreCase("")) {
            return BitmapToStringConverter.convertToBitmap(imgStr);
        }
        return defaultBitmap;
    }

    public Object readObject(String key, Object defaultValue) {
        String objectStr = mSharedPref.getString(key, "");
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            objectStr = decrypt(mSharedPref.getString(encryptKey(key), ""));
        }
        if (!objectStr.equalsIgnoreCase("")) {
            return mGson.fromJson(objectStr, defaultValue.getClass());
        }
        return defaultValue;
    }

    public List readList(String key, List<Object> defaultValue) {
        String objectStr = mSharedPref.getString(key, "");
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            objectStr = decrypt(mSharedPref.getString(encryptKey(key), ""));
        }
        if (!objectStr.equalsIgnoreCase("")) {
            return mGson.fromJson(objectStr, defaultValue.getClass());
        }
        return defaultValue;
    }

    public String[] readStringArray(String key, String[] defaultValue) {
        String objectStr = mSharedPref.getString(key, "");
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            objectStr = decrypt(mSharedPref.getString(encryptKey(key), ""));
        }
        if (!objectStr.equalsIgnoreCase("")) {
            return mGson.fromJson(objectStr, defaultValue.getClass());
        }
        return defaultValue;
    }

    public int[] readIntArray(String key, int[] defaultValue) {
        String objectStr = mSharedPref.getString(key, "");
        if (isEncrypted && mSharedPref.contains(encryptKey(key))) {
            objectStr = decrypt(mSharedPref.getString(encryptKey(key), ""));
        }
        if (!objectStr.equalsIgnoreCase("")) {
            return mGson.fromJson(objectStr, defaultValue.getClass());
        }
        return defaultValue;
    }

    public boolean deleteAll() {
        return mSharedPref.edit().clear().commit();
    }

    public void deleteValue(String key) {
        mSecurePrefs.removeValue(key);
    }

}
