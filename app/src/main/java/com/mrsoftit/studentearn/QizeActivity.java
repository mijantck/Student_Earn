package com.mrsoftit.studentearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.startapp.sdk.ads.banner.Mrec;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.text.DecimalFormat;
import java.util.Locale;

public class QizeActivity extends AppCompatActivity {


    TextView quationId,a,b,c,d,TimerQ,counthave1Q;

    String CourrectAns1 = "member function";

    private static final long START_TIME_IN_MILLIS = 180000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    SharedPreferences coins;


    TextView textViewCoins2444,textViewDoller;
    int spinCount;
    private AdView mAdViewq1;


    String q1 = "In C++, a function contained within a class is called";
    String q2 = "The function abort() is declared in the header file";

    String a1 = " boolean values";
    String a2 = "<math.h>";

    String b1 = " boolean value";
    String b2 = " <iostream.h>";

    String c1 = " numeric values";
    String c2 = " <stdio.h>";

    String d1 = "numeric value";
    String d2 = " <stdlib.h>";

    String qS,aS,bS,cS,dS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qize);

        quationId = findViewById(R.id.quationId);
        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        textViewDoller = findViewById(R.id.textViewDoller);
        TimerQ = findViewById(R.id.TimerQ);
        counthave1Q = findViewById(R.id.counthave1Q);
        textViewCoins2444 = findViewById(R.id.textViewCoins2444);

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);
        spinCount = Integer.parseInt(coins.getString("TimerQC", "0"));
        int coinCount = Integer.parseInt(coins.getString("Coins", "0"));

        float x = (float) (0.0002 * coinCount);
        if (x >= 0) {
            textViewDoller.setText(new DecimalFormat("##.###").format(x)+"");
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdViewq1 = findViewById(R.id.adViewq1);
        AdRequest adRequestl1 = new AdRequest.Builder().build();
        mAdViewq1.loadAd(adRequestl1);



        qS = coins.getString("q",q1);
        aS = coins.getString("a", a1);

        bS = coins.getString("b", b1);
        cS = coins.getString("c", c1);
        dS = coins.getString("d", d1);

        a.setText(aS);
        b.setText(bS);
        c.setText(cS);
        d.setText(dS);


        textViewCoins2444.setText(coinCount+"");
        counthave1Q.setText(spinCount+"/10");





        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTimerRunning){
                    Toast.makeText(QizeActivity.this, "Time is Running ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(QizeActivity.this, "Try again", Toast.LENGTH_SHORT).show();

                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning){
                    Toast.makeText(QizeActivity.this, "Time is Running ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(QizeActivity.this, "Try again", Toast.LENGTH_SHORT).show();

                }
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTimerRunning){
                    Toast.makeText(QizeActivity.this, "Time is Running ", Toast.LENGTH_SHORT).show();

                    return;
                }

                int coinCount = Integer.parseInt(coins.getString("Coins", "0"));

                coinCount = coinCount + 5;

                SharedPreferences.Editor coinsEditC = coins.edit();
                coinsEditC.putString("Coins", String.valueOf(coinCount));
                coinsEditC.apply();

                textViewCoins2444.setText(coinCount+"");

                  startTimer();

                if (quationId.getText().toString().equals(q1)){
                    quationId.setText(q2);
                    a.setText(a2);
                    b.setText(b2);
                    c.setText(c2);
                    d.setText(d2);

                    SharedPreferences.Editor coinsEdit1 = coins.edit();
                    coinsEdit1.putString("q",q2);
                    coinsEdit1.putString("a", a2);
                    coinsEdit1.putString("b", b2);
                    coinsEdit1.putString("c", c2);
                    coinsEdit1.putString("d", d2);
                    coinsEdit1.apply();
                }else {
                    quationId.setText(q1);
                    a.setText(a1);
                    b.setText(b1);
                    c.setText(c1);
                    d.setText(d1);

                    SharedPreferences.Editor coinsEdit1 = coins.edit();
                    coinsEdit1.putString("q",q1);
                    coinsEdit1.putString("a", a1);
                    coinsEdit1.putString("b", b1);
                    coinsEdit1.putString("c", c1);
                    coinsEdit1.putString("d", d1);
                    coinsEdit1.apply();
                    final FirebaseDatabase database =  FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/");
                    FirebaseAuth  mAuth = FirebaseAuth.getInstance();
                    final FirebaseUser user =  mAuth.getCurrentUser();
                    final String userId = user.getUid();

                    DatabaseReference mRef =  database.getReference().child("Users").child(userId);

                    mRef.child("Coins").setValue(String.valueOf(coinCount));


                }


                final StartAppAd rewardedVideo = new StartAppAd(QizeActivity.this);

                rewardedVideo.loadAd(StartAppAd.AdMode.VIDEO, new AdEventListener() {
                    @Override
                    public void onReceiveAd(Ad ad) {
                        rewardedVideo.showAd();
                    }

                    @Override
                    public void onFailedToReceiveAd(Ad ad) {

                    }
                });



            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning){
                    Toast.makeText(QizeActivity.this, "Time is Running ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(QizeActivity.this, "Try again", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateButtons();
            }
        }.start();
        mTimerRunning = true;
        updateButtons();
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }


    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        updateButtons();
    }
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        if (timeLeftFormatted.equals("03:00")){
            TimerQ.setText("");
        }else {

            TimerQ.setText(timeLeftFormatted);
        }


    }
    private void updateButtons() {
        if (mTimerRunning) {
            //  mButtonReset.setVisibility(View.INVISIBLE);
        } else {

            if (mTimeLeftInMillis < 1000) {
            } else {
                TimerQ.setVisibility(View.VISIBLE);
            }
            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                resetTimer();
                TimerQ.setVisibility(View.VISIBLE);
            } else {

            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("TimerQ", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("TimerQ", MODE_PRIVATE);
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        updateButtons();
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }

}