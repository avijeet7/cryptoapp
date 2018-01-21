package aviapps.cryptosentiment.Common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by avije on 21-Jan-18.
 */

public class StatMethod {

    private static SharedPreferences getSharedPref(Context context, String filename) {
        try {
            SharedPreferences pref = context.getSharedPreferences(filename, Context.MODE_MULTI_PROCESS);
            return pref;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void savePrefs(Context context, String filename, String key, String value) {
        try {
            SharedPreferences.Editor editor = getSharedPref(context, filename).edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getStrPref(Context context, String filename, String key) {
        try {
            return getSharedPref(context, filename).getString(key, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}