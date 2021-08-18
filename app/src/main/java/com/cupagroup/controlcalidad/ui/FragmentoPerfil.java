package com.cupagroup.controlcalidad.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.db.AppDatabase;

import java.util.List;
import java.util.Objects;

/**
 * Fragmento para la pestaña "PERFIL" De la sección "Mi Cuenta"
 */
public class FragmentoPerfil extends Fragment {
    private AppDatabase mAppDatabase;
    private SharedPreferences mPreferences;
    private SharedPreferences mSharedPrefData;
    private String mShareFile = "user_data";
    private String mPrefFile = "pref_data";

    public ImageView icUserSettings;
    public ImageView icNaveSettings;
    public TextView tvUserName, tvUserMail, tvNaveName, tvNaveAddress, tvResponsableName;



    public FragmentoPerfil() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppDatabase = AppDatabase.getInstance(requireContext());
        mPreferences = requireContext()
                .getSharedPreferences(mShareFile, Context.MODE_PRIVATE);
        mSharedPrefData = requireContext()
                .getSharedPreferences(mPrefFile, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragmento_perfil, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        icUserSettings = requireView().findViewById(R.id.ic_user_settings);
        icNaveSettings = requireView().findViewById(R.id.account_set_nave);

        // User TextView
        tvUserName = requireView().findViewById(R.id.texto_nombre);
        tvUserMail = requireView().findViewById(R.id.texto_email);

        // Nave TextView
        tvNaveName = requireView().findViewById(R.id.account_nave_nombre);
        tvNaveAddress = requireView().findViewById(R.id.account_nave_address);

        // Responsable TextView
        tvResponsableName = requireView().findViewById(R.id.texto_responsable);

        // Set text
        tvUserMail.setText(mPreferences.getString("user_mail", "Sin correo registrado"));
        tvUserName.setText(mPreferences.getString("user_name", "Usuario no definido"));

        tvNaveName.setText(mPreferences.getString("nave_name", "Sin declarar"));
        tvNaveAddress.setText(mPreferences.getString("nave_address", "Sin nave, no hay dirección"));

        tvResponsableName.setText(mPreferences.getString("responsable_name", "Sin declarar"));


        icUserSettings.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showUserSettings();
            }
        });

        icNaveSettings.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showNavesSettings();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showNavesSettings(){
        final Dialog dialogNave = new Dialog(getContext());
        List<String> navesEntries = mAppDatabase.getNaves().getAllNavesEntries();

        dialogNave.setTitle("Naves disponibles");
        dialogNave.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialogNave.setContentView(R.layout.radiobutton_dialog);

        RadioGroup radioGroup = dialogNave.findViewById(R.id.radio_group);
        for (int i = 0; i < navesEntries.size(); i++){
            RadioButton radioButton = new RadioButton(requireContext());
            long mNaveID = mAppDatabase.getNaves().getIdByNaveName(navesEntries.get(i));

            radioButton.setTextSize(18);
            radioButton.setPadding(15,15,15,15);
            radioButton.setId(Math.toIntExact(mNaveID));
            radioButton.setText(navesEntries.get(i));

            radioGroup.addView(radioButton);
        }
        dialogNave.show();
        dialogNave.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_business);
        radioGroup.check((int) mPreferences.getLong("nave_id", 1));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCountNave = group.getChildCount();
                for (int x = 0; x < childCountNave; x++){
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId){
                        long mNaveID = mAppDatabase.getNaves().getIdByNaveName(btn.getText().toString());
                        String mAddress = mAppDatabase.getNaves().getAddressFromId(mNaveID);
                        String mResponsable = "Responsable: " + mAppDatabase
                                .getResponsable().getResponsableNameByNaveID(mNaveID);
                        String mResponsableEmail = mAppDatabase
                                .getResponsable().getResponsableEmailByNaveID(mNaveID);

                        // Save data into preference file "user_data"
                        SharedPreferences.Editor mPreferencesEdit = mPreferences.edit();

                        // Save Nave data
                        mPreferencesEdit.putLong("nave_id", mNaveID);
                        mPreferencesEdit.putString("nave_name", btn.getText().toString());
                        mPreferencesEdit.putString("nave_address", mAddress);

                        // Save Responsable Data into file "pref_data"
                        mPreferencesEdit.putString("responsable_name", mResponsable);
                        mPreferencesEdit.putString("responsable_email", mResponsableEmail);

                        // Save data
                        mPreferencesEdit.apply();



                        // We change Nave name an address in UI
                        tvNaveName.setText(btn.getText().toString());
                        tvNaveAddress.setText(mAddress);
                        tvResponsableName.setText(mResponsable);

                        //Close dialog box
                        dialogNave.dismiss();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showUserSettings(){
        final Dialog dialog = new Dialog(getContext());
        List<String> userEntries = mAppDatabase.getPreferenceUser().getAllEntries();


        dialog.setTitle(R.string.label_user_pref);
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.setContentView(R.layout.radiobutton_dialog);

        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
        for (int i = 0; i < userEntries.size(); i++){
            RadioButton radioButton = new RadioButton(requireContext());
            long mUserID = mAppDatabase.getPreferenceUser().getIdByName(userEntries.get(i));
            radioButton.setTextSize(18);
            radioButton.setPadding(15,15,15,15);
            radioButton.setId(Math.toIntExact(mUserID));
            radioButton.setText(userEntries.get(i));

            radioGroup.addView(radioButton);
        }
        dialog.show();
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.usuario);
        radioGroup.check((int) mPreferences.getLong("user_id", 1));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x=0; x < childCount; x++){
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId){
                        Long userID = mAppDatabase
                                .getPreferenceUser().getIdByName(btn.getText().toString());
                        String userMail = mAppDatabase.getPreferenceUser().getEmailById(userID);

                        // Save data into preference file "user_data"
                        mPreferences.edit().putString("user_name", btn.getText().toString()).apply();
                        mPreferences.edit().putString("user_mail", userMail).apply();
                        mPreferences.edit().putLong("user_id", userID).apply();

                        // We change name an mail in UI
                        tvUserMail.setText(userMail);
                        tvUserName.setText(btn.getText().toString());

                        dialog.dismiss();

                        /* For development use only
                        Toast.makeText(
                                requireContext(),
                                "Usuario modificado!!!"+mPreferences.getLong("user_id", 1),
                                Toast.LENGTH_LONG
                        ).show();

                         */
                    }
                }
            }
        });
    }
}
