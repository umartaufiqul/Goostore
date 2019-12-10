package com.example.goostore;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class MainPage extends AppCompatActivity{

    ImageView profileButton;
    boolean checkLogin = false;
    SharedPreferences sp;
    //Add a button for see more.
    ImageView SeeMoreButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        SeeMoreButton = findViewById(R.id.book);
        //sp = getSharedPreferences("logged", MODE_PRIVATE);
        profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(sp.getBoolean("logged", false)){
                    Intent intent = new Intent(MainPage.this, Profile.class);
                    startActivity(intent);
                }
                else{*/
                Intent intent = new Intent(MainPage.this, Login.class);
                   startActivity(intent);
                //}
            }
        });

        SeeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, categoryPage.class);
                startActivity(intent);
            }
        });

    }

}
