package com.cupagroup.controlcalidad.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.cupagroup.controlcalidad.BuildConfig;
import com.cupagroup.controlcalidad.db.AppDatabase;
import com.cupagroup.controlcalidad.db.entity.QualityControl;
import com.cupagroup.controlcalidad.db.entity.Responsables;
import com.cupagroup.controlcalidad.sync.MailSender;
import com.cupagroup.controlcalidad.sync.SyncManager;
import com.cupagroup.controlcalidad.ui.FragmentoCategorias;
import com.cupagroup.controlcalidad.ui.FragmentoCuenta;
import com.cupagroup.controlcalidad.ui.FragmentoInicio;
import com.cupagroup.controlcalidad.ui.FragmentoMiscelaneo;
import com.cupagroup.controlcalidad.utils.MovableActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.cupagroup.controlcalidad.R;

import static com.cupagroup.controlcalidad.utils.Constants.SENDER_EMAIL;
import static com.cupagroup.controlcalidad.utils.Constants.SENDER_PASSWORD;
import static com.cupagroup.controlcalidad.utils.Constants.SERVER_URL;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private MovableActionButton floatingActionButton;

    //Menu Lateral
    public final int rID_Home = R.id.item_inicio;
    public final int rID_MyAccount = R.id.item_cuenta;
    public final int rID_Categories = R.id.item_categorias;
    public final int rID_Conf = R.id.item_configuracion;

    //Menu Interno
    public final int rID_Add = R.id.action_add;
    public final int rID_Save = R.id.action_save;

    // Shared Files
    private static AppDatabase mAppDatabase;
    private static SharedPreferences mPreferences;
    private static SharedPreferences mDataShared;
    private static SharedPreferences mUserData;
    private static final String mDataUser = "user_data";
    private static final String mShareFile = "pref_data";
    private static final String mShareCampos = "campos_data";

    // Fragment
    public Fragment fragmentoGenerico = null;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);

        // Integer define in SplashActivity for declare Nave/Cantera if it doesn't have been declare
        int intentExtraValue = getIntent().getIntExtra("fragment_id", 0);
        agregarToolbar();

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView =  findViewById(R.id.nav_view);

        // Contador flotante
        floatingActionButton = findViewById(R.id.floating_count);
        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Database and Shared Files
        mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        // Preferencias pref_data
        mPreferences = getApplicationContext()
                .getSharedPreferences(mShareFile, MODE_PRIVATE);
        // Preferencias campos_data
        mDataShared = getApplicationContext()
                .getSharedPreferences(mShareCampos, MODE_PRIVATE);
        // Preferencias user_data
        mUserData = getApplicationContext()
                .getSharedPreferences(mDataUser, MODE_PRIVATE);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Responsable Details
                String responsable_name = mUserData.getString("responsable_name", "No Responsable");
                String responsable_email = mUserData.getString("responsable_email", "No Responsable");
                Log.i("floating", responsable_email + " / " + responsable_name);

                Map<String, ?> allEntries = mUserData.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                }

                // this type of vibration requires API 29
                final VibrationEffect vibrationEffect1;
                final VibrationEffect vibrationEffect2;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    // this effect creates the vibration of default amplitude for 1000ms(1 sec)
                    vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);

                    // create vibrator effect with the constant EFFECT_CLICK
                    vibrationEffect2 = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);

                    // it is safe to cancel other vibrations currently taking place
                    vibrator.cancel();

                    vibrator.vibrate(vibrationEffect1);
                }
            }
        });

        if (navigationView != null) {
            prepararDrawer(navigationView);

            // Seleccionar item por defecto
            if (intentExtraValue == rID_Conf) {
                seleccionarItem(navigationView.getMenu().getItem(1));
            }else{
                seleccionarItem(navigationView.getMenu().getItem(0));
            }

        }else {
            Log.i("Background", "Color + "+drawerLayout.getBackground());
        }

        floatingActionButton.setOnLongClickListener(
                v -> {
                    v.setRotationX(90);
                    v.setRotation(90);

                    return false;
                });
    }


    private void agregarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Colocar ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.drawer_toggle);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setSubtitle("Control de calidad");
        }

    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    menuItem.setChecked(true);
                    seleccionarItem(menuItem);
                    drawerLayout.closeDrawers();

                    return true;
                });
    }

    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) {
            case rID_Home:
                fragmentoGenerico = new FragmentoInicio(floatingActionButton);
                break;
            case rID_MyAccount:
                fragmentoGenerico = new FragmentoCuenta();
                floatingActionButton.setVisibility(View.INVISIBLE);
                break;
            case rID_Categories:
                fragmentoGenerico = new FragmentoCategorias();
                floatingActionButton.setVisibility(View.VISIBLE);

                break;
            case rID_Conf:
                floatingActionButton.setVisibility(View.INVISIBLE);
                startActivity(new Intent(this, ActividadConfiguracion.class));
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragmentoGenerico)
                    .commit();
        }

        // Setear título actual
        setTitle(itemDrawer.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = findViewById(item.getItemId());

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case rID_Add:
                new SyncManager.ensayoCycle(view).syncLocalToServer(fragmentManager);
                break;

            case rID_Save:
                // Check if we send email to manager
                boolean sendEmail = mPreferences.getBoolean("send_email", false);

                // Retreive the rest of all data
                List<QualityControl> mData = mAppDatabase.getQualityControl()
                        .getAllSessionById(mPreferences.getLong("sessionId", 999999999));
                String mEmail = mDataShared.getString("responsable_email", "malopez@cupagroup.com");

                FragmentoMiscelaneo.LoadingDialog loadingDialog =
                        new FragmentoMiscelaneo.LoadingDialog(view.getContext(), view, fragmentManager);
                loadingDialog.setCancelable(false);
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
                loadingDialog.setFeatureDrawableResource(
                        Window.FEATURE_LEFT_ICON,
                        R.drawable.sync_black_36dp
                );

                if (sendEmail){
                    Log.i("EMAIL", "Es verdadero!!!");
                    envioCorreo(view);
                }else{
                    Log.i("EMAIL", "Es falso!!!");
                }

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void setTextCount(int value){
        String texto = String.valueOf(value);
        Drawable floatinIcon = ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.ic_drag_indicator,
                null);

        floatingActionButton.setText(texto);
        floatingActionButton.setIcon(floatinIcon);
        floatingActionButton.setIconSize(38);
    }

    public void envioCorreo(View view){
        new MailCreator().execute("");
    }

    public class MailCreator extends AsyncTask<String, String, String> {
        // Variables
        Long sessionID = mAppDatabase.getSession().getIdFromActiveSession();

        // Declaracion de varaibles
        // User details
        String user_name = mUserData.getString("user_name","No user name");
        String user_mail = mUserData.getString("user_mail","No user mail");
        Long user_id =  mUserData.getLong("user_id",0L);

        // Nave/Cantera Details
        String nave_name = mUserData.getString("nave_name","No nave name");
        String cantera_name = mDataShared.getString("canteraName","No cantera name");

        // Responsable Details
        String responsable_name = mUserData.getString("responsable_name", "No Responsable name");
        String responsable_email = mUserData.getString("responsable_email", "No Responsable email");

        @Override
        protected String doInBackground(String... strings) {
            try {

                MailSender sender = new MailSender(
                        getBaseContext(),
                        SENDER_EMAIL,
                        SENDER_PASSWORD
                );
                sender.sendUserDetailWithImage(
                        "Nuevo Ensayo",
                        "Hi",
                        user_mail,
                        responsable_email,
                        user_name,
                        nave_name,
                        cantera_name,
                        SERVER_URL+"calidad/show/"+user_id+"/"+sessionID
                );

            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"Mail Sent",Toast.LENGTH_LONG).show();
        }
    }
}
