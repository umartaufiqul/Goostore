package com.example.goostore;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private ImageView creat_account1Button;
    private SharedPreferences sp;
    private SharedPreferences spUser;
    private FirebaseAuth auth;
    private DatabaseReference mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Get auth instance
        auth = FirebaseAuth.getInstance();
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

                boolean registOK = newUser(v);
                //Trying google way
                if (registOK) {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(Registration.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(Registration.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(Registration.this, Profile.class));
                                        finish();
                                    }
                                }
                            });
                }
                else {
                    //System.out.println("Please, fill in all the fields above");
                    TextView fillAll= findViewById(R.id.textViewFillAll);
                    fillAll.setText("Please, fill in all the fields above correctly");
                }
            }
        });
    }

    public boolean newUser(View view) {
        //DBHandler dbHandler = new DBHandler(getApplicationContext(), null, null, 1);
        boolean regist;
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
        if (email.equals("") || password.equals("") || name.equals("") || phone_number.equals("") || address.equals("") || bank_account.equals("")) {
            regist = false;
        }
        else {
            regist = true;
        }
        try {
            Integer.parseInt(phone_number);
            Integer.parseInt(bank_account);
        } catch (NumberFormatException nfe) {
            regist= false;
            Toast.makeText(Registration.this, "Incorrect format for Phone Number or Bank Account (all number)", Toast.LENGTH_LONG).show();
        }


        //Trying google way
        mDB = FirebaseDatabase.getInstance().getReference().child("users");
        mDB.push().setValue(sample_user);
        return regist;
    }

}