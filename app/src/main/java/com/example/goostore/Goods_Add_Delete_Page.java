package com.example.goostore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Goods_Add_Delete_Page extends AppCompatActivity{

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView mButtonMyAuction;
    private ImageView mButtonHome;
    private ImageView mButtonProfile;
    private ImageView mButtonChooseImage;
    private ImageView mButtonUpload;
    private EditText mEditTextBookName;
    private EditText mEditTextUserEmail;
    private EditText mEditTextBasePrice;
    private EditText mEditeTextCategory;
    private EditText mEditeTextDeadLine;
    private ImageView mImageView;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;
    private Integer GoodsSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods__add__delete__page);


        mButtonMyAuction = findViewById(R.id.myauctionbtn);
        mButtonHome = findViewById(R.id.homebtn);
        mButtonProfile = findViewById(R.id.profilebtn);
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mEditTextBookName = findViewById(R.id.edit_text_goods_name);
        mEditTextUserEmail = findViewById(R.id.edit_text_user_email);
        mEditTextBasePrice = findViewById(R.id.edit_text_base_price);
        mEditeTextCategory = findViewById(R.id.edit_text_category);
        mEditeTextDeadLine = findViewById(R.id.edit_text_deadline);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference("Goods");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Goods");

        mEditTextUserEmail.setText(firebaseUser.getEmail());

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GoodsSize = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        mButtonMyAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseUser != null){
                    Intent intent = new Intent(Goods_Add_Delete_Page.this, myAuctionPage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Goods_Add_Delete_Page.this, Login.class);
                    startActivity(intent);
                }
            }
        });

        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser != null) {
                    Intent intent = new Intent(Goods_Add_Delete_Page.this, MainPage.class);
                    startActivity(intent);
                }
            }
        });

        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseUser != null){
                    Intent intent = new Intent(Goods_Add_Delete_Page.this, Profile.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Goods_Add_Delete_Page.this, Login.class);
                    startActivity(intent);
                }
            }
        });

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String days = mEditeTextDeadLine.getText().toString().trim();
                Integer result = Integer.parseInt(days);
                if(result < 2 || result > 7) {
                    Toast.makeText(Goods_Add_Delete_Page.this, "Please enter the deadline again", Toast.LENGTH_SHORT).show();
                }else if(mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(Goods_Add_Delete_Page.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                }else{
                    uploadFile(result);
                }
            }
        });


    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData();
            mImageView = findViewById(R.id.image_view);
            try {
                Picasso.get().load(mImageUri).into(mImageView);
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Integer result){
        if(mImageUri != null){
            //给图片起名字
            StorageReference fileReference = mStorageRef.child(firebaseUser.getUid() + "/" + System.currentTimeMillis() + "/" + "image1." + getFileExtension(mImageUri));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            Calendar rightNow = Calendar.getInstance();
            rightNow.add(Calendar.DAY_OF_YEAR, result);
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                //What we will do after delay
                                @Override
                                public void run() {
                                }
                            }, 500);
                            //Upload 成功后将图片的名字和路径赋给Goods
                            Toast.makeText(Goods_Add_Delete_Page.this, "Upload Successful", Toast.LENGTH_LONG).show();
                            //Every Goods has its unique ID.
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadUrl = uriTask.getResult();
                            Goods goods = new Goods(mEditTextBookName.getText().toString().trim(), downloadUrl.toString(), mEditTextBasePrice.getText().toString().trim(),
                                    mEditeTextCategory.getText().toString().trim(), firebaseUser.getEmail(), reStr);

                            String goodsId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(goodsId).setValue(goods);

                            Map<String, Object> update = new HashMap<>();
                            DatabaseReference userGood = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).child("goods");
                            userGood.orderByKey().equalTo("count").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        update.put("count", 1);
                                        update.put("good1", goodsId);
                                        userGood.updateChildren(update);
                                    }
                                    else {
                                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                            Toast.makeText(Goods_Add_Delete_Page.this, "COUNT WORK", Toast.LENGTH_LONG).show();
                                            int count = childSnapshot.getValue(Integer.class);
                                            count++;
                                            update.put("count", count);
                                            update.put("good"+Integer.toString(count), goodsId);
                                            userGood.updateChildren(update);
                                        }
                                    }
                                    Intent intent = new Intent(Goods_Add_Delete_Page.this, MainPage.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Goods_Add_Delete_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageActivity(){
        Intent intent = new Intent(this, Goods_Add_Delete_Page.class);
        startActivity(intent);
    }

}
