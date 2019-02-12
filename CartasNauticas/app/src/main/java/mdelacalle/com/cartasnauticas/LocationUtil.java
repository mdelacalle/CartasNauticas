package mdelacalle.com.cartasnauticas;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;


/**
 * @author mlmateo
 */

public class LocationUtil {


    public static final long TWO_MINUTES = 1000 * 60 * 2;


    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new
     *                            one
     */
    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        final long timeDelta = location.getTime() - currentBestLocation.getTime();
        final boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        final boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        final boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        final int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        final boolean isLessAccurate = accuracyDelta > 0;
        final boolean isMoreAccurate = accuracyDelta < 0;
        final boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        final boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else return isNewer && !isSignificantlyLessAccurate && isFromSameProvider;
    }


    /** Checks whether two providers are the same */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    /**
     * @return whether or not any provider is enabled
     */
    public static boolean isLocationEnabled(final LocationManager locationManager) {
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }


    /**
     * @return the best last known location among enabled providers
     */
    public static Location getLastKnownLocation(final Context context,
                                                final LocationManager locationManager) {
        Location lastLocation = null;
        if (PermissionUtil.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            final List<String> matchingProviders = locationManager.getProviders(true);
            for (String provider : matchingProviders) {
                final Location location = locationManager.getLastKnownLocation(provider);
                if ((location != null) && isBetterLocation(location, lastLocation)) {
                    lastLocation = location;
                }
            }
        }

        return lastLocation;
    }

}
