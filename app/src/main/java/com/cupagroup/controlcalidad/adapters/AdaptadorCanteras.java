package com.cupagroup.controlcalidad.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.activities.CanterasListClass;
import com.cupagroup.controlcalidad.activities.NavesListClass;
import com.cupagroup.controlcalidad.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorCanteras
        extends RecyclerView.Adapter<AdaptadorCanteras.ViewHolder>
{
    private ArrayList<CanterasListClass>  dataSet;
    private int lastPosition = -1;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView tvCantera;
        public TextView tvNave;
        public TextView tvAddress;
        public ImageView ivSelectNave;
        public RelativeLayout wrapCanteras;

        public ViewHolder(View v) {
            super(v);
            wrapCanteras = (RelativeLayout) v.findViewById(R.id.customListItem);
            tvCantera = (TextView) v.findViewById(R.id.cantera);
            tvNave = (TextView) v.findViewById(R.id.nave);
            tvAddress = (TextView) v.findViewById(R.id.tvDireccion);
            ivSelectNave = (ImageView) v.findViewById(R.id.ivSelectNave);

        }

        public TextView getTvCantera(){
            return tvCantera;
        }
        public TextView getTvNave(){
            return tvNave;
        }
        public TextView getTvAddress(){
            return tvAddress;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showNaves(Long idCantera, int position, ViewHolder holder) {
        AppDatabase mAppDatabase;
        SharedPreferences mPreferences;
        String mShareFile = "user_data";
        mAppDatabase = AppDatabase.getInstance(holder.itemView.getContext());
        mPreferences = holder.itemView.getContext()
                .getSharedPreferences(mShareFile, Context.MODE_PRIVATE);

        List<NavesListClass> navesEntries = mAppDatabase.getNaves().getAll(idCantera);

        final Dialog dialog = new Dialog(holder.itemView.getContext());
        dialog.setTitle(R.string.label_nave_pref);
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.setContentView(R.layout.radiobutton_dialog);

        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
        for (int i = 0; i < navesEntries.size(); i++){
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());

            Long mNaveID = navesEntries.get(i).getId_nave();

            radioButton.setTextSize(18);
            radioButton.setPadding(15,15,15,15);
            radioButton.setId(Math.toIntExact(mNaveID));
            radioButton.setText(navesEntries.get(i).getName_nave());
            radioButton.setTag(mNaveID);

            radioGroup.addView(radioButton);
        }
        dialog.show();
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.ic_business);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();


                for (int x=0; x < childCount; x++){
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId){
                        // We clear old data
                        dataSet.get(
                                mPreferences.getInt("cantera_position", 1)
                        ).setAddressNave("");
                        dataSet.get(
                                mPreferences.getInt("cantera_position", 1)
                        ).setNameNave("");

                        // Save data into preference file "user_data"
                        mPreferences.edit()
                                .putString(
                                        "cantera_name", holder.getTvCantera().getText().toString()
                                ).apply();
                        mPreferences.edit().putInt("cantera_position", position).apply();
                        mPreferences.edit().putString("nave", btn.getText().toString()).apply();
                        mPreferences.edit()
                                .putString(
                                        "nave_address",
                                        mAppDatabase.getNaves()
                                                .getAddressFromId((Long) btn.getTag())
                                ).apply();

                        // We change name an mail in UI
                        dataSet.get(position).setNameNave(btn.getText().toString());
                        dataSet.get(position)
                                .setAddressNave(mAppDatabase.getNaves()
                                        .getAddressFromId((Long) btn.getTag())
                                );

                        notifyDataSetChanged();
                        /* For development use only
                            Toast.makeText(
                            holder.itemView.getContext(),
                            "DATA REVIEW Cantera NAME: " + holder.getTvCantera().getText().toString(),
                            Toast.LENGTH_LONG
                            ).show();

                         */
                    }
                }
            }
        });
    }

    public AdaptadorCanteras(ArrayList<CanterasListClass> data) {
        this.dataSet = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_canteras, parent, false);

        return new AdaptadorCanteras.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.getTvCantera().setText(dataSet.get(position).getNameCantera());
        holder.getTvNave().setText(dataSet.get(position).getNameNave());
        holder.getTvAddress().setText(dataSet.get(position).getAddressNave());



        holder.ivSelectNave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showNaves(dataSet.get(position).getIdCantera(), position, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}