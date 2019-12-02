package com.example.goostore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Registration extends AppCompatActivity {
    ImageView creat_account1Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Create Account1 Button (Registration Page)
        creat_account1Button = findViewById(R.id.creat_account_btn1);
        creat_account1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText input_email = findViewById(R.id.editTextReg1);
                String email = input_email.getText().toString();

                EditText input_password = findViewById(R.id.editTextReg2);
                String password = input_password.getText().toString();

                EditText input_name = findViewById(R.id.editTextReg3);
                String name = input_name.getText().toString();

                EditText input_phone_number = findViewById(R.id.editTextReg4);
                String phone_number = input_phone_number.getText().toString();

                EditText input_address = findViewById(R.id.editTextReg5);
                String address = input_address.getText().toString();

                User sample_user = new User("", "", "", "", "");
                boolean register_properly = sample_user.Registration(email, password, name, phone_number, address);

                if (register_properly) { //if all fields are non-empty
                    //checkLogin = true;
                    Intent intent = new Intent(Registration.this, Profile.class);
                    startActivity(intent);
                }
                else {
                    //System.out.println("Please, fill in all the fields above");
                    TextView fillAll= findViewById(R.id.textView2);
                    fillAll.setText("Please, fill in all the fields above");
                }
            }
        });
    }
}
