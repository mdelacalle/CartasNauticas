package mdelacalle.com.cartasnauticas;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.URL;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class AddPointDialogFragment extends DialogFragment {


    private Geodetic3D _position;
    private MarksRenderer _renderer;
    private ImageView _circle;
    private ImageView _ancla;
    private ImageView _corazon;
    private ImageView _faro;
    private ImageView _pescao;
    private Point _point = null;
    private int _nextId;
    private int _pointId;

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    private boolean isEditing;
    private Mark _markToEdit;


    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup containerVG, Bundle savedInstanceState) {
        super.onCreateView(inflater, containerVG, savedInstanceState);
        final ConstraintLayout container = (ConstraintLayout) inflater.inflate(R.layout.dialog_add_point, null);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        _circle = ((ImageView)container.findViewById(R.id.circle));
        _ancla = ((ImageView)container.findViewById(R.id.ancla));
        _corazon = ((ImageView)container.findViewById(R.id.corazon));
        _faro = ((ImageView)container.findViewById(R.id.faro));
        _pescao = ((ImageView)container.findViewById(R.id.pescao));


        final Realm realm = Realm.getDefaultInstance();
        if(_point==null) {
            _point = realm.where(Point.class).equalTo("id", _pointId).findFirst();
        }


        if(_point==null&&!isEditing) {

            Number maxId = realm.where(Point.class).max("id");
            _nextId = (maxId == null) ? 1 : maxId.intValue() + 1;

            _point = new Point();
            _point.setIcon("file:///circle_red.png");
            final double latitude = (double) Math.round(_position._latitude._degrees * 10000d) / 10000d;
            final double longitude = (double) Math.round(_position._longitude._degrees * 10000d) / 10000d;
            _point.setLatitude(latitude);
            _point.setLongitude(longitude);
            _point.setId(_nextId);
            ((TextView)container.findViewById(R.id.lon_value)).setText(""+longitude);
            ((TextView)container.findViewById(R.id.lat_value)).setText(""+latitude);

        }else{
            if(_position==null) {
                _position = new Geodetic3D(Angle.fromDegrees(_point.getLatitude()), Angle.fromDegrees(_point.getLongitude()), 0);
            }
            ((TextView)container.findViewById(R.id.lon_value)).setText(""+_point.getLongitude());
            ((TextView)container.findViewById(R.id.lat_value)).setText(""+_point.getLatitude());
            initializeMenu(_point.getIcon());
            isEditing = true;
        }

        _circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanBorders();
                String icon = "file:///circle_red.png";
                if(isEditing){
                    Utils.saveIcon(_point,icon);
                }else {
                    _point.setIcon(icon);
                }
                _circle.setBackgroundResource(R.drawable.bg_orange_round_borders);
            }
        });

        _ancla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanBorders();
                String icon = "file:///harbor.png";
                if(isEditing){
                    Utils.saveIcon(_point,icon);
                }else {
                    _point.setIcon(icon);
                }
                _ancla.setBackgroundResource(R.drawable.bg_orange_round_borders);
            }
        });

        _corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanBorders();
                String icon = "file:///heart.png";
                if(isEditing){
                    Utils.saveIcon(_point,icon);
                }else {
                    _point.setIcon(icon);
                }
                _corazon.setBackgroundResource(R.drawable.bg_orange_round_borders);
            }
        });

        _faro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanBorders();
                String icon = "file:///lighthouse.png";
                if(isEditing){
                    Utils.saveIcon(_point,icon);
                }else {
                    _point.setIcon(icon);
                }
                _faro.setBackgroundResource(R.drawable.bg_orange_round_borders);
            }
        });

        _pescao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanBorders();
                String icon = "file:///aquarium.png";
                if(isEditing){
                    Utils.saveIcon(_point,icon);
                }else {
                    _point.setIcon(icon);
                }
                _pescao.setBackgroundResource(R.drawable.bg_orange_round_borders);
            }
        });


        CheckBox cbLabels =  (CheckBox)container.findViewById(R.id.cb_label);
        if(isEditing){
            Utils.saveHasLabels(_point,cbLabels.isChecked());
        }else {
            _point.setHasLabel(cbLabels.isChecked());
        }


        cbLabels.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isEditing){
                    Utils.saveHasLabels(_point,isChecked);
                }else {
                    _point.setHasLabel(isChecked);
                }
            }
        });


      final  MarkTouchListener mtl = new MarkTouchListener() {
            @Override
            public boolean touchedMark(Mark mark) {

                Log.e("****", "CLICK ON MARK :"+mark.getPosition().toString());
                return false;
            }
        };

        String label ="";


        TextView buttonCrear = (TextView) container.findViewById(R.id.crear_punto);

        if(isEditing){
            buttonCrear.setText(getString(R.string.borrar_punto));
        }

        buttonCrear.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 if(isEditing){

                     Map<String, String> params = new HashMap<>();
                     params.put(TrackingManager.POINT, _position.toString());
                     TrackingManager.trackEvent(getActivity(), TrackingManager.DELETING_POINT, params);

                     _renderer.removeMark(_markToEdit);
                     if(_point.isManaged()) {
                         if (!realm.isInTransaction()) {
                             realm.beginTransaction();
                         }
                         _point.deleteFromRealm();
                         realm.commitTransaction();
                         realm.refresh();
                         realm.close();
                     }
                 }else {

                     final MarkTouchListener mtl = new MarkTouchListener() {
                         @Override
                         public boolean touchedMark(Mark mark) {

                             Log.e("****", "CLICK ON MARK :" + mark.getPosition().toString());
                             MarkerUserData mud = (MarkerUserData) mark.getUserData();

                             AddPointDialogFragment apdp = new AddPointDialogFragment();
                             apdp.setPointId(mud.get_id());
                             apdp.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
                             apdp.setRenderer(_renderer);
                             apdp.setMark(mark);
                             apdp.setEditing(true);
                             apdp.setPoint(_point);
                             apdp.setPosition(mark.getPosition());
                             apdp.show(mud.get_activity().getFragmentManager(), MainActivity.class.getName());

                             Handler handler = new Handler();
                             handler.postDelayed(new Runnable() {
                                 @Override
                                 public void run() {
                                     dismissAllowingStateLoss();
                                 }
                             }, 500);


                             return false;
                         }
                     };


                     String label = "";
                     if (_point.hasLabel) {
                         label = _point.getLatitude() + "," + _point.getLongitude();
                     }

                     MarkerUserData mud = new MarkerUserData(_point.getId(), getActivity());

                     _renderer.addMark(new Mark(
                             label,//
                             new URL(_point.getIcon(), false), //
                             _position, //
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

                     realm.close();
                 }

                 dismissAllowingStateLoss();
             }
         });



        TextView saveRealmButton = (TextView) container.findViewById(R.id.crear_guardar_punto);

        if(isEditing){
            saveRealmButton.setText(getString(R.string.editar_punto));
        }

        saveRealmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEditing){
                    _renderer.removeMark(_markToEdit);
                }

                final  MarkTouchListener mtl = new MarkTouchListener() {
                    @Override
                    public boolean touchedMark(Mark mark) {

                        Log.e("****", "CLICK ON MARK :"+mark.getPosition().toString());
                        MarkerUserData mud = (MarkerUserData) mark.getUserData();

                        AddPointDialogFragment apdp = new AddPointDialogFragment();
                        apdp.setPointId(mud.get_id());
                        apdp.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
                        apdp.setRenderer(_renderer);
                        apdp.setMark(mark);
                        apdp.setPoint(_point);
                        apdp.setEditing(true);
                        apdp.show(mud.get_activity().getFragmentManager(), MainActivity.class.getName());

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismissAllowingStateLoss();
                            }
                        },500);
                        return false;
                    }
                };


                String label = "";
                if(_point.hasLabel){
                    label = _point.getLatitude() +","+ _point.getLongitude();
                }

                MarkerUserData mud = new MarkerUserData(_point.getId(),getActivity());

                _renderer.addMark(new Mark(
                        label,//
                        new URL(_point.getIcon(), false), //
                        _position, //
                        AltitudeMode.RELATIVE_TO_GROUND, //
                        50000000, //
                        true,
                        18f,
                        Color.black(),
                        Color.white(),
                       1,
                        mud,
                        false,
                        mtl,
                        false));


                Map<String, String> params = new HashMap<>();
                params.put(TrackingManager.POINT, _position.toString());
                TrackingManager.trackEvent(getActivity(), TrackingManager.SAVING_POINT, params);


                  if (!realm.isInTransaction()) {
                      realm.beginTransaction();
                  }
                  realm.copyToRealmOrUpdate(_point);
                  realm.commitTransaction();
                  realm.refresh();
                  realm.close();
                  Toast.makeText(getActivity(), getResources().getString(R.string.el_punto_se_guardo_correctamente), Toast.LENGTH_LONG).show();


                dismissAllowingStateLoss();

            }
        });


        container.findViewById(R.id.cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dismissAllowingStateLoss();
                realm.close();
            }
        });



        return container;

    }

    private void initializeMenu(String icon) {
        cleanBorders();
        if(icon.equals("file:///circle_red.png")){
            _circle.setBackgroundResource(R.drawable.bg_orange_round_borders);
        }
        if(icon.equals("file:///harbor.png")){
            _ancla.setBackgroundResource(R.drawable.bg_orange_round_borders);
        }
        if(icon.equals("file:///heart.png")){
            _corazon.setBackgroundResource(R.drawable.bg_orange_round_borders);
        }
        if(icon.equals("file:///aquarium.png")){
            _pescao.setBackgroundResource(R.drawable.bg_orange_round_borders);
        }
        if(icon.equals("file:///lighthouse.png")){
            _faro.setBackgroundResource(R.drawable.bg_orange_round_borders);
        }

    }

    private void cleanBorders() {
        _circle.setBackgroundResource(0);
        _ancla.setBackgroundResource(0);
        _corazon.setBackgroundResource(0);
        _faro.setBackgroundResource(0);
        _pescao.setBackgroundResource(0);
    }


    public void setPosition(Geodetic3D position) {
        this._position = position;
    }

    public void setRenderer(MarksRenderer renderer) {
        this._renderer = renderer;
    }

    public void setPointId(int _pointId) {
        this._pointId = _pointId;
    }

    public void setPoint(Point _pointId) {
        this._point  = _pointId;
    }

    public void setMark(Mark mark) {
        this._markToEdit = mark;
    }
}
