package com.mrsoftit.studentearn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class UserInfoAddActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{


    EditText userEmail,userFirstName,lastName;
    Button infoSave;
    String email,firstName,lasstName;

    public SharedPreferences coins;
    private String currentCoins;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mRef;
    private DatabaseReference mRefref;
    ProgressDialog progressDoalog;
    private Uri filePath;

    String imageURL;

    FirebaseStorage storage;
    StorageReference storageRef;
    ImageView ppusers;
    Button image_chos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_add);
        userEmail = findViewById(R.id.userEmail);
        userFirstName = findViewById(R.id.userFirstName);
        lastName = findViewById(R.id.lastName);
        infoSave = findViewById(R.id.infoSave);
        image_chos = findViewById(R.id.image_chos);

        progressDoalog = new ProgressDialog(UserInfoAddActivity.this);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();

        coins = getSharedPreferences("Rewards", MODE_PRIVATE);

        final FirebaseDatabase database =  FirebaseDatabase.getInstance("https://studentearn-e18b5-default-rtdb.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user =  mAuth.getCurrentUser();
        final String userId = user.getUid();


        ppusers = findViewById(R.id.ppusers);

        permition();

        image_chos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage(UserInfoAddActivity.this);
            }
        });
        infoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDoalog.show();
                email = userEmail.getText().toString();
                firstName = userFirstName.getText().toString();
                lasstName = lastName.getText().toString();

                if (email.equals("")&&firstName.equals("")&&lasstName.equals("")){
                    progressDoalog.dismiss();
                    Toast.makeText(UserInfoAddActivity.this, "Input all info", Toast.LENGTH_SHORT).show();
                    return;
                }

                checkuser();

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


                                String fullName = firstName+" "+lasstName;
                                String phone = user.getPhoneNumber();
                                mRef =  database.getReference().child("Users").child(userId);
                                mRefref =  database.getReference().child("UsersRefe").child(phone);

                                int coinCount = Integer.parseInt(coins.getString("Coins", "0"));
                                SharedPreferences.Editor coinsEdit = coins.edit();
                                coinsEdit.putString("Coins", String.valueOf(userModles.getCoins()));
                                coinsEdit.putString("userEmail", email);
                                coinsEdit.putString("Name",fullName);
                                coinsEdit.putString("phone",phone);
                                coinsEdit.putString("refe",fullRefeid);
                                coinsEdit.putString("imageUrl",imageURL);
                                coinsEdit.apply();

                                mRef.child("Coins").setValue(userModles.getCoins());
                                mRef.child("Email").setValue(email);
                                mRef.child("Name").setValue(fullName);
                                mRef.child("phone").setValue(phone);
                                mRef.child("userID").setValue(userId);
                                mRef.child("imageUrl").setValue(imageURL);


                                mRefref.child("refe").setValue(fullRefeid);
                                mRefref.child("userID").setValue(userId);

                                progressDoalog.dismiss();

                                startActivity(new Intent(UserInfoAddActivity.this,ReferActivity.class));
                                finish();
                                progressDoalog.dismiss();
                            }
                        }else {

                            String fullName = firstName+" "+lasstName;
                            String phone = user.getPhoneNumber();
                            mRef =  database.getReference().child("Users").child(userId);

                            int coinCount = Integer.parseInt(coins.getString("Coins", "0"));

                            SharedPreferences.Editor coinsEdit = coins.edit();
                            coinsEdit.putString("Coins", String.valueOf("0"));
                            coinsEdit.putString("userEmail", email);
                            coinsEdit.putString("Name",fullName);
                            coinsEdit.putString("phone",phone);
                            coinsEdit.putString("imageUrl",imageURL);
                            coinsEdit.apply();

                            mRef.child("Coins").setValue("0");
                            mRef.child("Email").setValue(email);
                            mRef.child("Name").setValue(fullName);
                            mRef.child("phone").setValue(phone);
                            mRef.child("userID").setValue(userId);
                            mRef.child("imageUrl").setValue(imageURL);
                            progressDoalog.dismiss();

                            startActivity(new Intent(UserInfoAddActivity.this,ChoiceSelection.class));
                            finish();

                            progressDoalog.dismiss();

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        progressDoalog.dismiss();
                    }
                });


    }



    //image choos

    private void selectImage(Context context) {
        final CharSequence[] options = {"Select Image", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Select Image")) {

                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        filePath = data.getData();
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        //   profile_image.setImageBitmap(selectedImage);
                        uploadImage();

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            filePath = data.getData();
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                //   profile_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                uploadImage();
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }


    //upload in fireStore


    private void uploadImage() {

        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("loading...");
            progressDialog.show();

            StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Download file From Firebase Storage
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadPhotoUrl) {


                                    Picasso.get().load(downloadPhotoUrl.toString())
                                            .into(ppusers);
                                    imageURL = downloadPhotoUrl.toString();
                                    progressDialog.dismiss();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploading "+(int)progress+"%");
                        }
                    });
        }
    }

    //// permition esey permition

    @AfterPermissionGranted(123)
    private void permition() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_PHONE_STATE,
        };

        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "",
                    123, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
       /* if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }*/
    }


}