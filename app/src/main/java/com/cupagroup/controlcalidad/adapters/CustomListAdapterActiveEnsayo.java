package com.cupagroup.controlcalidad.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.db.AppDatabase;
import com.cupagroup.controlcalidad.utils.CustomListActiveEnsayo;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomListAdapterActiveEnsayo
        extends ArrayAdapter<CustomListActiveEnsayo> implements View.OnClickListener {

    private AppDatabase mAppDatabase;
    private SharedPreferences mPreferences;
    private final ArrayList<CustomListActiveEnsayo>  dataSet;
    private int mStatus;
    private Context mContext;

    public int color = Color.parseColor("#ED7070");

    private static class ViewHolder {
        TextView txtRefComercial;
        TextView txtFormatos;
        TextView txtComment;
        TextView txtCreatedAt;
        ImageView delete;
    }

    public CustomListAdapterActiveEnsayo(
            ArrayList<CustomListActiveEnsayo> data,
            @NonNull Context context,
            int resource)
    {
        super(context, R.layout.list_active_ensayo, data);
        this.dataSet = data;
        this.mContext = context;
        this.mStatus = resource;

    }

    @Override
    public void onClick(View v) {
        mAppDatabase = AppDatabase.getInstance(getContext());
        if( v.getId() == R.id.delete_item) {
            String tag = (String) v.getTag();
            String[] tagPos = tag.split(",");
            int dbPosition = Integer.parseInt(tagPos[0]);
            int lvPosition = Integer.parseInt(tagPos[1]);
            CustomListActiveEnsayo object = getItem(lvPosition);
            Long message = object.deleteSerial(this,object);

            Context context = v.getRootView().getContext();
            AlertDialog.Builder alertDialogBuilder = null;
            try {
                alertDialogBuilder = new AlertDialog.Builder(context);
            } catch (Exception e) {
                e.printStackTrace();
            }

            assert alertDialogBuilder != null;
            alertDialogBuilder.setTitle("Estas seguro?");
            alertDialogBuilder
                    .setMessage("Desea eliminar el ensayo Nº " + lvPosition + "?")
                    .setCancelable(true)
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        this.remove(object);
                        mAppDatabase.getQualityControl().deleteItem(Long.parseLong(String.valueOf(dbPosition)));
                    })
                    .setNegativeButton("Cancelar", (((dialog, which) -> dialog.cancel())));

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CustomListActiveEnsayo customListActiveEnsayo = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_active_ensayo, parent, false);

            viewHolder.txtRefComercial = (TextView) convertView.findViewById(R.id.ensayo_ref_comercial);
            viewHolder.txtFormatos = (TextView) convertView.findViewById(R.id.txt_dimensiones);
            viewHolder.txtComment = (TextView) convertView.findViewById(R.id.txt_descripcion);
            viewHolder.txtCreatedAt = (TextView) convertView.findViewById(R.id.txt_created_at);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete_item);

            // Aqui cambiamos el color del icono "Delete"
            viewHolder.delete.setColorFilter(color);

            result = convertView;
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        String formatAll = customListActiveEnsayo.getmFormatos() + " - " + customListActiveEnsayo.getmForma();
        String tagPosition = customListActiveEnsayo.getmIdEnsayo()+","+position;

        viewHolder.txtRefComercial.setText(customListActiveEnsayo.getmRefComercial());
        viewHolder.txtFormatos.setText(formatAll);
        viewHolder.txtComment.setText(customListActiveEnsayo.getmComment());
        viewHolder.txtCreatedAt.setText(customListActiveEnsayo.getmCreatedAt());
        viewHolder.delete.setOnClickListener(this);
        viewHolder.delete.setTag(tagPosition);


        return result;
    }
}
