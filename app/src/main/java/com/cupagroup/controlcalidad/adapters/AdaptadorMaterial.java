package com.cupagroup.controlcalidad.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Query;

import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.activities.MainActivity;
import com.cupagroup.controlcalidad.db.AppDatabase;
import com.cupagroup.controlcalidad.modelo.Campos;
import com.cupagroup.controlcalidad.sync.SyncManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Adaptador para caracteristicas del material usadas en la sección "Categorías"
 */
public class AdaptadorMaterial
        extends RecyclerView.Adapter<AdaptadorMaterial.ViewHolder> {

    private final List<Campos> items;
    private Context mContext;
    private AppDatabase mAppDatabase;
    private SharedPreferences mPreferences;
    private SharedPreferences mDataShared;
    private SharedPreferences mUserShare;
    private String mShareUser = "user_data";
    private String mShareFile = "pref_data";
    private String mShareCampos = "campos_data";

    // SharedPreference Variables
    private String mNaveName;
    private Long mNaveId;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView etiqueta;
        public TextView titulo;
        public EditText input;
        public Spinner spinner;
        public LinearLayout container;

        public ViewHolder(View v) {
            super(v);

            View rootView = v.getRootView();

            container = (LinearLayout) v.findViewById(R.id.container_campos);
            etiqueta = (TextView) v.findViewById(R.id.sugerencia_campo);
            titulo = (TextView) v.findViewById(R.id.titulo_campo);
            input = (EditText) v.findViewById(R.id.edit_campo);
            spinner = (Spinner) v.findViewById(R.id.spinner_campo);
        }
    }

    public AdaptadorMaterial(Context context, List<Campos> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mPreferences = viewGroup.getContext()
                .getSharedPreferences(mShareFile, Context.MODE_PRIVATE);
        mDataShared = viewGroup.getContext()
                .getSharedPreferences(mShareCampos, Context.MODE_PRIVATE);
        mUserShare = viewGroup.getContext()
                .getSharedPreferences(mShareUser, Context.MODE_PRIVATE);

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_campos, viewGroup, false);

        return new ViewHolder(v);
    }

    /*
     * La etiqueta es el hint
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(AdaptadorMaterial.ViewHolder viewHolder, int i) {
        String inputVal = null;
        String inputPosition = null;

        Campos item = items.get(i);
        viewHolder.etiqueta.setText(item.getEtiqueta());
        viewHolder.titulo.setText(item.getValor());
        float elevation = 4;
        int round = 3;

        if ( item.getValor().equals("FORMA")
                || item.getValor().equals("CALIDAD")
                || item.getValor().equals("ESPESOR")
                || item.getValor().equals("FORMATOS")
                || item.getValor().equals("REF. COMERCIAL"))
        {
            inputVal = mPreferences.getString(item.getValor(), "");
            inputPosition = mDataShared.getString(item.getValor(), "");
        }else {
            inputVal = mDataShared.getString(item.getValor(), "");
        }
        viewHolder.input.setText(inputVal);

        // DESARROLLO
        Log.i("VARIABLES", "item.getValor: "+ item.getValor() + " inputPosiion: "+ inputPosition+ " inputVal: "+ inputVal);


        switch (item.getValor()){
            case "REF. COMERCIAL":
                List<String> referencia = new ArrayList<String>();
                viewHolder.input.setVisibility(View.GONE);
                viewHolder.spinner.setVisibility(View.VISIBLE);
                mAppDatabase = AppDatabase.getInstance(viewHolder.itemView.getContext());

                // DATOS DE LA NAVE DECLARADA EN EL PERFIL
                mNaveName = mUserShare.getString("nave_name", "None");
                mNaveId = mUserShare.getLong("nave_id", 0);

                // DATOS DE LA REFERENCIA COMERCIAL OBTENIDA DE LOS DATOS DE LA NAVE
                String mCanterasId = mAppDatabase.getNaves().getCanterasByIdNave(mNaveId);
                String[] ids = mCanterasId.split(",");

                for (String id : ids) {
                    String ref = mAppDatabase.getCanteras().getCanteraByid(id);
                    Log.i("ARRAY", "ELEMENTOS EN ARRAY " + id + " Cantera " + ref);
                    referencia.add(ref);
                }
                
                ArrayAdapter<String> CanterasAdapter =
                        new ArrayAdapter<String>(
                                mContext,
                                R.layout.spinner_layout,
                                referencia
                        );
                //CanterasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.spinner.setAdapter(CanterasAdapter);
                viewHolder.etiqueta.setText(mNaveName);

                if (inputVal.isEmpty()) {
                    viewHolder.spinner.setSelection(0);
                }else {
                    assert inputPosition != null;
                    viewHolder.spinner.setSelection(Integer.parseInt(inputPosition));

                }

                Log.i("BOX MEASURE", "Height: " + viewHolder.itemView.getHeight() + " Width: " + viewHolder.itemView.getWidth());

                break;
            case "FORMATOS":
                viewHolder.input.setVisibility(View.GONE);
                viewHolder.spinner.setVisibility(View.VISIBLE);

                // Init DataBase
                mAppDatabase = AppDatabase.getInstance(viewHolder.itemView.getContext());

                // DATOS DE LA NAVE DECLARADA EN EL PERFIL/
                mNaveName = mUserShare.getString("nave_name", "None");
                mNaveId = mUserShare.getLong("nave_id", 0);

                List<String> formatos = mAppDatabase.getFormatos().getAllByNaveId(mNaveId);
                Integer mCount = mAppDatabase.getFormatos().getAllCountByNaveId(mNaveId);

                formatos.add("Selección");
                //Comentario para desarrollo
                Log.i("FORMATOS", "SIZE is: " + formatos.size() + "AND COUNT: " + mCount);
                Log.i("VARIABLES", "item.getValor: "+ item.getValor() + " inputPosiion: "+ inputPosition+ " inputVal: "+ inputVal);

                ArrayAdapter<String> FormatoAdapter =
                        new ArrayAdapter<String>(
                                viewHolder.itemView.getContext(),
                                R.layout.spinner_layout,
                                formatos
                        );
                viewHolder.spinner.setAdapter(FormatoAdapter);

                if (inputVal.isEmpty()) {
                    Log.i("FORMATOS", "Valor InputVAl Empty"+ inputVal);
                    viewHolder.spinner.setSelection(0);
                }else {
                    Log.i("FORMATOS", "Valor InputVAl Not Empty "+ inputVal);
                    assert inputPosition != null;
                    viewHolder.spinner.setSelection(0);
                    //viewHolder.spinner.setSelection(Integer.parseInt(inputPosition));
                }

                break;

            case "FORMA":
                viewHolder.input.setVisibility(View.GONE);
                viewHolder.spinner.setVisibility(View.VISIBLE);
                mAppDatabase = AppDatabase.getInstance(viewHolder.itemView.getContext());
                List<String> formas = mAppDatabase.getFormas().getAll();
                // Variables extras
                // indice para aquellos ListView que tienes como valores cadenas de texto Ej. FORMAS
                Integer intIndex = formas.indexOf(inputPosition);

                ArrayAdapter<String> FormasAdapter =
                        new ArrayAdapter<String>(
                                viewHolder.itemView.getContext(),
                                R.layout.spinner_layout,
                                formas
                        );
                viewHolder.spinner.setAdapter(FormasAdapter);
                if (inputVal.isEmpty()){
                    viewHolder.spinner.setSelection(0);
                }else{
                    assert inputPosition != null;
                    viewHolder.spinner.setSelection(intIndex);
                }

                break;

            case "CALIDAD":
                viewHolder.input.setVisibility(View.GONE);
                viewHolder.spinner.setVisibility(View.VISIBLE);
                mAppDatabase = AppDatabase.getInstance(viewHolder.itemView.getContext());
                List<String> quality = mAppDatabase.getCalidad().getAll();
                Integer mCountCalidad = mAppDatabase.getCalidad().getAllCount();
                intIndex = quality.indexOf(inputPosition);


                Collections.sort(quality);

                ArrayAdapter<String> QualityAdapter =
                        new ArrayAdapter<String>(
                                viewHolder.itemView.getContext(),
                                R.layout.spinner_layout,
                                quality
                        );
                viewHolder.spinner.setAdapter(QualityAdapter);

                //Comentario para desarrollo
                Log.i("FORMATOS", "SIZE is: " + quality.size() + "AND COUNT: " + mCountCalidad);
                Log.i("VARIABLES", "item.getValor: "+ item.getValor() + " inputPosiion: "+ inputPosition+ " inputVal: "+ inputVal);


                if (inputVal.isEmpty()){
                    viewHolder.spinner.setSelection(0);
                }else{
                    assert inputPosition != null;
                    viewHolder.spinner.setSelection(intIndex);
                }
                break;
            case "ESPESOR":
                viewHolder.input.setVisibility(View.GONE);
                viewHolder.spinner.setVisibility(View.VISIBLE);
                mAppDatabase = AppDatabase.getInstance(viewHolder.itemView.getContext());
                List<String> espesor = mAppDatabase.getEspesor().getAll();
                Collections.sort(espesor);
                intIndex = espesor.indexOf(inputPosition);

                ArrayAdapter<String> EspesorAdapter =
                        new ArrayAdapter<String>(
                                viewHolder.itemView.getContext(),
                                R.layout.spinner_layout,
                                espesor
                        );
                viewHolder.spinner.setAdapter(EspesorAdapter);
                if (inputVal.isEmpty()){
                    viewHolder.spinner.setSelection(0);
                }else{
                    assert inputPosition != null;
                    viewHolder.spinner.setSelection(intIndex);
                }
                break;
            default:
                viewHolder.input.setVisibility(View.VISIBLE);
                viewHolder.spinner.setVisibility(View.GONE);

                Log.i("BOX MEASURE", "Height: " + viewHolder.itemView.getHeight() + " Width: " + viewHolder.itemView.getWidth());

                break;
        }



        // Escucha al cambiar el valor del spinner
        viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = mDataShared.edit();
                SharedPreferences.Editor prefEditor = mPreferences.edit();
                Object item = parent.getItemAtPosition(position);

                TextView textView = (TextView)parent.getSelectedView();
                String spinnerText = textView.getText().toString();

                String tagName = String.valueOf(viewHolder.titulo.getText());

                if ( tagName.equals("FORMATOS")
                        || tagName.equals("FORMA")
                        || tagName.equals("CALIDAD")
                        || tagName.equals("ESPESOR")
                        || tagName.equals("REF. COMERCIAL"))
                {

                    if (tagName.equals("REF. COMERCIAL")){
                        editor.putString("canteraName", item.toString()).apply();

                        Log.e("SPINNER ",
                                "EQUAL TO. canteraName: "+ item.toString());
                    }

                    if( tagName.equals("FORMATOS") && item.toString().equals("Selección")){
                        Log.e("SPINNER ", "No aplica data");

                    }else {
                        editor.putString(tagName,String.valueOf(position));
                        prefEditor.putString(tagName,spinnerText);

                        prefEditor.apply();
                        editor.apply();

                        Log.e("SPINNER ",
                                "EQUAL TO. onItemSelected: "+ tagName + " -- "+ position+" -- "+ item  );
                    }


                }else {
                    editor.putString(tagName, String.valueOf(position));
                    editor.apply();

                    Log.e("SPINNER ",
                            "NOT EQUAL TO. onItemSelected: "+ tagName + " -- "+ position+" -- "+ item  );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Escucha al cambiar el valor del input
        viewHolder.input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences.Editor editor = mDataShared.edit();
                SharedPreferences.Editor prefEditor = mPreferences.edit();
                String tagName = String.valueOf(viewHolder.titulo.getText());
                String textVal = String.valueOf(viewHolder.input.getText());

                if(textVal.isEmpty() || textVal.equals("0")){
                    editor.remove(tagName);
                    editor.apply();

                    int totalDamage = SyncManager.sumAllItems();
                    ((MainActivity) viewHolder.itemView.getContext()).setTextCount(totalDamage);
                }else {
                    editor.putString(tagName, textVal);
                    editor.apply();

                    int totalDamage = SyncManager.sumAllItems();
                    ((MainActivity) viewHolder.itemView.getContext()).setTextCount(totalDamage);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("BOX MEASURE", "Height: " + viewHolder.itemView.getHeight() + " Width: " + viewHolder.itemView.getWidth());
            }
        });

        // OnTouche para desarrollo
        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("BOX MEASURE", "Height: " + v.getHeight() + " Width: " + v.getWidth());
                return false;
            }
        });

        //Actualiza el flotador
        int totalDamage = SyncManager.sumAllItems();
        ((MainActivity) viewHolder.itemView.getContext()).setTextCount(totalDamage);
    }



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
