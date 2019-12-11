package com.example.goostore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Goods_item extends AppCompatActivity implements GoodsAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private GoodsAdapter mAdapter;

    private ImageView mCategoryImage;
    private ImageView mHomeButton;
    private ImageView mProfileButton;
    private ImageView mMyAuctionButton;
    private TextView mCategoryName;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Goods> mGoods;
    private String selectedCategory = "";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_item);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_bar);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mHomeButton = findViewById(R.id.homebtn);
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Goods_item.this, MainPage.class);
                startActivity(intent);
            }
        });

        mProfileButton = findViewById(R.id.profilebtn);
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseUser != null){
                    Intent intent = new Intent(Goods_item.this, Profile.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Goods_item.this, Login.class);
                    startActivity(intent);
                }
            }
        });

        mMyAuctionButton = findViewById(R.id.myauctionbtn);
        mMyAuctionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseUser != null){
                    Intent intent = new Intent(Goods_item.this, myAuctionPage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Goods_item.this, Login.class);
                    startActivity(intent);
                }
            }
        });

        //fill the category name
        selectedCategory = getIntent().getStringExtra("Category");
        mCategoryName = findViewById(R.id.categoryName);
        mCategoryName.setText(selectedCategory);

        //fill the category image
        mCategoryImage = findViewById(R.id.imageView3);
        if(selectedCategory.equals("Book"))
            mCategoryImage.setImageDrawable(getResources().getDrawable(R.drawable.book));
        else if(selectedCategory.equals("Bike"))
            mCategoryImage.setImageDrawable(getResources().getDrawable(R.drawable.bike));
        else if(selectedCategory.equals("House"))
            mCategoryImage.setImageDrawable(getResources().getDrawable(R.drawable.house));
        else if(selectedCategory.equals("Home Appliances"))
            mCategoryImage.setImageDrawable(getResources().getDrawable(R.drawable.refrigerator));
        else if(selectedCategory.equals("Electronic Goods"))
            mCategoryImage.setImageDrawable(getResources().getDrawable(R.drawable.hairdryer));
        else if(selectedCategory.equals("Other"))
            mCategoryImage.setImageDrawable(getResources().getDrawable(R.drawable.other));

        //Find the goods for the category in database
        mGoods = new ArrayList<>();

        mAdapter = new GoodsAdapter(Goods_item.this, mGoods, selectedCategory);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(Goods_item.this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Goods");

        //Fill the goods item
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGoods.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Goods goods = postSnapshot.getValue(Goods.class);
                    goods.setKey(postSnapshot.getKey());
                    mGoods.add(goods);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Goods_item.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        ImageView addGoodBtn = findViewById(R.id.addBtn);
        addGoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Goods_item.this, Goods_Add_Delete_Page.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onItemClick(int position){
        Goods selectedGoods = mGoods.get(position);
        final String selectedKey = selectedGoods.getKey();

        //StorageReference imageRef = mStorage.getReferenceFromUrl(selectedGoods.getImageUrl());
        Intent intent = new Intent(Goods_item.this, GoodsPage.class);
        intent.putExtra("GoodsID", selectedKey);
        startActivity(intent);
    }
}
