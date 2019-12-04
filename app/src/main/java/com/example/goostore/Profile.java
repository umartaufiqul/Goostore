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
        TextView uName = (TextView) findViewById(R.id.UserName);
        TextView uPwd = (TextView) findViewById(R.id.userPassword);
        TextView uPhone = (TextView) findViewById(R.id.userPhoneNumber);
        TextView uAddress = (TextView) findViewById(R.id.userAddress);
        TextView uBankAcc = (TextView) findViewById(R.id.userBankAccount);

        DBHandler dbHandler = new DBHandler(getApplicationContext(), null, null, 1);
        User user = dbHandler.findUser(email);

        if (user != null) {
            uEmail.setText(email);
            uName.setText(user.getName());
            uPwd.setText(user.getEmail());
            uPhone.setText(user.getPhoneNumber());
            uAddress.setText(user.getAddress());
            uBankAcc.setText(user.getBankAccount());
        } else {
            uName.setText("No Match Found");
        }

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
