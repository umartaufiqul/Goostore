package com.example.goostore;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class Profile extends AppCompatActivity {

    ImageView homeButton;
    ImageView logoutButton;
    ImageView modificationButton;

   // SharedPreferences spUser;
    //SharedPreferences spLogin;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Google way
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mDB = FirebaseDatabase.getInstance().getReference();

        TextView uEmail = findViewById(R.id.UserEmail);
        TextView uName = findViewById(R.id.UserName);
        TextView uPwd = findViewById(R.id.userPassword);
        TextView uPhone = findViewById(R.id.userPhoneNumber);
        TextView uAddress = findViewById(R.id.userAddress);
        TextView uBankAcc = findViewById(R.id.userBankAccount);

        StorageReference sRef = FirebaseStorage.getInstance().getReference();

        uEmail.setText(firebaseUser.getEmail());
        mDB.child("users").orderByChild("email").equalTo(firebaseUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    User mUser = childSnapshot.getValue(User.class);
                    uName.setText(mUser.getName());
                    uPwd.setText(mUser.getPassword());
                    uPhone.setText(mUser.getPhoneNumber());
                    uAddress.setText(mUser.getAddress());
                    uBankAcc.setText(mUser.getBankAccount());

                    StorageReference profPicLoc = sRef.child(firebaseUser.getUid()+"/profile.jpg");
                    profPicLoc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            new DownloadImageTask((ImageView) findViewById(R.id.Thumbnail)).execute(uri.toString());
                            //ImageView thumbnail = findViewById(R.id.Thumbnail);
                            //thumbnail.content
                        }
                    });
                    //Glide.with
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Home Button
        homeButton = findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainPage.class);
                startActivity(intent);
            }
        });

        //Logout Button
        logoutButton = findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, MainPage.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
            }
        });


        //Modification Button
        modificationButton = findViewById(R.id.modifyBtn);
        modificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, modificationPage.class);
                startActivity(intent);
            }
        });

        ImageView auctionBtn = findViewById(R.id.myauctionbtn);
        auctionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, myAuctionPage.class);
                startActivity(intent);
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(Bitmap.createScaledBitmap(result, 200, 200, false));
        }
    }


}
