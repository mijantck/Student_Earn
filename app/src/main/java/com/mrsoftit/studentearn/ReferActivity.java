package com.mrsoftit.studentearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.ArrayList;
import java.util.List;

public class ReferActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mRefref;
    private DatabaseReference mRef;

    EditText referCodeEditeText;
    String  referCodeEditeTextSting;
    String coint,referUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);


        referCodeEditeText = findViewById(R.id.referCodeEditeText);

        // and show interstitial ad
        StartAppAd.showAd(this);


    }

    public void checkRefer(View view) {
        referCodeEditeTextSting =  referCodeEditeText.getText().toString();

        referCheck();

    }


    public void referCheck(){

        final FirebaseDatabase database =  FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user =  mAuth.getCurrentUser();
        final String userId = user.getUid();

        mRefref =  database.getReference().child("UsersRefe");

        DatabaseReference node = mRefref;
        final List<userModle> userModleList = new ArrayList<>();

        node.orderByChild("refe").equalTo(referCodeEditeTextSting)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                refeModle userModles = postSnapshot.getValue(refeModle.class);

                                referUser = userModles.getUserID();

                                mRef =  database.getReference().child("Users");
                                DatabaseReference node = mRef;
                                final List<userModle> userModleList = new ArrayList<>();
                                node.orderByChild("userID").equalTo(userModles.getUserID())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                        userModle userModles = postSnapshot.getValue(userModle.class);

                                                        coint = userModles.getCoins();
                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                           }

                            if (coint!=null && referUser != null ){

                                int coinCount = Integer.parseInt(coint);

                                if (coinCount>1000){
                                    coinCount = coinCount + 200;
                                }

                                mRef =  database.getReference().child("Users").child(referUser);
                                mRef.child("Coins").setValue(coinCount+"");

                                mRef =  database.getReference().child("Users").child(user.getUid());
                                mRef.child("Coins").setValue("200");

                                Intent intent = new Intent(ReferActivity.this,ChoiceSelection.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }


                        }





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

    }

    public void sckep(View view) {
        startActivity(new Intent(ReferActivity.this,ChoiceSelection.class));
        finish();
    }
}