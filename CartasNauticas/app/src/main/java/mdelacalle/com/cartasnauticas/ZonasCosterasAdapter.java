package mdelacalle.com.cartasnauticas;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ZonasCosterasAdapter extends RecyclerView.Adapter <ZonasCosterasAdapter.ViewHolder> implements AemetAPIListener{

    private final ArrayList<AreaCostera> mAreasCosteras;
    private final ConstraintLayout mContainer;
    private ViewGroup mParent;

    public ZonasCosterasAdapter(ArrayList<AreaCostera> areasCosteras, ConstraintLayout container) {
        this.mAreasCosteras = areasCosteras;
        this.mContainer = container;
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
              mContainer.findViewById(R.id.progressBar).setVisibility(View.GONE);

              AemetAPI.getPrediccionCosteraByArea(areaCostera.getArea(),ZonasCosterasAdapter.this);

              Log.e("***", "Busca el clima en " +areaCostera.getArea() );
          }
      });

    }

    @Override
    public int getItemCount() {
        return mAreasCosteras.size();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {


        //TODO: Ver si hay alguna predicción en Realm
        mContainer.findViewById(R.id.select_area).setVisibility(View.GONE);
        mContainer.findViewById(R.id.progress).setVisibility(View.VISIBLE);
        ((TextView)mContainer.findViewById(R.id.progress_msg)).setText(mParent.getContext().getString(R.string.error_api_aemet_message));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(ConstraintLayout itemView) {
            super(itemView);
        }
    }
}
