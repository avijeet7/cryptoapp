package aviapps.cryptosentiment.Common;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * Created by Avijeet on 21-Jan-18.
 */

public class StatMethod {

    private static SharedPreferences getSharedPref(Context context, String filename) {
        try {
            return context.getSharedPreferences(filename, Context.MODE_MULTI_PROCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void savePrefs(Context context, String filename, String key, String value) {
        try {
            SharedPreferences.Editor editor = getSharedPref(context, filename).edit();
            editor.putString(key, value);
            editor.apply();
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

    public static String addCommas(String num) {
        try { // Check if string is a valid number, if not return the number
            double d = Double.parseDouble(num);
        } catch (Exception e) {
            e.printStackTrace();
            return num;
        }
        String value = "";
        try {
            value = num.trim(); // Remove all leading and trailing spaces
            int decLoc = value.indexOf("."); // Find index of decimal point, if any
            int i;
            if (decLoc == -1) {
                decLoc = value.length();
            }
            i = decLoc - 3;
            if (i >= 1 && value.charAt(i - 1) != '-') { // Handle for negatives
                value = value.substring(0, i) + "," + value.substring(i, value.length()); // #,###.##
                i -= 2;
                while (i >= 1) { // ##,##,##,--,--.--
                    if (value.charAt(i - 1) == '-') // Handle for negatives
                        break;
                    value = value.substring(0, i) + "," + value.substring(i, value.length());
                    i -= 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            value = "";
        }
        return value;
    }
}
