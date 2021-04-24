package com.mrsoftit.studentearn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startapp.sdk.adsbase.StartAppAd;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    AlertDialog.Builder builder;

   boolean intenStart;

    public SharedPreferences coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        StartAppAd.disableSplash();


        builder = new AlertDialog.Builder(this);

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);

        FirebaseUser user = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Thread background = new Thread() {
                        public void run() {
                            try {

                                intenStart = true;

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("Catch block", Log.getStackTraceString(e));
                            }
                        }
                    };
                    background.start();
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {

                    intenStart = false;
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };


        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork) {
        } else {
            Intent intent = new Intent(SplashScreen.this, NoInternetActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
        String date = dateFormat.format(new Date());

        SharedPreferences.Editor coinsEdit = coins.edit();

        String pDate = coins.getString("date", "0");


        if (!pDate.equals(date)){
            coinsEdit.putString("date",date);
            coinsEdit.putString("quzsCount","15");
            coinsEdit.putString("cupchacount","10");
            coinsEdit.putString("spincount","10");
            coinsEdit.putString("dailyVideocount","10");
            coinsEdit.putString("scrach","10");
            coinsEdit.putString("TimerQC","10");
            coinsEdit.apply();

        }

        if (user != null){
            Intent intent = new Intent(getApplicationContext(), ChoiceSelection.class);
            startActivity(intent);
            finish();

        }else{
            Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
            startActivity(intent);
            fileList();
        }

      /*  if (!checkVPN()){
            //Setting message manually and performing action on button click
            builder.setMessage("Place connect USA VPN")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();
            return;
        }else {

        }*/







    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // release listener in onStop
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public boolean checkVPN() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting();
    }
}
