package mdelacalle.com.cartasnauticas;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;

public class TrackingManager {

    public static final String GLOBE = "GLOBE" ;
    public static final String POINT_DIALOG = "POINT_DIALOG" ;
    public static final String CREATE_POINT = "CREATE_POINT" ;
    public static final String EDITING_POINT = "EDITING_POINT" ;
    public static final String SAVING_POINT = "SAVING_POINT" ;
    public static final String DELETING_POINT = "DELETING_POINT" ;
    public static final String POINT = "POINT" ;


    private static FirebaseAnalytics mFirebaseAnalytics;


    public static void trackScreen(Activity activity, String firebaseScreen){
        if(activity!=null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
            mFirebaseAnalytics.setCurrentScreen(activity, firebaseScreen, null);
        }
    }


    public static void trackEvent(Activity activity,String event,Map<String, String> parameters){
        if(activity!=null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
            Bundle bundle = new Bundle();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
            mFirebaseAnalytics.logEvent(event, bundle);
        }
    }
}
