package com.example.goostore;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Profile extends AppCompatActivity {

    ImageView homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Get the user data from database
        String email = getIntent().getStringExtra("user");

        TextView uEmail = (TextView) findViewById(R.id.UserEmail);
        uEmail.setText(email);

        TextView uName = (TextView) findViewById(R.id.UserName);
        uName.setText(DataBase.Users.get(email).get("Name"));

        TextView uPwd = (TextView) findViewById(R.id.userPassword);
        uPwd.setText(DataBase.Users.get(email).get("Password"));

        TextView uPhone = (TextView) findViewById(R.id.userPhoneNumber);
        uPhone.setText(DataBase.Users.get(email).get("PhoneNumber"));

        TextView uAddress = (TextView) findViewById(R.id.userAddress);
        uAddress.setText(DataBase.Users.get(email).get("Address"));

        TextView uBankAcc = (TextView) findViewById(R.id.userBankAccount);
        uBankAcc.setText(DataBase.Users.get(email).get("BankAccount"));


        //Home Button
        homeButton = findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainPage.class);
                startActivity(intent);
            }
        });

    }
}
