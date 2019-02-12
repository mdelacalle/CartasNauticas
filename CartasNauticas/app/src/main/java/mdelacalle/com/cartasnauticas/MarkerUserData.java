package mdelacalle.com.cartasnauticas;

import android.app.Activity;

import org.glob3.mobile.generated.MarkUserData;

public class MarkerUserData extends MarkUserData {

    public Activity get_activity() {
        return _activity;
    }

    private final Activity _activity;

    public int get_id() {
        return _id;
    }

    int _id;
    public MarkerUserData(int id, Activity activity) {
        _id =id;
         _activity = activity;
    }
}
