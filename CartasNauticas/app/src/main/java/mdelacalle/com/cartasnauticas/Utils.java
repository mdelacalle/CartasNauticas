package mdelacalle.com.cartasnauticas;

import io.realm.Realm;

public class Utils {

    public static void saveIcon(Point point, String icon){

        Realm realm = Realm.getDefaultInstance();
        if(!realm.isInTransaction()){
            realm.beginTransaction();
        }
        point.setIcon(icon);
        realm.commitTransaction();
        realm.close();

    }
    public static void saveHasLabels(Point point, boolean icon){

        Realm realm = Realm.getDefaultInstance();
        if(!realm.isInTransaction()){
            realm.beginTransaction();
        }
        point.setHasLabel(icon);
        realm.commitTransaction();
        realm.close();

    }
    public static void saveLatLon(Point point, double lat,double lon){

        Realm realm = Realm.getDefaultInstance();
        if(!realm.isInTransaction()){
            realm.beginTransaction();
        }
        point.setLatitude(lat);
        point.setLongitude(lon);
        realm.commitTransaction();
        realm.close();

    }

}
