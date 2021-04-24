package com.mrsoftit.studentearn;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.anupkumarpanwar.scratchview.ScratchView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.startapp.sdk.ads.banner.Mrec;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.text.DecimalFormat;
import java.util.Locale;

import in.myinnos.androidscratchcard.ScratchCard;

public class ScratchActivity extends AppCompatActivity  {


    private ScratchCard mScratchCard;
    public SharedPreferences coins;

    private boolean connected;
    private String currentCoins;
    private String Name;
    private String Phone;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mRef;


    private static final long START_TIME_IN_MILLIS = 180000;
   // private static final long START_TIME_IN_MILLIS = 9000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;
    int newcoitn;
    int videoCount;

    TextView timer;
    ImageView back;
    TextView textViewCoins1,counthave,timert,textView22,textViewDoller11;

    ScratchView scratchView;

    AdView mAdViews1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);


        scratchView = findViewById(R.id.scratch_view);

       // mScratchCard = (ScratchCard) findViewById(R.id.scratchCard);
        textViewCoins1 = findViewById(R.id.textViewCoins1);
        counthave = findViewById(R.id.counthave);
        timert = findViewById(R.id.timert);
        textView22 = findViewById(R.id.textView22);
        back = findViewById(R.id.back);
        textViewDoller11 = findViewById(R.id.textViewDoller11);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        coins = getSharedPreferences("Rewards", MODE_PRIVATE);
        currentCoins = coins.getString("Coins", "0");


        videoCount = Integer.parseInt(coins.getString("scrach", "0"));

        textViewCoins1.setText(currentCoins+"");

        counthave.setText(videoCount+"/ 10");


        mAdViews1 = findViewById(R.id.adViews1);
        AdRequest adRequestl1 = new AdRequest.Builder().build();
        mAdViews1.loadAd(adRequestl1);

        int coinCount = Integer.parseInt(coins.getString("Coins", "0"));
        float x = (float) (0.0002 * coinCount);
        if (x >= 0) {
            textViewDoller11.setText(new DecimalFormat("##.###").format(x)+"");
        }

        SharedPreferences prefs = getSharedPreferences("dasda", MODE_PRIVATE);
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        if (mTimerRunning){
            scratchView.setVisibility(View.GONE);
            timert.setVisibility(View.VISIBLE);

        }

        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                scratchView.setVisibility(View.GONE);

                SharedPreferences.Editor coinsEdit = coins.edit();
                if (mTimerRunning) {
                    timert.setVisibility(View.VISIBLE);
                    return;
                }else {
                    int coinCount1 = Integer.parseInt(coins.getString("Coins", "0"));
                    coinCount1 = coinCount1 + 5;
                    coinsEdit.putString("Coins", String.valueOf(coinCount1));
                    coinsEdit.apply();

                    startTimer();
                    timert.setVisibility(View.VISIBLE);

                    showRewardedVideo();
                }
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if (percent>=0.5) {
                    Log.d("Reveal Percentage", "onRevealPercentChangedListener: " + String.valueOf(percent));
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

        if (timeLeftFormatted.equals("01:00")){

            mScratchCard.setVisibility(View.VISIBLE);
        }else {
            timert.setText(timeLeftFormatted);
        }

    }
    private void updateButtons() {
        if (mTimerRunning) {
            //  mButtonReset.setVisibility(View.INVISIBLE);
        } else {

            if (mTimeLeftInMillis < 1000) {
                timert.setVisibility(View.GONE);

            } else {

            }
            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                resetTimer();
            } else {

            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("dasda", MODE_PRIVATE);
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
        SharedPreferences prefs = getSharedPreferences("dasda", MODE_PRIVATE);
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