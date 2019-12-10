package com.example.goostore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class myAuctionPage extends AppCompatActivity {

    ArrayList<Goods> mGoods;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_auction_page);

        RecyclerView recyclerView = findViewById(R.id.recycleUser);

        mGoods = new ArrayList<>();
        GoodsAdapter adapter = new GoodsAdapter(myAuctionPage.this, mGoods);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Goods");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGoods.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Goods goods;
                    goods = postSnapshot.getValue(Goods.class);
                    goods.setKey(postSnapshot.getKey());
                    mGoods.add(goods);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(myAuctionPage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
