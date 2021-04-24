package com.mrsoftit.studentearn;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;


    //firebase auth object
    private FirebaseAuth mAuth;
     DatabaseReference mRef;

     boolean userAlready = false;

    public SharedPreferences coins;

    ProgressDialog pd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);


        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);


        pd = new ProgressDialog(VerifyPhoneActivity.this);
        pd.setMessage("loading");


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                pd.show();
                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
     /*   PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, // first parameter is user's mobile number
                60, // second parameter is time limit for OTP
                // verification which is 60 seconds in our case.
                TimeUnit.SECONDS, // third parameter is for initializing units
                // for time period which is in seconds in our case.
                TaskExecutors.MAIN_THREAD, // this task will be excuted on Main thread.
                mCallbacks // we are calling callback method when we recieve OTP for
                // auto verification of user.
        );*/
    }



    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            checkuser();

                            //verification successful we will start the profile activity
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            Toast.makeText(VerifyPhoneActivity.this, message+"", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    public void checkuser(){
        final FirebaseDatabase database =  FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user =  mAuth.getCurrentUser();
        final String userId = user.getUid();

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

                            if (dataSnapshot.exists()){
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    userModle userModles = postSnapshot.getValue(userModle.class);
                                    userModleList.add(userModles);
                                    Random rand = new Random();
                                    // Generate random integers in range 0 to 999
                                    int  rand_int1 = rand.nextInt(100);
                                    // here you can access to name property like university.name

                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
                                    String date = dateFormat.format(new Date());

                                    String userID = user.getUid();

                                    String fullRefeid ="";

                                    fullRefeid = userID.substring(0, 5);

                                    mRef =  database.getReference().child("Users").child(userId);


                                    SharedPreferences.Editor coinsEdit = coins.edit();
                                    coinsEdit.putString("Coins", String.valueOf(userModles.getCoins()));
                                    coinsEdit.putString("userEmail", userModles.getEmail());
                                    coinsEdit.putString("Name",userModles.getName());
                                    coinsEdit.putString("phone",userModles.getPhone());
                                    coinsEdit.putString("refe",fullRefeid);
                                    coinsEdit.apply();

                                    mRef.child("Coins").setValue(userModles.getCoins());
                                    mRef.child("Email").setValue( userModles.getEmail());
                                    mRef.child("Name").setValue(userModles.getName());
                                    mRef.child("phone").setValue(userModles.getPhone());
                                    mRef.child("userID").setValue(userId);

                                    pd.dismiss();
                                    Intent intent = new Intent(VerifyPhoneActivity.this, ChoiceSelection.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   // startActivity(intent);
                                }
                            }

                        }else {
                            pd.dismiss();

                            Intent intent = new Intent(VerifyPhoneActivity.this, UserInfoAddActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }


}