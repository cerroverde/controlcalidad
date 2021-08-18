package com.cupagroup.controlcalidad.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.adapters.AdaptadorMaterial;
import com.cupagroup.controlcalidad.modelo.Campos;

/**
 * Fragmento que representa el contenido de cada pestaña dentro de la sección "Categorías"
 */
public class FragmentoCategoria extends Fragment {

    private static final String INDICE_SECCION
            = "com.restaurantericoparico.FragmentoCategoriasTab.extra.INDICE_SECCION";

    private RecyclerView reciclador;
    private GridLayoutManager layoutManager;
    private AdaptadorMaterial adaptadorMaterial;

    public static FragmentoCategoria nuevaInstancia(int indiceSeccion) {
        FragmentoCategoria fragment = new FragmentoCategoria();
        Bundle args = new Bundle();
        args.putInt(INDICE_SECCION, indiceSeccion);
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        int indiceSeccion = getArguments().getInt(INDICE_SECCION);
        View view = inflater.inflate(R.layout.fragmento_grupo_items, container, false);
        reciclador = view.findViewById(R.id.reciclador);
        layoutManager = new GridLayoutManager(getActivity(), 2);

        reciclador.setLayoutManager(layoutManager);

        if (indiceSeccion == 0) {
            adaptadorMaterial = new AdaptadorMaterial(getActivity(),Campos.MATERIALS);
        }

        reciclador.setAdapter(adaptadorMaterial);
        return view;
    }
}