package com.mrsoftit.studentearn;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChoiceSelection extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView coins2;
    private boolean connected;
    public SharedPreferences coins;
    private String currentCoins;
    private String Name;
    private String referCodeSting;
    private TextView referCode;
    private String Phone;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mRef;
    private DatabaseReference mRefoption;

    private DrawerLayout drawer;
    TextView userNameTexViwe, phoneNumberView, textViewDualer;

    AlertDialog.Builder builder;

    private static final long START_TIME_IN_MILLIS = 180000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    String timeLeftFormatted;
    private static final String ONESIGNAL_APP_ID = "224f3b6e-5258-43c6-8ac7-cd8d5c325df2";

    int integer;
    float x;


    List<OptiosModel> option;

    private AdView mAdViewc1,mAdViewc2,mAdViewc3;

    private CountDownTimer mCountDownTimerm;
    private long mTimeLeftInMillism = 40000;

    CircleImageView profile_image222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_selection);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        StartAppAd.disableSplash();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();


        coins2 = (TextView) findViewById(R.id.textViewCoins);


        drawer = findViewById(R.id.drawer_layout);
        userNameTexViwe = findViewById(R.id.userNameTexViwe);
        phoneNumberView = findViewById(R.id.phoneNumberView);
        referCode = findViewById(R.id.referCode);
        profile_image222 = findViewById(R.id.profile_image222);

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.Spinwell140));

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        final Handler handler = new Handler();
        //  mAuth = FirebaseAuth.getInstance();

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);
        currentCoins = coins.getString("Coins", "0");
        Name = coins.getString("Name", "0");
        referCodeSting = coins.getString("refe", "0");
        Phone = coins.getString("userEmail", "0");

        mRef = database.getReference().child("Users").child(userId);
        mRef.child("Coins").setValue(currentCoins);

        String pDate = coins.getString("date", "0");

        coins2 = (TextView) findViewById(R.id.textViewCoins);
        textViewDualer = (TextView) findViewById(R.id.textViewDualer);
        coins2.setText(currentCoins);
        phoneNumberView.setText(Phone);
        userNameTexViwe.setText(Name);

        integer = Integer.valueOf(Integer.parseInt(currentCoins));
        x = (float) (0.0002 * integer);
        if (x >= 0) {
            textViewDualer.setText(new DecimalFormat("##.###").format(x)+"");
        }

        String imageUrlS = coins.getString("imageUrl", "https://i.ibb.co/9gdBbLK/pp.png");

        if (imageUrlS.equals("0")){
            imageUrlS = "https://i.ibb.co/9gdBbLK/pp.png";

            Toast.makeText(this, imageUrlS+"", Toast.LENGTH_SHORT).show();

        }
        Picasso.get().load(imageUrlS).into(profile_image222);

        View heder = navigationView.getHeaderView(0);
        ImageView pNav = heder.findViewById(R.id.pNav);
        TextView navUser = heder.findViewById(R.id.navUser);
        TextView navEmail = heder.findViewById(R.id.navEmail);

        Picasso.get().load(imageUrlS).into(pNav);
        navEmail.setText(Phone);
        navUser.setText(Name);


        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);


        //Banner
        MobileAds.initialize(this, initializationStatus -> {
        });

        mAdViewc1 = findViewById(R.id.adViewc1);
        mAdViewc2 = findViewById(R.id.adViewc2);
        mAdViewc3 = findViewById(R.id.adViewc3);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        AdRequest adRequest2 = new AdRequest.Builder().build();
        AdRequest adRequest3 = new AdRequest.Builder().build();
        mAdViewc1.loadAd(adRequest1);
        mAdViewc2.loadAd(adRequest2);
        mAdViewc3.loadAd(adRequest3);



        final Handler handler1 = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                int coinCount = Integer.parseInt(coins.getString("Coins", "0"));
                coins2.setText(String.valueOf(coinCount));
                Log.d("Handlers", "Called on main thread");
                handler1.postDelayed(this, 2000);
            }
        };
        handler.post(runnableCode);


        mRefoption = database.getReference().child("options");
        DatabaseReference node = mRefoption;

        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    option = new ArrayList<>();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        OptiosModel optiosModel = postSnapshot.getValue(OptiosModel.class);
                        option.add(new OptiosModel(optiosModel.getOption()));
                        saveData();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

      /*  mCountDownTimerm = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillism = millisUntilFinished;

            }

            @Override
            public void onFinish() {
                showRewardedVideo();
            }
        }.start();*/

    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Rewards", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(option);
        editor.putString("test", json);
        editor.apply();


    }

    public void startVideo(View view) {
        if (!checkVPN()) {
            //Setting message manually and performing action on button click
            builder.setMessage("Place connect VPN")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {

                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();
            return;
        }

        startActivity(new Intent(ChoiceSelection.this,VideoWatchActivity.class));

    }

    public void instruction(View view) {
        if (!checkVPN()) {
            //Setting message manually and performing action on button click
            builder.setMessage("Place connect VPN")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();
            return;
        }

        Intent openInstructions = new Intent(getApplicationContext(), Instructions.class);
        startActivity(openInstructions);
    }

    public void scratch(View view) {
        if (!checkVPN()) {
            //Setting message manually and performing action on button click
            builder.setMessage("Place connect VPN")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();
            return;
        }
        Intent openInstructions = new Intent(getApplicationContext(), ScratchActivity.class);
        startActivity(openInstructions);
    }

    public void mathq(View view) {

        if (!checkVPN()) {
            //Setting message manually and performing action on button click
            builder.setMessage("Place connect VPN")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();
            return;
        }
        Intent openRedeem = new Intent(getApplicationContext(), MathaQuzActivity.class);
        startActivity(openRedeem);
    }

    public void dailyCheck(View view) {

        if (!checkVPN()) {
            //Setting message manually and performing action on button click
            builder.setMessage("Place connect VPN")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();
            return;
        }
        Intent openDailyChecks = new Intent(getApplicationContext(), DailyCheckins.class);
        startActivity(openDailyChecks);
    }

    public void recapchaCheck(View view) {

        if (!checkVPN()) {
            //Setting message manually and performing action on button click
            builder.setMessage("Place connect VPN")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();
            return;
        }

    }

    public void luckyWheell(View view) {

        if (!checkVPN()) {
            //Setting message manually and performing action on button click
            builder.setMessage("Place connect VPN")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();
            return;
        }
        Intent openLuckyWheel = new Intent(getApplicationContext(), LuckyWheel.class);
        startActivity(openLuckyWheel);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(ChoiceSelection.this, ChoiceSelection.class));

                break;
            case R.id.userInfoUp:

                if (!checkVPN()) {
                    //Setting message manually and performing action on button click
                    builder.setMessage("Place connect VPN")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();

                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.show();
                } else {
                    startActivity(new Intent(ChoiceSelection.this, ProfileActivity.class));
                }
                break;
            case R.id.redeem:
                if (!checkVPN()) {
                    //Setting message manually and performing action on button click
                    builder.setMessage("Place connect VPN")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.show();
                } else {
                    startActivity(new Intent(ChoiceSelection.this, RedeemPayTm.class));
                }

                break;
            case R.id.refermenu:
                startActivity(new Intent(ChoiceSelection.this,InvitationActivity.class));
                break;
            case R.id.youtube:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC0b3qW2GhVpk0DPFO4mut5g"));
                startActivity(intent);
                break;
            case R.id.like:
                Intent likeq = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Student-Earning-109219161304379"));
                startActivity(likeq);
                break;

                case R.id.sheare:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Your Refer code: "+referCodeSting+" \n"+getResources().getString(R.string.refer1)+"\n"+getResources().getString(R.string.refer2)+"\n"+getResources().getString(R.string.refer3)+"\n"+getResources().getString(R.string.refer4)+"\n");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                clearAppData();
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(ChoiceSelection.this, ChoiceSelection.class));
                    break;
                case R.id.redeem:

                    if (!checkVPN()) {
                        //Setting message manually and performing action on button click
                        builder.setMessage("Place connect VPN")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.show();
                    } else {
                        startActivity(new Intent(ChoiceSelection.this, RedeemPayTm.class));
                    }
                    break;
                case R.id.how_to_work:
                    startActivity(new Intent(ChoiceSelection.this, Instructions.class));
                    break;

            }

            return true;
        }
    };

    public boolean checkVPN() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting();
    }


    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear " + packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        if (timeLeftFormatted.equals("01:00")) {

        } else {


        }

    }

    private void updateButtons() {
        if (mTimerRunning) {
            //  mButtonReset.setVisibility(View.INVISIBLE);
            //  play.setText("Pause");
        } else {

            if (mTimeLeftInMillis < 1000) {
                //  play.setText("start");
            } else {
                //   play.setVisibility(View.VISIBLE);
            }
            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                resetTimer();
                //  play.setVisibility(View.VISIBLE);
            } else {

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("videosADS", MODE_PRIVATE);
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
        SharedPreferences prefs = getSharedPreferences("videosADS", MODE_PRIVATE);
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

    public void teligram(View view) {

       // startActivity(new Intent(ChoiceSelection.this, LeaderActivity.class));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Amazon-Easy-Cash-100492335476524"));
        startActivity(intent);
        StartAppAd.showAd(this);
    }

    public void redeem(View view) {

        startActivity(new Intent(ChoiceSelection.this, RedeemPayTm.class));
        StartAppAd.showAd(this);
    }

    public void invit(View view) {

        startActivity(new Intent(ChoiceSelection.this, InvitationActivity.class));
        StartAppAd.showAd(this);
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

    public void quizes(View view) {
        startActivity(new Intent(ChoiceSelection.this, QizeActivity.class));
        StartAppAd.showAd(this);
    }

    public void ledarBoard(View view) {
         startActivity(new Intent(ChoiceSelection.this, LeaderActivity.class));
        StartAppAd.showAd(this);
    }
}
