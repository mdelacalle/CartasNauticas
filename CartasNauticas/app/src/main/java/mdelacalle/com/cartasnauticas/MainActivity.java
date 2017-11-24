package mdelacalle.com.cartasnauticas;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URLTemplateLayer;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

public class MainActivity extends Activity {

    G3MBuilder_Android _builder;
    G3MWidget_Android _g3mWidget;
    private RelativeLayout _layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        _builder = new G3MBuilder_Android(this);
        _builder.setAtmosphere(true);



        LayerSet layerSet = new LayerSet();


        final Geodetic2D lower = new Geodetic2D( //
                Angle.fromDegrees(26), //
                Angle.fromDegrees(-20));
        final Geodetic2D upper = new Geodetic2D( //
                Angle.fromDegrees(46.5), //
                Angle.fromDegrees(5.5));

        final Sector demSector = new Sector(lower, upper);


        /*
        final URLTemplateLayer baseLayer = URLTemplateLayer.newMercator("https://[1234].aerial.maps.cit.api.here.com/maptile/2.1/maptile/newest/satellite.day/{level}/{x}/{y}/256/png8?app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg"
                ,Sector.fullSphere(),false,2,18,TimeInterval.fromDays(30));
      */

        final URLTemplateLayer baseLayer = URLTemplateLayer.newWGS84("http://www.ign.es/wmts/pnoa-ma?Layer=OI.OrthoimageCoverage&Style=default&TileMatrixSet=EPSG:4326&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image/png&TileMatrix=EPSG:4326:{level}&TileCol={x}&TileRow={y}"
                ,Sector.fullSphere(),false,1,18,TimeInterval.fromDays(30));

        baseLayer.setTitle("Here demo");
        baseLayer.setEnable(true);
       layerSet.addLayer(baseLayer);

     //       final URLTemplateLayer imageryLayer = URLTemplateLayer.newMercator("http://ideihm.covam.es/wms/enc?LAYERS=RasterENC&TRANSPARENT=TRUE&FORMAT=image%2Fpng&SERVICE=WMS&VERSION=1.1.1&" +
       //         "REQUEST=GetMap&STYLES=&SRS=EPSG%3A3857&BBOX={west},{south},{east},{north}&WIDTH=256&HEIGHT=256",Sector.fullSphere(),true,2,18,TimeInterval.fromDays(30));

      //  final URLTemplateLayer imageryLayer = URLTemplateLayer.newWGS84("http://ideihm.covam.es/wms/enc?LAYERS=RasterENC&TRANSPARENT=TRUE&FORMAT=image%2Fpng&SERVICE=WMS&VERSION=1.1.1&" +
        //       "REQUEST=GetMap&STYLES=&SRS=EPSG%3A4326&BBOX={west},{south},{east},{north}&WIDTH=256&HEIGHT=256",Sector.fullSphere(),true,2,18,TimeInterval.fromDays(30));


     //   imageryLayer.setTitle("Cartas Naúticas IHM");
       // imageryLayer.setEnable(true);
       // layerSet.addLayer(imageryLayer);
        _builder.getPlanetRendererBuilder().setLayerSet(layerSet);
        _builder.setShownSector(demSector.shrinkedByPercent(0.2f));
        _g3mWidget = _builder.createWidget();


        _layout = (RelativeLayout) findViewById(R.id.glob3);

        //_g3mWidget.setCameraPosition(correctedPosition);
        // _g3mWidget.getG3MWidget().setAnimatedCameraPosition(TimeInterval.fromSeconds(3), correctedPosition, Angle.fromDegrees(0), Angle.fromDegrees(-60D), false);

        _layout.addView(_g3mWidget);


    }
}
