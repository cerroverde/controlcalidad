package com.cupagroup.controlcalidad.ui;

import android.icu.util.VersionInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cupagroup.controlcalidad.BuildConfig;
import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.utils.Constants;

/**
 * Fragmento para la pestaña "TARJETAS" de la sección "Mi Cuenta"
 */
public class FragmentoTarjetas extends Fragment {
    private TextView mVersion;

    public FragmentoTarjetas() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String iVersion = "Versión "+BuildConfig.VERSION_NAME;

        // Set Version text from BuildConfig constant
        mVersion = (TextView) requireView().findViewById(R.id.about_version);
        mVersion.setText(iVersion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragmento_tarjetas, container, false);
    }


}
