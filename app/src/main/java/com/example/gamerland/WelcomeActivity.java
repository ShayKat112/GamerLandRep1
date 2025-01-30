package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edUsername;
    EditText edPassword;
    Button btnLogin;
    Button btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister) {
                Intent intent1 = new Intent(WelcomeActivity.this, RegisterActivity.class); // Assuming RegisterActivity exists
                startActivity(intent1);
        } else if (v == btnLogin) {
                Intent intent2 = new Intent(WelcomeActivity.this, HomeActivity.class); // Assuming LoginActivity exists
                startActivity(intent2);
        }
    }
}