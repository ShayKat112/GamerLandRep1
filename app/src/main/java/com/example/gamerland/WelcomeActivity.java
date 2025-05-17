package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edEmail;
    EditText edPassword;
    Button btnLogin;
    Button btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister) {
                Intent intent1 = new Intent(WelcomeActivity.this, RegisterActivity.class); // Assuming RegisterActivity exists
                startActivity(intent1);
        } else if (v == btnLogin) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(WelcomeActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            } signIn(email, password);
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(WelcomeActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(WelcomeActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}