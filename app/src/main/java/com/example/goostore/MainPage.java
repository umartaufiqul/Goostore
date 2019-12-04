package com.example.goostore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainPage extends AppCompatActivity {

    ImageView profileButton;
    boolean checkLogin = false;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        sp = getSharedPreferences("logged", MODE_PRIVATE);
        profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getBoolean("logged", false)){
                    Intent intent = new Intent(MainPage.this, Profile.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainPage.this, Login.class);
                    startActivity(intent);
                }
            }
        });

    }

}
