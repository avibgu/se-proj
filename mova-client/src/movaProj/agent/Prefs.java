package movaProj.agent;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs 
{
        public static SharedPreferences get(Context context)
        {
                return context.getSharedPreferences("SH_PUSHY", 0);
        }
        
        public static void addKey(Context context, String key, String val)
        {
                SharedPreferences settings = Prefs.get(context);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(key, val);
                editor.commit();
        }
        
        public static void removeKey(Context context, String key)
        {
                SharedPreferences settings = Prefs.get(context);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove(key);
                editor.commit();
        }
        
        public static String getKey(Context context, String key)
        {
                SharedPreferences prefs = Prefs.get(context);
                return prefs.getString(key, null);
        }
}