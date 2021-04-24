package com.mrsoftit.studentearn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;

import java.text.DecimalFormat;

public class InvitationActivity extends AppCompatActivity {

    public SharedPreferences coins;
    private String currentCoins;
    private String referCodeSting;

    ImageView back;
    TextView textViewCoins1,counthave,timert,textView22,textViewDoller11,textViewCoins1ee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        TextView ff = findViewById(R.id.refecodeMe);
        TextView reee = findViewById(R.id.reee);

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);
        referCodeSting = coins.getString("refe", "0");
        ff.setText(referCodeSting+"");


        textViewCoins1 = findViewById(R.id.textViewCoins1);
        counthave = findViewById(R.id.counthave);
        timert = findViewById(R.id.timert);
        textView22 = findViewById(R.id.textView22);
        back = findViewById(R.id.back);
        textViewDoller11 = findViewById(R.id.textViewDoller11sd);
        textViewCoins1ee = findViewById(R.id.textViewCoins1ee);

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);
        currentCoins = coins.getString("Coins", "0");
        textViewCoins1ee.setText(currentCoins);


        int coinCount = Integer.parseInt(coins.getString("Coins", "0"));
        float x = (float) (0.001 * coinCount);
        if (x >= 0) {
            textViewDoller11.setText(new DecimalFormat("##.###").format(x)+"");
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stringYouExtracted = referCodeSting+"";
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) InvitationActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", stringYouExtracted);

                clipboard.setPrimaryClip(clip);
                Toast.makeText(InvitationActivity.this, "Copy coupon code copied to clickboard!", Toast.LENGTH_SHORT).show();
            }
        });

        reee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Your Refer code: "+referCodeSting+" \n"+getResources().getString(R.string.refer1)+"\n"+getResources().getString(R.string.refer2)+"\n"+getResources().getString(R.string.refer3)+"\n"+getResources().getString(R.string.refer4)+"\n");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });
    }
}