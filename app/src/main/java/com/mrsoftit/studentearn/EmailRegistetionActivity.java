package com.mrsoftit.studentearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailRegistetionActivity extends AppCompatActivity {


    //defining view objects
    private EditText userE,userEM,userPas,userPasRR,reff,userpho;
    private TextView signupT;
    private ProgressDialog progressDialog;
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    public SharedPreferences coins;



    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private DatabaseReference mRefref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_registetion);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);


        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }


        //initializing views
        userE = (EditText) findViewById(R.id.userE);
        userEM = (EditText) findViewById(R.id.userEM);
        userPas = (EditText) findViewById(R.id.userPas);
        userPasRR = (EditText) findViewById(R.id.userPasRR);
        userpho = (EditText) findViewById(R.id.userpho);
        reff = (EditText) findViewById(R.id.reff);

        signupT = (TextView) findViewById(R.id.signupT);

        progressDialog = new ProgressDialog(this);

        signupT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });

    }

    private void registerUser(){

       mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database =  FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/");


        //getting email and password from edit texts
        final String email = userEM.getText().toString().trim();
        String password  = userPas.getText().toString().trim();
        String userPasRR1  = userPasRR.getText().toString().trim();
        final String userE1  = userE.getText().toString().trim();
        final String userpho1  = userpho.getText().toString().trim();

        if (!password.equals(userPasRR1)){
            Toast.makeText(this,"Password not match",Toast.LENGTH_LONG).show();
        }
        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(userE1)){
            Toast.makeText(this,"Please enter User Name",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){

                            final FirebaseUser user =  mAuth.getCurrentUser();
                            final String userId = user.getUid();

                            String userID = user.getUid();

                            String fullRefeid ="";
                            fullRefeid = userID.substring(0, 5);

                            String fullName = userE1;
                            String phone =userpho1;
                            mRef =  database.getReference().child("Users").child(userId);
                            mRefref =  database.getReference().child("UsersRefe").child(phone);


                            SharedPreferences.Editor coinsEdit = coins.edit();
                            coinsEdit.putString("Coins", String.valueOf("0"));
                            coinsEdit.putString("userEmail", email);
                            coinsEdit.putString("Name",fullName);
                            coinsEdit.putString("phone",phone);
                            coinsEdit.putString("refe",fullRefeid);
                            coinsEdit.apply();

                            mRef.child("Coins").setValue("0");
                            mRef.child("Email").setValue(email);
                            mRef.child("Name").setValue(fullName);
                            mRef.child("phone").setValue(phone);
                            mRef.child("userID").setValue(userId);

                            mRefref.child("refe").setValue(fullRefeid);
                            mRefref.child("userID").setValue(userId);

                            Toast.makeText(EmailRegistetionActivity.this, mRef+"", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(EmailRegistetionActivity.this,ChoiceSelection.class));
                            finish();


                        }else{
                            //display some message here
                            Toast.makeText(EmailRegistetionActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

}