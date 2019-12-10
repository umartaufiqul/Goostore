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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class MainPage extends AppCompatActivity{

    ImageView profileButton;
    ImageView homeButton;
    ImageView myAuctionButton;
    ImageView bookButton;
    ImageView bikeButton;
    ImageView houseButton;
    ImageView homeAppliancesButton;
    ImageView electronicGoodsButton;
    ImageView otherButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    boolean checkLogin = false;
    SharedPreferences sp;
    //Add a button for see more.

    ImageView SeeMoreButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        SeeMoreButton = findViewById(R.id.book);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //sp = getSharedPreferences("logged", MODE_PRIVATE);
        profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseUser != null){
                    Intent intent = new Intent(MainPage.this, Profile.class);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(MainPage.this, Login.class);
                    startActivity(intent);
                }
            }
        });

        homeButton = findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, MainPage.class);
                startActivity(intent);
            }
        });

        myAuctionButton = findViewById(R.id.myauctionbtn);
        myAuctionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseUser != null){
                     Intent intent = new Intent(MainPage.this, myAuctionPage.class);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(MainPage.this, Login.class);
                    startActivity(intent);
                }
            }
        });

        bookButton = findViewById(R.id.book);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Goods_item.class);
                intent.putExtra("Category", "Book");
                startActivity(intent);
            }
        });

        bikeButton = findViewById(R.id.bike);
        bikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Goods_item.class);
                intent.putExtra("Category", "Bike");
                startActivity(intent);
            }
        });

        houseButton = findViewById(R.id.house);
        houseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Goods_item.class);
                intent.putExtra("Category", "House");
                startActivity(intent);
            }
        });

        homeAppliancesButton = findViewById(R.id.refrigerator);
        homeAppliancesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Goods_item.class);
                intent.putExtra("Category", "Home Appliances");
                startActivity(intent);
            }
        });

        electronicGoodsButton = findViewById(R.id.hairdryer);
        electronicGoodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Goods_item.class);
                intent.putExtra("Category", "Electronic Goods");
                startActivity(intent);
            }
        });

        otherButton = findViewById(R.id.other);
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Goods_item.class);
                intent.putExtra("Category", "Other");
                startActivity(intent);
            }
        });






    }

}
