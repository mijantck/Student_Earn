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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmailActivity extends AppCompatActivity {

    TextView eCreate;

    //defining views
    private TextView buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup,phoneButton;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    public SharedPreferences coins;
    private String currentCoins;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private DatabaseReference mRefref;
    TextView forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        eCreate= findViewById(R.id.eCreate);
        forget= findViewById(R.id.forget);


        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseDatabase database =  FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();




        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.emaillo);
        editTextPassword = (EditText) findViewById(R.id.passlo);
        buttonSignIn =  findViewById(R.id.loginTT);
        phoneButton =  findViewById(R.id.phoneButton);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmailActivity.this, EmailForgetPassActivity.class));
            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(EmailActivity.this, SignInActivity.class));
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();

            }
        });
        progressDialog = new ProgressDialog(this);

        eCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmailActivity.this,EmailRegistetionActivity.class));

            }
        });
    }

    //method for user login
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            checkuser();
                        }
                    }
                });

    }

    public void checkuser(){
        final FirebaseDatabase database =  FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user =  mAuth.getCurrentUser();
        final String userId = user.getUid();

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);


        // mRef =  database.getReference().child("Users").child(userId);
        mRef =  database.getReference().child("Users");
        DatabaseReference node = mRef;
        final List<userModle> userModleList = new ArrayList<>();
        node.orderByChild("userID").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userModleList.clear();

                        if (dataSnapshot.exists()){
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                userModle userModles = postSnapshot.getValue(userModle.class);
                                userModleList.add(userModles);

                                String userID = user.getUid();

                                String fullRefeid ="";
                                fullRefeid = userID.substring(0, 5);

                                String fullName = userModles.getName();
                                String phone = userModles.getPhone();
                                mRef =  database.getReference().child("Users").child(userId);
                                mRefref =  database.getReference().child("UsersRefe").child(phone);

                                SharedPreferences.Editor coinsEdit = coins.edit();
                                coinsEdit.putString("Coins", String.valueOf(userModles.getCoins()));
                                coinsEdit.putString("userEmail", editTextEmail.getText().toString());
                                coinsEdit.putString("Name",fullName);
                                coinsEdit.putString("phone",phone);
                                coinsEdit.putString("refe",fullRefeid);
                                coinsEdit.apply();

                                mRef.child("Coins").setValue(userModles.getCoins());
                                mRef.child("Email").setValue(editTextEmail.getText().toString());
                                mRef.child("Name").setValue(fullName);
                                mRef.child("phone").setValue(phone);
                                mRef.child("userID").setValue(userId);

                                mRefref.child("refe").setValue(fullRefeid);
                                mRefref.child("userID").setValue(userId);

                                progressDialog.dismiss();
                                finish();
                                startActivity(new Intent(EmailActivity.this,ChoiceSelection.class));

                            }
                        }else {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        progressDialog.dismiss();
                    }
                });

    }
}