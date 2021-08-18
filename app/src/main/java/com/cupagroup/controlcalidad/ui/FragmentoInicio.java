package com.cupagroup.controlcalidad.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.adapters.AdaptadorInicio;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Fragmento para la sección de "Inicio"
 */
public class FragmentoInicio extends Fragment {
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorInicio adaptador;
    private ExtendedFloatingActionButton floatingCount;


    public FragmentoInicio(ExtendedFloatingActionButton floatingActionButton) {
        this.floatingCount = floatingActionButton;

        floatingActionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_inicio, container, false);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);

        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);

        adaptador = new AdaptadorInicio(floatingCount);
        reciclador.setAdapter(adaptador);

        return view;
    }

}
