package com.example.goostore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoodsPage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView mButtonHome;
    private ImageView mButtonMyAuction;
    private ImageView mButtonProfile;
    private ImageView mButtonBid;
    private ImageView mGoodsImageView;
    private ImageView mSellerImage;
    private TextView mTextViewSellerName;
    private TextView mTextViewGoodsName;
    private TextView mTextViewCurrentPrice;
    private TextView mTextViewGoodsCategory;
    private TextView mTextViewDeadLine;
    private EditText mEditTextBidPrice;

    private Uri mImageUri;
    private String GoodsID = "";
    String BidPrice;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_page);

        GoodsID = getIntent().getStringExtra("GoodsID");

        mButtonMyAuction = findViewById(R.id.myauctionbtn);
        mButtonHome = findViewById(R.id.homebtn);
        mButtonProfile = findViewById(R.id.profilebtn);
        mButtonBid = findViewById(R.id.bid_button);
        mGoodsImageView = findViewById(R.id.imageView7);
        mSellerImage = findViewById(R.id.seller_image);
        mTextViewGoodsName = findViewById(R.id.GoodName);
        mTextViewCurrentPrice = findViewById(R.id.price2);
        mTextViewDeadLine = findViewById(R.id.text_view_deadline);
        mTextViewGoodsCategory = findViewById(R.id.good_category);
        mTextViewSellerName = findViewById(R.id.seller_name);
        mEditTextBidPrice = findViewById(R.id.textBidPrice);


        mStorageRef = FirebaseStorage.getInstance().getReference("Goods");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Goods");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        getGoodsDetails(GoodsID);

        mButtonMyAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseUser != null) {
                    Intent intent = new Intent(GoodsPage.this, myAuctionPage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(GoodsPage.this, Login.class);
                    startActivity(intent);
                }
            }
        });

        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoodsPage.this, MainPage.class);
                startActivity(intent);
            }
        });

        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseUser != null){
                    Intent intent = new Intent(GoodsPage.this, Profile.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(GoodsPage.this, Login.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void getGoodsDetails(String goodsID) {
        mDatabaseRef.child(goodsID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Goods goods = dataSnapshot.getValue(Goods.class);
                    mTextViewGoodsName.setText(goods.getName());
                    mTextViewCurrentPrice.setText(goods.getBasePrice());
                    mTextViewGoodsCategory.setText(goods.getCategory());
                    //mEditTextBidPrice.setText(goods.getBasePrice());
                    mTextViewDeadLine.setText(goods.getDeadLine());
                    mTextViewSellerName.setText(goods.getSellerEmail());
                    BidPrice = goods.getBasePrice();

                    Picasso.get()
                            .load(goods.getImageUrl())
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .centerCrop()
                            .into(mGoodsImageView);


                    Picasso.get().load(goods.getImageUrl()).into(mGoodsImageView);

                    mButtonBid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(firebaseUser != null && Double.parseDouble(mEditTextBidPrice.getText().toString()) > Double.parseDouble(BidPrice) && (!goods.getSellerEmail().equals(firebaseUser.getEmail()))){
                                Map<String, Object> update = new HashMap<>();
                                //Set new bid price
                                mDatabaseRef.child(goodsID).child("basePrice").setValue(mEditTextBidPrice.getText().toString());
                                update.put("bidWinner", firebaseUser.getEmail());
                                mDatabaseRef.child(goodsID).updateChildren(update);
                                mTextViewCurrentPrice.setText(mEditTextBidPrice.getText().toString());//Upload current price
                                mEditTextBidPrice.setText(mEditTextBidPrice.getText().toString());

                                update.clear();
                                String goodsId = goodsID;
                                DatabaseReference userGood = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).child("bidItems");
                                userGood.orderByKey().equalTo("count").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            update.put("count", 1);
                                            userGood.updateChildren(update);
                                            update.clear();
                                            update.put("item1", goodsId);
                                            userGood.child("bidList").updateChildren(update);
                                        } else {
                                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                int count = childSnapshot.getValue(Integer.class);
                                                count++;
                                                update.put("count", count);
                                                userGood.updateChildren(update);
                                                update.clear();
                                                update.put("item" + count, goodsId);
                                                userGood.child("bidList").updateChildren(update);
                                            }
                                        }
                                        Intent intent = new Intent(GoodsPage.this, MainPage.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else if(firebaseUser != null && Double.parseDouble(mEditTextBidPrice.getText().toString()) <= Double.parseDouble(BidPrice) && (!goods.getSellerEmail().equals(firebaseUser.getEmail()))) {
                                Toast.makeText(GoodsPage.this, "Your Price is lower than current bid.Please try again", Toast.LENGTH_SHORT).show();
                            } else if(firebaseUser != null && goods.getSellerEmail().equals(firebaseUser.getEmail())){
                                //Seller tries to modify their own goods' price
                                Toast.makeText(GoodsPage.this, "Illegal Operation", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(GoodsPage.this, Login.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
