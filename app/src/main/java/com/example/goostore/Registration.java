package com.example.goostore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Registration extends AppCompatActivity {
    ImageView creat_account1Button;
    SharedPreferences sp;
    SharedPreferences spUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Create Account1 Button (Registration Page)
        creat_account1Button = findViewById(R.id.creat_account_btn1);
        creat_account1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp = getSharedPreferences("logged", MODE_PRIVATE);
                spUser = getSharedPreferences("uEmail", MODE_PRIVATE);

                if (sp.getBoolean("logged", false)) {
                    Intent intent = new Intent(Registration.this, Profile.class);
                    startActivity(intent);
                }
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

                EditText input_bank_account = findViewById(R.id.editTextReg6);
                String bank_account = input_bank_account.getText().toString();
                User sample_user = new User("", "", "", "", "", "");
                boolean register_OK = sample_user.Registration(email, password, name, phone_number, address, bank_account);

                boolean registOK = newUser(v);

                if (registOK && register_OK) {
                    //if all fields are non-empty
                    Intent intent = new Intent(Registration.this, Profile.class);
                    intent.putExtra("user", email);
                    sp.edit().putBoolean("logged", true).apply();
                    spUser.edit().putString("uEmail", email).apply();
                    startActivity(intent);
                }
                else if (registOK == false) {
                    TextView fillAll= findViewById(R.id.textViewFillAll);
                    fillAll.setText("User with this email already exist");
                }
                else {
                    //System.out.println("Please, fill in all the fields above");
                    TextView fillAll= findViewById(R.id.textViewFillAll);
                    fillAll.setText("Please, fill in all the fields above");
                }
            }
        });
    }

    public boolean newUser(View view) {
        DBHandler dbHandler = new DBHandler(getApplicationContext(), null, null, 1);
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

        EditText input_bank_account = findViewById(R.id.editTextReg6);
        String bank_account = input_bank_account.getText().toString();

        User sample_user = new User(email, password, name, phone_number, address, bank_account);
        boolean regist = dbHandler.addUser(sample_user);
        return regist;
    }

}