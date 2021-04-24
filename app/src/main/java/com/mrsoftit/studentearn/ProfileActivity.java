package com.mrsoftit.studentearn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.startapp.sdk.adsbase.StartAppAd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ProfileActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{


    private FirebaseAuth mAuth;
    DatabaseReference mRef;

    boolean userAlready = false;

    public SharedPreferences coins;

    ProgressDialog pd;

    TextView userRafeInput;
    EditText userNameInput,userEmailInput,userAddressInput;
    Button userProUpButton;
    ImageView back12211,imageUrl;


    String imageURL;

    FirebaseStorage storage;
    StorageReference storageRef;
    ImageView ppusers;
    Button image_chos,imageoooo;
    private Uri filePath;
    String imageUrlS,userAddressInputS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pd = new ProgressDialog(ProfileActivity.this);
        pd.setMessage("loading");

        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();


        userRafeInput = findViewById(R.id.userRafeInput);
        userNameInput = findViewById(R.id.userNameInput);
        userEmailInput = findViewById(R.id.userEmailInput);
        userProUpButton = findViewById(R.id.userProUpButton);
        back12211 = findViewById(R.id.back12211);
        imageUrl = findViewById(R.id.imageUrlI);
        imageoooo = findViewById(R.id.imageoooo);
        userAddressInput = findViewById(R.id.userAddressInput);

        back12211.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imageoooo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage(ProfileActivity.this);


            }
        });




        userRafeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text whatever you want" , userRafeInput.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(ProfileActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });

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


                                    coins = getSharedPreferences("Rewards", MODE_PRIVATE);
                                    imageUrlS = coins.getString("imageUrl", "https://i.ibb.co/9gdBbLK/pp.png");
                                    userAddressInputS = coins.getString("userAddressInput", "0");
                                    userRafeInput.setText(coins.getString("refe", "0"));
                                    userNameInput.setText(userModles.getName());
                                    userEmailInput.setText(userModles.getEmail());
                                    userAddressInput.setText(userModles.getUserAddressInput());

                                    if (imageUrlS.equals("0")){
                                        imageUrlS = "https://i.ibb.co/9gdBbLK/pp.png";
                                    }
                                    Picasso.get().load(imageUrlS).into(imageUrl);

                                    imageURL= imageUrlS;




                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
                                    String date = dateFormat.format(new Date());

                                    String userID = user.getUid();

                                    String fullRefeid ="";

                                    fullRefeid = userID.substring(0, 5);


                                }
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        userProUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.show();

                mRef =  database.getReference().child("Users").child(userId);
                SharedPreferences.Editor coinsEdit = coins.edit();
                coinsEdit.putString("userEmail", userEmailInput.getText().toString());
                coinsEdit.putString("Name",userNameInput.getText().toString());
                if (imageUrlS == null){
                    imageUrlS = "https://i.ibb.co/9gdBbLK/pp.png";

                }
                coinsEdit.putString("imageUrl",imageUrlS);
                coinsEdit.putString("userAddressInputS",userAddressInput.getText().toString());
                coinsEdit.apply();

                mRef.child("Email").setValue(userEmailInput.getText().toString());
                mRef.child("Name").setValue(userNameInput.getText().toString());
                mRef.child("imageURL").setValue(imageUrlS);
                mRef.child("userAddressInput").setValue(userAddressInput.getText().toString());

                pd.dismiss();

                Intent intent = new Intent(ProfileActivity.this, ChoiceSelection.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

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


                                    Picasso.get().load(downloadPhotoUrl.toString()).into(imageUrl);
                                    imageURL = downloadPhotoUrl.toString();

                                    imageUrlS = imageURL;
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