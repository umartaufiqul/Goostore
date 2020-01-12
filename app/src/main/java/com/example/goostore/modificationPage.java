package com.example.goostore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.Distribution;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class modificationPage extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference mDB;
    InputStream inputStream;
    int changeImage = 0;

    ImageView completeButton;
    LinearLayout deleteButton;
    FloatingActionButton fab;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    public static final int PICK_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mDB = FirebaseDatabase.getInstance().getReference();

        TextView uEmail = findViewById(R.id.editEmail);
        uEmail.setText(firebaseUser.getEmail());

        ImageView editImage = findViewById(R.id.editImage);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRead();
            }
        });

        completeButton = findViewById(R.id.complete_button);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modify();
            }
        });

        deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(modificationPage.this, "Sukses", Toast.LENGTH_LONG).show();
                openDialog();
            }
        });
    }

    private void modify() {

        EditText uName = findViewById(R.id.editName);
        EditText uPwd = findViewById(R.id.editPassword);
        EditText uPhone = findViewById(R.id.editPhone);
        EditText uAddress = findViewById(R.id.editAddress);
        EditText uBankAcc = findViewById(R.id.editBankAcc);

        String name = uName.getText().toString();
        String password = uPwd.getText().toString();
        String phone = uPhone.getText().toString();
        String address = uAddress.getText().toString();
        String bankAcc = uBankAcc.getText().toString();

        Map<String, Object> update = new HashMap<>();

        if (name.equals("") && password.equals("") && phone.equals("") && address.equals("") && bankAcc.equals("") && (changeImage == 0)) {
            Toast.makeText(modificationPage.this, "Make sure to make at least one modification", Toast.LENGTH_LONG).show();
            return;
        }
        if (!name.equals("")) {
            update.put("name", name);
        }
        if (!password.equals("")) {
            update.put("password", password);
            updatePassword(password);
        }
        if (!phone.equals("")) {
            update.put("phoneNumber", phone);
        }
        if (!address.equals("")) {
            update.put("address", address);
        }
        if (!bankAcc.equals("")) {
            update.put("bankAccount", bankAcc);
        }
        if (changeImage == 1) {
            updateProfilePic();
        }
        mDB.child("users").child(firebaseUser.getUid()).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void Void) {
                Toast.makeText(modificationPage.this, "Modification success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(modificationPage.this, Profile.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(modificationPage.this, "Modification failed", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void updateProfilePic() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = storage.getReference();
        StorageReference profileRef = mStorageRef.child(firebaseUser.getUid()+"/profile.jpg");
        UploadTask uploadTask = profileRef.putStream(inputStream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(modificationPage.this, "Image add failed" + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mDB = FirebaseDatabase.getInstance().getReference().child("users");
                finish();
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
                    inputStream = modificationPage.this.getContentResolver().openInputStream(imgData);
                    changeImage = 1;
                } catch (FileNotFoundException e) {
                    return;
                }
            }
        }
    }

    private void requestRead() {
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
                Toast.makeText(modificationPage.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void readFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void updatePassword(String newPassword) {
        EditText oldPass = findViewById(R.id.editOldPassword);
        String oldpass = oldPass.getText().toString();
        if (oldpass.isEmpty()) {
            Toast.makeText(modificationPage.this, "Please insert your old password to authenticate", Toast.LENGTH_LONG).show();
            return;
        }
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), oldpass);
        firebaseUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                EditText uPwd = findViewById(R.id.editPassword);
                if (uPwd.getText().toString().isEmpty()) {
                    return;
                }
                firebaseUser.updatePassword(newPassword).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(modificationPage.this, "Password update fail", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(modificationPage.this, modificationPage.class);
                        startActivity(intent);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(modificationPage.this, "Wrong old password", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(modificationPage.this, modificationPage.class);
                startActivity(intent);
            }
        });
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(modificationPage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        LinearLayout confirm_delete = dialog.findViewById(R.id.confirm_delete);
        confirm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText confirm_password = dialog.findViewById(R.id.confirm_password);
                String conf_password = confirm_password.getText().toString();
                if (conf_password.isEmpty()) {
                    Toast.makeText(modificationPage.this, "Please enter password to confirm account deletion", Toast.LENGTH_LONG).show();
                    return;
                }
                AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), conf_password);
                firebaseUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mDB.child("users/" + firebaseUser.getUid()).removeValue();
                        firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(modificationPage.this, Login.class);
                                startActivity(intent);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(modificationPage.this, "Wrong password", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        LinearLayout cancel_delete = dialog.findViewById(R.id.cancel_delete);
        cancel_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}

