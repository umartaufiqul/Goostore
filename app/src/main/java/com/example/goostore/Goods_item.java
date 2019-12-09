package com.example.goostore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Goods> mGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_item);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_bar);

        mGoods = new ArrayList<>();

        mAdapter = new GoodsAdapter(Goods_item.this, mGoods);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(Goods_item.this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Goods");

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
    }

    @Override
    public void onItemClick(int position){
        Goods selectedGoods = mGoods.get(position);
        final String selectedKey = selectedGoods.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedGoods.getImageUrl());
        Intent intent = new Intent(Goods_item.this, GoodsPage.class);
        intent.putExtra("GoodsNumber", selectedKey);
    }
}
