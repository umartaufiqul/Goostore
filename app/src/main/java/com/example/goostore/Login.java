package com.example.goostore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    ImageView loginButton;
    ImageView creat_accountButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Create_Account Button
        creat_accountButton = findViewById(R.id.creat_account_btn);
        creat_accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

        //Login Button
        loginButton = findViewById(R.id.loginbtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText input_email = findViewById(R.id.editText);
                String email = input_email.getText().toString();

                EditText input_password = findViewById(R.id.editText3);
                String password = input_password.getText().toString();

                User sample_user = new User("", "", "", "", "", "");
                boolean LoginOK = sample_user.Login(email, password);

                if(LoginOK){ //check if account details are right
                    //checkLogin = true;
                    Intent intent = new Intent(Login.this, Profile.class);
                    intent.putExtra("user", email);
                    startActivity(intent);
                }
                else{ //if account details are wrong
                    TextView wrongPassword= findViewById(R.id.textViewWrongPassword);
                    wrongPassword.setText("Invalid email or password");
                }
            }
        });

    }

}
