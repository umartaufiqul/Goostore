package com.example.goostore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;

public class myAuctionPage extends AppCompatActivity implements GoodsAdapter.OnItemClickListener {

    ArrayList<Goods> mGoods;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ValueEventListener mDBListener;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_auction_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        StorageReference sRef = FirebaseStorage.getInstance().getReference();
        TextView uEmail = findViewById(R.id.UserName);
        uEmail.setText(firebaseUser.getEmail());

        StorageReference profPicLoc = sRef.child(firebaseUser.getUid()+"/profile.jpg");
        profPicLoc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                new DownloadImageTask((ImageView) findViewById(R.id.Thumbnail)).execute(uri.toString());
                //ImageView thumbnail = findViewById(R.id.Thumbnail);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycleUser);

        mGoods = new ArrayList<>();
        GoodsAdapter adapter = new GoodsAdapter(myAuctionPage.this, mGoods);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mDatabaseRef.child("Goods").orderByChild("sellerEmail").equalTo(firebaseUser.getEmail()).addValueEventListener(new ValueEventListener() {
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
                TextView sellLoad = findViewById(R.id.selling_loading);
                sellLoad.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(myAuctionPage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageView homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myAuctionPage.this, MainPage.class);
                startActivity(intent);
            }
        });

        ImageView profileBtn = findViewById(R.id.profilebtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myAuctionPage.this, Profile.class);
                startActivity(intent);
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        private DownloadImageTask(ImageView bmImage) {
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

    @Override
    public void onItemClick(int position){
        Goods selectedGoods = mGoods.get(position);
        final String selectedKey = selectedGoods.getKey();

        //StorageReference imageRef = mStorage.getReferenceFromUrl(selectedGoods.getImageUrl());
        Intent intent = new Intent(myAuctionPage.this, GoodsPage.class);
        intent.putExtra("GoodsID", selectedKey);
        startActivity(intent);
    }
}
