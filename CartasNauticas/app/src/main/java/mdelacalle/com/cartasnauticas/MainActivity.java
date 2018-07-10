package mdelacalle.com.cartasnauticas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MEventContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.SGShape;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapeLoadListener;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.TerrainTouchListener;
import org.glob3.mobile.generated.Tile;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.URLTemplateLayer;
import org.glob3.mobile.generated.Vector2F;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends Activity {

    G3MBuilder_Android _builder;
    G3MWidget_Android _g3mWidget;
    private RelativeLayout _layout;
    private LocationManager mLocationManager;
    private Location mLocation;
    private Geodetic3D mPosition = new Geodetic3D(Angle.fromDegrees(0),Angle.fromDegrees(0),0);
    MarksRenderer _weatherMarkers = new MarksRenderer(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Dexter.withActivity(MainActivity.this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                           if (report.areAllPermissionsGranted()) {
                                startGlob3();
                            } else {
                                Toast.makeText(MainActivity.this, getText(R.string.ask_for_location), Toast.LENGTH_LONG).show();
                                startGlob3();
                            }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();



        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/ps-regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        TrackingManager.trackScreen(MainActivity.this, TrackingManager.GLOBE);
    }


    public Location getLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(LocationUtil.getLastKnownLocation(MainActivity.this, mLocationManager) == null ){
            // Si no tenemos acceso al GPS en absoluto, devolvemos siempre el KMO.
            return null;
        }else {
            mLocation = LocationUtil.getLastKnownLocation(MainActivity.this, mLocationManager);
        }
        return mLocation;
    }


    @SuppressWarnings("unused")
    private PeriodicalTask testGPS() {
        final PeriodicalTask periodicalTask = new PeriodicalTask(TimeInterval.fromSeconds(5), new GTask() {

            @Override
            public void run(final G3MContext context) {
                Log.d("****", "LOCATION FOUND:"+getLocation().toString());
                if(mLocation!=null) {

//                    mPosition = new Geodetic3D(Angle.fromDegrees(36.5556), Angle.fromDegrees(-6.2544),
//                            200);
                    mPosition = new Geodetic3D(Angle.fromDegrees(mLocation.getLatitude()), Angle.fromDegrees(mLocation.getLongitude()),
                            200);
                }

            }
        });
        return periodicalTask;

    }


    private void startGlob3() {


        _builder = new G3MBuilder_Android(this);
        _builder.setAtmosphere(true);

        ArrayList<PeriodicalTask> tasks = new ArrayList<>();
        tasks.add(testGPS());

        _builder.setPeriodicalTasks(tasks);

        LayerSet layerSet = new LayerSet();

        final URLTemplateLayer baseLayer = URLTemplateLayer.newMercator("https://[1234].aerial.maps.cit.api.here.com/maptile/2.1/maptile/newest/satellite.day/{level}/{x}/{y}/256/png8?app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg"
                ,Sector.fullSphere(),false,2,18,TimeInterval.fromDays(30));


        baseLayer.setTitle("Here demo");
        baseLayer.setEnable(true);
        layerSet.addLayer(baseLayer);

        final URLTemplateLayer imageryLayer = URLTemplateLayer.newMercator("http://ideihm.covam.es/wms/enc?LAYERS=RasterENC&TRANSPARENT=TRUE&FORMAT=image%2Fpng&SERVICE=WMS&VERSION=1.1.1&" +
                 "REQUEST=GetMap&STYLES=&SRS=EPSG%3A3857&BBOX={west},{south},{east},{north}&WIDTH=256&HEIGHT=256",Sector.fullSphere(),true,2,18,TimeInterval.fromDays(30));

         imageryLayer.setTitle("Cartas Naúticas IHM");
         imageryLayer.setEnable(true);
         layerSet.addLayer(imageryLayer);


        _builder.getPlanetRendererBuilder().setLayerSet(layerSet);
        _builder.addRenderer(_weatherMarkers);


        //_builder.setShownSector(demSector.shrinkedByPercent(0.2f));

        final ShapesRenderer planeShapeRenderer = new ShapesRenderer();


        planeShapeRenderer.loadJSONSceneJS( //
                new URL("file:///poi.json", false), //
                "", //
                false, // isTransparent
                true, // depthTest
                mPosition, AltitudeMode.ABSOLUTE, new ShapeLoadListener() {

                    @Override
                    public void onBeforeAddShape(final SGShape shape) {
                        shape.setScale(200);
                        shape.setPitch(Angle.fromDegrees(90));
                    }


                    @Override
                    public void onAfterAddShape(final SGShape shape) {

                        shape.setAnimatedPosition(
                                TimeInterval.fromSeconds(5),
                                mPosition, true);
                    }


                    @Override
                    public void dispose() {

                    }
                });

        _builder.addRenderer(planeShapeRenderer);
        _builder.setInitializationTask(new GInitializationTask() {

            boolean isDone = false;

            @Override
            public boolean isDone(G3MContext context) {
                return isDone;
            }

            @Override
            public void run(G3MContext context) {

                Realm realm = Realm.getDefaultInstance();
                RealmResults<Point> points = realm.where(Point.class).findAll();

                for (Point point:points){

                    String label = "";
                    if(point.hasLabel){
                        label = point.getLatitude() +","+ point.getLongitude();
                    }

                    Geodetic3D position =new Geodetic3D(Angle.fromDegrees(point.getLatitude()), Angle.fromDegrees(point.getLongitude()),
                            0);


                    final MarkTouchListener mtl = new MarkTouchListener() {
                        @Override
                        public boolean touchedMark(Mark mark) {


                            Log.e("****", "CLICK ON MARK :"+mark.getPosition().toString());
                            MarkerUserData mud = (MarkerUserData) mark.getUserData();

                            Realm realm = Realm.getDefaultInstance();
                            Point _point = realm.where(Point.class).equalTo("id" ,mud.get_id() ).findFirst();

                            AddPointDialogFragment apdp = new AddPointDialogFragment();
                            apdp.setPointId(_point.getId());
                            apdp.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
                            apdp.setRenderer(_weatherMarkers);
                            apdp.setEditing(true);
                            apdp.setMark(mark);
                            apdp.show(getFragmentManager(), MainActivity.class.getName());

                            Map<String, String> params = new HashMap<>();
                            params.put(TrackingManager.POINT, mark.getPosition().toString());
                            TrackingManager.trackEvent(MainActivity.this, TrackingManager.EDITING_POINT, params);

                            return false;
                        }
                    };

                    MarkerUserData mud = new MarkerUserData(point.getId(),MainActivity.this);

                    _weatherMarkers.addMark(new Mark(
                            label,//
                            new URL(point.getIcon(), false), //
                            position, //
                            AltitudeMode.RELATIVE_TO_GROUND, //
                            50000000, //
                            true,
                            18f,
                            Color.black(),
                            Color.white(),
                            12,
                            mud,
                            false,
                            mtl,
                            false));
                }

                isDone = true;

            }
        });


        _g3mWidget = _builder.createWidget();

        _g3mWidget.setAnimatedCameraPosition(
                new Geodetic3D(Angle.fromDegrees(39.d),Angle.fromDegrees(-3d), 8000000), TimeInterval.fromSeconds(3));

        _g3mWidget.getG3MWidget().getPlanetRenderer().addTerrainTouchListener(new TerrainTouchListener() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean onTerrainTouch(G3MEventContext ec, Vector2F pixel, Camera camera, Geodetic3D position, Tile tile) {

                Log.e("****", "LOCATION CLICK:"+position.toString());

                AddPointDialogFragment apdp = new AddPointDialogFragment();
                apdp.setPosition(position);
                apdp.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
                apdp.setRenderer(_weatherMarkers);
                apdp.show(getFragmentManager(), MainActivity.class.getName());

                Map<String, String> params = new HashMap<>();
                params.put(TrackingManager.POINT, position.toString());
                TrackingManager.trackEvent(MainActivity.this, TrackingManager.CREATE_POINT, params);

                return true;
            }
        });




        _layout = (RelativeLayout) findViewById(R.id.glob3);

        //_g3mWidget.setCameraPosition(correctedPosition);
        // _g3mWidget.getG3MWidget().setAnimatedCameraPosition(TimeInterval.fromSeconds(3), correctedPosition, Angle.fromDegrees(0), Angle.fromDegrees(-60D), false);

        _layout.addView(_g3mWidget);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
