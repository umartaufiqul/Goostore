package com.example.goostore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class categoryPage extends AppCompatActivity {

    private ImageView mAddButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        mAddButton = findViewById(R.id.imageView4);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseUser != null) {
                    Intent intent = new Intent(categoryPage.this, Goods_Add_Delete_Page.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(categoryPage.this, Login.class);
                    startActivity(intent);
                }
            }
        });
    }
}
