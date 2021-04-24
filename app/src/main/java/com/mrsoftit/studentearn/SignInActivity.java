package com.mrsoftit.studentearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

public class SignInActivity extends AppCompatActivity {


    private EditText editTextMobile;
    CountryCodePicker ccp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextMobile = findViewById(R.id.editTextMobile);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                Toast.makeText(SignInActivity.this, ccp.getSelectedCountryCodeWithPlus()+"", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();

                String fullnumber = ccp.getSelectedCountryCodeWithPlus()+mobile;


                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(SignInActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", fullnumber);
                startActivity(intent);
            }
        });
    }
}