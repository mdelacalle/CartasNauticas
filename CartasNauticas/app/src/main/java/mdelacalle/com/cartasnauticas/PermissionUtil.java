package mdelacalle.com.cartasnauticas;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * @author mlmateo
 */

public class PermissionUtil {


    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 7001;
    public static final int PERMISSIONS_REQUEST_LOCATION              = 7002;


    private static int getPermissionRequestCode(final String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return PERMISSIONS_REQUEST_LOCATION;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
        }
        return -1;
    }


    /**
     * @return true if API version < 23 or the given permission was granted. false otherwise
     */
    public static boolean checkPermission(final Context context,
                                          final String permission) {
        return (ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Prompts the user to grant the given permission
     */
    public static void requestPermission(final Activity activity,
                                         final String permission) {
        ActivityCompat.requestPermissions(activity, new String[]{permission},
                                          getPermissionRequestCode(permission));
    }


    /**
     * @return true if the permission was already denied. If the user checked "Never ask again",
     * then returns false
     */
    public static boolean shouldShowRequestPermissionRationale(final Activity activity,
                                                               final String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
}
