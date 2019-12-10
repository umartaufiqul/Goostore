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
    ImageView myAuctionBtn;
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
                Intent intent = new Intent(MainPage.this, Login.class);
                   startActivity(intent);
            }
        });

        SeeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, categoryPage.class);
                startActivity(intent);
            }
        });

        myAuctionBtn = findViewById(R.id.myauctionbtn);
        myAuctionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, myAuctionPage.class);
                startActivity(intent);
            }
        });

    }

}
