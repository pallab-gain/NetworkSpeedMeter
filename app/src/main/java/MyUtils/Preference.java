package MyUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xerxes on 1/22/15.
 * "I have not failed, I have just found 10000 ways that won't work."
 */
public class Preference {
    private static Preference ourInstance = null;
    private final String MyPREFERENCES = "NetWorkMonitor";
    SharedPreferences sharedpreferences;
    int font_size;
    public static Preference getInstance(Context context) {
        if( ourInstance==null )
            ourInstance = new Preference(context);
        return ourInstance;
    }

    private Preference(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    public void setFont_size(final int font_size){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("font_size",font_size);
        editor.commit();
    }
    public int getFont_size(){
        return sharedpreferences.getInt("font_size",10);
    }
    public void setFontColor(final String color){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("font_color",color);
        editor.commit();
    }
    public String getFont_color(){
        return sharedpreferences.getString("font_color","#FFFFF");
    }
}
