package kz.kuzovatov.pavel.robomanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public enum PreferenceManager {
    INSTANCE;
    private static final String HTTP = "http://";
    private final static String CON_PROPS = "RobomanagerSettings";
    private static final String API_ADDRESS = "/rest/robots/";
    private final static String SERVER_ADDRESS = "SERVER_ADDRESS";
    private final static String WAITING_TIME = "WAITING_TIME";
    private final static String DEFAULT_ADDRESS = "192.168.1.2:8080";
    private SharedPreferences settings;

    public String getUrl(Context context){
        settings = context.getSharedPreferences(CON_PROPS, 0);
        String url = HTTP + DEFAULT_ADDRESS + API_ADDRESS;
        if (settings != null){
            url = HTTP + settings.getString(SERVER_ADDRESS, DEFAULT_ADDRESS) + API_ADDRESS;
        }
        return url;
    }

    public int getWaitingResponseTime(Context context){
        settings = context.getSharedPreferences(CON_PROPS, 0);
        int waitingResponseTime = 10000;
        if (settings != null) {
            waitingResponseTime = settings.getInt(WAITING_TIME, 10000);
        }
        return waitingResponseTime;
    }
}


