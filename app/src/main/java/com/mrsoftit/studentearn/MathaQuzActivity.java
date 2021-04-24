package com.mrsoftit.studentearn;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.startapp.sdk.ads.banner.Mrec;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

public class MathaQuzActivity extends AppCompatActivity {


    TextView number1, number2, coins2, textViewCoins2, counthave1,textViewDollor;
    Button submet;
    EditText sumEdite;
    ImageView back122;

    int rand_int1;
    int rand_int2;

    int sumrandom;

    private boolean connected;
    public SharedPreferences coins;
    private String currentCoins;
    private String Name;
    private String Phone;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mRef;

    private static final long START_TIME_IN_MILLIS = 180000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;
    int integer;
    float x;


    private AdView adViewm3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matha_quz);

        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        submet = findViewById(R.id.submetID);
        sumEdite = findViewById(R.id.sumEdite);
        back122 = findViewById(R.id.back122);
        textViewDollor = findViewById(R.id.textViewDollor);




        // Get the Main relative layout of the entire activity
        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.hghg);




        back122.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textViewCoins2 = findViewById(R.id.textViewCoins2);
        counthave1 = findViewById(R.id.counthave1);


        coins2 = (TextView) findViewById(R.id.textViewCoins);

        mAuth = FirebaseAuth.getInstance();

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);
        currentCoins = coins.getString("Coins", "0");
      //  coins2.setText(currentCoins+"");
        int coinCount = Integer.parseInt(coins.getString("Coins", "0"));

        textViewCoins2.setText(coinCount+"");
        numGenaret();
        int videoCount = Integer.parseInt(coins.getString("quzsCount", "0"));

        counthave1.setText(videoCount+"");

        integer = Integer.valueOf(Integer.parseInt(currentCoins));
        x = (float) (0.0002  * integer);
        if (x >= 0) {
            textViewDollor.setText(new DecimalFormat("##.###").format(x)+"");
        }



        //Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adViewm3 = findViewById(R.id.adViewm3);

        AdRequest adRequestl1 = new AdRequest.Builder().build();

        adViewm3.loadAd(adRequestl1);




        submet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mTimerRunning) {


                } else {
                    String sum = sumEdite.getText().toString();
                    if (sum.equals("")) {
                        Toast.makeText(MathaQuzActivity.this, "Place enter sum", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        int sumINT = Integer.parseInt(sum);
                        if (sumrandom == sumINT) {

                            showRewardedVideo();

                            int coinCount = Integer.parseInt(coins.getString("Coins", "0"));
                            coinCount = coinCount + 5;
                            SharedPreferences.Editor coinsEdit = coins.edit();
                            coinsEdit.putString("Coins", String.valueOf(coinCount));
                            coinsEdit.apply();
                         //   coins2.setText(String.valueOf(coinCount));


                            // startVideoMath();
                            numGenaret();
                            sumEdite.getText().clear();
                            startTimer();
                            showRewardedVideo();


                        }

                        int videoCount = Integer.parseInt(coins.getString("quzsCount", "0"));
                        int newcoitn;
                        counthave1.setText(videoCount+"");


                        if (15 >= videoCount && 0 <= videoCount) {

                            newcoitn = videoCount - 1;
                            SharedPreferences.Editor coinsEdit = coins.edit();
                            coinsEdit.putString("quzsCount", String.valueOf(newcoitn));
                            coinsEdit.apply();


                        } else {
                        }
                    }



                }


            }
        });


    }


    public void numGenaret() {
        // create instance of Random class
        Random rand = new Random();
        // Generate random integers in range 0 to 999

        rand_int1 = rand.nextInt(100);
        rand_int2 = rand.nextInt(10);
        number1.setText(rand_int1 + "");
        number2.setText(rand_int2 + "");

        sumrandom = rand_int1 + rand_int2;


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

        if (timeLeftFormatted.equals("03:00")) {
            submet.setText("SUBMIT");
        } else {
            submet.setText(timeLeftFormatted);
        }

    }

    private void updateButtons() {
        if (mTimerRunning) {
            //  mButtonReset.setVisibility(View.INVISIBLE);
            submet.setText("Pause");
        } else {

            if (mTimeLeftInMillis < 1000) {
                submet.setText("start");
            } else {
                submet.setVisibility(View.VISIBLE);
            }
            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                resetTimer();
                submet.setVisibility(View.VISIBLE);
            } else {

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("math", MODE_PRIVATE);
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
        SharedPreferences prefs = getSharedPreferences("math", MODE_PRIVATE);
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

        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
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