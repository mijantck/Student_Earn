package com.mrsoftit.studentearn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class LuckyWheel extends AppCompatActivity {

    private Calendar calendar;
    private int weekday;
    private String todayString;
    List<LuckyItem> data = new ArrayList<>();
    private int coin;

    private Button play;

    private static final long START_TIME_IN_MILLIS = 180000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    SharedPreferences coins;
     AdView mAdViewl1,mAdViewl2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_wheel);

        play = findViewById(R.id.play);


        ImageView imageView = findViewById(R.id.imageView11);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        coins = getSharedPreferences("Rewards", MODE_PRIVATE);
        final LuckyWheelView luckyWheelView = (LuckyWheelView) findViewById(R.id.luckyWheel);

        findViewById(R.id.play).setEnabled(true);
        findViewById(R.id.play).setAlpha(1f);


        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        weekday = calendar.get(Calendar.DAY_OF_WEEK);
        todayString = year + "" + month + "" + day;
        final SharedPreferences spinChecks = getSharedPreferences("SPINCHECK", 0);
        final boolean currentDay = spinChecks.getBoolean(todayString, false);


        //Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdViewl1 = findViewById(R.id.adViewl1);
        mAdViewl2 = findViewById(R.id.adViewl2);
        AdRequest adRequestl1 = new AdRequest.Builder().build();
        AdRequest adRequestl2 = new AdRequest.Builder().build();
        mAdViewl1.loadAd(adRequestl1);
        mAdViewl2.loadAd(adRequestl2);



        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.text = "20";
        luckyItem1.color = Color.parseColor("#8574F1");
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.text = "5";
        luckyItem2.color = Color.parseColor("#8E84FF");
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.text = "10";
        luckyItem3.color = Color.parseColor("#752BEF");
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.text = "25";
        luckyItem4.color = ContextCompat.getColor(getApplicationContext(), R.color.Spinwell140);
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.text = "20";
        luckyItem5.color = Color.parseColor("#8574F1");
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.text = "15";
        luckyItem6.color = Color.parseColor("#8E84FF");
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.text = "7";
        luckyItem7.color = Color.parseColor("#752BEF");
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.text = "3";
        luckyItem8.color = ContextCompat.getColor(getApplicationContext(), R.color.Spinwell140);
        data.add(luckyItem8);

        luckyWheelView.setData(data);
        luckyWheelView.setRound(getRandomRound());

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTimerRunning) {


                } else {

                    int index = getRandomIndex();

                    luckyWheelView.startLuckyWheelWithTargetIndex(index);

                    SharedPreferences.Editor spins = spinChecks.edit();
                    spins.putBoolean(todayString, true);
                    spins.apply();
                    findViewById(R.id.play).setEnabled(false);
                    findViewById(R.id.play).setAlpha(.5f);


                    startTimer();
                    showRewardedVideo();

                }

            }
        });
        luckyWheelView.setLuckyRoundItemSelectedListener(index -> {
            if (index ==1 ){
                coin = 20;
            } if (index ==2 ){
                coin = 5;
            } if (index ==3 ){
                coin = 10;
            } if (index ==4 ){
                coin = 25;
            } if (index ==5){
                coin = 20;
            } if (index ==6 ){
                coin = 15;
            } if (index ==7 ){
                coin = 7;
            } if (index ==8 ){
                coin = 3; }

            Toast.makeText(getApplicationContext(), String.valueOf("+ " + coin +" Coins"), Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor coinsEdit = coins.edit();

            int coinCount = Integer.parseInt(coins.getString("Coins", "0"));
            coinCount = coinCount + (coin);

            coinsEdit.putString("Coins", String.valueOf(coinCount));
            coinsEdit.apply();

        });
    }

    private int getRandomIndex() {
        int[] ind = new int[] {1,2,3,4,5,6,7,8};
        int rand = new Random().nextInt(ind.length);
        return ind[rand];
    }

    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(10) + 15;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,ChoiceSelection.class);
        startActivity(intent);
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
            play.setText("start");
        }else {
            play.setText(timeLeftFormatted);
        }

    }
    private void updateButtons() {
        if (mTimerRunning) {
            //  mButtonReset.setVisibility(View.INVISIBLE);
            play.setText("Pause");
        } else {

            if (mTimeLeftInMillis < 1000) {
                play.setText("start");
            } else {
                play.setVisibility(View.VISIBLE);
            }
            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                resetTimer();
                play.setVisibility(View.VISIBLE);
            } else {

            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
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
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
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
    public void showRewardedVideo() {
        final StartAppAd rewardedVideo = new StartAppAd(this);


        rewardedVideo.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
                // Grant the reward to user

            }
        });

        rewardedVideo.loadAd(StartAppAd.AdMode.VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                rewardedVideo.showAd();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                // Can't show rewarded video
            }
        });
    }


}
