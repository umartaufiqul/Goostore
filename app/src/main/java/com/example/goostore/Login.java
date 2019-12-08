package com.example.goostore;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    ImageView loginButton;
    ImageView creat_accountButton;
    ImageView homeButton;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener firebaseASL;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Google way
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser!= null) {
            Intent intent = new Intent(Login.this, Profile.class);
            startActivity(intent);
        }
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

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Login.this, Profile.class);
                            startActivity(intent);
                        }
                        else {
                            TextView wrongPassword = findViewById(R.id.textViewWrongPassword);
                            wrongPassword.setText("Invalid email or password!");
                        }
                    }
                });
            }
        });

        //Home Button
        homeButton = findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainPage.class);
                startActivity(intent);
            }
        });

    }

}
