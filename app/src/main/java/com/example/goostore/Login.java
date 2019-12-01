package com.example.goostore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

public class Login extends AppCompatActivity {

    ImageView loginButton;
    ImageView creat_accountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Create_Account Button
        creat_accountButton = findViewById(R.id.creat_account_btn);
        creat_accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Login.class); //here should be Registration.class*
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
                String password = input_email.getText().toString();

                User sample_user = new User("", "", "", "", "");
                boolean checkLogin = sample_user.Login(email, password);

                if(checkLogin){
                    Intent intent = new Intent(Login.this, Profile.class);
                    startActivity(intent);
                }
                else{
                    sample_user.Registration(email, password, "Rakhman", "87714607938", "Munji Hall, 434"); //shouldn't be here
                    Intent intent = new Intent(Login.this, Login.class);
                    startActivity(intent);
                }
            }
        });

    }

}
