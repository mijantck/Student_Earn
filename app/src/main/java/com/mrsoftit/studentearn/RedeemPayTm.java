package com.mrsoftit.studentearn;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.startapp.sdk.adsbase.StartAppAd;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RedeemPayTm extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView mobileno;
    private SharedPreferences coins,money;
    private String currentCoins,currentMoney;
    private String email;
    private EditText editText,howMuchsend;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef,mRefStatus;
    private float usermoney;
    private int usermoneyCoins,usercoins,userCoinuse,uhavecoin;

    private TextView coins2;
    String item;
    private int usercoin;
    private float x,moneysend,howMuch;
    private Integer integer;
    TextView userDoller,textViewCoins233;
    ImageView back13;
    AlertDialog.Builder builder;
    Button button;



    List<OptiosModel> option ;
    final List<String> categories = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_pay_tm);
        firebaseAuth = FirebaseAuth.getInstance();

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);
        currentCoins = coins.getString("Coins", "0");

        builder = new AlertDialog.Builder(this);



        button = findViewById(R.id.button7);
        userDoller = findViewById(R.id.userDoller);
        textViewCoins233 = findViewById(R.id.textViewCoins233);
        howMuchsend = findViewById(R.id.howMuchsend);
        editText = findViewById(R.id.payTmmobile);
        back13 = findViewById(R.id.back13);

        back13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textViewCoins233.setText(currentCoins);

        // and show interstitial ad
        StartAppAd.showAd(this);

        integer = Integer.valueOf(Integer.parseInt(currentCoins));
        x = (float) (0.0002 * integer);
        if (x >=0 ){
            userDoller.setText("It is " + x + "USD");
        }


        loadData();

        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.imageView13);




        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  String hm = howMuchsend.getText().toString();
                  String ac = editText.getText().toString();


                    if (ac.isEmpty()){
                        Toast.makeText(RedeemPayTm.this, "Enter Account number ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                  if (hm.isEmpty()){
                      Toast.makeText(RedeemPayTm.this, "Enter Number", Toast.LENGTH_SHORT).show();
                      return;
                  }

                  if(integer < 1000){
                      Toast.makeText(RedeemPayTm.this, "Not available balance", Toast.LENGTH_SHORT).show();
                      return;
                  }
                  if (x <0.13){
                      Toast.makeText(RedeemPayTm.this, "Minimum paypment Request is 0.13$", Toast.LENGTH_SHORT).show();

                      return;
                  }


                    howMuch = Float.parseFloat(hm);
                    uhavecoin = (int) (howMuch / 0.0002f);
                    userCoinuse = Math.round(uhavecoin);

                    if (x <howMuch ){
                        Toast.makeText(RedeemPayTm.this, "Not available balance", Toast.LENGTH_SHORT).show();
                        return;
                    }


                  if (x<5 && x>= 0.13){
                      if (!item.equals("Mobile recherche")) {
                          //Setting message manually and performing action on button click
                          builder.setMessage("Please select mobile recherche ")
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
                      send();
                  }else if(x >=5 ){
                      send();
                  }
                }
            });

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
       item = parent.getItemAtPosition(position).toString();


        if (item.equals("Mobile recherche")){
            button.setText("recherche request ");

        }else {
            button.setText("withdraw");
        }



    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private void send(){

        final ProgressDialog dialog = new ProgressDialog(RedeemPayTm.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();


        email = editText.getText().toString();
        currentCoins = coins.getString("Coins", "0");
        int currentCoinsINT = Integer.valueOf(Integer.parseInt(currentCoins));

        int result = currentCoinsINT - userCoinuse;

        FirebaseDatabase database =  FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 =  mAuth.getCurrentUser();
        String userId = user1.getUid();

        mRef =  database.getReference().child("Users").child(userId);

        mRef.child("Coins").setValue(result+"").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


            }
        });


        mRefStatus =  database.getReference().child("Redeem").push();
        mRefStatus.child("Status").setValue("Review");
        mRefStatus.child("Email").setValue(email);
        mRefStatus.child("account").setValue(email);
        mRefStatus.child("MoneyUSD").setValue(String.valueOf(howMuch));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/").getReference().child("Users").child(userId).child("Redeem").push();
        Map<String, Object> map = new HashMap<>();
        map.put("id", databaseReference.getKey());
        map.put("email", email);
        map.put("account", email);
        map.put("Redeem", howMuch);
        Calendar c = Calendar.getInstance();

        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = day + ". " + month + ". " + year;
        map.put("Date", date);
        databaseReference.setValue(map);

        SharedPreferences.Editor coinsEdit = coins.edit();
        coinsEdit.putString("Coins", String.valueOf(result));
        coinsEdit.apply();

        integer = Integer.valueOf(Integer.parseInt(currentCoins));
        x = (float) (0.0002 * integer);

        if (x >=0 ){
            userDoller.setText("It is " + x + "USD");
        }

        textViewCoins233.setText(currentCoins);

        startActivity(new Intent(RedeemPayTm.this,ChoiceSelection.class));
        finish();

        dialog.dismiss();

      //  Toast.makeText(RedeemPayTm.this, result+"", Toast.LENGTH_SHORT).show();

    }

    private void sendMessage() {
        final ProgressDialog dialog = new ProgressDialog(RedeemPayTm.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    email = editText.getText().toString();
                    dialog.dismiss();

                    currentCoins = coins.getString("Coins", "0");
                   int currentCoinsINT = Integer.valueOf(Integer.parseInt(currentCoins));

                    int result = currentCoinsINT - userCoinuse;

                    mRef.child("Coins").setValue(result+"").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                        }
                    });

                    FirebaseDatabase database =  FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/");
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user1 =  mAuth.getCurrentUser();
                    String userId = user1.getUid();
                    mRefStatus =  database.getReference().child("Redeem").push();
                    mRefStatus.child("Status").setValue("Review");
                    mRefStatus.child("Email").setValue(email);
                    mRefStatus.child("account").setValue(email);
                    mRefStatus.child("MoneyUSD").setValue(String.valueOf(howMuch));

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/").getReference().child("Users").child(userId).child("Redeem").push();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", databaseReference.getKey());
                    map.put("email", email);
                    map.put("account", email);
                    map.put("Redeem", howMuch);
                    Calendar c = Calendar.getInstance();

                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year = c.get(Calendar.YEAR);
                    String date = day + ". " + month + ". " + year;
                    map.put("Date", date);
                    databaseReference.setValue(map);

                    SharedPreferences.Editor coinsEdit = coins.edit();
                    coinsEdit.putString("Coins", String.valueOf(result));
                    coinsEdit.apply();

                    integer = Integer.valueOf(Integer.parseInt(currentCoins));
                    x = (float) (0.0002 * integer);

                    if (x >=0 ){
                        userDoller.setText("It is " + x + "USD");
                    }

                    Toast.makeText(RedeemPayTm.this, result+"", Toast.LENGTH_SHORT).show();

                  /*  Intent intent = new Intent(RedeemPayTm.this, ChoiceSelection.class);
                    startActivity(intent);
                    finish();*/

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
            }
}, 2500);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    private void loadData() {
        int count = 0;
        SharedPreferences sharedPreferences =  getSharedPreferences("Rewards", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("test", null);
        Type type = new TypeToken<ArrayList<OptiosModel>>() {}.getType();
        option = gson.fromJson(json, type);
        if (option == null) {
            option = new ArrayList<>();



        }else {
            for (OptiosModel number: option) {

                categories.add(number.getOption());

            }
        }
    }


    public void payTm(View view) {

    }
}
