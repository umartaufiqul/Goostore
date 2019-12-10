package com.example.goostore;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Registration extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences spUser;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDB;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private InputStream inputStream;
    private User sample_user;
    public static final int PICK_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Get auth instance
        auth = FirebaseAuth.getInstance();

        //Add the image button
        ImageView addImage = findViewById(R.id.imageView6);
        addImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRead();
            }

        });

        //Create Account1 Button (Registration Page)
        ImageView creat_account1Button = findViewById(R.id.creat_account_btn1);
        creat_account1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp = getSharedPreferences("logged", MODE_PRIVATE);
                spUser = getSharedPreferences("uEmail", MODE_PRIVATE);

                if (sp.getBoolean("logged", false)) {
                    Intent intent = new Intent(Registration.this, Profile.class);
                    startActivity(intent);
                }
                EditText input_email = findViewById(R.id.editTextReg1);
                String email = input_email.getText().toString();

                EditText input_password = findViewById(R.id.editTextReg2);
                String password = input_password.getText().toString();


                boolean registOK = newUser();
                //Trying google way
                if (registOK) {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(Registration.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(Registration.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        user = task.getResult().getUser();
                                        storage = FirebaseStorage.getInstance();
                                        mStorageRef = storage.getReference();
                                        sample_user.setProfilePic(user.getUid()+"/profile.jpg");
                                        StorageReference profileRef = mStorageRef.child(user.getUid()+"/profile.jpg");
                                        UploadTask uploadTask = profileRef.putStream(inputStream);
                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Registration.this, "Image add failed" + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                mDB = FirebaseDatabase.getInstance().getReference().child("users");
                                                mDB.child(user.getUid()).setValue(sample_user);
                                                startActivity(new Intent(Registration.this, Profile.class));
                                                finish();
                                            }
                                        });
                                    }
                                }
                            });
                }
                else {
                    //System.out.println("Please, fill in all the fields above");
                    TextView fillAll= findViewById(R.id.textViewFillAll);
                    fillAll.setText("Please, fill in all the fields above correctly");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            if (requestCode == PICK_IMAGE) {
                try {
                    Uri imgData = data.getData();
                    Cursor cursor = getContentResolver().query(imgData, null, null, null, null);
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    TextView img = findViewById(R.id.insert_your_image);
                    cursor.moveToFirst();
                    img.setText(cursor.getString(nameIndex));
                    cursor.close();
                    //img.setText(imgData.toString());
                    inputStream = Registration.this.getContentResolver().openInputStream(imgData);
                } catch (FileNotFoundException e) {
                    return;
                }
            }
        }
    }

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            readFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFile();
            } else {
                // Permission Denied
                Toast.makeText(Registration.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void readFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public boolean newUser() {
        //DBHandler dbHandler = new DBHandler(getApplicationContext(), null, null, 1);
        boolean regist;
        EditText input_email = findViewById(R.id.editTextReg1);
        String email = input_email.getText().toString();

        EditText input_password = findViewById(R.id.editTextReg2);
        String password = input_password.getText().toString();

        EditText input_name = findViewById(R.id.editTextReg3);
        String name = input_name.getText().toString();
        EditText input_phone_number = findViewById(R.id.editTextReg4);
        String phone_number = input_phone_number.getText().toString();

        EditText input_address = findViewById(R.id.editTextReg5);
        String address = input_address.getText().toString();

        EditText input_bank_account = findViewById(R.id.editTextReg6);
        String bank_account = input_bank_account.getText().toString();

        sample_user = new User(email, password, name, phone_number, address, bank_account);
        if (email.equals("") || password.equals("") || name.equals("") || phone_number.equals("") || address.equals("") || bank_account.equals("")) {
            regist = false;
        }
        else {
            regist = true;
        }
        try {
            Integer.parseInt(phone_number);
        } catch (NumberFormatException nfe) {
            regist= false;
            Toast.makeText(Registration.this, "Incorrect format for Phone Number", Toast.LENGTH_LONG).show();
        }

        return regist;
    }

}