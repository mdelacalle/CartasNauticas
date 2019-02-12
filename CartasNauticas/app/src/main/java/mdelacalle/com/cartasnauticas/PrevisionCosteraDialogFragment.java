package mdelacalle.com.cartasnauticas;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PrevisionCosteraDialogFragment extends DialogFragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup containerVg, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, containerVg, savedInstanceState);
        ConstraintLayout container = (ConstraintLayout) inflater.inflate(R.layout.aemet_dialog, null);

        container.findViewById(R.id.closs_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ZonasCosterasAdapter zca = new ZonasCosterasAdapter(AemetAPIConstants.getAreasCosteras(),container,getActivity());
        RecyclerView listaAreasCosteras = container.findViewById(R.id.lista_areas);

        listaAreasCosteras.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listaAreasCosteras.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listaAreasCosteras.getContext(),
                layoutManager.getOrientation());

        listaAreasCosteras.addItemDecoration(dividerItemDecoration);
        listaAreasCosteras.setAdapter(zca);

        return container;
    }



}
