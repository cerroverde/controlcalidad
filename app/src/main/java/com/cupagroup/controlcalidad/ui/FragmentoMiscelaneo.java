package com.cupagroup.controlcalidad.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.activities.MainActivity;
import com.cupagroup.controlcalidad.adapters.CustomListAdapterActiveEnsayo;
import com.cupagroup.controlcalidad.db.AppDatabase;
import com.cupagroup.controlcalidad.db.entity.QualityControl;
import com.cupagroup.controlcalidad.sync.SyncManager;
import com.cupagroup.controlcalidad.utils.CustomListActiveEnsayo;
import com.cupagroup.controlcalidad.utils.CustomOutlineProvider;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.cupagroup.controlcalidad.sync.SyncManager.sendDataSsesion;
import static com.cupagroup.controlcalidad.sync.SyncManager.sessionCycle;

public class FragmentoMiscelaneo extends Fragment  {
    private AppDatabase mAppDatabase;
    private SharedPreferences mPreferences;
    private SharedPreferences mDataShared;
    private SharedPreferences mDataUser;
    private Editable mComments;
    private Integer tFallos;

    // Campos respectivos de un item
    public TextView iControl;
    public TextView iCanteras;
    public TextView iPiezas;
    public TextView iNumeroControl;
    public TextView iCanteraNave;
    public TextView iCommentText;
    public TextView iPiezasNumero;

    public RelativeLayout iComment;
    //public ImageView iComment;

    public ImageView iCommentIcon;
    public Chip iSendEmail;
    public ListView iActiveEsayos;


    public Handler mHandler= new Handler();
    private CustomListAdapterActiveEnsayo adapter;
    private ArrayList<CustomListActiveEnsayo> dataModels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mShareFile = "pref_data";
        mPreferences = requireContext()
                .getSharedPreferences(mShareFile, Context.MODE_PRIVATE);
        String mShareCampos = "campos_data";
        mDataShared = requireContext()
                .getSharedPreferences(mShareCampos, Context.MODE_PRIVATE);
        String mShareUser = "user_data";
        mDataUser = requireContext()
                .getSharedPreferences(mShareUser, Context.MODE_PRIVATE);
        tFallos = 0;

    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragmento_comentario, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAppDatabase = AppDatabase.getInstance(this.getContext());

        iControl = (TextView) requireView().findViewById(R.id.info_numero_control);
        iCanteras = (TextView) requireView().findViewById(R.id.info_cantera);
        iPiezas = (TextView) requireView().findViewById(R.id.info_piezas);
        iNumeroControl = (TextView) requireView().findViewById(R.id.numero_control);
        iComment = (RelativeLayout) requireView().findViewById(R.id.icono_indicador_derecho);
        //iComment = (ImageView) requireView().findViewById(R.id.icono_indicador_derecho);
        iCommentIcon = (ImageView) requireView().findViewById(R.id.icono_password);
        iCommentText = (TextView) requireView().findViewById(R.id.info_comment_text);
        iCanteraNave = (TextView) requireView().findViewById(R.id.cantera_nave);
        iSendEmail = (Chip) requireView().findViewById(R.id.sendEmail);
        iPiezasNumero = (TextView) requireView().findViewById(R.id.piezas_numero);
        iActiveEsayos = (ListView) requireView().findViewById(R.id.lv_active_ensayos);

        Map<String, ?> entries = mDataShared.getAll();
        Set<String> keys = entries.keySet();

        String tComment = mPreferences.getString("comentario", "None");
        Long tControl = mPreferences.getLong("sessionId", 999999999);
        if (tControl.equals(99L)){
            sessionCycle(tControl);
            tControl = mPreferences.getLong("sessionId", 999999999);
        }

        String tNave = mDataUser.getString("nave_name", "Sin definir");
        Integer tPiezas = mAppDatabase.getQualityControl().getCountControls(tControl);


