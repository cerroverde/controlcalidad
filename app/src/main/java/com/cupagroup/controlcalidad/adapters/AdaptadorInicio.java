package com.cupagroup.controlcalidad.adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.modelo.Comida;
import com.cupagroup.controlcalidad.ui.FragmentoCategorias;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import static com.cupagroup.controlcalidad.sync.SyncManager.getHeightUnit;
import static com.cupagroup.controlcalidad.sync.SyncManager.savedParamShared;


/**
 * Adaptador para mostrar el menu principal "Nuevo ensayo" - "Historial" - "Estadisticas"
 */
public class AdaptadorInicio
        extends RecyclerView.Adapter<AdaptadorInicio.ViewHolder> {

    private ExtendedFloatingActionButton floatingCount;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView descripcion;
        public TextView titulo;
        public RelativeLayout dashboard;

        public ViewHolder(View v) {
            super(v);
            descripcion = (TextView) v.findViewById(R.id.nombre_comida);
            titulo = (TextView) v.findViewById(R.id.precio_comida);
            dashboard = (RelativeLayout) v.findViewById(R.id.dashboard_items);
        }

    }

    public AdaptadorInicio(ExtendedFloatingActionButton floatingCount) {
            this.floatingCount = floatingCount;
    }

    @Override
    public int getItemCount() {
        return Comida.COMIDAS_POPULARES.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_inicio, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Comida item = Comida.COMIDAS_POPULARES.get(i);
        viewHolder.itemView.setBackgroundResource(R.drawable.background_border);

        /*
        Glide.with(viewHolder.itemView.getContext())
                .load(item.getIdDrawable())
                .centerCrop()
                .into(viewHolder.imagen);
         */
        if (getHeightUnit("unitHeight") != "Null"){
            int unitHG = Integer.parseInt(getHeightUnit("unitHeight"));
            viewHolder.dashboard.getLayoutParams().height = unitHG;
        }else{
            viewHolder.dashboard.getLayoutParams().height = 368;
        }

        viewHolder.descripcion.setText(item.getDescripcion());
        viewHolder.titulo.setText(item.getTitulo());

        viewHolder.dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentoGenerico = null;
                FragmentManager fragmentManager =
                        ((AppCompatActivity) v.getContext())
                                .getSupportFragmentManager();

                if (viewHolder.titulo.getText() == "NUEVO ENSAYO"){
                    floatingCount.setVisibility(View.VISIBLE);
                    fragmentoGenerico = new FragmentoCategorias();

                }else {
                    Toast.makeText(
                            viewHolder.itemView.getContext(),
                            "Función deshabilitada temporalmente!!!",
                            Toast.LENGTH_LONG).show();
                }

                if (fragmentoGenerico != null) {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.contenedor_principal, fragmentoGenerico)
                            .commit();
                }
            }
        });

        viewHolder.itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.i("LayoutChange", "v Height: "+ v.getHeight());
                Log.i("LayoutChange", "v.Root Height: "+ v.getRootView().getHeight());
                Log.i("LayoutChange", "contenedor principal Height: "+ v.getRootView().findViewById(R.id.reciclador).getHeight());

                int unitHeight = v.getRootView().findViewById(R.id.reciclador).getHeight() / 3;

                savedParamShared("unitHeight", String.valueOf(unitHeight));
            }
        });
    }


}