package com.cupagroup.controlcalidad.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.db.AppDatabase;
import com.google.android.material.chip.Chip;

import java.util.Map;
import java.util.Set;

public class AdaptadorMiscelaneo
        extends RecyclerView.Adapter<AdaptadorMiscelaneo.ViewHolder> {


    private AppDatabase mAppDatabase;
    private SharedPreferences mPreferences;
    private String mShareCampos = "campos_data";
    private Editable mComments;


    public AdaptadorMiscelaneo(){
        Log.i("Miscelaneo", "Se ha inicializado");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView iControl;
        public TextView iCanteras;
        public TextView iPiezas;
        public ImageView iComment;
        public TextView iCommentText;



        public ViewHolder(View v) {
            super(v);

            iControl = (TextView) v.findViewById(R.id.info_numero_control);
            iCanteras = (TextView) v.findViewById(R.id.info_cantera);
            iPiezas = (TextView) v.findViewById(R.id.info_piezas);
            iComment = (ImageView) v.findViewById(R.id.icono_indicador_derecho);
            iCommentText = (TextView) v.findViewById(R.id.info_comment_text);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragmento_comentario, parent, false);
        mPreferences = parent.getContext()
                .getSharedPreferences(mShareCampos, Context.MODE_PRIVATE);


        return new AdaptadorMiscelaneo.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewGroup padre = (ViewGroup) holder.itemView.getParent();
        String tControl = mPreferences.getString("sessionId", "No hay sesión activa");

        //TODO tCantera no esta buscando en el archivo de preferencias "user_data"
        StringBuilder tCantera = new StringBuilder(mPreferences.getString("cantera_name", "Sin definir"));

        Map<String, ?> entries = mPreferences.getAll();
        Set<String> keys = entries.keySet();
        for (String key : keys){
            tCantera.append(key);

            Log.i("Shared Data", key);
        }
        holder.iCommentText.setText(tCantera.toString());
        holder.iControl.setText(tControl);
    }



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
