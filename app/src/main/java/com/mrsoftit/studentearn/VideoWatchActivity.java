package com.mrsoftit.studentearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
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

import java.text.DecimalFormat;
import java.util.Locale;

public class VideoWatchActivity extends AppCompatActivity {




    private static final long START_TIME_IN_MILLIS = 180000;
    // private static final long START_TIME_IN_MILLIS = 9000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;
    int newcoitn;
    int videoCount;

    TextView timer,textViewCoins1ee,textViewDoller11sd;
    ImageView back;

    public SharedPreferences coins;
    private String currentCoins;



    private AdView mAdViewq1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_watch);

        timer = findViewById(R.id.yimer);
        textViewCoins1ee = findViewById(R.id.textViewCoins1ee);
        textViewDoller11sd = findViewById(R.id.textViewDoller11sd);
        back = findViewById(R.id.back);


        coins = getSharedPreferences("Rewards", MODE_PRIVATE);
        currentCoins = coins.getString("Coins", "0");
        textViewCoins1ee.setText(currentCoins);


        int coinCount = Integer.parseInt(coins.getString("Coins", "0"));
        float x = (float) (0.0002 * coinCount);
        if (x >= 0) {
            textViewDoller11sd.setText(new DecimalFormat("##.###").format(x)+"");
        }


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdViewq1 = findViewById(R.id.adViews1hhh);
        AdRequest adRequestl1 = new AdRequest.Builder().build();
        mAdViewq1.loadAd(adRequestl1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void startVideo(View view) {

        SharedPreferences.Editor coinsEdit = coins.edit();
        if (mTimerRunning) {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            return;
        }else {

            int coinCount1 = Integer.parseInt(coins.getString("Coins", "0"));
            coinCount1 = coinCount1 + 5;
            coinsEdit.putString("Coins", String.valueOf(coinCount1));
            coinsEdit.apply();

            startTimer();

            showRewardedVideo();
        }



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
            timer.setText("");
        }else {
            timer.setText(timeLeftFormatted);
        }

    }
    private void updateButtons() {
        if (mTimerRunning) {
            //  mButtonReset.setVisibility(View.INVISIBLE);
        } else {

            if (mTimeLeftInMillis < 1000) {
                timer.setVisibility(View.GONE);

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
        SharedPreferences prefs = getSharedPreferences("wh", MODE_PRIVATE);
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
        SharedPreferences prefs = getSharedPreferences("wh", MODE_PRIVATE);
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