        // Stablish color depending of state of the comment
        if (tComment.equals("None")) {
            iCommentIcon.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.red),
                    PorterDuff.Mode.SRC_IN);
        }else {
            iCommentIcon.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.green),
                    PorterDuff.Mode.SRC_IN);
        }


        iNumeroControl.setText(String
                .format(
                        Locale.getDefault(),
                        "%d", tControl
                )
        );

        //Cambiamos el color del texto de Nº de Control, Nave y Nº de Piezas
        iNumeroControl.setTextColor(getResources().getColor(R.color.textTitulosColor));

        iCanteraNave.setText(tNave);
        iCanteraNave.setTextColor(getResources().getColor(R.color.textTitulosColor));

        iPiezasNumero.setText(String
                .format(
                        Locale.getDefault(),
                        "%d", tPiezas
                )
        );
        iPiezasNumero.setTextColor(getResources().getColor(R.color.textTitulosColor));
        //iFallos.setText(String.format(Locale.getDefault(),"Total de fallos %d", tFallos));


        // Email Status Resnponsable
        boolean mailStatus = mPreferences.getBoolean("send_email", false);
        if (mailStatus){
            if (!iSendEmail.isChecked() ){
                iSendEmail.setChecked(true);
            }
        }else {
            iSendEmail.setChecked(false);
        }

        //Press Send email button
        iSendEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mPreferences.edit().putBoolean("send_email", true).apply();

                }else{
                    mPreferences.edit().putBoolean("send_email", false).apply();
                }

            }
        });

        // Press Comment Button
        iComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( dialogComment() ){
                    iCommentIcon.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.green),
                            PorterDuff.Mode.SRC_IN);
                }else {
                    iCommentIcon.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.red),
                            PorterDuff.Mode.SRC_IN);
                }
            }
        });
        /*// Press End Session Button
        iEndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog loadingDialog = new LoadingDialog(v.getContext(), v);
                loadingDialog.setCancelable(false);
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
                loadingDialog.setFeatureDrawableResource(
                        Window.FEATURE_LEFT_ICON,
                        R.drawable.sync_black_36dp
                );
            }
        });
        //Press Add Ensayo Button
        iNewEnsayo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SyncManager.ensayoCycle(v).syncLocalToServer();
            }
        });*/

        getActiveEnsayos();
    }

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager)
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0 );

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i("RESUME", "AQUI SE RESUMIO OnPause");
    }

    public boolean dialogComment(){
        String savedComment = mPreferences.getString("comentario", "Sin comentario");
        boolean mResult;
        final AlertDialog dialogBuilder =
                new AlertDialog.Builder(
                        requireContext(),
                        R.style.MyAlertDialogStyle).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        editText.setText(savedComment);
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(
                        getContext(),
                        "No se realizan cambios en el comentario",
                        Toast.LENGTH_LONG
                ).show();
                mPreferences.edit().putString("comentario","None").apply();

                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mComments = editText.getText();
                if ( String.valueOf(mComments).equals("") || String.valueOf(mComments).isEmpty()){
                    Toast.makeText(
                            getContext(),
                            "Es mejor decir algo que no decir nada!!!",
                            Toast.LENGTH_LONG
                    ).show();
                }else {
                    mPreferences.edit().putString("comentario", String.valueOf(mComments)).apply();
                    iCommentIcon.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.green),
                            PorterDuff.Mode.SRC_IN);

                    dialogBuilder.dismiss();
                }
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();


        mResult = !mPreferences.getString("comentario", "None").equals("None");
        return mResult;
    }

    public boolean getActiveEnsayos(){
        boolean state = false;
        Long nSession = mAppDatabase.getSession().getIdFromActiveSession();
        List<QualityControl> activeEnsayosList =
                mAppDatabase.getQualityControl().getAllSessionById(nSession);
        dataModels = new ArrayList<>();
        CustomListActiveEnsayo customListActiveEnsayo = null;

        if (activeEnsayosList.size() > 1 ){ state = true; }
        for (int i=0; i < activeEnsayosList.size(); i++){
            Log.i("FECHA", "ESTA ES LA FECHA: " + activeEnsayosList.get(i).getCreate_at());

            customListActiveEnsayo = new CustomListActiveEnsayo(
                    activeEnsayosList.get(i).getEnsayo_id(),
                    activeEnsayosList.get(i).getRef_comercial(),
                    activeEnsayosList.get(i).getFormatos(),
                    activeEnsayosList.get(i).getForma(),
                    activeEnsayosList.get(i).getComment(),
                    activeEnsayosList.get(i).getCreate_at()
            );

            /*customListActiveEnsayo.setmComment(activeEnsayosList.get(i).getComment());
            customListActiveEnsayo.setmCreatedAt(activeEnsayosList.get(i).getCreate_at());
            customListActiveEnsayo.setmFormatos(formatos);*/

            dataModels.add(customListActiveEnsayo);

        }
        adapter = new CustomListAdapterActiveEnsayo(dataModels, requireContext(),1);
        iActiveEsayos.setAdapter(adapter);

        iActiveEsayos.setElevation(5);
        iActiveEsayos.setOutlineProvider( new CustomOutlineProvider(5));
        iActiveEsayos.setClipToOutline(true);
        iActiveEsayos.setDivider(null);


        return state;
    }

    public static class LoadingDialog extends Dialog{
        private final Context mContext;
        private final View mView;
        private int pStatus = 0;
        private TextView tv;
        AppDatabase mAppDatabase;
        private FragmentManager fragmentManager;

        public LoadingDialog(Context context, View v, FragmentManager fragmentManager){
            super(context);
            mContext = context;
            mView = v;
            this.fragmentManager = fragmentManager;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mAppDatabase = AppDatabase.getInstance(this.getContext());
            Handler mHandler= new Handler();
            requestWindowFeature(Window.FEATURE_LEFT_ICON);
            setTitle("Procesando");


            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflateView = inflater.inflate(R.layout.loading_dialog, findViewById(R.id.loading_cont));
            setContentView(inflateView);


            Drawable drawable =
                    ResourcesCompat.getDrawable(
                            getContext().getResources(),
                            R.drawable.circular_progress,
                            null
                    );
            final ProgressBar mProgress = findViewById(R.id.circularProgressbar);
            mProgress.setProgress(0);
            mProgress.setSecondaryProgress(100);
            mProgress.setMax(100);
            mProgress.setProgressDrawable(drawable);
            tv = (TextView) findViewById(R.id.tv);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    new sendDataSsesion(mView, LoadingDialog.this, fragmentManager).execute();
                    while (pStatus < 100){
                        pStatus += 1;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.setProgress(pStatus);
                                String txt = pStatus + "%";
                                tv.setText(txt);
                            }
                        });
                        try {
                            Thread.sleep(120);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            // Al parecer estaba creando un ensayo nuevo en el array :(
                            /*mAppDatabase.getQualityControl()
                                    .insert(new SyncManager.ensayoCycle(mView).getDataQuality());*/

                            /**
                             * Ya estaba comentado
                             */
                            //new sendDataSsesion(mView).execute();
                            //dismiss();
                        }
                    });
                }
            }).start();

        }
    }


}
