package com.cupagroup.controlcalidad.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cupagroup.controlcalidad.R;
import com.cupagroup.controlcalidad.ui.FragmentoCuenta;

import static com.cupagroup.controlcalidad.sync.SyncManager.syncAll;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private SharedPreferences mSharedPrefData;
    private String mShareFile = "pref_data";
    private String mPrefFile = "user_data";
    private Long mSessionID;
    private String mNave;
    private ProgressBar mProgressBar;
    private Handler mHandler;

    private int mProgressBarStatus = 0;
    private final int rID_Conf = R.id.item_configuracion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler();

        mPreferences = this.getApplication()
                .getSharedPreferences(mShareFile, Context.MODE_PRIVATE);
        mSharedPrefData = this.getApplication()
                .getSharedPreferences(mPrefFile, Context.MODE_PRIVATE);

        // CONDICIAONALES PARA IR A PERFIL
        mSessionID =  mPreferences.getLong("sessionId", 999999999);
        mNave = mSharedPrefData.getString("nave_name", "Sin declarar");

        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        syncAll(SplashActivity.this, mSessionID);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressBarStatus < 100){
                    mProgressBarStatus++;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgressBarStatus);
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mSessionID == 999999999){
                            if (syncAll(SplashActivity.this, mSessionID)){
                                Log.i("Sync","Synchronization successfully");
                            }else {
                                Log.e("Sync","Synchronization unsuccessfully");
                            }
                        }else{
                            Log.i("SessionINFO", "Session is active with number: "+mSessionID);
                        }
                        if(mNave.equals("Sin declarar")){
                            startActivity(new Intent(
                                    SplashActivity.this,
                                    MainActivity.class).putExtra("fragment_id",rID_Conf)
                            );
                            finish();
                        }else{
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        }).start();
    }
}
