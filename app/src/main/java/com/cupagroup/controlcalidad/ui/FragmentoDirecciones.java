package com.cupagroup.controlcalidad.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.activities.CanterasListClass;
import com.cupagroup.controlcalidad.activities.MainActivity;
import com.cupagroup.controlcalidad.adapters.AdaptadorCanteras;
import com.cupagroup.controlcalidad.adapters.AdaptadorDirecciones;
import com.cupagroup.controlcalidad.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento para la pestaña "DIRECCIONES" De la sección "Mi Cuenta"
 */
public class FragmentoDirecciones extends Fragment {
    private AppDatabase mAppDatabase;
    private SharedPreferences mPreferences;
    private String mShareFile = "user_data";
    private  static AdaptadorCanteras adaptadorCanteras;

    private Integer canteraPosition;
    private String naveName;
    private String naveAddress;


    public RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    public ArrayList<CanterasListClass> dataModels;


    public FragmentoDirecciones() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppDatabase = AppDatabase.getInstance(getContext());
        mPreferences = requireContext()
                .getSharedPreferences(mShareFile, Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragmento_canteras, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) requireView().findViewById(R.id.lvCanteras);
        mLayoutManager = new LinearLayoutManager(getActivity());
        canteraPosition = mPreferences.getInt("cantera_position",0);
        naveName = mPreferences.getString("nave", "Sin declarar");
        naveAddress = mPreferences.getString("nave_address", "Sin declarar");

        mRecyclerView.setLayoutManager(mLayoutManager);

        List<CanterasListClass> mCanteras = mAppDatabase.getCanteras().getAllCanteras();

        dataModels = new ArrayList<>();
        dataModels.addAll(mCanteras);
        dataModels.get(canteraPosition).setNameNave(naveName);
        dataModels.get(canteraPosition).setAddressNave(naveAddress);

        adaptadorCanteras = new AdaptadorCanteras(dataModels);
        mRecyclerView.setAdapter(adaptadorCanteras);

    }
}
