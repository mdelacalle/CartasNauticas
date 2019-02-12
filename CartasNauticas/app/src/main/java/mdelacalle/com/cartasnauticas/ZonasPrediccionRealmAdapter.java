package mdelacalle.com.cartasnauticas;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ZonasPrediccionRealmAdapter extends RealmRecyclerViewAdapter <Zona,ZonasPrediccionRealmAdapter.ViewHolder> {

    public ZonasPrediccionRealmAdapter(@Nullable OrderedRealmCollection<Zona> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @NonNull
    @Override
    public ZonasPrediccionRealmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zonas_item, parent, false);
        ViewHolder vh = new ViewHolder(layout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ZonasPrediccionRealmAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        public void bind(int position){
            ((TextView)itemView.findViewById(R.id.nombre)).setText(getData().get(position).getNombre());
            ((TextView)itemView.findViewById(R.id.texto_zona)).setText(getData().get(position).getTexto());
        }
    }
}
