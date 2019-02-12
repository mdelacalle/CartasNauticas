package mdelacalle.com.cartasnauticas;

import android.app.Activity;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;

public class ZonasCosterasAdapter extends RecyclerView.Adapter <ZonasCosterasAdapter.ViewHolder> implements AemetAPIListener{

    private final ArrayList<AreaCostera> mAreasCosteras;
    private final ConstraintLayout mContainer;
    private final Activity mActivity;
    private ViewGroup mParent;
    int mArea = 0;

    public ZonasCosterasAdapter(ArrayList<AreaCostera> areasCosteras, ConstraintLayout container, Activity activity) {
        this.mAreasCosteras = areasCosteras;
        this.mContainer = container;
        this.mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.area_costera_item, parent, false);
        ViewHolder zcvh = new ViewHolder(v);
        mParent = parent;
        return zcvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      TextView tv = ((ConstraintLayout)holder.itemView).findViewById(R.id.area_costera);

      final AreaCostera areaCostera = mAreasCosteras.get(position);
      tv.setText(areaCostera.getArea());

      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              mContainer.findViewById(R.id.select_area).setVisibility(View.GONE);
              mContainer.findViewById(R.id.progress).setVisibility(View.VISIBLE);
              AemetAPI.getPrediccionCosteraByArea(areaCostera.getCodigo(),ZonasCosterasAdapter.this);
              mArea = areaCostera.getCodigo();
              Log.e("***", "Busca el clima en " + mArea);
          }
      });

    }

    @Override
    public int getItemCount() {
        return mAreasCosteras.size();
    }

    @Override
    public void onSuccess() {

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                PrediccionCosteraArea prediccion = realm.where(PrediccionCosteraArea.class).equalTo("area", mArea).findFirst();
                renderPrevision(prediccion);
            }
        });
    }

    @Override
    public void onError() {

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                PrediccionCosteraArea prediccion = realm.where(PrediccionCosteraArea.class).equalTo("area", mArea).findFirst();

                if (prediccion != null) {
                    renderPrevision(prediccion);
                } else {

                    mContainer.findViewById(R.id.select_area).setVisibility(View.GONE);
                    mContainer.findViewById(R.id.progress).setVisibility(View.VISIBLE);
                    mContainer.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                    ((TextView) mContainer.findViewById(R.id.progress_msg)).setTextColor(Color.RED);
                    ((TextView) mContainer.findViewById(R.id.progress_msg)).setText(mParent.getContext().getString(R.string.error_api_aemet_message));
                }
            }
        });
    }
    private void renderPrevision(PrediccionCosteraArea prediccion) {
        mContainer.findViewById(R.id.progress).setVisibility(View.GONE);
        mContainer.findViewById(R.id.prediccion).setVisibility(View.VISIBLE);
        ((TextView) mContainer.findViewById(R.id.nombre)).setText(prediccion.getNombre());
        ((TextView) mContainer.findViewById(R.id.elaborado)).setText("Elab.: "+prediccion.getElaborado());
        ((TextView) mContainer.findViewById(R.id.inicio)).setText("Inicio: "+prediccion.getInicio());
        ((TextView) mContainer.findViewById(R.id.fin)).setText("Fin: "+prediccion.getFin());
        ((TextView) mContainer.findViewById(R.id.avisoText)).setText(prediccion.getAviso().getTexto());
        ((TextView) mContainer.findViewById(R.id.situacionText)).setText(prediccion.getSituacion().getTexto());
        ((TextView) mContainer.findViewById(R.id.tendenciaText)).setText(prediccion.getTendencia().getTexto());

        RecyclerView zonas = mContainer.findViewById(R.id.zonas);
        ZonasPrediccionRealmAdapter zpra = new ZonasPrediccionRealmAdapter(prediccion.getPrediccion().getZonas(),true);
        zonas.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        zonas.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mActivity,
                layoutManager.getOrientation());

        zonas.addItemDecoration(dividerItemDecoration);
        zonas.setAdapter(zpra);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(ConstraintLayout itemView) {
            super(itemView);
        }
    }
}
