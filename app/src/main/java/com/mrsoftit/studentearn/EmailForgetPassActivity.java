package com.mrsoftit.studentearn;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EmailForgetPassActivity extends AppCompatActivity {

    EditText mEditEmail;
    TextView resetPass;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private ProgressDialog progressDialog;
    //defining firebaseauth object


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_forget_pass);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mEditEmail=  findViewById(R.id.emailree);
        resetPass=  findViewById(R.id.resetPass);

        progressDialog = new ProgressDialog(this);



        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEditEmail.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(EmailForgetPassActivity.this, "Enter Email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                mFirebaseAuth.sendPasswordResetEmail(mEditEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(EmailForgetPassActivity.this, "An email has been sent to you.", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(EmailForgetPassActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}